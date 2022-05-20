package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.service.MovieService;

@Controller
@RequestMapping("admin/movies")
public class AdminMovieController {
    private final ModelFiller modelFiller;

    @Autowired
    public AdminMovieController(ModelFiller modelFiller) {
        this.modelFiller = modelFiller;
    }

    @GetMapping
    public String getMovies(@RequestParam(defaultValue = "0") Integer pageNum,
                            @RequestParam(defaultValue = "15") Integer pageSize,
                            Model model) {
        modelFiller.fillModelForPaginatedMovies(pageNum, pageSize, model);

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
