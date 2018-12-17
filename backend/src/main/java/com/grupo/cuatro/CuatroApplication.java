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
        //e.indexCreate();

        //System.out.println(e.getCantidadFecha("Dec"));
        //System.out.println(e.getCantidadDeportePais("Tenis", "Chile"));
        //System.out.println(e.getCantidadDeportePais("Tenis", "Argentina"));
        //System.out.println(e.getCantidadDeportePais("Tenis", "España"));
        //System.out.println(e.getCantidad("Boxeo"));
        System.out.println("Twits en Ecuador "+e.getCantidadPais("Ecuador"));
        System.out.println("Twits en Paraguay "+e.getCantidadPais("Paraguay"));
        System.out.println("Twits en Chile "+e.getCantidadPais("Chile"));
        System.out.println("Twits en Argentina "+e.getCantidadPais("Argentina"));
        System.out.println("Twits en España "+e.getCantidadPais("España"));
        System.out.println(e.getCantidadFecha("12"));
        //System.out.println(e.getCantidadPais("Chile"));
        //System.out.println(e.getCantidadPais("chile"));
        //System.out.println(e.getCantidadPais("Santiago"));
        //System.out.println(e.getCantidadPais("Argentina"));
        //System.out.println(e.getCantidadPais("argentina"));
        //System.out.println(e.getCantidad("Tenis"));
        SpringApplication.run(CuatroApplication.class, args);
	}

	@Bean
    Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(ApiInfo.DEFAULT);
    }
}

