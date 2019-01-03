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
@Table(name="sports")
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="sport_id")
    private Long sportId;

    @Column(name="sport_name")
    private String sportName;

    @Column(name="sport_tweet_count")
    private Long sportTweetCount;

    //relaciones
    //un deporte pertenece a muchas estadisticas
    @OneToMany(targetEntity = Statistic.class, mappedBy = "sport", cascade = CascadeType.ALL)
    @JsonBackReference("statistic-sport")
    private List<Statistic> statistics;

    //un deporte tiene muchos sport keywords
    @OneToMany(targetEntity = SportKeyword.class, mappedBy = "sport", cascade = CascadeType.ALL)
    @JsonManagedReference("sport-sportkeyword")
    private List<SportKeyword> sportKeywords;

}
