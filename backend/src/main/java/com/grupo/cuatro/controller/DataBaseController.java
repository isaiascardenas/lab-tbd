package com.grupo.cuatro.controller;

import com.grupo.cuatro.Elastic;
import com.grupo.cuatro.model.*;
import com.grupo.cuatro.repository.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/database")
@CrossOrigin("*")
public class DataBaseController {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private SportRepository sportRepository;
    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired
    private FechaRepository fechaRepository;
    @Autowired
    private InfluentialUserRepository influentialUserRepository;

    private Elastic e = new Elastic();

    @RequestMapping(value="/seed_users", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity seedUsers() {
        List<String> usersList = e.getUsers();
        for(String user: usersList) {
            Float influencia = Float.parseFloat(e.getUserInfluencia(user));
            ArrayList<String> pais = e.getUserPais(user);
            List<String> deportesUsuario = e.usuarioHabla(user);
            Country country = countryRepository.getCountryByCountryNameIsLike(pais.get(0));
            //long influenciaPais = e.getInfluenciaPais(country.getCountryName());
            //country.setInfluenciaPais(influenciaPais);
            InfluentialUser influentialUser = new InfluentialUser();
            influentialUser.setUserName(user);
            influentialUser.setUserInfluence(influencia);
            influentialUser.setCountry(country);
            ArrayList<Sport> listaDeportesUsuario = new ArrayList<>();
            for(String dep : deportesUsuario){
                System.out.println(dep);
                Sport deporte = sportRepository.getSportBySportNameIsLike(dep);
                listaDeportesUsuario.add(deporte);
            }
            influentialUser.setSports(listaDeportesUsuario);
            influentialUserRepository.save(influentialUser);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/seed_sports", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity seedSports(){
        List<Country> listaPaises = countryRepository.findAll();
        ArrayList<Sport> listaDeportes = new ArrayList<>();
        for (Country pais : listaPaises){
            //System.out.println(pais.getCountryName());
            ArrayList<String> listaDeportesAux = e.paisHabla(pais.getCountryName());
            for(String dep : listaDeportesAux){
                Sport deporte = sportRepository.getSportBySportNameIsLike(dep);
                listaDeportes.add(deporte);
                //System.out.println(listaDeportes);
            }
            pais.setSports(listaDeportes);
            countryRepository.save(pais);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value="/seed", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity seed() {
        java.sql.Date sqlDate;
        sqlDate = new java.sql.Date(System.currentTimeMillis()); //datetime for statistic
        List<Sport> sports = sportRepository.findAll();
        List<Country> countries = countryRepository.findAll();

        for (Sport sport : sports) {
            for (Country country : countries) {
                Statistic statistic = new Statistic();
                statistic.setStatisticDate(sqlDate);
                statistic.setStatisticQuery("tweetCountBySportByCountry");
                statistic.setSport(sport);
                statistic.setCountry(country);
                Long statisticCount = 0L;

                //primero se acumula el contador por su nombre
                statisticCount += e.getCantidadDeportePais(sport.getSportName(), country.getCountryName());

                //luego por cada keyword
                for(CountryKeyword countryKeyword : country.getCountryKeywords()) {
                    for(SportKeyword sportKeyword : sport.getSportKeywords()) {
                        statisticCount += e.getCantidadDeportePais(sportKeyword.getSportKeywordWord(), countryKeyword.getCountryKeywordWord());
                    }
                }
                statistic.setStatisticCount(statisticCount);
                statisticRepository.save(statistic);
            }
        }
        //valores para estadistica individual
        for (Sport sport : sports) {
            Long sportTweetCount = 0L;
            sportTweetCount += e.getCantidad(sport.getSportName());
            for (SportKeyword sportKeyword : sport.getSportKeywords()) {
                sportTweetCount += e.getCantidad(sportKeyword.getSportKeywordWord());
            }
            sport.setSportTweetCount(sportTweetCount);
            sportRepository.save(sport);
        }

        for (Country country : countries) {
            Long countryTweetCount = 0L;
            countryTweetCount += e.getCantidadPais(country.getCountryName());
            for (CountryKeyword countryKeyword : country.getCountryKeywords()) {
                countryTweetCount += e.getCantidadPais(countryKeyword.getCountryKeywordWord());
            }
            country.setCountryTweetCount(countryTweetCount);
            countryRepository.save(country);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "seed/data", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity seedData(){

        //LLENANDO CHILE
        CountryKeyword santiago = new CountryKeyword();
        CountryKeyword iquique = new CountryKeyword();
        CountryKeyword shile = new CountryKeyword();
        santiago.setCountryKeywordWord("santiago");
        iquique.setCountryKeywordWord("iquique");
        shile.setCountryKeywordWord("concepcion");
        ArrayList<CountryKeyword> listaChile = new ArrayList<CountryKeyword>();
        listaChile.add(santiago);
        listaChile.add(iquique);
        listaChile.add(shile);
        Country chile = new Country();
        chile.setCountryName("Chile");
        chile.setCountryCode("CL");
        chile.setCountryPopulation(18751405);
        santiago.setCountry(chile);
        iquique.setCountry(chile);
        shile.setCountry(chile);
        chile.setCountryKeywords(listaChile);
        long influenciaChile = e.getInfluenciaPais(chile.getCountryName());
        chile.setInfluenciaPais(influenciaChile);
        countryRepository.save(chile);
        //LLENANDO CHILE

        //LENANDO ARGENTINA
        CountryKeyword buenosAires = new CountryKeyword();
        CountryKeyword mendoza = new CountryKeyword();
        CountryKeyword rosario = new CountryKeyword();
        buenosAires.setCountryKeywordWord("buenos aires");
        mendoza.setCountryKeywordWord("mendoza");
        rosario.setCountryKeywordWord("rosario");
        ArrayList<CountryKeyword> listaArgentina = new ArrayList<CountryKeyword>();
        listaArgentina.add(buenosAires);
        listaArgentina.add(mendoza);
        listaArgentina.add(rosario);
        Country argentina = new Country();
        argentina.setCountryName("Argentina");
        argentina.setCountryCode("AR");
        argentina.setCountryPopulation(44494512);
        buenosAires.setCountry(argentina);
        mendoza.setCountry(argentina);
        rosario.setCountry(argentina);
        argentina.setCountryKeywords(listaArgentina);
        long influenciaArgentina = e.getInfluenciaPais(argentina.getCountryName());
        argentina.setInfluenciaPais(influenciaArgentina);
        countryRepository.save(argentina);
        //LENANDO ARGENTINA

        //LENANDO MEXICO
        CountryKeyword ciudadDeMexico = new CountryKeyword();
        CountryKeyword puebla = new CountryKeyword();
        CountryKeyword guadalajara = new CountryKeyword();
        ciudadDeMexico.setCountryKeywordWord("ciudad de mexico");
        puebla.setCountryKeywordWord("puebla");
        guadalajara.setCountryKeywordWord("guadalajara");
        ArrayList<CountryKeyword> listaMexico = new ArrayList<CountryKeyword>();
        listaMexico.add(ciudadDeMexico);
        listaMexico.add(puebla);
        listaMexico.add(guadalajara);
        Country Mexico = new Country();
        Mexico.setCountryName("México");
        Mexico.setCountryCode("MX");
        Mexico.setCountryPopulation(123982528);
        ciudadDeMexico.setCountry(Mexico);
        puebla.setCountry(Mexico);
        guadalajara.setCountry(Mexico);
        Mexico.setCountryKeywords(listaMexico);
        long influenciaMexico = e.getInfluenciaPais(Mexico.getCountryName());
        Mexico.setInfluenciaPais(influenciaMexico);
        countryRepository.save(Mexico);
       //LLENANDO MEXICO

        //LLENANDO ESPAÑA
        CountryKeyword barcelona = new CountryKeyword();
        CountryKeyword madrid = new CountryKeyword();
        CountryKeyword sevilla = new CountryKeyword();
        barcelona.setCountryKeywordWord("barcelona");
        madrid.setCountryKeywordWord("madrid");
        sevilla.setCountryKeywordWord("sevilla");
        ArrayList<CountryKeyword> listaEspana = new ArrayList<CountryKeyword>();
        listaEspana.add(barcelona);
        listaEspana.add(madrid);
        listaEspana.add(sevilla);
        Country espana = new Country();
        espana.setCountryName("España");
        espana.setCountryCode("ES");
        espana.setCountryPopulation(46659312);
        barcelona.setCountry(espana);
        madrid.setCountry(espana);
        sevilla.setCountry(espana);
        espana.setCountryKeywords(listaEspana);
        long influenciaEspana = e.getInfluenciaPais(espana.getCountryName());
        espana.setInfluenciaPais(influenciaEspana);
        countryRepository.save(espana);
        //LLENANDO ESPAÑA

        //LLENANDO COLOMBIA
        CountryKeyword medellin = new CountryKeyword();
        CountryKeyword bogota = new CountryKeyword();
        CountryKeyword cali = new CountryKeyword();
        medellin.setCountryKeywordWord("medellin");
        bogota.setCountryKeywordWord("bogota");
        cali.setCountryKeywordWord("cali");
        ArrayList<CountryKeyword> listaColombia = new ArrayList<>();
        listaColombia.add(medellin);
        listaColombia.add(bogota);
        listaColombia.add(cali);
        Country colombia = new Country();
        colombia.setCountryName("Colombia");
        colombia.setCountryCode("CO");
        colombia.setCountryPopulation(4550000);
        medellin.setCountry(colombia);
        bogota.setCountry(colombia);
        cali.setCountry(colombia);
        colombia.setCountryKeywords(listaColombia);
        long influenciaColombia = e.getInfluenciaPais(colombia.getCountryName());
        colombia.setInfluenciaPais(influenciaColombia);
        countryRepository.save(colombia);
        //LLENANDO COLOMBIA

        //LLENANDO URUGUAY
        CountryKeyword montevideo = new CountryKeyword();
        CountryKeyword salto = new CountryKeyword();
        CountryKeyword paysandu = new CountryKeyword();
        montevideo.setCountryKeywordWord("montevideo");
        salto.setCountryKeywordWord("salto");
        paysandu.setCountryKeywordWord("paysandu");
        ArrayList<CountryKeyword> listaUruguay = new ArrayList<CountryKeyword>();
        listaUruguay.add(montevideo);
        listaUruguay.add(salto);
        listaUruguay.add(paysandu);
        Country uruguay = new Country();
        uruguay.setCountryName("Uruguay");
        uruguay.setCountryCode("UY");
        uruguay.setCountryPopulation(3519014);
        montevideo.setCountry(uruguay);
        salto.setCountry(uruguay);
        paysandu.setCountry(uruguay);
        uruguay.setCountryKeywords(listaUruguay);
        long influenciaUruguay = e.getInfluenciaPais(uruguay.getCountryName());
        uruguay.setInfluenciaPais(influenciaUruguay);
        countryRepository.save(uruguay);
        //LLENANDO URUGUAY

        //LLENANDO PARAGUAY
        CountryKeyword asuncion = new CountryKeyword();
        CountryKeyword ciudadDelEste = new CountryKeyword();
        CountryKeyword luque = new CountryKeyword();
        asuncion.setCountryKeywordWord("asuncion");
        ciudadDelEste.setCountryKeywordWord("ciudad del este");
        luque.setCountryKeywordWord("luque");
        ArrayList<CountryKeyword> listaParaguay = new ArrayList<>();
        listaParaguay.add(asuncion);
        listaParaguay.add(ciudadDelEste);
        listaParaguay.add(luque);
        Country paraguay = new Country();
        paraguay.setCountryName("Paraguay");
        paraguay.setCountryCode("PY");
        paraguay.setCountryPopulation(7152703);
        asuncion.setCountry(paraguay);
        ciudadDelEste.setCountry(paraguay);
        luque.setCountry(paraguay);
        paraguay.setCountryKeywords(listaParaguay);
        long influenciaParaguay = e.getInfluenciaPais(paraguay.getCountryName());
        paraguay.setInfluenciaPais(influenciaParaguay);
        countryRepository.save(paraguay);
        //LLENANDO PARAGUAY

        //LLENANDO ECUADOR
        CountryKeyword quito = new CountryKeyword();
        CountryKeyword cuenca = new CountryKeyword();
        CountryKeyword santoDomingo = new CountryKeyword();
        quito.setCountryKeywordWord("quito");
        cuenca.setCountryKeywordWord("cuenca");
        santoDomingo.setCountryKeywordWord("santo domingo");
        ArrayList<CountryKeyword> listaEcuador = new ArrayList<>();
        listaEcuador.add(quito);
        listaEcuador.add(cuenca);
        listaEcuador.add(santoDomingo);
        Country ecuador = new Country();
        ecuador.setCountryName("Ecuador");
        ecuador.setCountryCode("EC");
        ecuador.setCountryPopulation(17267986);
        quito.setCountry(ecuador);
        cuenca.setCountry(ecuador);
        santoDomingo.setCountry(ecuador);
        ecuador.setCountryKeywords(listaEcuador);
        long influenciaEcuador = e.getInfluenciaPais(ecuador.getCountryName());
        ecuador.setInfluenciaPais(influenciaEcuador);
        countryRepository.save(ecuador);
        //LLENANDO ECUADOR

        //LLENANDO VENEZUELA
        CountryKeyword maracaibo = new CountryKeyword();
        CountryKeyword maracay = new CountryKeyword();
        CountryKeyword ciudadGuayana = new CountryKeyword();
        maracaibo.setCountryKeywordWord("maracaibo");
        maracay.setCountryKeywordWord("maracay");
        ciudadGuayana.setCountryKeywordWord("ciudad guayana");
        ArrayList<CountryKeyword> listaVenezuela = new ArrayList<CountryKeyword>();
        listaVenezuela.add(maracaibo);
        listaVenezuela.add(maracay);
        listaVenezuela.add(ciudadGuayana);
        Country venezuela = new Country();
        venezuela.setCountryName("Venezuela");
        venezuela.setCountryCode("VE");
        venezuela.setCountryPopulation(31828000);
        maracaibo.setCountry(venezuela);
        maracay.setCountry(venezuela);
        ciudadGuayana.setCountry(venezuela);
        venezuela.setCountryKeywords(listaVenezuela);
        long influenciaVenezuela = e.getInfluenciaPais(venezuela.getCountryName());
        venezuela.setInfluenciaPais(influenciaVenezuela);
        countryRepository.save(venezuela);
        //LLENANDO VENEZUELA

        //LLENANDO RUGBY
        Sport rugby = new Sport();
        rugby.setSportName("Rugby");
        SportKeyword rugbista = new SportKeyword();
        rugbista.setSportKeywordWord("rugbista");
        ArrayList<SportKeyword> listaRugby = new ArrayList<>();
        listaRugby.add(rugbista);
        rugbista.setSport(rugby);
        rugby.setSportKeywords(listaRugby);
        sportRepository.save(rugby);
        //LLENANDO RUGBY

        //LLENANDO BASKET
        Sport basketball = new Sport();
        basketball.setSportName("Basketball");
        SportKeyword basket = new SportKeyword();
        SportKeyword basquet = new SportKeyword();
        SportKeyword basquetball = new SportKeyword();
        SportKeyword basquetbolista = new SportKeyword();
        SportKeyword koby = new SportKeyword();
        basket.setSportKeywordWord("basket");
        basquet.setSportKeywordWord("basquet");
        basquetball.setSportKeywordWord("basquetball");
        basquetbolista.setSportKeywordWord("basquetbolista");
        koby.setSportKeywordWord("Koby Bryant");
        ArrayList<SportKeyword> listaBasketball = new ArrayList<>();
        listaBasketball.add(basket);
        listaBasketball.add(basquet);
        listaBasketball.add(basquetball);
        listaBasketball.add(basquetbolista);
        listaBasketball.add(koby);
        basket.setSport(basketball);
        basquet.setSport(basketball);
        basquetball.setSport(basketball);
        basquetbolista.setSport(basketball);
        koby.setSport(basketball);
        basketball.setSportKeywords(listaBasketball);
        sportRepository.save(basketball);
        //LLENANDO BASKET

        //LLENANDO TENIS
        Sport tenis = new Sport();
        tenis.setSportName("Tenis");
        SportKeyword tenista = new SportKeyword();
        SportKeyword chino = new SportKeyword();
        SportKeyword martin = new SportKeyword();
        SportKeyword chinoDos = new SportKeyword();
        tenista.setSportKeywordWord("tenista");
        chino.setSportKeywordWord("Chino Rios");
        martin.setSportKeywordWord("Martin Del Potro");
        chinoDos.setSportKeywordWord("Marcelo Rios");
        ArrayList<SportKeyword> listaTenis = new ArrayList<>();
        listaTenis.add(tenista);
        listaTenis.add(chino);
        listaTenis.add(martin);
        listaTenis.add(chinoDos);
        tenista.setSport(tenis);
        chino.setSport(tenis);
        martin.setSport(tenis);
        chinoDos.setSport(tenis);
        tenis.setSportKeywords(listaTenis);
        sportRepository.save(tenis);
        //LLENANDO TENIS

        //LLENANDO BOXEO
        Sport boxeo = new Sport();
        boxeo.setSportName("Boxeo");
        SportKeyword myke = new SportKeyword();
        SportKeyword floyd = new SportKeyword();
        SportKeyword ali = new SportKeyword();
        SportKeyword boxeador = new SportKeyword();
        SportKeyword boxista = new SportKeyword();
        myke.setSportKeywordWord("Myke Tyson");
        floyd.setSportKeywordWord("Floyd Mayweather");
        ali.setSportKeywordWord("Muhammad Ali");
        boxeador.setSportKeywordWord("boxeador");
        boxista.setSportKeywordWord("boxista");
        ArrayList<SportKeyword> listaBoxeo = new ArrayList<>();
        listaBoxeo.add(myke);
        listaBoxeo.add(floyd);
        listaBoxeo.add(ali);
        listaBoxeo.add(boxeador);
        listaBoxeo.add(boxista);
        myke.setSport(boxeo);
        floyd.setSport(boxeo);
        ali.setSport(boxeo);
        boxeador.setSport(boxeo);
        boxista.setSport(boxeo);
        boxeo.setSportKeywords(listaBoxeo);
        sportRepository.save(boxeo);
        //LLENANDO BOXEO

        //LLENANDO VOLLEYBALL
        Sport volleyball = new Sport();
        volleyball.setSportName("Volleyball");
        SportKeyword volley = new SportKeyword();
        volley.setSportKeywordWord("volley");
        ArrayList<SportKeyword> listaVolley = new ArrayList<>();
        listaVolley.add(volley);
        volley.setSport(volleyball);
        volleyball.setSportKeywords(listaVolley);
        sportRepository.save(volleyball);
        //LLENANDO VOLLEYBALL

        //LLENANDO NATACION
        Sport natacion = new Sport();
        natacion.setSportName("Natación");
        SportKeyword nadador = new SportKeyword();
        SportKeyword phelps = new SportKeyword();
        nadador.setSportKeywordWord("nadador");
        phelps.setSportKeywordWord("Michael Phelps");
        ArrayList<SportKeyword> listaNatacion = new ArrayList<>();
        listaNatacion.add(nadador);
        listaNatacion.add(phelps);
        nadador.setSport(natacion);
        phelps.setSport(natacion);
        natacion.setSportKeywords(listaNatacion);
        sportRepository.save(natacion);
        //LLENANDO NATACION

        //LLENANDO FUTBOL FEMENINO
        Sport femenino = new Sport();
        femenino.setSportName("Futbol Femenino");
        SportKeyword ccFem = new SportKeyword();
        SportKeyword ccFemDos = new SportKeyword();
        SportKeyword femenino2 = new SportKeyword();
        ccFem.setSportKeywordWord("Colo Colo Femenino");
        ccFemDos.setSportKeywordWord("ColoColo Femenino");
        femenino2.setSportKeywordWord("Femenino");
        ArrayList<SportKeyword> listaFemenino = new ArrayList<>();
        listaFemenino.add(ccFem);
        listaFemenino.add(ccFemDos);
        listaFemenino.add(femenino2);
        ccFem.setSport(femenino);
        ccFemDos.setSport(femenino);
        femenino2.setSport(femenino);
        femenino.setSportKeywords(listaFemenino);
        sportRepository.save(femenino);
        //LLENANDO FUTBOL FEMENINO

        Elastic e = new Elastic();
        List<Fecha> fechas = fechaRepository.findAll();
        ArrayList<Long> listaFechas = new ArrayList<>();
        listaFechas = e.getCantidadFecha();
        System.out.println(listaFechas);
        //Statistic st = new Statistic();
        for(int i = 0; i < 31; i++){
            Fecha auxiliar = new Fecha();
            auxiliar.setFechaCount(listaFechas.get(i));
            if(i <= 9){

                auxiliar.setFechaValue("2018-12-"+"0"+(i+1));
            }
            else{
                auxiliar.setFechaValue("2018-12-"+(i+1));
            }
            //auxiliar.setStatistic(st);
            fechas.add(auxiliar);
            fechaRepository.save(auxiliar);

        }
        //st.setFechas(fechas);
        //statisticRepository.save(st);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity createIndex(){
        Elastic e = new Elastic();
        e.indexCreate();
        return new ResponseEntity(HttpStatus.OK);
    }
}