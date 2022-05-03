package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketService;
import ua.klieshchunov.spring.cinemaSpringProject.service.UserService;
import ua.klieshchunov.spring.cinemaSpringProject.utils.UserDetailsImpl;

import javax.validation.Valid;
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
        User user = getUserFromContext();
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Ticket> page = ticketService
                .findTicketsByUserPaginatedAndSorted(user, pageable);
        List<Ticket> ticketsPaginated = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("tickets", ticketsPaginated);
        model.addAttribute("user", user);

        return "userPanel/index";
    }

    @GetMapping("update/name")
    public String getUpdateNamePage(@ModelAttribute User user) {
        return "userPanel/updateName";
    }
    
    @PatchMapping("update/name")
    public String updateName(@ModelAttribute @Valid User user) {
        return "redirect:/profile";
    }

    @GetMapping("update/surname")
    public String getUpdateSurnamePage(@ModelAttribute User user) {
        return "userPanel/updateSurname";
    }
    
    @PatchMapping("update/surname")
    public String updateSurname(@ModelAttribute @Valid User user) {
        return "redirect:/profile";
    }

    @GetMapping("update/email")
    public String getUpdateEmailPage(@ModelAttribute("user") User user) {
        return "userPanel/updateEmail";
    }
    
    @PostMapping("update/email")
    public String updateEmail(@ModelAttribute @Valid User userFromForm,
                              BindingResult bindingResult) {
        User userFromContext = getUserFromContext();

        if (!userService.isCorrectPassword(userFromForm.getPassword(), userFromContext))
            bindingResult.rejectValue("password", "error.update.oldPassword");
        if (userService.userWithSuchEmailExists(userFromForm.getEmail()))
            bindingResult.rejectValue("email", "error.userExists");

        if(bindingResult.hasErrors()) return "userPanel/updateEmail";

        userFromContext.setEmail(userFromForm.getEmail());
        updateUser(userFromContext);

        return "redirect:/profile/";
    }

    private void updateUser(User updatedUser) {
        userService.updateUser(updatedUser);
        updateUserDetails(updatedUser);
    }

    private void updateUserDetails(User updatedUser) {
        UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userDetails.setUser(updatedUser);
    }

    @GetMapping("update/password")
    public String getUpdatePasswordPage(@ModelAttribute User user) {
        return "userPanel/updatePassword";
    }
    
    @PatchMapping("update/password")
    public String updatePassword(@ModelAttribute @Valid User user) {
        return "redirect:/profile";
    }

    private User getUserFromContext() {
        final String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(userEmail);
    }
}


