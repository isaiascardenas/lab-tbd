package com.grupo.cuatro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "country_id")
    private Long countryId;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "country_tweet_count")
    private Long countryTweetCount;

    /*@Column(name = "influencia")
    private Double influencia;*/

    //relaciones
    //un pais pertenece a muchas estadisticas
    //fetch = eager
    @OneToMany(targetEntity = Statistic.class, mappedBy = "country", cascade = CascadeType.ALL)
    @JsonBackReference("statistic-country")
    private List<Statistic> statistics;

    //un pais tiene muchos country keywords
    @OneToMany(targetEntity = CountryKeyword.class, mappedBy = "country", cascade = CascadeType.ALL)
    @JsonManagedReference("country-countryKeyword")
    private List<CountryKeyword> countryKeywords;

    //metodo para agregar estadistica
    public void addStatistic(Statistic statistic) {
        this.statistics.add(statistic);
    }
}