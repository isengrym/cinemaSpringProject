package ua.klieshchunov.spring.cinemaSpringProject.service;

import org.springframework.data.domain.Page;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> findAllMovies();
    Page<Movie> findAllMoviesPaginatedAndSorted(Integer pageNumber, Integer pageSize);
    Movie findMovieById(int movieId);

}
