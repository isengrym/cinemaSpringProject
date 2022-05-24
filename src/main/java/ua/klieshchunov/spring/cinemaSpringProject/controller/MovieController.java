package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.dto.PaginationDto;
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
    private final ModelFiller<Movie> movieModelFiller;

    @Autowired
    public MovieController(MovieService movieService,
                           SeanceService seanceService,
                           @Qualifier("movieModelFiller") ModelFiller<Movie> modelFiller) {
        this.movieService = movieService;
        this.seanceService = seanceService;
        this.movieModelFiller = modelFiller;
    }

    @GetMapping
    public String getAllMovies(@RequestParam(defaultValue = "0") Integer pageNum,
                               @RequestParam(defaultValue = "6") Integer pageSize,
                               @RequestParam(defaultValue= "true") String showInactive,
                               Model model) {

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Movie> page;

        if (Boolean.parseBoolean(showInactive))
            page = movieService.findAllMoviesPaginatedAndSorted(pageable);
        else
            page = movieService.findMoviesWithSeancesPaginatedAndSorted(pageable);


        PaginationDto paginationDto = new PaginationDto();
        paginationDto.pageNumber = pageNum;

        model.addAttribute("showInactive", showInactive);
        movieModelFiller.fillModelForPaginatedItems(page, paginationDto, model);

        return "movies/index";
    }

    @GetMapping("/{id}")
    public String getSpecificMovie(@PathVariable("id") int id, Model model) {
        Movie movie = movieService.findMovieById(id);
        List<Seance> seances = seanceService.findAllFutureSeancesForMovie(movie);
        Map<LocalDate, List<Seance>> seancesByDates = seanceService.collectSeancesByDate(seances);

        model.addAttribute("movie", movie);
        model.addAttribute("seancesByDates", seancesByDates);
        return "movies/show";
    }
}
