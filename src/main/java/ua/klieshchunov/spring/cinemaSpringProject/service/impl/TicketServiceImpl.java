package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.TicketRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketCreationService;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketService;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.TicketAlreadyExistsException;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService{
    private final TicketRepository ticketRepository;
    private final TicketCreationService ticketCreationService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             TicketCreationService ticketCreationService) {
        this.ticketRepository = ticketRepository;
        this.ticketCreationService = ticketCreationService;
    }

    @Override
    public List<Ticket> findAllTicketsForSeance(Seance seance) {
        return ticketRepository.findAllTicketsBySeance(seance);
    }

    @Override
    public Page<Ticket> findTicketsByUserPaginatedAndSorted(User user, Pageable pageable) {
        Page<Ticket> pageWithSeances = getTickets(user, pageable);

        return pageWithSeances;
    }

    private Page<Ticket> getTickets(User user, Pageable pageable) {
        int pageNum = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Pageable customizedPageable = PageRequest.of(pageNum, pageSize);

        return ticketRepository.findAllByUser(user, customizedPageable);
    }

    @Override
    @Transactional(rollbackOn = {NoFreePlacesException.class, TicketAlreadyExistsException.class})
    public void createTicket(Ticket ticket)
            throws NoFreePlacesException, TicketAlreadyExistsException {
        ticketCreationService.decrementFreePlaces(ticket);
        ticketCreationService.createTicketIfNotExists(ticket);
    }

    @Override
    public Map<Integer, Ticket> formHallMap(List<Ticket> ticketsForSeance) {
        final int placesInRow = 11;
        Map<Integer,Ticket> hallMap = new HashMap<>();

        for(Ticket existingTicket : ticketsForSeance) {
            int absolutePlaceNumber = calculateAbsolutePlaceNumber(existingTicket, placesInRow);
            hallMap.put(absolutePlaceNumber, existingTicket);
        }

        return hallMap;
    }

    private int calculateAbsolutePlaceNumber(Ticket ticket, int totalPlacesInRow) {
        int placesBeforeRequiredRow = (ticket.getRow() - 1) * totalPlacesInRow;
        int placeInRequiredRow = ticket.getPlace();
        return  placesBeforeRequiredRow + placeInRequiredRow;
    }


}
