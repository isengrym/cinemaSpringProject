package ua.klieshchunov.spring.cinemaSpringProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"ua.klieshchunov.spring.cinemaSpringProject.model.entity"})
public class CinemaSpringProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaSpringProjectApplication.class, args);
	}

}
