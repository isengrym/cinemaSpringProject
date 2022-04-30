package ua.klieshchunov.spring.cinemaSpringProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.TicketAlreadyExistsException;
import java.util.List;
import java.util.Map;

public interface TicketService {
    List<Ticket> findAllTicketsForSeance(Seance seance);
    Page<Ticket> findTicketsByUserPaginatedAndSorted(User user, Pageable pageable);
    Map<Integer, Ticket> formHallMap(List<Ticket> ticketsForSeance);
    void createTicket(Ticket ticket) throws NoFreePlacesException, TicketAlreadyExistsException;

}
