package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {
    @Override
    List<Movie> findAll();

    @Override
    <S extends Movie> S save(S movie);

    @Query("select distinct seance.movie from Seance seance where seance.startDateEpochSeconds > :currentTime")
    List<Movie> findMoviesWithSeances(int currentTime);

    @Query("select distinct seance.movie from Seance seance where seance.startDateEpochSeconds > :currentTime")
    Page<Movie> findMoviesWithSeances(int currentTime, Pageable pageable);

    Page<Movie> findAll(Pageable pageable);

    Movie findById(@Param("id") int id);
}
