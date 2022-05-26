package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;
import ua.klieshchunov.spring.cinemaSpringProject.service.DateService;
import ua.klieshchunov.spring.cinemaSpringProject.service.MovieService;
import ua.klieshchunov.spring.cinemaSpringProject.service.PaginationService;
import ua.klieshchunov.spring.cinemaSpringProject.service.ShowtimeService;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.Interval;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Controller
@RequestMapping("admin/showtimes")
public class AdminShowtimeController {
    private final ModelFiller<Showtime> showtimeModelFiller;
    private final PaginationService paginationService;
    private final ShowtimeService showtimeService;
    private final MovieService movieService;
    private final DateService dateService;

    @Autowired
    public AdminShowtimeController(@Qualifier("showtimeModelFiller") ModelFiller<Showtime> showtimeModelFiller,
                                   PaginationService paginationService,
                                   ShowtimeService showtimeService,
                                   MovieService movieService,
                                   DateService dateService) {
        this.showtimeModelFiller = showtimeModelFiller;
        this.paginationService = paginationService;
        this.showtimeService = showtimeService;
        this.movieService = movieService;
        this.dateService = dateService;
    }

    @GetMapping
    public String getShowtimes(@RequestParam(defaultValue = "0") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "id") String sortBy,
                               @RequestParam(defaultValue = "ASC") String sortOrder,
                               Model model) {
        Sort sort = paginationService.formSort(sortBy, sortOrder);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Showtime> page = showtimeService.findAllFutureShowtimesPaginatedAndSorted(pageable);

        showtimeModelFiller.fillModelForPaginatedItems(page, model);

        return "adminPanel/showtimes/index";
    }

    @GetMapping("/new")
    public String newShowtimePage(Model model) {
        Showtime showtime = new Showtime();
        List<Movie> movies = movieService.findAllMovies();
        model.addAttribute("showtime", showtime);
        model.addAttribute("movies", movies);
        return "adminPanel/showtimes/new";
    }

    @PostMapping("/new")
    public String addShowtime(@ModelAttribute("showtime") @Valid Showtime showtime,
                              BindingResult bindingResult,
                              @ModelAttribute("dateString") String dateString,
                              Model model) {
        showtime.setFreePlaces(77);
        setDateToShowtime(showtime, dateString);

        List<Interval> busyIntervals = collectBusyIntervalsForDayOfShowtime(showtime);
        Interval interval = new Interval(showtime.getStartDateTime(), showtime.getEndDateTime());

        if (dateService.isInThePast(showtime.getStartDateTime()))
            bindingResult.rejectValue("startDateEpochSeconds", "error.pastData");
        if (dateService.isBefore9amOrAfter10pm(showtime.getStartDateTime()))
            bindingResult.rejectValue("startDateEpochSeconds", "error.beyondWorkingHours");
        if (dateService.isCrossingIntervals(interval, busyIntervals))
            bindingResult.rejectValue("startDateEpochSeconds", "error.collidesOtherShowtimes");

        if (bindingResult.hasErrors()) {
            List<Movie> movies = movieService.findAllMovies();
            model.addAttribute("movies", movies);
            return "adminPanel/showtimes/new";
        }

        showtimeService.addShowtime(showtime);
        return "redirect:/admin/showtimes/";
    }

    private void setDateToShowtime(Showtime showtime, String dateString) {
        LocalDateTime parsedDate = LocalDateTime.parse(dateString);
        Integer epochSecondsDate = (int)parsedDate.toEpochSecond(ZoneOffset.UTC);
        showtime.setStartDateEpochSeconds(epochSecondsDate);
    }


    private List<Interval> collectBusyIntervalsForDayOfShowtime(Showtime showtime) {
        List<Showtime> showtimes = showtimeService.findAllFutureShowtimes();
        LocalDate dayOfAddedShowtime = showtime.getStartDateTime().toLocalDate();
        return showtimeService.collectIntervalsOfShowtimes(showtimes, dayOfAddedShowtime);
    }

    @GetMapping("/{id}")
    public String getShowtimeUpdatePage(@PathVariable("id") int id) {
        return "adminPanel/showtimes/edit";
    }

    @PutMapping("/{id}")
    public String updateShowtime(@PathVariable("id") int id) {
        return "adminPanel/showtimes/index";
    }

    @DeleteMapping("/{id}")
    public String deleteShowtime(@PathVariable("id") int id,
                                 Model model) {
        Showtime showtime = showtimeService.findShowtimeById(id);
        showtimeService.deleteShowtime(showtime);

        return "redirect:/admin/showtimes/";
    }
}
