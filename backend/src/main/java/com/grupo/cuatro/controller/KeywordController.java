package com.grupo.cuatro.controller;

import com.grupo.cuatro.model.Keyword;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.repository.KeywordRepository;
import com.grupo.cuatro.repository.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/keyword")
@CrossOrigin("*")//temporal
public class KeywordController {
    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private SportRepository sportRepository;

    //obtener todas las keywords
    @RequestMapping(value="/all", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() {
        return new ResponseEntity(keywordRepository.findAll(), HttpStatus.OK);
    }

    //obtener una keyword por su id
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getById(@PathVariable("id") Long id) {
        Keyword keywordValue;
        Optional<Keyword> keyword = keywordRepository.findById(id);
        if(keyword.isPresent()) {
            keywordValue = keyword.get();
            return new ResponseEntity(keywordValue, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //crear una keyword y asociarla a un deporte (con id transient)
    @RequestMapping(value="/create", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create(@RequestBody Keyword resource) {
        //extraigo llave foranea de resource para establecer la relacion
        Sport sportValue;
        Optional<Sport> sport = sportRepository.findById(resource.getSportId());
        if(sport.isPresent()) {
            sportValue = sport.get();
            resource.setSport(sportValue); //tricky (aqui se establece la relacion)
            return new ResponseEntity(keywordRepository.save(resource), HttpStatus.CREATED);
        } else {
            //se retorna bad request porque se esta intentando asociar una keyword a un deporte inexistente
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
