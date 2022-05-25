package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.repository.CrudRepository;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Integer> {
    @Override
    List<Genre> findAll();
}
