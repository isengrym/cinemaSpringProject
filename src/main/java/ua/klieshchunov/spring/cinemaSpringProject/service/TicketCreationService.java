package ua.klieshchunov.spring.cinemaSpringProject.service;

import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.TicketAlreadyExistsException;

public interface TicketCreationService {
    void createTicketAndDecrementFreePlacesTransactional(Ticket ticket)
            throws NoFreePlacesException, TicketAlreadyExistsException;

    void createTicketIfNotExists(Ticket ticket)
            throws TicketAlreadyExistsException;
}
