package com.grupo.cuatro.controller;

import com.grupo.cuatro.Elastic;
import com.grupo.cuatro.model.*;
import com.grupo.cuatro.repository.CountryRepository;
import com.grupo.cuatro.repository.SportRepository;
import com.grupo.cuatro.repository.StatisticRepository;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    private Elastic e = new Elastic();

    @RequestMapping(value="/seed", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity seed() {
        java.sql.Date sqlDate;
        sqlDate = new java.sql.Date(System.currentTimeMillis()); //datetime for statistic
        List<Sport> sports = sportRepository.findAll();
        List<Country> countries = countryRepository.findAll();
        //valores para estadistica compuesta
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
                //se setea el valor acumulado
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
        santiago.setCountry(chile);
        iquique.setCountry(chile);
        shile.setCountry(chile);
        chile.setCountryKeywords(listaChile);
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
        buenosAires.setCountry(argentina);
        mendoza.setCountry(argentina);
        rosario.setCountry(argentina);
        argentina.setCountryKeywords(listaArgentina);
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
        Mexico.setCountryName("Mexico");
        ciudadDeMexico.setCountry(Mexico);
        puebla.setCountry(Mexico);
        guadalajara.setCountry(Mexico);
        Mexico.setCountryKeywords(listaMexico);
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
        barcelona.setCountry(espana);
        madrid.setCountry(espana);
        sevilla.setCountry(espana);
        espana.setCountryKeywords(listaEspana);
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
        medellin.setCountry(colombia);
        bogota.setCountry(colombia);
        cali.setCountry(colombia);
        colombia.setCountryKeywords(listaColombia);
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
        montevideo.setCountry(uruguay);
        salto.setCountry(uruguay);
        paysandu.setCountry(uruguay);
        uruguay.setCountryKeywords(listaUruguay);
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
        asuncion.setCountry(paraguay);
        ciudadDelEste.setCountry(paraguay);
        luque.setCountry(paraguay);
        paraguay.setCountryKeywords(listaParaguay);
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
        quito.setCountry(ecuador);
        cuenca.setCountry(ecuador);
        santoDomingo.setCountry(ecuador);
        ecuador.setCountryKeywords(listaEcuador);
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
        maracaibo.setCountry(venezuela);
        maracay.setCountry(venezuela);
        ciudadGuayana.setCountry(venezuela);
        venezuela.setCountryKeywords(listaVenezuela);
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

        return new ResponseEntity(HttpStatus.OK);
    }
}