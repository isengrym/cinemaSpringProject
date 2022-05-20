package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;

@Controller
@RequestMapping("admin/seances")
public class AdminSeanceController {
    private final ModelFiller modelFiller;

    @Autowired
    public AdminSeanceController(ModelFiller modelFiller) {
        this.modelFiller = modelFiller;
    }

    @GetMapping
    public String getSeances(@RequestParam(defaultValue = "0") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy,
                             @RequestParam(defaultValue = "ASC") String sortOrder,
                             Model model) {
        modelFiller.fillModelForPaginatedSeances(pageNum, pageSize, sortBy, sortOrder, model);

        return "adminPanel/seances/index";
    }

    @GetMapping("/new")
    public String newSeancePage() {
        return "adminPanel/seances/new";
    }

    @PostMapping("/new")
    public String addSeance() {
        return "edit";
    }

    @GetMapping("/{id}")
    public String getSeance(@PathVariable("id") int id) {
        return "edit";
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
