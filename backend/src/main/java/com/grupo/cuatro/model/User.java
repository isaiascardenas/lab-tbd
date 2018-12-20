package com.grupo.cuatro.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="user_id")
    private Long userId;

    @Column(name="user_name")
    private String userName;

    @Column(name="user_email", unique = true)
    private String userEmail;

    public User(String nombre, String userEmail){
        this.userName = nombre;
    }
}
