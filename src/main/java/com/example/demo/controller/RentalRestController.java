package com.example.demo.controller;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Rental;
import com.example.demo.entity.User;
import com.example.demo.service.RentalService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class RentalRestController {
    public RentalRestController() {
    }


    private RentalService rentalService;

    @Autowired
    public void setRentalService(RentalService rentalService) {
        this.rentalService = rentalService;
    }


    @GetMapping(value = "/rentals")
    public List <Rental> getAllRentals() {
        return rentalService.getAllRental();
    }

    @GetMapping(value = "/rentals/{id}")
    public Rental getRentalById(@PathVariable("id") int id) {
        return rentalService.getRentalById(id);
    }

    @DeleteMapping(value = "/rentals/{id}")
    public void deleteRental(@PathVariable("id") int id) {
        Rental rental = rentalService.getRentalById(id);
        rentalService.removeRental(rental);
    }

    @PostMapping(value = "/rentals")
    public void addRental(@RequestBody Rental rental) {
        rentalService.addRental(rental);
    }

    @PutMapping(value = "/rentals/{id}")
    public Rental updatRentalById(@PathVariable("id") int id, @RequestBody Rental rental) {
        Rental rental1 = rentalService.getRentalById(id);
        rental1.setDateStart(rental.getDateStart());
        rental1.setDateFinish(rental.getDateFinish());
        rental1.setPayment(rental.getPayment());
        return rentalService.updateRental(rental1);
    }
   @Bean
    public List<Rental> rentalDataDownload(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Rental> rentals = null;
        rentals = rentalService.getAllRental();
        try {
            objectMapper.writeValue(new File("src/main/resources/json/rentals.json"),rentals);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rentals;
    }
















}
