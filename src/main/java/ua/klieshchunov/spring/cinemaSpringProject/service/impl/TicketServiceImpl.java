package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.TicketRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService{
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> findAllTicketsForSeance(Seance seance) {
        return ticketRepository.findAllBySeance(seance);
    }

    @Override
    public Map<Integer, Ticket> formHallMap(List<Ticket> ticketsForSeance) {
        final int rows = 7;
        final int placesInRow = 11;
        boolean placeAlreadyFilledIn;

        Map<Integer,Ticket> hallMap = new HashMap<>();
        for(int row=1; row<=rows; row++) {

            for(int place=1; place<placesInRow; place++) {
                placeAlreadyFilledIn = false;
                int absolutePlaceNumber = calculateAbsolutePlaceNumber(row, place, placesInRow);

                for(Ticket existingTicket : ticketsForSeance) {
                    if (row == existingTicket.getRowNumber() && place == existingTicket.getPlaceNumber()) {
                        hallMap.put(absolutePlaceNumber, existingTicket);
                        placeAlreadyFilledIn = true;
                        break;
                    }
                }
                if (!placeAlreadyFilledIn)
                    hallMap.put(absolutePlaceNumber, null);
            }
        }
        return hallMap;
    }

    private int calculateAbsolutePlaceNumber(int row, int placeInRow, int totalPlacesInRow) {
        return (row - 1) * totalPlacesInRow + placeInRow;
    }
}
