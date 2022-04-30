package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "rentals")
public class Rental implements Serializable {

  public   Rental(){
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="dateStart")
    private LocalDate dateStart;

    @Column(name="dateFinish")
    private LocalDate dateFinish;

    @Column(name="payment")
    private float payment;

    @Transient
    @ManyToMany(mappedBy = "rentals")
    private Set <Movie> movies;


    public Rental(Long id, LocalDate dateStart, LocalDate dateFinish, float payment,Set<Movie>movies ) {
        this.id = id;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.payment = payment;
        this.movies=movies;

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(LocalDate dateFinish) {
        this.dateFinish = dateFinish;
    }

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }

}
