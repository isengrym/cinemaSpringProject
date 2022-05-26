package ua.klieshchunov.spring.cinemaSpringProject.controller.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;

@Component
public class TicketModelFiller implements ModelFiller<Ticket> {

    @Override
    public void fillModelForPaginatedItems(Page<Ticket> page, Model model) {
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("tickets", page.getContent());
    }
}
