package com.grupo.cuatro;

import com.grupo.cuatro.model.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.grupo.cuatro.repository.UserRepository;

@SpringBootApplication
@EnableSwagger2
public class CuatroApplication {

    private UserRepository userRepository;

	public static void main(String[] args) {
        Elastic e = new Elastic();
        //e.indexCreate();
        System.out.println(e.getCantidadFecha());
        SpringApplication.run(CuatroApplication.class, args);
	}

	@Bean
    Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(ApiInfo.DEFAULT);
    }

    /*@Bean
    InitializingBean sendDatabase(){
	    return () -> {
	        userRepository.save(new User("Jorge", "email1"));
	        userRepository.save(new User("Hola", "email2"));
        };
    }*/
}


