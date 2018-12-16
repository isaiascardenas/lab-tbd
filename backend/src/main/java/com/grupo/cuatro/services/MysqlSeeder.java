package com.grupo.cuatro.services;

import java.util.*;
import com.grupo.cuatro.Elastic;
import com.grupo.cuatro.model.Country;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.model.TweetCount;
import com.grupo.cuatro.repository.CountryRepository;
import com.grupo.cuatro.repository.SportRepository;
import com.grupo.cuatro.repository.StatisticRepository;
import com.grupo.cuatro.repository.TweetCountRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MysqlSeeder {


    private Elastic e = new Elastic();
    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TweetCountRepository tweetCountRepository;

    private java.sql.Date sqlDate;

    public void seed() {
        List<Sport> sports = sportRepository.findAll();
        List<Country> countries = countryRepository.findAll();
        Long sportTweetCount;
        Long sportTweetCountByCountry;
        sqlDate = new java.sql.Date(System.currentTimeMillis()); //datetime for statistic


        for (Sport sport : sports) {
            System.out.println("name: " + sport.getSportName());
            System.out.println("count: " + this.e.getCantidad(sport.getSportName()));

            //new count statistic creation
            sportTweetCount = Long.valueOf( this.e.getCantidad(sport.getSportName()));
            Statistic statistic = new Statistic();
            statistic.setSport(sport);
            statistic.setStatisticCount(sportTweetCount);
            statistic.setStatisticQuery("sportTweetCount");
            statistic.setStatisticDate(sqlDate);
            statisticRepository.save(statistic);

            //for each sport, add count statistic by its country (esto funciona lentisimo)
            for (Country country : countries) {
                sportTweetCountByCountry = Long.valueOf(this.e.getCantidadDeportePais(sport.getSportName(), country.getCountryName()));
                Statistic statistic1 = new Statistic();
                statistic1.setSport(sport);
                statistic1.setStatisticCount(sportTweetCountByCountry);
                statistic1.setStatisticQuery("sportTweetCountByCountry");
                statistic1.setStatisticDate(sqlDate);
                country.addStatistic(statistic1);
                countryRepository.save(country);
                statisticRepository.save(statistic1);
            }
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
            TweetCount tweetCount = new TweetCount();
            tweetCount.setCount(tweetCountryCount);
            tweetCount.setCountDate(sqlDate);
            tweetCount.setCountry(country);
            tweetCountRepository.save(tweetCount);
        }
    }


}

