package com.example.demo.controller;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Rental;
import com.example.demo.entity.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MovieService;
import com.example.demo.service.RentalService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
public class UserRestController {
    public UserRestController() {
    }


    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    private MovieService movieService;
    @Autowired
    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
    }
    private RentalService rentalService;

    @Autowired
    public void setRentalService(RentalService rentalService) {
        this.rentalService = rentalService;
    }


    @GetMapping(value = "/")
    public List <User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }
    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable("id") int id) {
        User user = userService.getUserById(id);
        userService.removeUser(user);
    }


    @PostMapping(value = "/")
    public void addUser(@RequestBody User user) {
     /*   LocalDate movieRelease = movie.getDateRelease();
        System.out.println(movieRelease.toString());
        movieRelease = LocalDate.of(2022, 03, 8);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse(String.valueOf(movieRelease));
            secondDate = df.parse(String.valueOf(LocalDate.now()));

        } catch (Exception e) {

        }

        int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        System.out.println(nDay);
        if (nDay <= 364) {
            movie.setPriceCategory("NEW 5 EUR/week");
            movieService.addMovie(movie);
        }
        if ((nDay > 364) & (nDay <= 1092)) {
            movie.setPriceCategory("USUAL 3.49 EUR/week");
            movieService.addMovie(movie);
        }
        if (nDay > 1092) {
            movie.setPriceCategory("OLD 1.99 EUR/week");
            movieService.addMovie(movie);
        }
        else*/
        userService.addUser(user);
     //   movieService.addMovie(movie);
      //  rentalService.addRental(rental);
    }

    @PutMapping(value = "/{id}")
    public User updatUserById(@PathVariable("id") int id, @RequestBody User user) {
        User user1 = userService.getUserById(id);
        user1.setName(user.getName());
        user1.setLastName(user.getLastName());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        return userService.updateUser(user1);
    }




  /*  @RequestMapping("json")
    public void json() {
        // URL url= this.getClass().getClassLoader().getResource("classpath : users.json");
        File jsonFile = null;
        try {
            jsonFile = ResourceUtils.getFile("src/main/resources/json/users.json");
            ObjectMapper objectMapper = new ObjectMapper();
            List <User> users = null;
            try {
                users = objectMapper.readValue(jsonFile, new TypeReference <List <User>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            userService.saveAllUsers(users);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

    @Bean
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
    }
/* @Bean
 public List<User> userDataDownload(){
     ObjectMapper objectMapper = new ObjectMapper();
     List<User> listusers = null;
     listusers= userService.getAllUsers();
     try {
         objectMapper.writeValue(new File("src/main/resources/json/users.json"), listusers);

     } catch (IOException e) {
         e.printStackTrace();
     }
     return listusers;
 }*/


}
