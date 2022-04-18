package ua.klieshchunov.spring.cinemaSpringProject.service;

import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> findAll();
    Movie findById(int id);
}
