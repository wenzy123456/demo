package com.example.demo.controller;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Rental;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
public class UserRestController {
    public UserRestController() {
    }


    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/users")
    public List <User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/users/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }
    @DeleteMapping(value = "users/{id}")
    public void deleteUser(@PathVariable("id") int id) {
        User user = userService.getUserById(id);
        userService.removeUser(user);
    }


    @PostMapping(value = "/users")
    public void addUser(@RequestBody User user) throws ParseException {
        Set <Movie> movies = user.getMovies();
        List<Movie> movie = new ArrayList <>(movies);
        if(movie.size()==0){
            userService.addUser(user);
            return;
        }
        movies(movie);
        userService.addUser(user);
    }

    private void movies(List <Movie> movie) throws ParseException {
        for (Movie value : movie) {
          Rental[] rental = value.getRentals().toArray(new Rental[0]);
            LocalDate movieRelease = value.getDateRelease();
            String datemovie = movieRelease.toString();
            String datenow = LocalDate.now().toString();
            LocalDate rentalDateStart = rental[0].getDateStart();
            LocalDate rentalDateFinish = rental[0].getDateFinish();
            String datestart = rentalDateStart.toString();
            String datefinish = rentalDateFinish.toString();
            payment(rental[0], datemovie, datestart, datefinish);
            movieCategory(value, nDays(datemovie, datenow));
        }
    }

    @PutMapping(value = "/users/{id}")
    public User updatUserById(@PathVariable("id") int id, @RequestBody User user) throws ParseException {
        User user1 = userService.getUserById(id);
        user1.setName(user.getName());
        user1.setLastName(user.getLastName());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        Set<Movie>movies = user.getMovies();
        List<Movie> movie = new ArrayList <>(movies);
        if(movie.size()==0) {
            userService.updateUser(user1);
        }
        movies(movie);
        user1.setMovies(user.getMovies());
        return userService.updateUser(user1);
    }
    public  int nDays(String firstString, String secondString) throws NullPointerException {
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse( firstString );
            secondDate = df.parse( secondString );
        } catch (Exception e) {
            System.out.println("Wrong parse");
        }
        if (secondDate != null) {
            return  (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        }

        return 0;
    }
    public  void movieCategory(Movie movie, int nDay) {
        if (nDay <= 364) {
            movie.setPriceCategory("NEW 5 EUR/week");
        }
        if ((nDay > 365) & (nDay <= 1092)) {
            movie.setPriceCategory("USUAL 3.49 EUR/week");
        }
        if ((nDay > 1092))  {
            movie.setPriceCategory("OLD 1.99 EUR/week");
        }
    }

    public void payment(Rental rental, String datemovie, String datestart, String dateend) throws ParseException {
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

        if (nDays( datestart, dateend ) >= 365 | nDays(datestart,dateend)<0 |(localDateStart.isBefore( localDateMovie ) | localDateEnd.isBefore( localDateMovie ))) {
            System.out.println("LAEN KEELATUD" );
            return;
        }
        if (localDateStart.isBefore( localDate364 ) & localDateEnd.isBefore( localDate364 )| localDateEnd.isEqual( localDate364 )) {
         BigDecimal   payment = BigDecimal.valueOf((nDays( datestart, dateend )*5f/7)).setScale(2, RoundingMode.DOWN );
            rental.setPayment(payment);
        }
        if ( localDateStart.isBefore( localDate364 ) & localDateEnd.isAfter( localDate364 ) & localDateEnd.isBefore( localDate1092 )| localDateStart.isEqual( localDate364 )) {
          BigDecimal  payment = BigDecimal.valueOf(nDays( datestart, date364 )*5f/7 + nDays( date364, dateend )* 3.49f/7).setScale(2,RoundingMode.DOWN );
            rental.setPayment(payment);
        }
        if ( localDateStart.isBefore( localDate1092 ) & localDateEnd.isBefore( localDate1092 ) & localDateStart.isAfter( localDate364 )|localDateEnd.isEqual( localDate1092 )) {
          BigDecimal  payment = BigDecimal.valueOf(nDays( datestart, dateend ) * 3.49f/7).setScale(2,RoundingMode.DOWN );
            rental.setPayment(payment);
        }

        if (localDateStart.isBefore( localDate1092 ) & localDateEnd.isAfter( localDate1092 )|localDateEnd.isEqual(localDate1092)) {
          BigDecimal  payment = BigDecimal.valueOf(nDays( datestart, date1092 )* 3.49f/7 + nDays( date1092, dateend )*1.99f/7).setScale(2,RoundingMode.DOWN );
            rental.setPayment(payment);
        }
        if (localDateStart.isAfter( localDate1092 ) & localDateEnd.isAfter( localDate1092 )|localDateStart.isEqual( localDate1092 )) {
          BigDecimal  payment = BigDecimal.valueOf(nDays( datestart, dateend ) * 1.99f / 7).setScale(2,RoundingMode.DOWN );
            rental.setPayment(payment);
        }
    }

    @GetMapping(value = "/queryusers")
    public List <User>findAllUsersByNative(@RequestBody String s) {
        return userService.findAllByNativeQuery(s);
    }

    /*    @Bean
    public List<User> dataDownload() {
        ObjectMapper objectMapper = new ObjectMapper();
        List <User> listusers = null;

        try {
        listusers = (List <User>) objectMapper.readValue(new File("src/main/resources/json/users.json"),User.class);
        //   listusers = (List <User>) objectMapper.readValue("{'id':1,'name':'Boris','lastName':'Nevzorov','email':'bn@mail.ru','password':'123'}",User.class);
            userService.saveAllUsers(listusers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listusers;
    }*/
 @Bean
 public List<User> userDataDownload(){
     ObjectMapper objectMapper = new ObjectMapper();
     List<User> listusers;
     listusers= userService.getAllUsers();
     try {
         objectMapper.writeValue(new File("src/main/resources/json/users.json"), listusers);

     } catch (IOException e) {
         e.printStackTrace();
     }
     return listusers;
 }

}
