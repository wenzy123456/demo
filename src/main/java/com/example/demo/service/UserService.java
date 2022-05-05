package com.example.demo.service;

import com.example.demo.entity.Movie;
import com.example.demo.entity.User;
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
public class UserService {

    @PersistenceContext
    EntityManager em;

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserService() {
    }

    public void addUser(User user)  {
        userRepository.save(user);
    }

    public User updateUser(User user){
        userRepository.save(user);
        return user;
    }


    public void removeUser(User user)  {
        userRepository.delete(user);
    }

    public List <User> getAllUsers()  {
        return userRepository.findAll();
    }


    public User getUserById(long id)  {
        User user = null;
        Optional <User> optional = userRepository.findById(id);
        if(optional.isPresent()){
            user = optional.get();
        }
        return user;
    }
    final static String QUERY = "select * from users\n" +
            "join users_movies ON users_movies.users_id = users.id\n" +
            "join movies on movies.id =users_movies.movies_id \n" +
            "join movies_rentals ON movies_rentals.movies_id = users_movies.movies_id\n" +
            "join rentals ON rentals.id=movies_rentals.rentals_id\n" +
            "where users.id=1;";
    public List<User> findAllUsersByNativeQuery() {
        return em.createNativeQuery(QUERY, User.class).getResultList();
    }



}
