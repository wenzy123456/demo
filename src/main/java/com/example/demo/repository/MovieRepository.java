package com.example.demo.repository;

import com.example.demo.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository <Movie, Long> {



  List <Movie> getMovieByDescription(String description);

  List <Movie> getMovieByActorsContains(String actors);

  List<Movie>getMovieByNameMovie(String nameMovie);

  List<Movie>getMoviesByPriceCategoryContains(String priceCategory);



}
