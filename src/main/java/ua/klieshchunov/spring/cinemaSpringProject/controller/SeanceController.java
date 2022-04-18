package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("seances")
public class SeanceController {
    @GetMapping
    public String getAllSeances() {
        //Got all of the seance objects from DAO, put it in model
        return "seances/index";
    }
    @GetMapping("/{id}")
    public String getSpecificSeance(@PathVariable("id") int id, Model model) {
        //Got the seance object from DAO, put it in model
        model.addAttribute("id", id);
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
