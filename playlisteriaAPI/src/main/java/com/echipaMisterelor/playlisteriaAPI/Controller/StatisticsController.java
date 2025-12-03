package com.echipaMisterelor.playlisteriaAPI.Controller;

import com.echipaMisterelor.playlisteriaAPI.Model.Statistics;
import com.echipaMisterelor.playlisteriaAPI.Services.Statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/all")
    public ResponseEntity<List<Statistics>> getAllStatistics() {
        List<Statistics> allStatistics = statisticsService.getAllStatistics();
        return new ResponseEntity<>(allStatistics, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Statistics> getStatistics(
            @RequestParam String action,
            @RequestParam(required = false, defaultValue = "") String argument) {
        Statistics statistic = statisticsService.getStatistics(action, argument);
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }

    @GetMapping("/getAllByType")
    public ResponseEntity<List<Statistics>> getStatisticsByType(@RequestParam String action) {
        List<Statistics> statistic = statisticsService.getStatisticsByType(action);
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateOrAddStatistic(
            @RequestParam String action,
            @RequestParam String argument) {
        statisticsService.updateOrAddStatistic(action, argument);
        return new ResponseEntity<>("Statistic updated or added successfully", HttpStatus.OK);
    }
}

