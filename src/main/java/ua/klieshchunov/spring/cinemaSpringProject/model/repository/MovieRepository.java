package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {
    @NonNull
    @Override
    List<Movie> findAll();

    @NonNull
    @Override
    <S extends Movie> S save(@NonNull S movie);

    @Query("select distinct showtime.movie from Showtime showtime where showtime.startDateEpochSeconds > :currentTime")
    List<Movie> findMoviesWithShowtimes(int currentTime);

    @Query("select distinct showtime.movie from Showtime showtime where showtime.startDateEpochSeconds > :currentTime")
    Page<Movie> findMoviesWithShowtimesPaginated(int currentTime, Pageable pageable);

    Page<Movie> findAll(Pageable pageable);

    Movie findById(@Param("id") int id);

}
