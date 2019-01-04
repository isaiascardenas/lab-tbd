package com.grupo.cuatro.controller;

import com.grupo.cuatro.Elastic;
import com.grupo.cuatro.Neo4j.GrafoDB;
import com.grupo.cuatro.model.Country;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.repository.CountryRepository;
import com.grupo.cuatro.repository.SportRepository;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/neo4j")
@CrossOrigin("*")
public class NeoController {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private SportRepository sportRepository;

    @RequestMapping(value = "/nodos", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity crearNodos(){
        GrafoDB grafo = new GrafoDB();
        grafo.connect("bolt://localhost", "neo4j", "secret");
        Elastic e = new Elastic();
        List<Country> paises = countryRepository.findAll();
        List<Sport> deportes = sportRepository.findAll();
        List<String> usuarios = e.getUsers();
        //ArrayList<String> usuariosAux = new ArrayList<>();
        for(Country pais : paises){
            grafo.crearNodoPais(pais.getCountryName(), Integer.toString(e.getInfluenciaPais(pais.getCountryName())));
        }

        for(Sport sport : deportes){
            grafo.crearNodoDeporte(sport.getSportName());
        }

        for(String usuario : usuarios){
            System.out.println("Entre aqui");
                e.usuarioHabla(usuario);
                grafo.crearNodoUsuario(usuario, e.getUserInfluencia(usuario));
        }

        for(String user : usuarios){
            ArrayList<String> listaDeportes = e.usuarioHabla(user);
            for(String deporte : listaDeportes){
                grafo.crearRelacionUsuarioDeporte(deporte, user);
            }
        }

        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "/grafo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> obtenerGrafo(){
        Elastic e = new Elastic();
        List<String> usuarios = e.getUsers();


        Map<String, Object> salida = new HashMap<>();
        salida.put("Usuarios", usuarios);

        return salida;
    }

}
