package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
//    @Query("SELECT movie FROM Movie movie")
    List<Movie> findAll();

//    @Query("SELECT movie FROM Movie movie WHERE movie.id=:id")
    Movie findById(@Param("id") int id);
}
