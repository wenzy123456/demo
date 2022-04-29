package com.example.demo.repository;

import com.example.demo.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository <Movie, Long> {

    Movie findAllById(long id);

 /*

    List<Movie>getMoviesByPriceCategory();

    List<Movie>getMoviesByNameMovie();*/

  List <Movie> getMovieByDescription(String description);

  List <Movie> getMovieByActorsContains(String actors);

  List<Movie>getMovieByNameMovie(String nameMovie);


  //List <Movie> getMovieByDateRelease_Year(LocalDate dateRelease);


}
