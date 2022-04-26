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
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketService;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.TicketAlreadyExistsException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService{
    private final TicketRepository ticketRepository;
    private final SeanceService seanceService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, SeanceService seanceService) {
        this.ticketRepository = ticketRepository;
        this.seanceService = seanceService;
    }

    @Override
    public List<Ticket> findAllTicketsForSeance(Seance seance) {
        return ticketRepository.findAllTicketsBySeance(seance);
    }

    @Override
    public Page<Ticket> findTicketsByUserPaginatedAndSorted(User user, Integer pageNum, Integer pageSize) {
        Pageable customizedPageable = PageRequest.of(pageNum, pageSize);
        Page<Ticket> pageWithSeances =
                ticketRepository.findAllByUser(
                        user, customizedPageable);

        return pageWithSeances;
    }

    @Override
    @Transactional
    public void createTicketAndDecrementFreePlaces(Ticket ticket) throws
            TicketAlreadyExistsException, NoFreePlacesException{
            createTicketIfNotExists(ticket);
            seanceService.decrementFreePlacesQuantity(ticket.getSeance());
    }

    private void createTicketIfNotExists(Ticket ticket) throws TicketAlreadyExistsException {
        if (!ticketAlreadyExists(ticket)) {
            ticketRepository.save(ticket);
        }
        else
            throw new TicketAlreadyExistsException("Ticket already exists in DB");
    }

    private boolean ticketAlreadyExists(Ticket ticket) {
        return ticketRepository.existsTicketBySeanceAndPlaceAndRow(
                ticket.getSeance(), ticket.getPlace(), ticket.getRow());
    }

    @Override
    public Map<Integer, Ticket> formHallMap(List<Ticket> ticketsForSeance) {
        final int placesInRow = 11;

        Map<Integer,Ticket> hallMap = new HashMap<>();

        for(Ticket existingTicket : ticketsForSeance) {
            int absolutePlaceNumber = calculateAbsolutePlaceNumber(
                    existingTicket.getRow(), existingTicket.getPlace(), placesInRow);
            hallMap.put(absolutePlaceNumber, existingTicket);

        }


        return hallMap;
    }

    private int calculateAbsolutePlaceNumber(int row, int placeInRow, int totalPlacesInRow) {
        return (row - 1) * totalPlacesInRow + placeInRow;
    }


}
