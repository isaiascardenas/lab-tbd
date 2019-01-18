package com.grupo.cuatro.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "influential_user")
public class InfluentialUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "influential_user_id")
    private Long influentialUserId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_influence")
    private Float userInfluence;

    //usuario habla de muchos deportes
    @ManyToMany
    @JoinTable(name = "user_sport",
            joinColumns = @JoinColumn(name = "influential_user_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_id"))
    private List<Sport> sports;

    //un usuario pertenece a un pais
    @Transient
    private Long countryId;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
}
