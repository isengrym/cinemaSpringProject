package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/movies")
public class AdminMovieController {
    @GetMapping
    public String getMovies() {
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
    public String getMovie(@PathVariable("id") int id) {
        return "adminPanel/movies/show";
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
