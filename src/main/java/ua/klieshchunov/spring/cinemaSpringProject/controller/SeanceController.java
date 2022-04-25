package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketService;

import java.util.List;

@Controller
@RequestMapping("seances")
public class SeanceController {
    private final SeanceService seanceService;
    private final TicketService ticketService;

    @Autowired
    public SeanceController(SeanceService seanceService, TicketService ticketService) {
        this.seanceService = seanceService;
        this.ticketService = ticketService;
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
    public String getSpecificSeance(@PathVariable("id") int id, Model model) {
        List<Ticket> tickets = ticketService.findAllTicketsForSeance(seanceService.findSeanceById(id));

        model.addAttribute("tickets", tickets);
        model.addAttribute("hallMap", ticketService.formHallMap(tickets));
        model.addAttribute("seance", seanceService.findSeanceById(id));
        return "seances/show";
    }

    @GetMapping("/{id}/ticket")
    public String getTicketPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        return "seances/ticket";
    }

    @PostMapping("/{id}/ticket")
    public String createTicket(@PathVariable("id") int id, Model model) {
        //Creating ticket
        return "redirect:/profile/show";
    }
}
