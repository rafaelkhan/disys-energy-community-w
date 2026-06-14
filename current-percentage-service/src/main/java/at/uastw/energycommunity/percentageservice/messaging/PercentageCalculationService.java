package at.uastw.energycommunity.percentageservice.messaging;

import at.uastw.energycommunity.percentageservice.config.RabbitConfig;
import at.uastw.energycommunity.percentageservice.repository.CurrentPercentageEntity;
import at.uastw.energycommunity.percentageservice.repository.CurrentPercentageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PercentageCalculationService {

    private final CurrentPercentageRepository percentageRepository;
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public PercentageCalculationService(CurrentPercentageRepository percentageRepository) {
        this.percentageRepository = percentageRepository;
    }

    @RabbitListener(queues = RabbitConfig.ENERGY_UPDATE_QUEUE)
    @Transactional
    public void readUpdateMessage(String message) throws Exception {
        System.out.println("Received update message: " + message);
        UsageUpdateMessage update = mapper.readValue(message, UsageUpdateMessage.class);

        double produced = update.getCommunityProduced();
        double used = update.getCommunityUsed();
        double gridUsed = update.getGridUsed();

        double communityDepleted;
        if (produced > 0) {
            communityDepleted = Math.min(used / produced * 100.0, 100.0);
        } else {
            // nothing was produced, so the pool is fully depleted as soon as energy is used
            communityDepleted = used + gridUsed > 0 ? 100.0 : 0.0;
        }

        double totalUsed = used + gridUsed;
        double gridPortion = totalUsed > 0 ? gridUsed / totalUsed * 100.0 : 0.0;

        CurrentPercentageEntity percentage = new CurrentPercentageEntity();
        percentage.setHour(update.getHour());
        percentage.setCommunityDepleted(Math.round(communityDepleted * 100.0) / 100.0);
        percentage.setGridPortion(Math.round(gridPortion * 100.0) / 100.0);

        // the table only holds the data of the current hour
        percentageRepository.deleteAll();
        percentageRepository.save(percentage);
        System.out.println("Saved percentage for hour " + update.getHour()
                + ": depleted=" + percentage.getCommunityDepleted()
                + "%, grid portion=" + percentage.getGridPortion() + "%");
    }
}
