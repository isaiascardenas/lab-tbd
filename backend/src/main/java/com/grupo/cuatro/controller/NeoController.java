package com.grupo.cuatro.controller;

import com.grupo.cuatro.Elastic;
import com.grupo.cuatro.Neo4j.GrafoDB;
import com.grupo.cuatro.model.Country;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.repository.CountryRepository;
import com.grupo.cuatro.repository.SportRepository;
import com.grupo.cuatro.repository.StatisticRepository;
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

    @Autowired
    private StatisticRepository statisticRepository;

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
                System.out.println("Entre al for y que pasa");
                System.out.println(deporte);
                System.out.println(user);
                grafo.crearRelacionUsuarioDeporte(deporte, user);
            }
        }

        e.crearRelacionUserDeporte();

        List<Statistic> statistics = statisticRepository.findAll();
        for(Statistic st : statistics){
            if(st.getStatisticCount() > 400){
                grafo.crearRelacionDeportePais(st.getSport().getSportName(), st.getCountry().getCountryName());
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

    @RequestMapping(value = "/deporte-usuario")
    @ResponseBody
    public Map<String, Object> getGrafo(){
        Elastic e = new Elastic();
        GrafoDB grafo = new GrafoDB();
        List<Country> paises = countryRepository.findAll();
        List<Sport> deportes = sportRepository.findAll();
        List<String> usuarios = e.getUsers();
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();
        int i = 0;
        for(String usuario : usuarios){
            ArrayList<String> listaDeportes = e.usuarioHabla(usuario);
            nodes.add(grafo.mapTriple("name", usuario, "label", "Usuario", "Influencia", e.getUserInfluencia(usuario)));
            int target = i;
            i++;
            for(String sport : listaDeportes){
                Map<String, Object> deporte = grafo.map("Nombre", sport, "label", "Deporte");
                int source = nodes.indexOf(deporte);
                if(source == -1){
                    nodes.add(deporte);
                    source = i++;
                }
                rels.add(grafo.map("sid", source, "tid", target));
            }

        }
        return grafo.map("nodes", nodes, "links", rels);
    }

    @RequestMapping(value = "/pais-deporte", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getGrafote(){
        GrafoDB grafoDB = new GrafoDB();
        Elastic e = new Elastic();
        List<Statistic> statistics = statisticRepository.findAll();
        List<Sport> deportecitos = sportRepository.findAll();
        List<Country> paiseeees = countryRepository.findAll();
        /*for(Statistic st : statistics){
            if(st.getStatisticCount() > 400){
                grafo.crearRelacionDeportePais(st.getSport().getSportName(), st.getCountry().getCountryName());
            }
        }*/
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();
        int i = 0;
        /*for(Statistic statistic : statistics){
            Map<String, Object> paisito = grafoDB.map("name", statistic.getCountry().getCountryName(), "label", "Country");
            int aux = nodes.indexOf(paisito);
            if(aux == -1){
                nodes.add(paisito);
            }
            int target = i;
            i++;
            for(Sport sp : deportecitos){
                Map<String, Object> deporte = grafoDB.map("Nombre", sp.getSportName(), "label", "Deporte");
                int source = nodes.indexOf(deporte);
                if(source == -1 && statistic.getStatisticCount() > 400){
                    nodes.add(deporte);
                    source = i++;
                }
                rels.add(grafoDB.map("source", source, "target", target));
            }
        }*/

        for(Country paisisito : paiseeees){
            ArrayList<String> listaDeportes = e.paisHabla(paisisito.getCountryName());
            nodes.add(grafoDB.map("name", paisisito.getCountryName(), "label", "Pais"));
            int target = i;
            i++;
            for(String sport : listaDeportes){
                Map<String, Object> deporte = grafoDB.map("Nombre", sport, "label", "Deporte");
                int source = nodes.indexOf(deporte);
                if(source == -1){
                    nodes.add(deporte);
                    source = i++;
                }
                rels.add(grafoDB.map("source", source, "target", target));
            }
        }
        return grafoDB.map("nodes", nodes, "links", rels);
    }
}
