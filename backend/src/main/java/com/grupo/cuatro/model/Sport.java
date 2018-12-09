package com.grupo.cuatro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="sports")
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="sport_id")
    private Long sportId;

    @Column(name="sport_name")
    private String sportName;

    //relaciones
    //un deporte tiene muchas estadisticas
    @OneToMany(targetEntity = Statistic.class, mappedBy = "sport", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Statistic> statistics;

    //un deporte tiene muchos keywords
    @OneToMany(targetEntity = Keyword.class, mappedBy = "sport", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Keyword> keywords;

}
