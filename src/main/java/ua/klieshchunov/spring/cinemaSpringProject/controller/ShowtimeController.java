package ua.klieshchunov.spring.cinemaSpringProject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ShowtimeModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.*;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.TicketAlreadyExistsException;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("showtimes")
public class ShowtimeController {
    private final ShowtimeService showtimeService;
    private final MovieService movieService;
    private final TicketService ticketService;
    private final UserService userService;
    private final PaginationService paginationService;
    private final ShowtimeModelFiller modelFiller;

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService,
                              MovieService movieService,
                              TicketService ticketService,
                              UserService userService,
                              PaginationService paginationService,
                              ShowtimeModelFiller modelFiller) {
        this.showtimeService = showtimeService;
        this.movieService = movieService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.paginationService = paginationService;
        this.modelFiller = modelFiller;
    }

    @GetMapping
    public String getAllShowtimes(@RequestParam(defaultValue = "0") Integer pageNum,
                                  @RequestParam(defaultValue = "8") Integer pageSize,
                                  @RequestParam(defaultValue = "startDateEpochSeconds") String sortBy,
                                  @RequestParam(defaultValue = "DSC") String sortOrder,
                                  @RequestParam(defaultValue = "-1") int movieId,
                                  Model model) {

        Sort sort = paginationService.formSort(sortBy, sortOrder);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Showtime> page = getAppropriatePage(movieId, pageable);

        List<Movie> moviesForFilter = movieService.findMoviesWithShowtimes();

        model.addAttribute("movies", moviesForFilter);
        model.addAttribute("movieId", movieId);
        modelFiller.fillModelForPaginatedItems(page, model);

        return "showtimes/index";
    }

    private Page<Showtime> getAppropriatePage(int movieId, Pageable pageable) {
        if (isParticularMovieId(movieId)) {
            Movie movie = movieService.findMovieById(movieId);
            return showtimeService.findAllFutureShowtimesForMoviePaginatedAndSorted(pageable, movie);
        }
        return showtimeService.findAllFutureShowtimesPaginatedAndSorted(pageable);
    }

    private boolean isParticularMovieId(int movieId) {
        return movieId != -1;
    }

    @GetMapping("/{id}")
    public String getParticularShowtime(@PathVariable("id") int id,
                                        Model model) {
        Showtime showtime = showtimeService.findShowtimeById(id);
        if (showtimeService.hasAlreadyEnded(showtime))
            return "redirect:/showtimes/";

        List<Ticket> tickets = ticketService.findAllTicketsForShowtime(showtime);

        model.addAttribute("showtime", showtime);
        model.addAttribute("hallMap", ticketService.formHallMap(tickets));

        return "showtimes/show";
    }

    @GetMapping("/{id}/ticket")
    public String getTicketPage(@PathVariable("id") int seanceId,
                                @RequestParam(name = "rowId") int rowId,
                                @RequestParam(name="placeId") int placeId,
                                @ModelAttribute("ticket") Ticket ticket,
                                Model model) {
        Showtime showtime = showtimeService.findShowtimeById(seanceId);
        if (showtimeService.hasAlreadyEnded(showtime))
            return "redirect:/showtimes/";

        User user = getUserFromContext();

        model.addAttribute("showtime", showtime);
        model.addAttribute("user", user);
        model.addAttribute("rowId", rowId);
        model.addAttribute("placeId", placeId);
        return "showtimes/ticket";
    }

    @PostMapping("/{id}/ticket")
    public String createTicket(@PathVariable("id") int seanceId,
                               @ModelAttribute("ticket") Ticket ticket) {

        Showtime showtime = showtimeService.findShowtimeById(seanceId);
        if (showtimeService.hasAlreadyEnded(showtime)) {
            log.warn(String.format("Showtime: {'id': '%d'} has already ended. Redirecting..",
                    seanceId));
            return "redirect:/showtimes/";
        }

        User user = getUserFromContext();

        ticket.setShowtime(showtime);
        ticket.setUser(user);

        try {
            ticketService.createTicket(ticket);
        } catch (TicketAlreadyExistsException e) {
            log.warn(String.format("Ticket: {'showtimeId': '%d', 'row': '%d', 'place': '%d'} " +
                    "already exists. Redirecting..",
                    ticket.getShowtime().getId(), ticket.getRow(), ticket.getPlace()));
            return "redirect:/showtimes/" + showtime.getId() + "?error";
        } catch (NoFreePlacesException e) {
            log.warn(String.format("Showtime: {'id': '%d'} has no free places. Redirecting..",
                    ticket.getShowtime().getId()));
            return "showtimes";
        }

        return "redirect:/profile/";
    }

    private User getUserFromContext() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(username);
    }

}
