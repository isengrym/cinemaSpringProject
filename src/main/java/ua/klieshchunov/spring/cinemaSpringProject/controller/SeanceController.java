package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.dto.PaginationDto;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.SeanceModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.PaginationService;
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
    private final PaginationService paginationService;
    private final SeanceModelFiller modelFiller;

    @Autowired
    public SeanceController(SeanceService seanceService,
                            TicketService ticketService,
                            UserService userService,
                            PaginationService paginationService,
                            SeanceModelFiller modelFiller) {
        this.seanceService = seanceService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.paginationService = paginationService;
        this.modelFiller = modelFiller;
    }

    @GetMapping
    public String getAllSeances(@RequestParam(defaultValue = "0") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "startDateEpochSeconds") String sortBy,
                                @RequestParam(defaultValue = "DSC") String sortOrder,
                                Model model) {

        Sort sort = paginationService.formSort(sortBy, sortOrder);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Seance> page = seanceService.findAllFutureSeancesPaginatedAndSorted(pageable);

        PaginationDto paginationDto = new PaginationDto(pageNum, sortBy, sortOrder);

        modelFiller.fillModelForPaginatedItems(page, paginationDto, model);
        return "seances/index";
    }

    @GetMapping("/{id}")
    public String getSpecificSeance(@PathVariable("id") int id,
                                    Model model) {
        Seance seance = seanceService.findSeanceById(id);
        if (seanceService.hasAlreadyEnded(seance))
            return "redirect:/seances/";

        List<Ticket> tickets = ticketService.findAllTicketsForSeance(seance);

        model.addAttribute("seance", seance);
        model.addAttribute("hallMap", ticketService.formHallMap(tickets));

        return "seances/show";
    }

    @GetMapping("/{id}/ticket")
    public String getTicketPage(@PathVariable("id") int seanceId,
                                @RequestParam(name = "rowId") int rowId,
                                @RequestParam(name="placeId") int placeId,
                                @ModelAttribute("ticket") Ticket ticket,
                                Model model) {
        Seance seance = seanceService.findSeanceById(seanceId);
        if (seanceService.hasAlreadyEnded(seance))
            return "redirect:/seances/";

        User user = getUserFromContext();

        model.addAttribute("seance", seance);
        model.addAttribute("user", user);
        model.addAttribute("rowId", rowId);
        model.addAttribute("placeId", placeId);
        return "seances/ticket";
    }

    @PostMapping("/{id}/ticket")
    public String createTicket(@PathVariable("id") int seanceId,
                               @ModelAttribute("ticket") Ticket ticket) {

        Seance seance = seanceService.findSeanceById(seanceId);
        if (seanceService.hasAlreadyEnded(seance))
            return "redirect:/seances/";

        User user = getUserFromContext();

        ticket.setSeance(seance);
        ticket.setUser(user);

        try {
            ticketService.createTicket(ticket);
        } catch (TicketAlreadyExistsException e) {
            //Log that
            return "redirect:/seances/" + seance.getId() + "?error";
        } catch (NoFreePlacesException e) {
            //Log that
            return "seances";
        }

        return "redirect:/profile/";
    }

    private User getUserFromContext() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userService.getUserByEmail(username);
    }

}
