package ua.klieshchunov.spring.cinemaSpringProject.controller.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ua.klieshchunov.spring.cinemaSpringProject.dto.PaginationDto;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;

import java.util.List;

@Component
public class ShowtimeModelFiller implements ModelFiller<Showtime> {

    @Override
    public void fillModelForPaginatedItems(Page<Showtime> page, PaginationDto dto, Model model) {
        List<Showtime> showtimesPaginated = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", dto.pageNumber);
        model.addAttribute("sortBy", dto.sortBy);
        model.addAttribute("sortOrder", dto.sortOrder);
        model.addAttribute("showtimes", showtimesPaginated);
    }
}
