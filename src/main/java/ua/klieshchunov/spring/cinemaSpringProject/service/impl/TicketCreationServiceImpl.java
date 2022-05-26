package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.TicketRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.ShowtimeService;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketCreationService;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.TicketAlreadyExistsException;

@Service
public class TicketCreationServiceImpl implements TicketCreationService {
    private final ShowtimeService showtimeService;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketCreationServiceImpl(ShowtimeService showtimeService,
                                     TicketRepository ticketRepository) {
        this.showtimeService = showtimeService;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void decrementFreePlaces(Ticket ticket)
            throws NoFreePlacesException {
        showtimeService.decrementFreePlacesQuantity(ticket.getShowtime());
    }

    @Override
    public void createTicketIfNotExists(Ticket ticket) throws TicketAlreadyExistsException {
        if (ticketAlreadyExists(ticket))
            throw new TicketAlreadyExistsException("Ticket already exists in DB");
        ticketRepository.save(ticket);
    }

    private boolean ticketAlreadyExists(Ticket ticket) {
        return ticketRepository.existsTicketByShowtimeAndPlaceAndRow(
                ticket.getShowtime(), ticket.getPlace(), ticket.getRow());
    }
}
