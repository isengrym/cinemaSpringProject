package ua.klieshchunov.spring.cinemaSpringProject.controller;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        boolean isUpdating = false;
        showtime.setFreePlaces(77);
        setDateToShowtime(showtime, dateString);
        validateGivenDateTime(showtime, bindingResult, dateString, isUpdating);

        if (bindingResult.hasErrors()) {
            List<Movie> movies = movieService.findAllMovies();
            model.addAttribute("movies", movies);
            return "adminPanel/showtimes/new";
        }

        showtimeService.addShowtime(showtime);
        return "redirect:/admin/showtimes/";
    }

    @GetMapping("/{id}")
    public String getShowtimeUpdatePage(@PathVariable("id") int id,
                                        Model model) {
        Showtime showtime = showtimeService.findShowtimeById(id);
        List<Movie> movies = movieService.findAllMovies();
        model.addAttribute("showtime", showtime);
        model.addAttribute("movies", movies);

        return "adminPanel/showtimes/edit";
    }

    @PatchMapping("/{id}")
    public String updateShowtime(@PathVariable("id") int id,
                                 @ModelAttribute("showtime") @Valid Showtime showtime,
                                 BindingResult bindingResult,
                                 @ModelAttribute("dateString") String dateString) {
        boolean isUpdating = true;
        validateGivenDateTime(showtime, bindingResult, dateString, isUpdating);

        if (bindingResult.hasErrors())
            return "adminPanel/showtimes/edit";

        showtimeService.addShowtime(showtime);
        return "redirect:/admin/showtimes";
    }

    private void validateGivenDateTime(Showtime showtime, BindingResult bindingResult, String dateString, boolean isUpdating) {
        addBindingResIfIsInThePast(showtime, bindingResult);
        addBindingResIfIsBefore9amOrAfter10pm(showtime, bindingResult);

        List<Interval> busyIntervals = collectBusyIntervalsForDayOfShowtime(showtime, isUpdating);

        if (isUpdating)
            setDateToShowtime(showtime, dateString);

        Interval interval = new Interval(showtime.getStartDateTime(), showtime.getEndDateTime());

        addBindingResIfIsCrossingIntervals(showtime, interval, busyIntervals, bindingResult);
    }


    private void addBindingResIfIsInThePast(Showtime showtime, BindingResult bindingResult) {
        if (dateService.isInThePast(showtime.getStartDateTime())) {
            log.debug(String.format("Given date (%d) is already in the past", showtime.getStartDateEpochSeconds()));
            bindingResult.rejectValue("startDateEpochSeconds", "error.pastData");
        }
    }

    private void addBindingResIfIsBefore9amOrAfter10pm(Showtime showtime, BindingResult bindingResult) {
        if (dateService.isBefore9amOrAfter10pm(showtime.getStartDateTime())) {
            log.debug(String.format("Given date (%d) is before 9am or after 10pm, " +
                    "which are not working hours", showtime.getStartDateEpochSeconds()));
            bindingResult.rejectValue("startDateEpochSeconds", "error.beyondWorkingHours");
        }
    }

    private void addBindingResIfIsCrossingIntervals(Showtime showtime, Interval interval, List<Interval> busyIntervals, BindingResult bindingResult) {
        if (dateService.isCrossingIntervals(interval, busyIntervals)) {
            log.debug(String.format("Given date (%d) is crossing another showtime", showtime.getStartDateEpochSeconds()));
            bindingResult.rejectValue("startDateEpochSeconds", "error.collidesOtherShowtimes");
        }
    }

    private void setDateToShowtime(Showtime showtime, String dateString) {
        LocalDateTime parsedDate = LocalDateTime.parse(dateString);
        Integer epochSecondsDate = (int)parsedDate.toEpochSecond(ZoneOffset.UTC);
        showtime.setStartDateEpochSeconds(epochSecondsDate);
    }

    private List<Interval> collectBusyIntervalsForDayOfShowtime(Showtime showtime, boolean isUpdating) {
        List<Showtime> showtimes = getShowtimes(showtime, isUpdating);
        LocalDate dayOfAddedShowtime = showtime.getStartDateTime().toLocalDate();
        return showtimeService.collectIntervalsOfShowtimes(showtimes, dayOfAddedShowtime);
    }

    private List<Showtime> getShowtimes(Showtime showtime, boolean isUpdating) {
        List<Showtime> showtimes = showtimeService.findAllFutureShowtimes();
        if (isUpdating)
            showtimes.remove(showtime);

        return showtimes;
    }

    @DeleteMapping("/{id}")
    public String deleteShowtime(@PathVariable("id") int id) {
        Showtime showtime = showtimeService.findShowtimeById(id);
        showtimeService.deleteShowtime(showtime);

        return "redirect:/admin/showtimes/";
    }

}
