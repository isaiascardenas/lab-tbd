package com.grupo.cuatro.controller;

import com.grupo.cuatro.Elastic;
import com.grupo.cuatro.model.Country;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.repository.CountryRepository;
import com.grupo.cuatro.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Validated
@RequestMapping(value = "/country")
@CrossOrigin(origins="*") //por mientras, esto se debe cambiar por la Ip donde estara el front
public class CountryController {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private StatisticRepository statisticRepository;

    private Elastic e = new Elastic();

    //obtener todos los paises ordenados por su codigo
    @RequestMapping(value="/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() {
        List<Country> countries = countryRepository.findAll();
        for(Country country : countries) {
            Float aux = new Long(country.getCountryPopulation()).floatValue();
            Float aux2 = country.getCountryTweetCount().floatValue();
            Float resultado = (aux2/aux)*1000000;
            country.setIndex(resultado);
            country.setInfluentialUsersCount(e.influentialUsersPais(country.getCountryName()));
        }
        countries.sort(Comparator.comparing(Country::getCountryCode));
        return new ResponseEntity(countries, HttpStatus.OK);
    }

    //obtener un pais por su id
    @RequestMapping(value="/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getById(@PathVariable("id") Long id) {
        Country countryValue;
        Optional<Country> country = countryRepository.findById(id);
        //primero se verifica que exista el pais con la id dada
        if(country.isPresent()) {
            countryValue = country.get();
            return new ResponseEntity(countryValue, HttpStatus.OK);
        }
        //si no existe se retorna not found
        else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //obtener pais por su nombre
    @RequestMapping(value="/name/{country_name}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getByCountryName(@PathVariable("country_name") String countryName) {
        Country country = countryRepository.getCountryByCountryNameIsLike(countryName);
        if(country != null) {
            return new ResponseEntity(country, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //crear un pais
    @RequestMapping(value="/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create(@RequestBody Country resource) {
        return new ResponseEntity(countryRepository.save(resource), HttpStatus.CREATED);
    }

    //obtener estadisticas por pais
    @RequestMapping(value="/{id}/statistics", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getCountryStatistics(@PathVariable("id") Long id) {
        //primero se verifica que exista el pais
        Country countryValue;
        Optional<Country> country = countryRepository.findById(id);
        if(country.isPresent()) {
            countryValue = country.get();
            List<Statistic> statistics = countryValue.getStatistics();
            Set<Statistic> uniqueStatistics = new HashSet<Statistic>(statistics);
            return new ResponseEntity(uniqueStatistics, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //asociar una estadistica a un pais (hay que probarlo)
    @RequestMapping(value="/{id}/statistic/{statistic_id}", method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity addStatistic(@PathVariable("id") Long id, @PathVariable("statistic_id") Long statisticId) {
        Country countryValue;
        Statistic statisticValue;

        Optional<Country> country = countryRepository.findById(id);
        Optional<Statistic> statistic = statisticRepository.findById(statisticId);
        if(statistic.isPresent() && country.isPresent()) {
            statisticValue = statistic.get();
            countryValue = country.get();
            countryValue.addStatistic(statisticValue);
            countryRepository.save(countryValue);
            return new ResponseEntity(countryValue, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value="/{id}/delete", method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteCountry(@PathVariable("id") Long id_country){
        this.countryRepository.deleteById(id_country);
    }
}
