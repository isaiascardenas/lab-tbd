package cl.citiaps.spring.backend.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import cl.citiaps.spring.backend.entities.FilmActorRepository;

public interface FilmActorRepository extends PagingAndSortingRepository<FilmActor, Integer> {
	

}