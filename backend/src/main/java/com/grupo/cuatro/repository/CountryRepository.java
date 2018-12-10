package com.grupo.cuatro.repository;

import com.grupo.cuatro.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country getCountryByCountryNameIsLike(String countryName);
}
