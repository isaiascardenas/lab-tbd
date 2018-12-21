package com.grupo.cuatro.controller;

import com.grupo.cuatro.repository.FechaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(value="/fecha")
@CrossOrigin("*")
public class FechaController {

    @Autowired
    private FechaRepository fechaRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() { return new ResponseEntity(fechaRepository.findAll(), HttpStatus.OK); }
}
