package com.grupo.cuatro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name="statistic_kind")
    private String statisticKind;

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
