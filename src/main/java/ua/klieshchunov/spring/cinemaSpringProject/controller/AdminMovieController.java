package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.PaginationDto;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.service.MovieService;

@Controller
@RequestMapping("admin/movies")
public class AdminMovieController {
    private final ModelFiller<Movie> movieModelFiller;
    private final MovieService movieService;

    @Autowired
    public AdminMovieController(@Qualifier("movieModelFiller") ModelFiller<Movie> modelFiller,
                                MovieService movieService) {
        this.movieModelFiller = modelFiller;
        this.movieService = movieService;
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
    public String newMoviePage() {
        return "adminPanel/movies/new";
    }

    @PostMapping("/new")
    public String addMovie() {
        return "adminPanel/movies/index";
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
