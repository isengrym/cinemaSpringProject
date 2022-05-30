package ua.klieshchunov.spring.cinemaSpringProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableAsync
@EntityScan(basePackages = {"ua.klieshchunov.spring.cinemaSpringProject.model.entity"})
public class CinemaSpringProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaSpringProjectApplication.class, args);
	}

}
