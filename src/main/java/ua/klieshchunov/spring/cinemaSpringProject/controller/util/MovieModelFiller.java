package ua.klieshchunov.spring.cinemaSpringProject.controller.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ua.klieshchunov.spring.cinemaSpringProject.dto.PaginationDto;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;

import java.util.List;

@Component
public class MovieModelFiller implements ModelFiller<Movie> {

    @Override
    public void fillModelForPaginatedItems(Page<Movie> page, PaginationDto dto, Model model) {
        List<Movie> moviesPaginated = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", dto.pageNumber);
        model.addAttribute("movies", moviesPaginated);
    }
}
