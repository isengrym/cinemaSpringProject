package ua.klieshchunov.spring.cinemaSpringProject.service;

import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SeanceService {
    List<LocalDate> collectDatesOfSeances(List<Seance> seances);
    Map<LocalDate,List<Seance>> collectSeancesByDate(List<LocalDate> dates, List<Seance> seances);
    List<Seance> findAll();
    List<Seance> findAllByMovie(Movie movie);
    Seance findById(int id);

}
