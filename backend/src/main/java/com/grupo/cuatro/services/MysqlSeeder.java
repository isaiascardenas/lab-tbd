package com.grupo.cuatro.services;

import java.util.*;
import com.grupo.cuatro.Elastic;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.repository.SportRepository;
import com.grupo.cuatro.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MysqlSeeder {


    private Elastic e = new Elastic();
    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private StatisticRepository statisticRepository;


    public void seed() {
        List<Sport> all = sportRepository.findAll();
        Long tweetCount;
        Long tweetCountryCount;
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis()); //datetime for statistic


        for (Sport sport : all) {

            System.out.println("name: " + sport.getSportName());
            System.out.println("count: " + this.e.getCantidad(sport.getSportName()));

            tweetCount = Long.valueOf( this.e.getCantidad(sport.getSportName()));

            //new count statistic creation
            Statistic statistic = new Statistic();
            statistic.setSport(sport);
            statistic.setStatisticCount(tweetCount);
            statistic.setStatisticQuery("get_total_count");
            statistic.setStatisticDate(sqlDate);

            //add to statistics repo
            statisticRepository.save(statistic);

            //get cant (cantidad total por deporte)
            //get pais  (cantidad tweets por pais)
            //get fecha (cantidad de tweets por fecha)

            //traer las estadisticas del deporte
            //almacenar el count y query

            // store getCantidad on Statistics table ...
        }
    }

}

