package ua.klieshchunov.spring.cinemaSpringProject.controller.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.service.PaginationService;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;

import java.util.List;

@Component
public class SeanceModelFiller implements ModelFiller<Seance> {

    @Override
    public void fillModelForPaginatedItems(Page<Seance> page, PaginationDto dto, Model model) {
        List<Seance> seancesPaginated = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", dto.pageNumber);
        model.addAttribute("sortBy", dto.sortBy);
        model.addAttribute("sortOrder", dto.sortOrder);
        model.addAttribute("seances", seancesPaginated);
    }
}
