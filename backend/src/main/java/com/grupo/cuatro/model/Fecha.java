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
@Table(name = "fecha")
public class Fecha {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "fecha_id")
    private Long fechaId;

    @Column(name = "fecha_value")
    private String fechaValue;

    @Column(name = "fecha_count")
    private Long fechaCount;

    @ManyToOne
    @JoinColumn(name="statistic_id")
    private Statistic statistic;

}
