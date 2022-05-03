package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import java.util.List;

public interface SeanceRepository {
    Seance findById(int id);
    List<Seance> findAll();
    List<Seance> findAllByMovie(Movie movie);
    Page<Seance> findAllFutureSeancesPaginatedAndSorted(int currentTime, Pageable pageable);
    void decrementFreePlaces(int id);
}