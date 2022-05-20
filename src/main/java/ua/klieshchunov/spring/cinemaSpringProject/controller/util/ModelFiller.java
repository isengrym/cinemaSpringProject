package ua.klieshchunov.spring.cinemaSpringProject.controller.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.service.MovieService;
import ua.klieshchunov.spring.cinemaSpringProject.service.PaginationService;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;

import java.util.List;

@Component
public class ModelFiller {
    private final MovieService movieService;
    private final SeanceService seanceService;
    private final PaginationService paginationService;

    @Autowired
    public ModelFiller(MovieService movieService,
                       SeanceService seanceService,
                       PaginationService paginationService) {
        this.movieService = movieService;
        this.seanceService = seanceService;
        this.paginationService = paginationService;
    }

    public void fillModelForPaginatedMovies(Integer pageNum, Integer pageSize, Model model) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Movie> page = movieService
                .findAllMoviesPaginatedAndSorted(pageable);

        List<Movie> moviesPaginated = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("movies", moviesPaginated);
    }


    public void fillModelForPaginatedSeances(Integer pageNum, Integer pageSize, String sortBy, String sortOrder, Model model) {
        Sort sort = paginationService.formSort(sortBy, sortOrder);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Seance> page = seanceService.findAllFutureSeancesPaginatedAndSorted(pageable);

        List<Seance> seancesPaginated = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("seances", seancesPaginated);
    }
}
