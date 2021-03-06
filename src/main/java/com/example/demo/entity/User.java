package com.example.demo.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

   @Column(name = "name")
    private String name;

   @Column(name ="lastName")
    private String lastName;

   @Column(name = "email")
    private String email;

   @Column(name = "password")
    private String password;

    @JoinTable(
            name = "users_movies"
            , joinColumns = {
            @JoinColumn(name = "users_id")
    }
            , inverseJoinColumns = {
            @JoinColumn(name = "movies_id")
    }
    )

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Movie> movies;

    public Set <Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set <Movie> movies) {
        this.movies = movies;
    }

    public User() {
    }
    public User (Set<Movie> movies){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void checkOut(User user){
        Set<Movie> movies = user.getMovies();
        List<Movie> movie = new ArrayList <>(movies);
        for (Movie valueMovie : movie) {
            valueMovie.setRented(true);
        }
    }

}

