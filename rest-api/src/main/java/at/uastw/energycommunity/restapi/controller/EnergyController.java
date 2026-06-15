package at.uastw.energycommunity.restapi.controller;

import at.uastw.energycommunity.restapi.dto.CurrentPercentageDto;
import at.uastw.energycommunity.restapi.dto.HistoricalUsageDto;
import at.uastw.energycommunity.restapi.repository.CurrentPercentageRepository;
import at.uastw.energycommunity.restapi.repository.HourlyUsageRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/energy")
public class EnergyController {

    private final CurrentPercentageRepository currentPercentageRepository;
    private final HourlyUsageRepository hourlyUsageRepository;

    public EnergyController(CurrentPercentageRepository currentPercentageRepository,
                            HourlyUsageRepository hourlyUsageRepository) {
        this.currentPercentageRepository = currentPercentageRepository;
        this.hourlyUsageRepository = hourlyUsageRepository;
    }

    @GetMapping("/current")
    public CurrentPercentageDto getCurrent() {
        // the table only holds one row, the one of the current hour
        return currentPercentageRepository.findAll().stream()
                .findFirst()
                .map(entity -> new CurrentPercentageDto(
                        entity.getHour(),
                        entity.getCommunityDepleted(),
                        entity.getGridPortion()))
                .orElse(new CurrentPercentageDto(
                        LocalDateTime.now().truncatedTo(ChronoUnit.HOURS), 0.0, 0.0));
    }

    @GetMapping("/historical")
    public List<HistoricalUsageDto> getHistorical(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return hourlyUsageRepository.findByHourBetweenOrderByHour(start, end).stream()
                .map(entity -> new HistoricalUsageDto(
                        entity.getHour(),
                        entity.getCommunityProduced(),
                        entity.getCommunityUsed(),
                        entity.getGridUsed()))
                .toList();
    }
}
