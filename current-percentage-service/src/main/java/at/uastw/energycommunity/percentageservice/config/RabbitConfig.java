package at.uastw.energycommunity.percentageservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// declares the queue so it is created in RabbitMQ on startup
@Configuration
public class RabbitConfig {

    public static final String ENERGY_UPDATE_QUEUE = "energy_update";

    @Bean
    public Queue energyUpdateQueue() {
        return new Queue(ENERGY_UPDATE_QUEUE);
    }
}
