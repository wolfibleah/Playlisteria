package com.echipaMisterelor.playlisteriaAPI.Repository;

import com.echipaMisterelor.playlisteriaAPI.Model.Statistics;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatisticsRepository extends CrudRepository<Statistics, Integer> {
    Statistics findStatisticsById(Integer id);
    Statistics findByActionAndArgument(String action, String argument);
    List<Statistics> findByAction(String action);
    @Transactional
    void deleteUsStatisticsById(Integer id);
    List<Statistics> findAll();
}
