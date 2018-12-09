package com.grupo.cuatro.controller;

import com.grupo.cuatro.model.Keyword;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.repository.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping(value="/sport")
@CrossOrigin("*")
public class SportController {
    @Autowired
    private SportRepository sportRepository;

    //obtener todos los deportes
    @RequestMapping(value="/all", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() {
        return new ResponseEntity(sportRepository.findAll(), HttpStatus.OK);
    }

    //obtener un deporte por su id
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getById(@PathVariable("id") Long id) {
        Sport sportValue;
        Optional<Sport> sport = sportRepository.findById(id);
        if(sport.isPresent()) {
            sportValue = sport.get();
            return new ResponseEntity(sportValue, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //crear un deporte
    @RequestMapping(value="/create", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create(@RequestBody Sport resource) {
        return new ResponseEntity(sportRepository.save(resource), HttpStatus.CREATED);
    }

    //obtener las keywords de un deporte
    @RequestMapping(value="/{id}/keywords", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getSportKeywords(@PathVariable("id") Long id) {
        Sport sportValue;
        Optional<Sport> sport = sportRepository.findById(id);
        if(sport.isPresent()) {
            sportValue = sport.get();
            List<Keyword> keywords = sportValue.getKeywords();
            return new ResponseEntity(keywords, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //obtener las estadisticas de un deporte
    @RequestMapping(value="/{id}/statistics", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getSportStatistics(@PathVariable("id") Long id) {
        Sport sportValue;
        Optional<Sport> sport = sportRepository.findById(id);
        if(sport.isPresent()) {
            sportValue = sport.get();
            List<Statistic> statistics = sportValue.getStatistics();
            return new ResponseEntity(statistics, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
