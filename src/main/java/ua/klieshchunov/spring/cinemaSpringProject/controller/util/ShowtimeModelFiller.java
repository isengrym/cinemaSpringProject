package ua.klieshchunov.spring.cinemaSpringProject.controller.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;
import ua.klieshchunov.spring.cinemaSpringProject.service.PaginationService;

import java.util.List;

@Component
public class ShowtimeModelFiller implements ModelFiller<Showtime> {
    private final PaginationService paginationService;

    @Autowired
    public ShowtimeModelFiller(PaginationService paginationService) {
        this.paginationService = paginationService;
    }

    @Override
    public void fillModelForPaginatedItems(Page<Showtime> page, Model model) {
        List<Showtime> showtimesPaginated = page.getContent();

        String sortBy = paginationService.getSortBy(page.getSort());
        String sortOrder = paginationService.getSortOrder(page.getSort());

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("showtimes", showtimesPaginated);
    }
}
