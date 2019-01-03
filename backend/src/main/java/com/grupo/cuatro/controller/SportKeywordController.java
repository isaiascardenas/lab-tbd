package com.grupo.cuatro.controller;

import com.grupo.cuatro.model.SportKeyword;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.repository.SportKeywordRepository;
import com.grupo.cuatro.repository.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/sportKeyword")
@CrossOrigin("*")//temporal
public class SportKeywordController {
    @Autowired
    private SportKeywordRepository sportKeywordRepository;

    @Autowired
    private SportRepository sportRepository;

    //obtener todas las keywords
    @RequestMapping(value="/all", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() {
        return new ResponseEntity(sportKeywordRepository.findAll(), HttpStatus.OK);
    }

    //obtener una keyword por su id
    @RequestMapping(value="/id/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getById(@PathVariable("id") Long id) {
        SportKeyword sportKeywordValue;
        Optional<SportKeyword> sportKeyword = sportKeywordRepository.findById(id);
        if(sportKeyword.isPresent()) {
            sportKeywordValue = sportKeyword.get();
            return new ResponseEntity(sportKeywordValue, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //crear una sport keyword y asociarla a un deporte (con id transient)
    @RequestMapping(value="/create", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create(@RequestBody SportKeyword resource) {
        //extraigo llave foranea de resource para establecer la relacion
        Sport sportValue;
        Optional<Sport> sport = sportRepository.findById(resource.getSportId());
        if(sport.isPresent()) {
            sportValue = sport.get();
            resource.setSport(sportValue); //tricky (aqui se establece la relacion)
            return new ResponseEntity(sportKeywordRepository.save(resource), HttpStatus.CREATED);
        } else {
            //se retorna bad request porque se esta intentando asociar una keyword a un deporte inexistente
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value= "/{id}/delete", method=RequestMethod.DELETE)
    @ResponseBody
    public void deleteKeyword(@PathVariable("id") Long id){
        this.sportKeywordRepository.deleteById(id);
    }
}
