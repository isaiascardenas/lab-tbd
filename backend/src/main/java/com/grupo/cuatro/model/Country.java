package com.grupo.cuatro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.lang.annotation.Target;
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

    //relaciones
    //muchos paises tiene muchas estadisticas
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "countries_statistics",
            joinColumns = @JoinColumn(name = "country_id", referencedColumnName = "country_id"),
            inverseJoinColumns = @JoinColumn(name = "statistic_id", referencedColumnName = "statistic_id"))
    private List<Statistic> statistics;

    //un pais tiene muchos tweetCounts
    @OneToMany(targetEntity = TweetCount.class, mappedBy = "country", cascade = CascadeType.ALL)
    private List<TweetCount> tweetCounts;

    //metodo para agregar estadistica
    public void addStatistic(Statistic statistic) {
        this.statistics.add(statistic);
    }
}