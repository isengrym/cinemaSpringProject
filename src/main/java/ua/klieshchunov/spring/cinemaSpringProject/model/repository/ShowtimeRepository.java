package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;

import java.util.List;

public interface ShowtimeRepository extends PagingAndSortingRepository<Showtime, Integer> {
    Showtime findById(int id);

    @NonNull
    List<Showtime> findAll();

    @Query("select showtime from Showtime showtime where showtime.startDateEpochSeconds > :currentTime and showtime.movie = :movie")
    List<Showtime> findAllByMovie(int currentTime, Movie movie);

    @Query("select showtime from Showtime showtime where showtime.startDateEpochSeconds > :currentTime")
    Page<Showtime> findAll(int currentTime, Pageable pageable);

    @Query("select showtime from Showtime showtime where showtime.startDateEpochSeconds > :currentTime and showtime.movie = :movie")
    Page<Showtime> findAllByMovie(int currentTime, Movie movie, Pageable pageable);

    @Modifying
    @Query("update Showtime showtime set showtime.freePlaces = showtime.freePlaces-1 where showtime.id = :id")
    void decrementFreePlaces(@Param("id") int id);
}