package ua.klieshchunov.spring.cinemaSpringProject.service;

import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.TicketAlreadyExistsException;

public interface TicketCreationService {
    void decrementFreePlaces(Ticket ticket)
            throws NoFreePlacesException;

    void createTicketIfNotExists(Ticket ticket)
            throws TicketAlreadyExistsException;
}
