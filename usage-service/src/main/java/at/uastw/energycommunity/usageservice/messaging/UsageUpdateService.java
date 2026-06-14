package at.uastw.energycommunity.usageservice.messaging;

import at.uastw.energycommunity.usageservice.config.RabbitConfig;
import at.uastw.energycommunity.usageservice.repository.HourlyUsageEntity;
import at.uastw.energycommunity.usageservice.repository.HourlyUsageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class UsageUpdateService {

    private final HourlyUsageRepository repository;
    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public UsageUpdateService(HourlyUsageRepository repository, RabbitTemplate rabbit) {
        this.repository = repository;
        this.rabbit = rabbit;
    }

    @RabbitListener(queues = RabbitConfig.ENERGY_INPUT_QUEUE)
    @Transactional
    public void readEnergyMessage(String message) throws Exception {
        System.out.println("Received message: " + message);
        EnergyMessage energyMessage = mapper.readValue(message, EnergyMessage.class);

        LocalDateTime hour = energyMessage.getDatetime().truncatedTo(ChronoUnit.HOURS);
        HourlyUsageEntity usage = repository.findById(hour).orElseGet(() -> {
            HourlyUsageEntity newUsage = new HourlyUsageEntity();
            newUsage.setHour(hour);
            return newUsage;
        });

        if ("PRODUCER".equals(energyMessage.getType())) {
            usage.setCommunityProduced(usage.getCommunityProduced() + energyMessage.getKwh());
        } else if ("USER".equals(energyMessage.getType())) {
            // the community pool is used first, the rest is taken from the grid
            double availableInPool = usage.getCommunityProduced() - usage.getCommunityUsed();
            double fromCommunity = Math.min(energyMessage.getKwh(), Math.max(availableInPool, 0));
            double fromGrid = energyMessage.getKwh() - fromCommunity;

            usage.setCommunityUsed(usage.getCommunityUsed() + fromCommunity);
            usage.setGridUsed(usage.getGridUsed() + fromGrid);
        } else {
            System.out.println("Unknown message type: " + energyMessage.getType());
            return;
        }

        repository.save(usage);

        // send the full new state of this hour to the percentage service, so it
        // can calculate the percentage without reading the hourly_usage table.
        UsageUpdateMessage update = new UsageUpdateMessage(
                usage.getHour(),
                usage.getCommunityProduced(),
                usage.getCommunityUsed(),
                usage.getGridUsed());
        rabbit.convertAndSend(RabbitConfig.ENERGY_UPDATE_QUEUE, mapper.writeValueAsString(update));
        System.out.println("Updated usage for hour " + hour);
    }
}
