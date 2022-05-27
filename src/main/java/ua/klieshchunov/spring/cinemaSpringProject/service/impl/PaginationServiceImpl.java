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
        return sortOrder.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }

    public String getSortBy(Sort sort) {
        String sortBy = "id";

        if (sort.isSorted())
            sortBy = sort.toString().split(":")[0];

        return sortBy;
    }

    public String getSortOrder(Sort sort) {
        String sortOrder = "ASC";

        if (sort.isSorted()) {
            sortOrder = sort.toString().split(":")[1];
            sortOrder = sortOrder.trim();
        }

        return sortOrder;
    }
}
