package com.grupo.cuatro.repository;

import com.grupo.cuatro.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, Long> {
}
