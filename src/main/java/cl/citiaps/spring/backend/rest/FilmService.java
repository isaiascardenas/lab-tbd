package cl.citiaps.spring.backend.rest;

import cl.citiaps.spring.backend.entities.Film;
import cl.citiaps.spring.backend.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/films")
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Film> getAllFilms(){ return filmRepository.findAll(); }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Film findOne(@PathVariable("id") Integer id) { return filmRepository.findOne(id); }
}
