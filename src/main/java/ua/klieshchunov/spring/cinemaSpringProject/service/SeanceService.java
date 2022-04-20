package ua.klieshchunov.spring.cinemaSpringProject.service;

import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import java.util.List;

public interface SeanceService {
    List<Seance> findAll();
    Seance findById(int id);

}
