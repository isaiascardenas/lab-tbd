package com.grupo.cuatro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="keywords")
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="keyword_id")
    private Long keywordId;

    @Column(name="keyword_word")
    private String keywordWord;

    //relaciones
    //muchas keywords pertenecen a un deporte
    @ManyToOne
    @JoinColumn(name="sport_id")
    @JsonBackReference("sport-keyword")
    private Sport sport;

    //llave foranea transient para establecer la relacion con deporte
    private transient Long sportId;
}
