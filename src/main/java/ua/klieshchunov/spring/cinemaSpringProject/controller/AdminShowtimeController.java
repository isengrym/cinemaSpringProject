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
import ua.klieshchunov.spring.cinemaSpringProject.dto.ShowtimeDto;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;
import ua.klieshchunov.spring.cinemaSpringProject.service.*;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.EmailSenderService;
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
    private final EmailSenderService emailSenderService;

    @Autowired
    public AdminShowtimeController(@Qualifier("showtimeModelFiller") ModelFiller<Showtime> showtimeModelFiller,
                                   PaginationService paginationService,
                                   ShowtimeService showtimeService,
                                   MovieService movieService,
                                   DateService dateService,
                                   EmailSenderService emailSenderService) {
        this.showtimeModelFiller = showtimeModelFiller;
        this.paginationService = paginationService;
        this.showtimeService = showtimeService;
        this.movieService = movieService;
        this.dateService = dateService;
        this.emailSenderService = emailSenderService;
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
        showtime.setStartDateEpochSeconds(dateService.convertStringDateToEpoch(dateString));

        ShowtimeDto showtimeDto = ShowtimeDto.toDto(showtime);
        validateGivenDateTime(showtimeDto, bindingResult);

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
        LocalDateTime newLocalDateTime = LocalDateTime.parse(dateString);

        ShowtimeDto showtimeDto = ShowtimeDto.toDto(showtime, newLocalDateTime);
        showtime.setStartDateEpochSeconds(dateService.convertStringDateToEpoch(dateString));

        validateGivenDateTime(showtimeDto, bindingResult);

        if (bindingResult.hasErrors())
            return "adminPanel/showtimes/edit";

        showtimeService.addShowtime(showtime);
        emailSenderService.sendEmailsForShowtime(showtime);

        return "redirect:/admin/showtimes";
    }

    private void validateGivenDateTime(ShowtimeDto showtime, BindingResult bindingResult) {
        addBindingResIfIsInThePast(showtime, bindingResult);
        addBindingResIfIsBefore9amOrAfter10pm(showtime, bindingResult);
        addBindingResIfIsCrossingIntervals(showtime, bindingResult);
    }


    private void addBindingResIfIsInThePast(ShowtimeDto showtimeDto, BindingResult bindingResult) {
        if (dateService.isInThePast(showtimeDto.startDateTimeAfterUpdating)) {
            log.debug(String.format("Given date (%d) is already in the past",
                    showtimeDto.startDateTimeAfterUpdating.toEpochSecond(ZoneOffset.UTC)));
            bindingResult.rejectValue("startDateEpochSeconds", "error.pastData");
        }
    }

    private void addBindingResIfIsBefore9amOrAfter10pm(ShowtimeDto showtimeDto, BindingResult bindingResult) {
        if (dateService.isBefore9amOrAfter10pm(showtimeDto.startDateTimeAfterUpdating)) {
            log.debug(String.format("Given date (%d) is before 9am or after 10pm, " +
                    "which are not working hours",
                    showtimeDto.startDateTimeAfterUpdating.toEpochSecond(ZoneOffset.UTC)));
            bindingResult.rejectValue("startDateEpochSeconds", "error.beyondWorkingHours");
        }
    }

    private void addBindingResIfIsCrossingIntervals(ShowtimeDto showtimeDto, BindingResult bindingResult) {
        List<Interval> busyIntervals = showtimeService.collectIntervals(showtimeDto);

        Showtime showtime = ShowtimeDto.toEntityWithNewStartTime(showtimeDto);
        Interval interval = new Interval(showtime.getStartDateTime(), showtime.getEndDateTime());

        if (dateService.isCrossingIntervals(interval, busyIntervals)) {
            log.debug(String.format("Given date (%d) is crossing another showtime", showtime.getStartDateEpochSeconds()));
            bindingResult.rejectValue("startDateEpochSeconds", "error.collidesOtherShowtimes");
        }
    }

    @DeleteMapping("/{id}")
    public String deleteShowtime(@PathVariable("id") int id) {
        Showtime showtime = showtimeService.findShowtimeById(id);
        showtimeService.deleteShowtime(showtime);

        return "redirect:/admin/showtimes/";
    }

}
