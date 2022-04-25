package ua.klieshchunov.spring.cinemaSpringProject.service;

import org.springframework.data.domain.Page;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SeanceService {
    Map<LocalDate,List<Seance>> collectSeancesByDate(List<Seance> seances);
    List<Seance> findAllSeances();
    List<Seance> findAllSeancesForMovie(Movie movie);
    boolean hasFreePlaces(Seance seance);
    void decrementFreePlacesQuantity(Seance seance) throws NoFreePlacesException;
    Page<Seance> findAllSeancesPaginatedAndSorted
            (Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    Seance findSeanceById(int seanceId);


}
