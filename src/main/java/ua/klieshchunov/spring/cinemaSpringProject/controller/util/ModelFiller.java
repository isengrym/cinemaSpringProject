package ua.klieshchunov.spring.cinemaSpringProject.controller.util;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import ua.klieshchunov.spring.cinemaSpringProject.dto.PaginationDto;

public interface ModelFiller <T> {
    void fillModelForPaginatedItems(Page<T> page, PaginationDto paginationDto, Model model);
}
