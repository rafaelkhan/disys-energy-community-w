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



    @GetMapping("/current")
    public CurrentPercentageDto getCurrent() {
        LocalDateTime currentHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        return new CurrentPercentageDto(currentHour, 78.54, 7.23);
    }


}
