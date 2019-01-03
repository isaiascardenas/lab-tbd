package com.grupo.cuatro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="sport_keywords")
public class SportKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="sportkeyword_id")
    private Long sportKeywordId;

    @Column(name="sportkeyword_word")
    private String sportKeywordWord;

    //relaciones
    //muchas keywords pertenecen a un deporte
    @ManyToOne
    @JoinColumn(name="sport_id")
    @JsonBackReference("sport-sportkeyword")
    private Sport sport;

    //llave foranea transient para establecer la relacion
    private transient Long sportId;
}
