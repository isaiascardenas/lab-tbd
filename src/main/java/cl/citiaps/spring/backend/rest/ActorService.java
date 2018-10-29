package cl.citiaps.spring.backend.rest;

import cl.citiaps.spring.backend.entities.Film;
import cl.citiaps.spring.backend.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cl.citiaps.spring.backend.entities.Actor;
import cl.citiaps.spring.backend.repository.ActorRepository;

@CrossOrigin(origins = "*")
@RestController  
@RequestMapping("/actors")
public class ActorService {
	
	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private FilmRepository filmRepository;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Iterable<Actor> getAllUsers() {
		return actorRepository.findAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Actor findOne(@PathVariable("id") Integer id) {
		return actorRepository.findOne(id);
	}

	@RequestMapping(value = "/{id}/films/{id_film}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Actor addFilm(@PathVariable("id") Integer id, @PathVariable("id_film") Integer id_film){
		Film pelicula = this.filmRepository.findOne(id_film);
		Actor actore = this.actorRepository.findOne(id);
		if(pelicula == null){
			System.out.println("Error, no existe usuario");
			return null;
		}
		actore.films.add(pelicula);
		return actorRepository.save(actore);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Actor create(@RequestBody Actor resource) {
	     return actorRepository.save(resource);
	}

}