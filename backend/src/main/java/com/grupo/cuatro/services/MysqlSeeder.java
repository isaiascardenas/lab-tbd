package com.grupo.cuatro.services;

import java.util.*;
import com.grupo.cuatro.Elastic;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.repository.SportRepository;
import com.grupo.cuatro.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MysqlSeeder {


    private Elastic e = new Elastic();
    @Autowired
    private SportRepository sportRepository;


    public void seed() {
        List<Sport> all = sportRepository.findAll();

        for (Sport sport : all) {

            System.out.println("name: " + sport.getSportName());
            System.out.println("count: " + this.e.getCantidad(sport.getSportName()));

            // store getCantidad on Statistics table ...
        }
    }

}

