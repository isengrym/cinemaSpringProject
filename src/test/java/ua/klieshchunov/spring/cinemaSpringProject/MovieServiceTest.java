package ua.klieshchunov.spring.cinemaSpringProject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Genre;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.MovieRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.MovieServiceImpl;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieServiceImpl movieService;

    @Test
    void shouldFindMovieById() {
        Movie movie = new Movie();
        movie.setId(1);
        movie.setTitle("Title");
        movie.setDirector("Director");
        movie.setAgeRestriction(18);
        movie.setDuration(120);
        movie.setImagePath("path");
        movie.setGenre(new Genre(1,"name"));

        when(movieRepository.findById(1)).thenReturn(movie);
        Movie movieFoundByService = movieService.findMovieById(1);

        Assertions.assertEquals(movieRepository.findById(1), movieFoundByService);
    }
}
