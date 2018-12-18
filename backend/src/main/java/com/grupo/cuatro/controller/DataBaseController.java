package com.grupo.cuatro.controller;

import com.grupo.cuatro.Elastic;
import com.grupo.cuatro.model.*;
import com.grupo.cuatro.repository.CountryRepository;
import com.grupo.cuatro.repository.SportRepository;
import com.grupo.cuatro.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/database")
@CrossOrigin("*")
public class DataBaseController {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private SportRepository sportRepository;
    @Autowired
    private StatisticRepository statisticRepository;
    private Elastic e = new Elastic();

    @RequestMapping(value="/seed", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity seed() {
        java.sql.Date sqlDate;
        sqlDate = new java.sql.Date(System.currentTimeMillis()); //datetime for statistic
        List<Sport> sports = sportRepository.findAll();
        List<Country> countries = countryRepository.findAll();
        //valores para estadistica compuesta
        for (Sport sport : sports) {
            for (Country country : countries) {
                Statistic statistic = new Statistic();
                statistic.setStatisticDate(sqlDate);
                statistic.setStatisticQuery("tweetCountBySportByCountry");
                statistic.setSport(sport);
                statistic.setCountry(country);
                Long statisticCount = 0L;

                //primero se acumula el contador por su nombre
                statisticCount += e.getCantidadDeportePais(sport.getSportName(), country.getCountryName());

                //luego por cada keyword
                for(CountryKeyword countryKeyword : country.getCountryKeywords()) {
                    for(SportKeyword sportKeyword : sport.getSportKeywords()) {
                        statisticCount += e.getCantidadDeportePais(sportKeyword.getSportKeywordWord(), countryKeyword.getCountryKeywordWord());
                    }
                }
                //se setea el valor acumulado
                statistic.setStatisticCount(statisticCount);
                statisticRepository.save(statistic);
            }
        }
        //valores para estadistica individual
        for (Sport sport : sports) {
            Long sportTweetCount = 0L;
            sportTweetCount += e.getCantidad(sport.getSportName());
            for (SportKeyword sportKeyword : sport.getSportKeywords()) {
                sportTweetCount += e.getCantidad(sportKeyword.getSportKeywordWord());
            }
            sport.setSportTweetCount(sportTweetCount);
            sportRepository.save(sport);
        }

        for (Country country : countries) {
            Long countryTweetCount = 0L;
            countryTweetCount += e.getCantidadPais(country.getCountryName());
            for (CountryKeyword countryKeyword : country.getCountryKeywords()) {
                countryTweetCount += e.getCantidadPais(countryKeyword.getCountryKeywordWord());
            }
            country.setCountryTweetCount(countryTweetCount);
            countryRepository.save(country);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
