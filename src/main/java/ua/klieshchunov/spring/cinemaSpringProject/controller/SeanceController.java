package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;

@Controller
@RequestMapping("seances")
public class SeanceController {
    private final SeanceService seanceService;

    @Autowired
    public SeanceController(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    @GetMapping
    public String getAllSeances(Model model) {
        model.addAttribute("seances", seanceService.findAll());
        return "seances/index";
    }
    @GetMapping("/{id}")
    public String getSpecificSeance(@PathVariable("id") int id, Model model) {
        model.addAttribute("seanceDto", seanceService.findById(id));
        return "seances/show";
    }

    @GetMapping("/{id}/ticket")
    public String getTicketPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        return "seances/ticket";
    }

    @PostMapping("/{id}/ticket")
    public String createTicket(@PathVariable("id") int id, Model model) {
        //Creating ticket
        return "redirect:/profile/show";
    }
}
