package ua.klieshchunov.spring.cinemaSpringProject.controller.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.PaginationService;

import java.util.List;

@Component
public class UserModelFiller implements ModelFiller<User> {
    private final PaginationService paginationService;

    @Autowired
    public UserModelFiller(PaginationService paginationService) {
        this.paginationService = paginationService;
    }

    @Override
    public void fillModelForPaginatedItems(Page<User> page, Model model) {
        List<User> usersPaginated = page.getContent();

        String sortBy = paginationService.getSortBy(page.getSort());
        String sortOrder = paginationService.getSortOrder(page.getSort());

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("users", usersPaginated);
    }
}
