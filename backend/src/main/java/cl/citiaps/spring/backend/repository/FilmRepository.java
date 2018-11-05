package cl.citiaps.spring.backend.repository;

import cl.citiaps.spring.backend.entities.Film;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FilmRepository extends PagingAndSortingRepository<Film, Integer> {
}
