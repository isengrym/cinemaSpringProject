package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface SeanceRepository extends CrudRepository<Seance, Integer> {
    Seance findById(@Param("id") int id);
    List<Seance> findAll();
    List<Seance> findAllByMovie(@Param("movie") Movie movie);
}