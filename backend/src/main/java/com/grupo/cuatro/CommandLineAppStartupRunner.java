package com.grupo.cuatro;

import com.grupo.cuatro.services.MysqlSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    private MysqlSeeder mysqlSeeder;

    @Override
    public void run(String... args) throws Exception {
        mysqlSeeder.seed();
    }
}
