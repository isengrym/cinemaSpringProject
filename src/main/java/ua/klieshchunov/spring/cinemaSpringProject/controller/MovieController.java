package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.service.MovieService;

@Controller
@RequestMapping("movies")
public class MovieController {
    @Autowired
    @Qualifier("movieServiceImpl")
    private MovieService movieService;

    @GetMapping
    public String getAllMovies(Model model) {
        model.addAttribute("movies",
                movieService.findAll());
        return "movies/index";
    }

    @GetMapping("/{id}")
    public String getSpecificMovie(@PathVariable("id") int id, Model model) {
        Movie movie = movieService.findById(id);
        System.out.println(movie);
        model.addAttribute("movie", movieService.findById(id));
        return "movies/show";
    }
}
