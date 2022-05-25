package ua.klieshchunov.spring.cinemaSpringProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> findAllMovies();
    List<Movie> findMoviesWithSeances();
    Page<Movie> findMoviesWithSeancesPaginatedAndSorted(Pageable pageable);
    Page<Movie> findAllMoviesPaginatedAndSorted(Pageable pageable);
    Movie findMovieById(int movieId);
    void addMovie(Movie movie);
}
