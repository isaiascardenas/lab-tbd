package com.grupo.cuatro;

import com.grupo.cuatro.services.MysqlSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class CuatroApplication {

	public static void main(String[] args) {


            // this make null pointer exception...
            // MysqlSeeder seeder = new MysqlSeeder();
            // seeder.seed();
        Elastic e = new Elastic();
        e.indexCreate();

        System.out.println(e.getCantidadPais("Argentina"));
        System.out.println(e.getCantidadPais("Chile"));
        System.out.println(e.getCantidadPais("Mexico"));
        System.out.println(e.getCantidadPais("España"));
        System.out.println(e.getCantidadPais("Venezuela"));

        SpringApplication.run(CuatroApplication.class, args);
	}

	@Bean
    Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(ApiInfo.DEFAULT);
    }
}

