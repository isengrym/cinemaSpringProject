package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import java.util.List;

public interface SeanceRepository extends PagingAndSortingRepository<Seance, Integer> {
    Seance findById(@Param("id") int id);
    List<Seance> findAll();
    List<Seance> findAllByMovie(@Param("movie") Movie movie);
    Page<Seance> findAll(Pageable pageable);
}