package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name="rentals")
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
    private BigDecimal payment;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set <Movie> movies;


    public Rental(Long id, LocalDate dateStart, LocalDate dateFinish, BigDecimal payment,Set<Movie>movies ) {
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

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

}
