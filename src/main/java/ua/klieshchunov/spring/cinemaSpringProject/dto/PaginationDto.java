package ua.klieshchunov.spring.cinemaSpringProject.dto;

public class PaginationDto {
    public Integer pageNumber;
    public String sortBy;
    public String sortOrder;

    public PaginationDto() {

    }

    public PaginationDto(Integer pageNumber, String sortBy, String sortOrder) {
        this.pageNumber = pageNumber;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }
}
