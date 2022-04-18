package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/seances")
public class AdminSeanceController {
    @GetMapping
    public String getSeances() {
        return "adminPanel/seances/index";
    }


    @GetMapping("/new")
    public String newSeancePage() {
        return "adminPanel/seances/new";
    }

    @PostMapping("/new")
    public String addSeance() {
        return "adminPanel/seances/show";
    }

    @GetMapping("/{id}")
    public String getSeance(@PathVariable("id") int id) {
        return "adminPanel/seances/show";
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
