package com.example.demo.controller;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Rental;
import com.example.demo.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
public class MovieRestController {
    public MovieRestController() {
    }

    private MovieService movieService;
    @Autowired
    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(value = "/movies")
    public List <Movie> getAllMovies() {
        return movieService.getAllMovies();
    }
    @GetMapping(value = "/movies/description")
    public List<Movie> getMovieByDescription(@RequestBody String description) {
        return movieService.getMovieByDescription(description);

    }
    @GetMapping(value = "/movies/actors")
    public List<Movie> getMovieByActors(@RequestBody String actors) {
        return movieService.getMovieByActors(actors);

    }

    @GetMapping(value = "/movies/namemovie")
    public List<Movie> getMovieByNameMovie(@RequestBody String nameMovie) {
        return movieService.getMovieByName(nameMovie);
    }

    @GetMapping(value = "/movies/pricecategory")
    public List<Movie> getMovieByPriceCategory(@RequestBody String priceCategory) {
        return movieService.getMovieByPriceCategory(priceCategory);
    }

    @GetMapping(value = "/movies/daterelease")
    public List<Movie>getMovieByDateRelease(@RequestBody String s) {
        return movieService.findMovieByDateRelease(s);
    }

    @GetMapping(value = "/movies/{id}")
    public Movie geMovieById(@PathVariable("id") int id) {
        return movieService.getMovieById(id);
    }

    @DeleteMapping(value = "/movies/{id}")
    public void deleteMovie(@PathVariable("id") int id) {
        Movie movie = movieService.getMovieById(id);
        movieService.removeMovie(movie);
    }


    @PostMapping(value = "/movies")
    public void addMovie(@RequestBody  Movie movie) throws ParseException {
       Set<Rental>rentals = movie.getRentals();
       Rental []  rental = rentals.toArray(new Rental[0]);
        LocalDate movieRelease = movie.getDateRelease();
        String datemovie = movieRelease.toString();
        String datenow = LocalDate.now().toString();
        LocalDate rentalDateStart = rental[0].getDateStart();
        LocalDate rentalDateFinish = rental[0].getDateFinish();
        String datestart = rentalDateStart.toString();
        String datefinish= rentalDateFinish.toString();
        payment(rental[0],datemovie,datestart,datefinish);
        movieCategory(movie, nDays(datemovie,datenow));
    }
    @PutMapping(value = "/movies/{id}")
    public Movie updatMovieById(@PathVariable("id") int id, @RequestBody Movie movie) {
        Movie movie1 = movieService.getMovieById(id);
        movie1.setDateRelease(movie.getDateRelease());
        movie1.setNameMovie(movie.getNameMovie());
        movie1.setPriceCategory(movie.getPriceCategory());
        movie1.setActors(movie.getActors());
        movie1.setDescription(movie.getDescription());
        return movieService.updateMovie(movie1);
    }
    public  int nDays(String firstString, String secondString)throws NullPointerException{
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse( firstString );
            secondDate = df.parse( secondString );
        } catch (Exception e) {
         System.out.println("Wrong parse");
        }
        return  (int) (((secondDate != null ? secondDate.getTime() : 0) -((firstDate != null ? firstDate.getTime() : 0) ) / (24 * 60 * 60 * 1000)));
    }
    public  void movieCategory(Movie movie, int nDay) {
        if (nDay <= 364) {
            movie.setPriceCategory("NEW 5 EUR/week");
            movieService.addMovie(movie);

        }
        if ((nDay > 365) & (nDay <= 1092)) {
            movie.setPriceCategory("USUAL 3.49 EUR/week");
            movieService.addMovie(movie);

        }
        if ((nDay > 1092))  {
            movie.setPriceCategory("OLD 1.99 EUR/week");
            movieService.addMovie(movie);
        }

    }

    public void payment(Rental rental,String datemovie, String datestart, String dateend) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
        Date datem = df.parse( datemovie );
        Date dates = df.parse( datestart );
        Date datee = df.parse( dateend );
        LocalDate localDateMovie = datem.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
        LocalDate localDate364 = datem.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate().plusDays( 364 );
        LocalDate localDate1092 = datem.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate().plusDays( 1092 );
        LocalDate localDateStart = dates.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
        LocalDate localDateEnd = datee.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
        String date364 = localDate364.toString();
        String date1092 =localDate1092.toString();
        BigDecimal payment;

        if (nDays( datestart, dateend ) >= 365 | nDays(datestart,dateend)<0 |(localDateStart.isBefore( localDateMovie ) | localDateEnd.isBefore( localDateMovie ))) {
            System.out.println("LAEN KEELATUD" );
            return;

        }

        if (localDateStart.isBefore( localDate364 ) & localDateEnd.isBefore( localDate364 )| localDateEnd.isEqual( localDate364 )) {
            payment = BigDecimal.valueOf((nDays( datestart, dateend )*5f/7)).setScale(2,RoundingMode.CEILING );
            rental.setPayment(payment);

        }
        if ( localDateStart.isBefore( localDate364 ) & localDateEnd.isAfter( localDate364 ) & localDateEnd.isBefore( localDate1092 )| localDateStart.isEqual( localDate364 )) {
            payment = BigDecimal.valueOf(nDays( datestart, date364 )*5f/7 + nDays( date364, dateend )* 3.49f/7).setScale(2,RoundingMode.CEILING );
            rental.setPayment(payment);
        }
        if ( localDateStart.isBefore( localDate1092 ) & localDateEnd.isBefore( localDate1092 ) & localDateStart.isAfter( localDate364 )|localDateEnd.isEqual( localDate1092 )) {
            payment = BigDecimal.valueOf(nDays( datestart, dateend ) * 3.49f/7).setScale(2,RoundingMode.CEILING );
            rental.setPayment(payment);
        }

        if (localDateStart.isBefore( localDate1092 ) & localDateEnd.isAfter( localDate1092 )|localDateEnd.isEqual(localDate1092)) {
            payment = BigDecimal.valueOf(nDays( datestart, date1092 )* 3.49f/7 + nDays( date1092, dateend )*1.99f/7).setScale(2,RoundingMode.CEILING );
            rental.setPayment(payment);
        }
        if (localDateStart.isAfter( localDate1092 ) & localDateEnd.isAfter( localDate1092 )|localDateStart.isEqual( localDate1092 )) {
            payment = BigDecimal.valueOf(nDays( datestart, dateend ) * 1.99f / 7).setScale(2,RoundingMode.CEILING );
            rental.setPayment(payment);
        }

    }


   @Bean
    public List<Movie> movieDataDownload(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Movie> movies ;
        movies = movieService.getAllMovies();
        try {
            objectMapper.writeValue(new File("src/main/resources/json/movies.json"),movies);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
