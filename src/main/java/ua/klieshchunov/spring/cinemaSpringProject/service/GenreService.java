package ua.klieshchunov.spring.cinemaSpringProject.service;

import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAllGenres();
}
