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

    @Column(name = "influencia_pais")
    private Long influenciaPais;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country_population")
    private String countryPopulation;

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

    //un pais tiene muchos usuarios influyentes
    @OneToMany(targetEntity = InfluentialUser.class, mappedBy = "country", cascade = CascadeType.ALL)
    @JsonBackReference("influential_user-country")
    private List<InfluentialUser> influentialUsers;

    @ManyToMany
    @JoinTable(name = "sport_country",
            joinColumns = @JoinColumn(name = "sport_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id"))
    private List<Sport> sports;


    //metodo para agregar estadistica
    public void addStatistic(Statistic statistic) {
        this.statistics.add(statistic);
    }
}