package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.service.PaginationService;

@Service
public class PaginationServiceImpl implements PaginationService {
    @Override
    public Sort formSort(String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return sort;
    }
}
