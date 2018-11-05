package cl.citiaps.spring.backend.rest;

import cl.citiaps.spring.backend.entities.Actor;
import cl.citiaps.spring.backend.entities.Film;
import cl.citiaps.spring.backend.repository.ActorRepository;
import cl.citiaps.spring.backend.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/films")
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ActorRepository actorRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Film> getAllFilms(){ return filmRepository.findAll(); }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Film findOne(@PathVariable("id") Integer id) { return filmRepository.findOne(id); }

    @RequestMapping(value = "/{id}/actors/{id_actor}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Film addActor(@PathVariable("id") Integer id, @PathVariable("id_actor") Integer id_actor){
        Film pelicula = this.filmRepository.findOne(id);
        Actor actore = this.actorRepository.findOne(id_actor);
        pelicula.actors.add(actore);
        return filmRepository.save(pelicula);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Film createFilm(@RequestBody Film resource){
        return this.filmRepository.save(resource);
    }
}
