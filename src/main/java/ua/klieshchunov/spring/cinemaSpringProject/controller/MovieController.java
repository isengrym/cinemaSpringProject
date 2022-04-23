package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.service.MovieService;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("movies")
public class MovieController {
    private final MovieService movieService;
    private final SeanceService seanceService;

    @Autowired
    public MovieController(@Qualifier("movieServiceImpl") MovieService movieService,
                           @Qualifier("seanceServiceImpl") SeanceService seanceService) {
        this.movieService = movieService;
        this.seanceService = seanceService;
    }

    @GetMapping
    public String getAllMovies(@RequestParam(defaultValue = "0") Integer pageNum,
                               @RequestParam(defaultValue = "6") Integer pageSize,
                               Model model) {

        Page<Movie> page = movieService
                .findAllPaginatedSorted(pageNum, pageSize);
        List<Movie> moviesPaginated = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("movies", moviesPaginated);
        return "movies/index";
    }

    @GetMapping("/{id}")
    public String getSpecificMovie(@PathVariable("id") int id, Model model) {
        List<Seance> seances = seanceService.findAllByMovie(movieService.findById(id));
        List<LocalDate> dates = seanceService.collectDatesOfSeances(seances);

        Map<LocalDate, List<Seance>> seancesByDates =
                seanceService.collectSeancesByDate(dates, seances);

        model.addAttribute("movie", movieService.findById(id));
        model.addAttribute("seancesByDates", seancesByDates);
        return "movies/show";
    }
}
