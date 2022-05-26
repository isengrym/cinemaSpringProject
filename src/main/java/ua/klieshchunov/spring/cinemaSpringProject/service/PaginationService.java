package ua.klieshchunov.spring.cinemaSpringProject.service;

import org.springframework.data.domain.Sort;

public interface PaginationService {
    Sort formSort(String sortBy, String sortOrder);
    String getSortBy(Sort sort);
    String getSortOrder(Sort sort);
}
