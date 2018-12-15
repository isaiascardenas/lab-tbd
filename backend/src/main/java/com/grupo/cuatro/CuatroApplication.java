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
	    System.out.println(e.getCantidad("Tenis"));
		System.out.println(e.getCantidad("Basket"));
		System.out.println(e.getCantidad("Volley"));
		System.out.println(e.getCantidad("Boxeo"));
		System.out.println(e.getCantidad("Rugby"));
		System.out.println(e.getCantidad("Futbol Femenino"));
		System.out.println(e.getCantidad("Natacion"));
		SpringApplication.run(CuatroApplication.class, args);
	}
	@Bean
    Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(ApiInfo.DEFAULT);
    }

}
