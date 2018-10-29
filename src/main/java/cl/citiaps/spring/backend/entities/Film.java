package cl.citiaps.spring.backend.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="film")
@NamedQuery(name="Film.findAll", query="SELECT f FROM Film f")
public class Film implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="film_id", unique=true, nullable = false)
    private int filmId;

    @Column(name = "title", nullable = false, length = 45)
    private String filmTitle;

    @Column(name = "description", nullable = false)
    private String filmDescription;

    @Column(name = "release_year", nullable = false)
    private int filmYear;

    @Column(name = "language_id", nullable = false)
    private int filmLanguageId;

    @Column(name = "original_language_id")
    private Long filmOriginalLanguajeId;

    @Column(name = "rental_duration", nullable = false)
    private int filmRentalDuration;

    @Column(name = "rental_rate", nullable = false)
    private double filmRentalRate;

    @Column(name = "length", nullable = false)
    private int filmLength;

    @Column(name = "replacement_cost", nullable = false)
    private double filmReplacementCost;

    @Column(name = "rating", nullable = false)
    private String filmRating;

    @Column(name = "special_features", nullable = false)
    private String filmFeatures;

    @Column(name = "last_update", nullable = false)
    private Timestamp lastUpdate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name="actor_id"))
    public List<Actor> actors;

    public Film() {

    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public String getFilmDescription() {
        return filmDescription;
    }

    public void setFilmDescription(String filmDescription) {
        this.filmDescription = filmDescription;
    }

    public int getFilmYear() {
        return filmYear;
    }

    public void setFilmYear(int filmYear) {
        this.filmYear = filmYear;
    }

    public int getFilmLanguageId() {
        return filmLanguageId;
    }

    public void setFilmLanguageId(int filmLanguageId) {
        this.filmLanguageId = filmLanguageId;
    }

    public Long getFilmOriginalLanguajeId() {
        return filmOriginalLanguajeId;
    }

    public void setFilmOriginalLanguajeId(Long filmOriginalLanguajeId) {
        this.filmOriginalLanguajeId = filmOriginalLanguajeId;
    }

    public int getFilmRentalDuration() {
        return filmRentalDuration;
    }

    public void setFilmRentalDuration(int filmRentalDuration) {
        this.filmRentalDuration = filmRentalDuration;
    }

    public double getFilmRentalRate() {
        return filmRentalRate;
    }

    public void setFilmRentalRate(double filmRentalRate) {
        this.filmRentalRate = filmRentalRate;
    }

    public int getFilmLength() {
        return filmLength;
    }

    public void setFilmLength(int filmLength) {
        this.filmLength = filmLength;
    }

    public double getFilmReplacementCost() {
        return filmReplacementCost;
    }

    public void setFilmReplacementCost(double filmReplacementCost) {
        this.filmReplacementCost = filmReplacementCost;
    }

    public String getFilmRating() {
        return filmRating;
    }

    public void setFilmRating(String filmRating) {
        this.filmRating = filmRating;
    }

    public String getFilmFeatures() {
        return filmFeatures;
    }

    public void setFilmFeatures(String filmFeatures) {
        this.filmFeatures = filmFeatures;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
