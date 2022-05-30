package ua.klieshchunov.spring.cinemaSpringProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.klieshchunov.spring.cinemaSpringProject.dto.ShowtimeDto;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.Interval;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ShowtimeService {
    Map<LocalDate,List<Showtime>> collectShowtimesByDate(List<Showtime> showtimes);
    Showtime findShowtimeById(int seanceId);
    List<Interval> collectIntervals(ShowtimeDto showtimeDto);
    List<Showtime> findAllFutureShowtimes();
    List<Showtime> findAllFutureShowtimesForMovie(Movie movie);
    Page<Showtime> findAllFutureShowtimesForMoviePaginatedAndSorted(Pageable pageable, Movie movie);
    Page<Showtime> findAllFutureShowtimesPaginatedAndSorted(Pageable pageable);
    boolean hasFreePlaces(Showtime showtime);
    boolean hasAlreadyEnded(Showtime showtime);
    void decrementFreePlacesQuantity(Showtime showtime) throws NoFreePlacesException;
    void addShowtime(Showtime showtime);
    void deleteShowtime(Showtime showtime);
}
