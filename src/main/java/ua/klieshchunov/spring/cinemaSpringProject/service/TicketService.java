package ua.klieshchunov.spring.cinemaSpringProject.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;

import java.util.List;
import java.util.Map;

public interface TicketService {
    List<Ticket> findAllBySeance(Seance seance);
    Map<Integer, Ticket> formHallMap(List<Ticket> ticketsForSeance);
}