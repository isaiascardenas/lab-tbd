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

    /*@RequestMapping(value = "/grafito", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getGrafo(){
        System.out.println("****definiendo variables*****");
        List<Club>  equipos= clubRepository.findAll();
        ArrayList<Nodo> nodos = new ArrayList<Nodo>();
        ArrayList<Link> links = new ArrayList<Link>();


        int i =1;
        int indiceEquipo=1;
        int count=0;
        int countEquipo=0;
        System.out.println("****iniciando bucle*****");
        for (Club equipo: equipos) {
            if(equipo.getId()<17) {


                //AGREGO EQUIPO

                nodos.add(new Nodo(equipo.getName(), i, 8, equipo.getNeonInfluential().getStatistic_r(), equipo.getUrl()));
                countEquipo=count;
                System.out.println("****agregando equipo*****");
                count++;
                i++;
                int y = 1;
                for (UsuarioInfluyente user : equipo.getNeonInfluential().getUsuariosInfluyentes()) {

                    Nodo nuevoNodo = new Nodo(user.getName(), indiceEquipo, y, Math.round(user.getFollowers() / 3327729.8865619544), null);
//                int pos=nodos.indexOf(nuevoNodo);
                    int pos = Nodo.buscarNodo(nodos, nuevoNodo);
                    if (pos < 0) {


                        nodos.add(nuevoNodo);
                        System.out.println("****agregando usuario*****");

                        links.add(new Link(countEquipo, count));
                        System.out.println("****agregando link*****");
                        y++;
                        count++;
                    } else {
                        links.add(new Link(countEquipo, pos));
                    }

                }
                indiceEquipo++;

            }
        }
        System.out.println("****generando salida*****");

        Map<String,Object> salida = new HashMap<String,Object>();
        salida.put("nodos",nodos);
        salida.put("links",links);

        return salida;
    }*/

}
