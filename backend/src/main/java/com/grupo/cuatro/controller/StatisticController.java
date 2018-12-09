package com.grupo.cuatro.controller;

import com.grupo.cuatro.model.Country;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.repository.CountryRepository;
import com.grupo.cuatro.repository.SportRepository;
import com.grupo.cuatro.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping(value="/statistic")
@CrossOrigin("*")//temporal
public class StatisticController {
    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private SportRepository sportRepository;

    //obtener todas las estadisticas
    @RequestMapping(value="/all", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() {
        return new ResponseEntity(statisticRepository.findAll(), HttpStatus.OK);
    }

    //obtener estadistica por id
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getById(@PathVariable("id") Long id) {
        Statistic statisticValue;
        Optional<Statistic> statistic = statisticRepository.findById(id);
        if(statistic.isPresent()) {
            statisticValue = statistic.get();
            return new ResponseEntity(statisticValue, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //crear estadistica y asociarla a un deporte (con id transient)
    @RequestMapping(value="/create", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create(@RequestBody Statistic resource) {
        //extraigo llave foranea de resource para establecer relacion
        Sport sportValue;
        Optional<Sport> sport = sportRepository.findById(resource.getSportId());
        if(sport.isPresent()) {
            sportValue = sport.get();
            resource.setSport(sportValue); //aqui se relacionan
            return new ResponseEntity(statisticRepository.save(resource), HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    //obtener paises de una estadistica
    @RequestMapping(value="/{id}/countries", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getStatisticCountries(@PathVariable("id") Long id) {
        Statistic statisticValue;
        Optional<Statistic> statistic = statisticRepository.findById(id);
        if(statistic.isPresent()) {
            statisticValue = statistic.get();
            List<Country> countries = statisticValue.getCountries();
            return new ResponseEntity(countries, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //asociar un pais a una estadistica (hay que probarlo)
    @RequestMapping(value="/{id}/country/{country_id}", method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity addCountry(@PathVariable("id") Long id, @PathVariable("country_id") Long countryId) {
        Statistic statisticValue;
        Country countryValue;

        Optional<Statistic> statistic = statisticRepository.findById(id);
        Optional<Country> country = countryRepository.findById(countryId);
        if(statistic.isPresent() && country.isPresent()) {
            statisticValue = statistic.get();
            countryValue = country.get();
            statisticValue.addCountry(countryValue);
            statisticRepository.save(statisticValue);
            return new ResponseEntity(statisticValue, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
