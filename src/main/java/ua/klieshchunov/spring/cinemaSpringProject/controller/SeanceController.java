package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketService;
import ua.klieshchunov.spring.cinemaSpringProject.service.UserService;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.TicketAlreadyExistsException;

import java.util.List;

@Controller
@RequestMapping("seances")
public class SeanceController {
    private final SeanceService seanceService;
    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public SeanceController(SeanceService seanceService, TicketService ticketService, UserService userService) {
        this.seanceService = seanceService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllSeances(@RequestParam(defaultValue = "0") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "startDateEpochSeconds") String sortBy,
                                @RequestParam(defaultValue = "DSC") String sortDirection,
                                Model model) {
        Page<Seance> page = seanceService
                .findAllSeancesPaginatedAndSorted(pageNum, pageSize, sortBy, sortDirection);
        List<Seance> seancesPaginated = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("seances", seancesPaginated);
        return "seances/index";
    }
    @GetMapping("/{id}")
    public String getSpecificSeance(@PathVariable("id") int id,
                                    Model model) {
        Seance seance = seanceService.findSeanceById(id);
        List<Ticket> tickets = ticketService.findAllTicketsForSeance(seance);

        model.addAttribute("tickets", tickets);
        model.addAttribute("hallMap", ticketService.formHallMap(tickets));
        model.addAttribute("seance", seance);
        return "seances/show";
    }

    @GetMapping("/{id}/ticket")
    public String getTicketPage(@PathVariable("id") int seanceId,
                                @RequestParam(name = "rowId") int rowId,
                                @RequestParam(name="placeId") int placeId,
                                @ModelAttribute("ticket") Ticket ticket,
                                Model model) {
        Seance seance = seanceService.findSeanceById(seanceId);

        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(username);

        model.addAttribute("seance", seance);
        model.addAttribute("user", user);
        model.addAttribute("rowId", rowId);
        model.addAttribute("placeId", placeId);
        return "seances/ticket";
    }

    @PostMapping("/{id}/ticket")
    public String createTicket(@PathVariable("id") int seanceId,
                               @ModelAttribute("ticket") Ticket ticket,
                               Model model) {
        Seance seance = seanceService.findSeanceById(seanceId);
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(username);
        ticket.setSeance(seance);
        ticket.setUser(user);

        try {
            ticketService.createTicketAndDecrementFreePlaces(ticket);
        } catch (TicketAlreadyExistsException e) {
            System.out.println("Ticket for seance with id " +
                    ticket.getSeance().getId() +
                    " for row " + ticket.getRow() + " and" +
                    " for place " + ticket.getPlace() + " already exists.");
            return "redirect:/seances/" + seance.getId() + "?error";
        } catch (NoFreePlacesException e) {
            System.out.println("No free places for seance with id: " +
                    ticket.getSeance().getId());

            return "seances";
        }

        return "redirect:/profile/";
    }
}
