package com.example.demo.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


@Entity
@Table(name = "movies")
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="dateRelease")
    private LocalDate dateRelease;

    @Column(name ="nameMovie")
    private String nameMovie;

    @Column(name="priceCategory")
     private String priceCategory;

     @Column(name="actors")
     private String actors;

     @Column(name="description")
     private String description;

     @Column(name="rented")
     private boolean rented;


    @JoinTable(
            name = "movies_rentals"
            , joinColumns = {
            @JoinColumn(name = "movies_id")
    }
            , inverseJoinColumns = {
            @JoinColumn(name = "rentals_id")
    }
    )

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Rental> rentals;


    public Set<Rental> getRentals() {
        return rentals;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public void setRentals(Set <Rental> rentals) {
        this.rentals = rentals;
    }

    public Movie() {
  }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(LocalDate dateRelease) {
        this.dateRelease = dateRelease;
    }

    public String getNameMovie() {
        return nameMovie;
    }

    public void setNameMovie(String nameMovie) {
        this.nameMovie = nameMovie;
    }

    public String getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(String priceCategory) {
        this.priceCategory = priceCategory;

    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    boolean checkOut(Movie movie){
      return movie.isRented();
    }
}