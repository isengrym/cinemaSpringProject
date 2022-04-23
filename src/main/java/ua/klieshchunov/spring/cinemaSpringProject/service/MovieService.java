package ua.klieshchunov.spring.cinemaSpringProject.service;

import org.springframework.data.domain.Page;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;

import java.util.List;

public interface MovieService {
    List<Movie> findAll();
    Page<Movie> findAllPaginatedSorted(Integer pageNumber, Integer pageSize);
    Movie findById(int id);

}
