package com.example.demo;

import com.example.demo.entity.Movie;
import com.example.demo.entity.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.service.MovieService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

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

	private MovieRepository movieRepository;

	@Autowired
	public void setMovieRepository(MovieRepository movieRepository) {

		this.movieRepository = movieRepository;
	}





	public DemoApplication() throws JsonProcessingException {
	}



	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(DemoApplication.class, args);

	}


/*	@Bean
	CommandLineRunner runner(UserService userService){
		return args -> {
			// read JSON and load json
			ObjectMapper mapper = new ObjectMapper();
			TypeReference <List <User>> typeReference = new TypeReference<List<User>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("src/main/resources/json/users.json");
			System.out.println(inputStream.toString());
			try {
				List<User> users = mapper.readValue(inputStream,typeReference);
				userService.saveAllUsers(users);
				System.out.println("Users Saved!");
			} catch (IOException e){
				System.out.println("Unable to save users: " + e.getMessage());
			}
		};
	}*/
//creating one


}
