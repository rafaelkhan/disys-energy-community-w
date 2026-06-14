package at.uastw.energycommunity.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

// fetches the current weather for Vienna from the open-meteo API (no API key needed)
public class WeatherClient {

    private static final String WEATHER_URL =
            "https://api.open-meteo.com/v1/forecast?latitude=48.21&longitude=16.37&current=cloud_cover,is_day";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // weather is cached for 10 minutes so we do not spam the API every few seconds
    private double cachedSunFactor = 0.5;
    private Instant lastFetch = Instant.MIN;

    // returns a factor between 0 (night) and 1 (clear sky at daytime)
    public double getSunFactor() {
        if (Duration.between(lastFetch, Instant.now()).toMinutes() < 10) {
            return cachedSunFactor;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(WEATHER_URL))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode current = mapper.readTree(response.body()).get("current");
            int cloudCover = current.get("cloud_cover").asInt();
            boolean isDay = current.get("is_day").asInt() == 1;

            if (isDay) {
                // full clouds still let some light through
                cachedSunFactor = 1.0 - cloudCover / 100.0 * 0.8;
            } else {
                cachedSunFactor = 0.0;
            }
            lastFetch = Instant.now();
            System.out.println("Weather updated: cloud cover " + cloudCover + "%, day=" + isDay
                    + " -> sun factor " + cachedSunFactor);
        } catch (Exception e) {
            // if the weather API is not reachable we keep the last known factor
            System.out.println("Could not fetch weather, using last factor: " + e.getMessage());
        }
        return cachedSunFactor;
    }
}
