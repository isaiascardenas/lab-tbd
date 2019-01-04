package com.grupo.cuatro;

import com.grupo.cuatro.Neo4j.GrafoDB;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.model.User;
import com.grupo.cuatro.repository.StatisticRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.grupo.cuatro.repository.UserRepository;

import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class CuatroApplication {

    private UserRepository userRepository;

	public static void main(String[] args) {
        //Elastic e = new Elastic();
        //e.indexCreate();
        /*System.out.println(e.getCantidadFecha());
        e.getInfluenciaPais("Argentina");
        e.getInfluenciaPais("Chile");
        e.getInfluenciaPais("España");
        e.getInfluenciaPais("Paraguay");
        e.getInfluenciaPais("Mexico");*/
        //e.getUsers();
        SpringApplication.run(CuatroApplication.class, args);
        //GrafoDB grafo = new GrafoDB();
        //grafo.connect("bolt://localhost", "neo4j", "secret");
        //grafo.crearNodoPais("Chile", Integer.toString(e.getInfluenciaPais("Chile")));
        //e.crearRelacionUserDeporte();
        //grafo.crearRelacionUsuarioPais("Chile", hola);
        //grafo.crearNodoUsuarios();
        //grafo.crearRelacionDeportePais("Rugby", "Chile");
	}

	@Bean
    Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(ApiInfo.DEFAULT);
    }

}


