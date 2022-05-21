package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.dto.PaginationDto;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.service.PaginationService;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;

@Controller
@RequestMapping("admin/seances")
public class AdminSeanceController {
    private final ModelFiller<Seance> seanceModelFiller;
    private final PaginationService paginationService;
    private final SeanceService seanceService;

    @Autowired
    public AdminSeanceController(@Qualifier("seanceModelFiller") ModelFiller<Seance> seanceModelFiller,
                                 PaginationService paginationService,
                                 SeanceService seanceService) {
        this.seanceModelFiller = seanceModelFiller;
        this.paginationService = paginationService;
        this.seanceService = seanceService;
    }

    @GetMapping
    public String getSeances(@RequestParam(defaultValue = "0") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy,
                             @RequestParam(defaultValue = "ASC") String sortOrder,
                             Model model) {
        Sort sort = paginationService.formSort(sortBy, sortOrder);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Seance> page = seanceService.findAllFutureSeancesPaginatedAndSorted(pageable);

        PaginationDto paginationDto = new PaginationDto(pageNum, sortBy, sortOrder);

        seanceModelFiller.fillModelForPaginatedItems(page, paginationDto, model);

        return "adminPanel/seances/index";
    }

    @GetMapping("/new")
    public String newSeancePage() {
        return "adminPanel/seances/new";
    }

    @PostMapping("/new")
    public String addSeance() {
        return "adminPanel/seances/edit";
    }

    @GetMapping("/{id}")
    public String getSeanceUpdatePage(@PathVariable("id") int id) {
        return "adminPanel/seances/edit";
    }

    @PutMapping("/{id}")
    public String updateSeance(@PathVariable("id") int id) {
        return "adminPanel/seances/index";
    }

    @DeleteMapping("/{id}")
    public String updateMovie(@PathVariable("id") int id) {
        return "adminPanel/seances/index";
    }
}
