package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketService;
import ua.klieshchunov.spring.cinemaSpringProject.service.UserService;

import java.util.List;

@Controller
@RequestMapping("profile")
public class ProfileController {
    private final UserService userService;
    private final TicketService ticketService;

    @Autowired
    public ProfileController(UserService userService, TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String index(@RequestParam(defaultValue = "0") Integer pageNum,
                        @RequestParam(defaultValue = "4") Integer pageSize,
                        Model model) {
        final String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(userEmail);

        Page<Ticket> page = ticketService
                .findTicketsByUserPaginatedAndSorted(user, pageNum, pageSize);
        List<Ticket> ticketsPaginated = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("tickets", ticketsPaginated);
        model.addAttribute("user", user);

        return "userPanel/index";
    }

    @GetMapping("update/name")
    public String getUpdateNamePage() {
        return "userPanel/updateName";
    }
    @PostMapping("update/name")
    public String updateName() {
        return "redirect:/profile";
    }

    @GetMapping("update/surname")
    public String getUpdateSurnamePage() {
        return "userPanel/updateSurname";
    }
    @PostMapping("update/surname")
    public String updateSurname() {
        return "redirect:/profile";
    }

    @GetMapping("update/email")
    public String getUpdateEmailPage() {
        return "userPanel/updateEmail";
    }
    @PostMapping("update/email")
    public String updateEmail() {
        return "redirect:/profile";
    }

    @GetMapping("update/password")
    public String getUpdatePasswordPage() {
        return "userPanel/updatePassword";
    }
    @PostMapping("update/password")
    public String updatePassword() {
        return "redirect:/profile";
    }
}
