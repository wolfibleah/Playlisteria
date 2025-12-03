package com.echipaMisterelor.playlisteriaAPI.Services.Statistics;

import com.echipaMisterelor.playlisteriaAPI.Model.Statistics;
import com.echipaMisterelor.playlisteriaAPI.Repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    private StatisticsRepository statisticsRepository;

    public void updateOrAddStatistic(String action, String argument) {
        Statistics existingStatistic = statisticsRepository.findByActionAndArgument(action, argument);

        if (existingStatistic == null) {
            Statistics newStatistic = new Statistics(action, argument, 1);
            statisticsRepository.save(newStatistic);
        } else {
            existingStatistic.setCounter(existingStatistic.getCounter() + 1);
            statisticsRepository.save(existingStatistic);
        }
    }

    public void updateOrAddSearchStatistic(String searchTerm) {
        updateOrAddStatistic("search", searchTerm);
    }

    public List<Statistics> getAllStatistics() {
        return statisticsRepository.findAll();
    }

    public Statistics getStatistics(String action, String argument) {
        return statisticsRepository.findByActionAndArgument(action, argument);
    }

    public List<Statistics> getStatisticsByType(String action) {
        return statisticsRepository.findByAction(action);
    }
}
