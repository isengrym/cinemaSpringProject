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
import ua.klieshchunov.spring.cinemaSpringProject.dto.PaginationDto;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.service.DateService;
import ua.klieshchunov.spring.cinemaSpringProject.service.MovieService;
import ua.klieshchunov.spring.cinemaSpringProject.service.PaginationService;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.Interval;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Controller
@RequestMapping("admin/seances")
public class AdminSeanceController {
    private final ModelFiller<Seance> seanceModelFiller;
    private final PaginationService paginationService;
    private final SeanceService seanceService;
    private final MovieService movieService;
    private final DateService dateService;

    @Autowired
    public AdminSeanceController(@Qualifier("seanceModelFiller") ModelFiller<Seance> seanceModelFiller,
                                 PaginationService paginationService,
                                 SeanceService seanceService,
                                 MovieService movieService,
                                 DateService dateService) {
        this.seanceModelFiller = seanceModelFiller;
        this.paginationService = paginationService;
        this.seanceService = seanceService;
        this.movieService = movieService;
        this.dateService = dateService;
    }

    @GetMapping
    public String getSeances(@RequestParam(defaultValue = "0") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy,
                             @RequestParam(defaultValue = "ASC") String sortOrder,
                             Model model) {
        Sort sort = paginationService.formSort(sortBy, sortOrder);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Seance> page = seanceService.findAllFutureSeancesPaginatedAndSorted(pageable);

        PaginationDto paginationDto = new PaginationDto(pageNum, sortBy, sortOrder);

        seanceModelFiller.fillModelForPaginatedItems(page, paginationDto, model);

        return "adminPanel/seances/index";
    }

    @GetMapping("/new")
    public String newSeancePage(Model model) {
        Seance seance = new Seance();
        List<Movie> movies = movieService.findAllMovies();
        model.addAttribute("seance", seance);
        model.addAttribute("movies", movies);
        return "adminPanel/seances/new";
    }

    @PostMapping("/new")
    public String addSeance(@ModelAttribute("seance") @Valid Seance seance,
                            BindingResult bindingResult,
                            @ModelAttribute("dateString") String dateString,
                            Model model) {
        seance.setFreePlaces(77);

        LocalDateTime parsedDate = LocalDateTime.parse(dateString);
        Integer epochSecondsDate = (int)parsedDate.toEpochSecond(ZoneOffset.UTC);
        seance.setStartDateEpochSeconds(epochSecondsDate);

        List<Interval> busyIntervals = collectBusyIntervalsForDayOfSeance(seance);
        Interval interval = new Interval(seance.getStartDateTime(), seance.getEndDateTime());

        if (dateService.isInThePast(seance.getStartDateTime()))
            bindingResult.rejectValue("startDateEpochSeconds", "error.pastData");
        if (dateService.isBefore9amOrAfter10pm(seance.getStartDateTime()))
            bindingResult.rejectValue("startDateEpochSeconds", "error.beyondWorkingHours");
        if (dateService.isCrossingIntervals(interval, busyIntervals))
            bindingResult.rejectValue("startDateEpochSeconds", "error.collidesOtherSeancesTime");

        if (bindingResult.hasErrors()) {
            List<Movie> movies = movieService.findAllMovies();
            model.addAttribute("movies", movies);
            return "adminPanel/seances/new";
        }

        seanceService.addSeance(seance);
        return "redirect:/admin/seances/";
    }


    private List<Interval> collectBusyIntervalsForDayOfSeance(Seance seance) {
        List<Seance> seances = seanceService.findAllFutureSeances();
        LocalDate dayOfAddedSeance = seance.getStartDateTime().toLocalDate();
        return seanceService.collectIntervalsOfSeances(seances, dayOfAddedSeance);
    }

    @GetMapping("/{id}")
    public String getSeanceUpdatePage(@PathVariable("id") int id) {
        return "adminPanel/seances/edit";
    }

    @PutMapping("/{id}")
    public String updateSeance(@PathVariable("id") int id) {
        return "adminPanel/seances/index";
    }

    @DeleteMapping("/{id}")
    public String updateMovie(@PathVariable("id") int id) {
        return "adminPanel/seances/index";
    }
}
