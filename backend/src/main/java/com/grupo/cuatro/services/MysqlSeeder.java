package com.grupo.cuatro.services;

import java.util.*;
import com.grupo.cuatro.Elastic;
import com.grupo.cuatro.model.Country;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.repository.CountryRepository;
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

    @Autowired
    private CountryRepository countryRepository;

    private java.sql.Date sqlDate;


    public void seed() {
        List<Sport> sports = sportRepository.findAll();
        Long tweetCount;
        sqlDate = new java.sql.Date(System.currentTimeMillis()); //datetime for statistic


        for (Sport sport : sports) {
            System.out.println("name: " + sport.getSportName());
            System.out.println("count: " + this.e.getCantidad(sport.getSportName()));

            //new count statistic creation
            tweetCount = Long.valueOf( this.e.getCantidad(sport.getSportName()));
            Statistic statistic = new Statistic();
            statistic.setSport(sport);
            statistic.setStatisticCount(tweetCount);
            statistic.setStatisticQuery("get_total_count");
            statistic.setStatisticDate(sqlDate);
            statisticRepository.save(statistic);
            //get pais  (cantidad tweets por pais)
            //get fecha (cantidad de tweets por fecha)

            //traer las estadisticas del deporte
            //almacenar el count y query

            // store getCantidad on Statistics table ...
        }
    }

    public void seedCountryCount() {
        List<Country> countries = countryRepository.findAll();
        Long tweetCountryCount;
        sqlDate = new java.sql.Date(System.currentTimeMillis());

        for (Country country : countries) {
            System.out.println("name: " + country.getCountryName());
            System.out.println("count: " + this.e.getCantidadPais(country.getCountryName()));

            //new count-by-country statistic creation
            tweetCountryCount = Long.valueOf(this.e.getCantidadPais(country.getCountryName()));
            Statistic statistic = new Statistic();

        }
    }

}

