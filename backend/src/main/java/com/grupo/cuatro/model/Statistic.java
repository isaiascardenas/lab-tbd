package com.grupo.cuatro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="statistics")
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="statistic_id")
    private Long statisticId;

    @Column(name="statistic_count")
    private Long statisticCount;

    @Column(name="statistic_query")
    private String statisticQuery;

    @Column(name="statistic_date")
    private Date statisticDate;

    //relaciones
    //muchas estadisticas pertenecen a muchos paises
    @ManyToMany(mappedBy = "statistics")
    @JsonBackReference("country-statistic")
    private List<Country> countries;

    //muchas estadisticas pertenecen a un deporte
    @ManyToOne
    @JoinColumn(name="sport_id")
    @JsonBackReference("sport-statistic")
    private Sport sport;

    //llave foranea transient para establecer la relacion con deporte
    private transient Long sportId;

    //metodo para agregar pais
    public void addCountry(Country country) {
        this.countries.add(country);
    }
}
