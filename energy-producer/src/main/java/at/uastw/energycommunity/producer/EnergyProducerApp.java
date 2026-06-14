package at.uastw.energycommunity.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class EnergyProducerApp {

    private static final String QUEUE_NAME = "energy_input";
    // a small community PV plant produces at most around 0.05 kWh per minute
    private static final double MAX_KWH_PER_MINUTE = 0.05;

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        ObjectMapper mapper = new ObjectMapper();
        WeatherClient weatherClient = new WeatherClient();
        Random random = new Random();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println("Energy Producer started, sending to queue " + QUEUE_NAME);

            while (true) {
                double sunFactor = weatherClient.getSunFactor();
                // random jitter between 80% and 120% so the values are not always the same
                double jitter = 0.8 + random.nextDouble() * 0.4;
                double kwh = MAX_KWH_PER_MINUTE * sunFactor * jitter;
                kwh = Math.round(kwh * 1000.0) / 1000.0;

                Map<String, Object> message = new LinkedHashMap<>();
                message.put("type", "PRODUCER");
                message.put("association", "COMMUNITY");
                message.put("kwh", kwh);
                message.put("datetime", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).toString());

                String json = mapper.writeValueAsString(message);
                channel.basicPublish("", QUEUE_NAME, null, json.getBytes());
                System.out.println("Sent: " + json);

                // wait a random 1-5 seconds before the next message
                Thread.sleep(1000 + random.nextInt(4000));
            }
        }
    }
}
