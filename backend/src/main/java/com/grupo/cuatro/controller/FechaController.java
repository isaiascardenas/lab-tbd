package com.grupo.cuatro.controller;

import com.grupo.cuatro.Neo4j.GrafoDB;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.repository.FechaRepository;
import com.grupo.cuatro.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(value="/fecha")
@CrossOrigin("*")
public class FechaController {

    @Autowired
    private FechaRepository fechaRepository;

    @Autowired
    StatisticRepository statisticRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() {
        return new ResponseEntity(fechaRepository.findAll(), HttpStatus.OK); }
}
