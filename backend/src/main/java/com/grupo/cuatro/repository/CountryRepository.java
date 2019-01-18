package com.grupo.cuatro.repository;

import com.grupo.cuatro.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country getCountryByCountryNameIsLike(String countryName);
}
