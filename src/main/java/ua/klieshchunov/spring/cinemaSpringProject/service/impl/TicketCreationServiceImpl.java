package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.TicketRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketCreationService;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.TicketAlreadyExistsException;
import javax.transaction.Transactional;

@Service
public class TicketCreationServiceImpl implements TicketCreationService {
    private final SeanceService seanceService;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketCreationServiceImpl(SeanceService seanceService,
                                     TicketRepository ticketRepository) {
        this.seanceService = seanceService;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void decrementFreePlaces(Ticket ticket)
            throws NoFreePlacesException {
        seanceService.decrementFreePlacesQuantity(ticket.getSeance());
    }

    @Override
    public void createTicketIfNotExists(Ticket ticket) throws TicketAlreadyExistsException {
        if (ticketAlreadyExists(ticket))
            throw new TicketAlreadyExistsException("Ticket already exists in DB");
        ticketRepository.save(ticket);
    }

    private boolean ticketAlreadyExists(Ticket ticket) {
        return ticketRepository.existsTicketBySeanceAndPlaceAndRow(
                ticket.getSeance(), ticket.getPlace(), ticket.getRow());
    }
}
