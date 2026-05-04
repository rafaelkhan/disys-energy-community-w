package at.uastw.energycommunity.restapi.controller;

import at.uastw.energycommunity.restapi.dto.CurrentPercentageDto;
import at.uastw.energycommunity.restapi.dto.HistoricalUsageDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/energy")
public class EnergyController {

    private final List<HistoricalUsageDto> sampleData = new ArrayList<>(List.of(
            new HistoricalUsageDto(LocalDateTime.of(2025, 1, 10, 12, 0), 12.500, 11.030, 0.500),
            new HistoricalUsageDto(LocalDateTime.of(2025, 1, 10, 13, 0), 15.015, 14.033, 2.049),
            new HistoricalUsageDto(LocalDateTime.of(2025, 1, 10, 14, 0), 18.050, 18.050, 1.076),
            new HistoricalUsageDto(LocalDateTime.of(2025, 1, 10, 15, 0), 20.220, 19.500, 0.860),
            new HistoricalUsageDto(LocalDateTime.of(2025, 1, 10, 16, 0), 17.800, 17.800, 1.230)
    ));

    @GetMapping("/current")
    public CurrentPercentageDto getCurrent() {
        LocalDateTime currentHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        return new CurrentPercentageDto(currentHour, 78.54, 7.23);
    }

    @GetMapping("/historical")
    public List<HistoricalUsageDto> getHistorical(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return sampleData.stream()
                .filter(entry -> !entry.getHour().isBefore(start) && !entry.getHour().isAfter(end))
                .toList();
    }
}
