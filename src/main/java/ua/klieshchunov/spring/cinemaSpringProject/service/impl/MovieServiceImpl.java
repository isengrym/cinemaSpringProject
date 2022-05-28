package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.MovieRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.MovieService;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.CurrentTime;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> findMoviesWithShowtimes() {
        int currentTime = CurrentTime.get();
        return movieRepository.findMoviesWithShowtimes(currentTime);
    }

    @Override
    public Page<Movie> findMoviesWithShowtimesPaginatedAndSorted(Pageable pageable) {
        int currentTime = CurrentTime.get();
        return movieRepository.findMoviesWithShowtimesPaginated(currentTime, pageable);
    }

    @Override
    public Page<Movie> findAllMoviesPaginatedAndSorted(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Override
    public Movie findMovieById(int id) {
        return movieRepository.findById(id);
    }

    @Override
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }
}
