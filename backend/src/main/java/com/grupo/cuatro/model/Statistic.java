package com.grupo.cuatro.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

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
    //muchas estadisticas tienen un pais
    @ManyToOne
    @JoinColumn(name="country_id")
    //@JsonManagedReference("statistic_country")
    private Country country;

    //muchas estadisticas tienen un deporte
    @ManyToOne
    @JoinColumn(name="sport_id")
    //@JsonManagedReference("statistic-sport")
    private Sport sport;

    //llave foranea transient para establecer la relacion con deporte y pais
    private transient Long sportId;
    private transient Long countryId;
}
