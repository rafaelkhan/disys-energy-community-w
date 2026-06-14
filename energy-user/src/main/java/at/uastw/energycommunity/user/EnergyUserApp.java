package at.uastw.energycommunity.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class EnergyUserApp {

    private static final String QUEUE_NAME = "energy_input";
    // an average household uses around 0.03 kWh per minute in peak hours
    private static final double PEAK_KWH_PER_MINUTE = 0.03;

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        ObjectMapper mapper = new ObjectMapper();
        Random random = new Random();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println("Energy User started, sending to queue " + QUEUE_NAME);

            while (true) {
                double timeOfDayFactor = getTimeOfDayFactor(LocalDateTime.now().getHour());
                // random jitter between 80% and 120% so the values are not always the same
                double jitter = 0.8 + random.nextDouble() * 0.4;
                double kwh = PEAK_KWH_PER_MINUTE * timeOfDayFactor * jitter;
                kwh = Math.round(kwh * 1000.0) / 1000.0;

                Map<String, Object> message = new LinkedHashMap<>();
                message.put("type", "USER");
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

    // more energy is used in the morning and evening peak hours
    private static double getTimeOfDayFactor(int hour) {
        if (hour >= 6 && hour <= 9) {
            return 1.0; // morning peak
        } else if (hour >= 17 && hour <= 21) {
            return 1.2; // evening peak
        } else if (hour >= 10 && hour <= 16) {
            return 0.6; // daytime
        } else {
            return 0.25; // night
        }
    }
}
