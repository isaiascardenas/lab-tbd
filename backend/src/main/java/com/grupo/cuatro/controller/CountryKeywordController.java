package com.grupo.cuatro.controller;

import com.grupo.cuatro.model.Country;
import com.grupo.cuatro.model.CountryKeyword;
import com.grupo.cuatro.repository.CountryKeywordRepository;
import com.grupo.cuatro.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/countryKeyword")
@CrossOrigin("*")//temporal
public class CountryKeywordController {
    @Autowired
    private CountryKeywordRepository countryKeywordRepository;

    @Autowired
    private CountryRepository countryRepository;

    //obtener todas las keywords
    @RequestMapping(value="/all", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() {
        return new ResponseEntity(countryKeywordRepository.findAll(), HttpStatus.OK);
    }

    //obtener una keyword por su id
    @RequestMapping(value="/id/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getById(@PathVariable("id") Long id) {
        CountryKeyword countryKeywordValue;
        Optional<CountryKeyword> countryKeyword = countryKeywordRepository.findById(id);
        if(countryKeyword.isPresent()) {
            countryKeywordValue = countryKeyword.get();
            return new ResponseEntity(countryKeywordValue, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //crear un country keyword y asociarla a un pais (con id transient)
    @RequestMapping(value="/create", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create(@RequestBody CountryKeyword resource) {
        //extraigo llave foranea de resource para establecer la relacion
        Country countryValue;
        Optional<Country> country = countryRepository.findById(resource.getCountryId());
        if(country.isPresent()) {
            countryValue = country.get();
            resource.setCountry(countryValue); //tricky (aqui se establece la relacion)
            return new ResponseEntity(countryKeywordRepository.save(resource), HttpStatus.CREATED);
        } else {
            //se retorna bad request porque se esta intentando asociar una keyword a un deporte inexistente
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value= "/{id}/delete", method=RequestMethod.DELETE)
    @ResponseBody
    public void deleteKeyword(@PathVariable("id") Long id){
        this.countryKeywordRepository.deleteById(id);
    }
}
