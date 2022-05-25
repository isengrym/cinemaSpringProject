package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.dto.PaginationDto;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Genre;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.GenreService;
import ua.klieshchunov.spring.cinemaSpringProject.service.MovieService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("admin/movies")
public class AdminMovieController {
    private final ModelFiller<Movie> movieModelFiller;
    private final MovieService movieService;
    private final GenreService genreService;

    @Autowired
    public AdminMovieController(@Qualifier("movieModelFiller") ModelFiller<Movie> modelFiller,
                                MovieService movieService,
                                GenreService genreService) {
        this.movieModelFiller = modelFiller;
        this.movieService = movieService;
        this.genreService = genreService;
    }

    @GetMapping
    public String getMovies(@RequestParam(defaultValue = "0") Integer pageNum,
                            @RequestParam(defaultValue = "15") Integer pageSize,
                            Model model) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Movie> page = movieService
                .findAllMoviesPaginatedAndSorted(pageable);

        PaginationDto paginationDto = new PaginationDto();
        paginationDto.pageNumber = pageNum;

        movieModelFiller.fillModelForPaginatedItems(page, paginationDto, model);

        return "adminPanel/movies/index";
    }


    @GetMapping("/new")
    public String newMoviePage(Model model) {
        Movie movie = new Movie();
        List<Genre> genres = genreService.findAllGenres();
        model.addAttribute("movie", movie);
        model.addAttribute("genres", genres);
        return "adminPanel/movies/new";
    }

    @PostMapping("/new")
    public String addMovie(@ModelAttribute @Valid Movie movie,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            List<Genre> genres = genreService.findAllGenres();
            model.addAttribute("genres", genres);
            return "adminPanel/movies/new";
        }

        movieService.addMovie(movie);
        return "redirect:/admin/movies/";
    }

    @GetMapping("/{id}")
    public String getMovieUpdatePage(@PathVariable("id") int id) {
        return "adminPanel/movies/edit";
    }

    @PutMapping("/{id}")
    public String updateMovie(@PathVariable("id") int id) {
        return "adminPanel/movies/index";
    }

    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable("id") int id) {
        return "adminPanel/movies/index";
    }
}
