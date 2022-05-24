package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findAll();
    @Query("select distinct seance.movie from Seance seance")
    List<Movie> findMoviesWithSeances();
    Page<Movie> findAll(Pageable pageable);
    Movie findById(@Param("id") int id);
}
