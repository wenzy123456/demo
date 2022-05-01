package com.example.demo.service;

import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {

    private MovieRepository movieRepository;

    @Autowired
    public void setMovieRepository(MovieRepository movieRepository) {

        this.movieRepository = movieRepository;
    }

    public MovieService() {
    }

    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void removeMovie(Movie movie) {
        movieRepository.delete(movie);
    }

    public Movie updateMovie(Movie movie) {
        movieRepository.save(movie);
        return movie;
    }

    public List <Movie> getAllMovies() {
        return movieRepository.findAll();

    }


    public Movie getMovieById(long id) {
        Movie movie = null;
        Optional <Movie> optional = movieRepository.findById(id);
        if (optional.isPresent()) {
            movie = optional.get();
        }
        return movie;
    }



    public List<Movie> getMovieByDescription(String description) {
        return movieRepository.getMovieByDescription(description);
    }
    public List<Movie> getMovieByActors(String actors) {
        return movieRepository.getMovieByActorsContains(actors);
    }


    public List<Movie> getMovieByName(String nameMovie) {
        return movieRepository.getMovieByNameMovie(nameMovie);
    }

}
