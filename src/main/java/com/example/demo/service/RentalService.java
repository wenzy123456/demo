package com.example.demo.service;

import com.example.demo.entity.Rental;
import com.example.demo.entity.User;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RentalService {
    @PersistenceContext
    private EntityManager em;

    private RentalRepository rentalRepository;

    @Autowired
    public void setRentalRepository(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public RentalService() {
    }

    public void addRental(Rental rental)  {
        rentalRepository.save(rental);
    }
    public Rental updateRental(Rental rental){
        rentalRepository.save(rental);
        return rental;
    }


    public void removeRental(Rental rental)  {
        rentalRepository.delete(rental);
    }

    public List <Rental> getAllRental()  {
        return rentalRepository.findAll();
    }


    public Rental getRentalById(long id)  {
        Rental rental = null;
        Optional <Rental> optional = rentalRepository.findById(id);
        if(optional.isPresent()){
            rental = optional.get();
        }
        return rental;
    }
    public void saveAllRental (List<Rental>rentals) {

        rentalRepository.saveAll(rentals);
    }

}
