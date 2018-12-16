package com.grupo.cuatro.repository;

import com.grupo.cuatro.model.TweetCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetCountRepository extends JpaRepository<TweetCount, Long> {
}
