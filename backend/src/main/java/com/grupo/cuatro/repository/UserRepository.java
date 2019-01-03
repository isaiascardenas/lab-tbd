package com.grupo.cuatro.repository;

import com.grupo.cuatro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}



