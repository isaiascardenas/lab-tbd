package com.grupo.cuatro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class CuatroApplication {

	public static void main(String[] args) {
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

