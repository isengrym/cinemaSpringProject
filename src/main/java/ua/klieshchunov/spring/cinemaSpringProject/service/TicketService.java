package ua.klieshchunov.spring.cinemaSpringProject.service;

import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.TicketAlreadyExistsException;

import java.util.List;
import java.util.Map;

public interface TicketService {
    List<Ticket> findAllTicketsForSeance(Seance seance);
    Map<Integer, Ticket> formHallMap(List<Ticket> ticketsForSeance);
    void createTicketAndDecrementFreePlaces(Ticket ticket) throws TicketAlreadyExistsException;
}
