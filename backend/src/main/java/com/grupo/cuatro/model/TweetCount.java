package com.grupo.cuatro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "tweet_counts")
public class TweetCount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="count_id")
    private Long countId;

    @Column(name="count")
    private Long count;

    @Column(name="count_date")
    private Date countDate;

    //relaciones
    //muchos tweetcounts pertenecen a un pais
    @ManyToOne
    @JoinColumn(name="country_id")
    @JsonBackReference("country-counts")
    private Country country;
}
