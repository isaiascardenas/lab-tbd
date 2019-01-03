package com.grupo.cuatro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="country_keywords")
public class CountryKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="countryKeyword_id")
    private Long countryKeywordId;

    @Column(name="countryKeyword_word")
    private String countryKeywordWord;

    //relaciones
    //muchas country keywords pertenecen a un pais
    @ManyToOne
    @JoinColumn(name="country_id")
    @JsonBackReference("country-countryKeyword")
    private Country country;

    //llave foranea transient para establecer la relacion con pais
    private transient Long countryId;
}
