package at.uastw.energycommunity.usageservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// declares the queues so they are created in RabbitMQ on startup
@Configuration
public class RabbitConfig {

    public static final String ENERGY_INPUT_QUEUE = "energy_input";
    public static final String ENERGY_UPDATE_QUEUE = "energy_update";

    @Bean
    public Queue energyInputQueue() {
        return new Queue(ENERGY_INPUT_QUEUE);
    }

    @Bean
    public Queue energyUpdateQueue() {
        return new Queue(ENERGY_UPDATE_QUEUE);
    }
}
