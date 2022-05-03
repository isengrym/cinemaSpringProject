package ua.klieshchunov.spring.cinemaSpringProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SeanceService {
    Map<LocalDate,List<Seance>> collectSeancesByDate(List<Seance> seances);
    Seance findSeanceById(int seanceId);
    List<Seance> findAllFutureSeances();
    List<Seance> findAllFutureSeancesForMovie(Movie movie);
    Page<Seance> findAllFutureSeancesPaginatedAndSorted(Pageable pageable);
    boolean hasFreePlaces(Seance seance);
    boolean hasAlreadyEnded(Seance seance);
    void decrementFreePlacesQuantity(Seance seance) throws NoFreePlacesException;




}
