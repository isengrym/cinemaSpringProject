package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService,
                             TicketService ticketService,
                             PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.passwordEncoder = passwordEncoder;
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
    public String getUpdateNamePage(@ModelAttribute("user") User user) {
        return "userPanel/updateName";
    }
    
    @PostMapping("update/name")
    public String updateName(@ModelAttribute @Valid User userFromForm,
                             BindingResult bindingResult,
                             @ModelAttribute("confirmationPassword") String confirmationPassword) {
        User userFromContext = getUserFromContext();

        if (!userService.isCorrectPassword(confirmationPassword, userFromContext))
            bindingResult.rejectValue("password", "error.update.oldPassword");

        if(bindingResult.hasErrors()) return "userPanel/updateName";

        userFromContext.setName(userFromForm.getName());
        updateUser(userFromContext);

        return "redirect:/profile/";
    }

    @GetMapping("update/surname")
    public String getUpdateSurnamePage(@ModelAttribute("user") User user) {
        return "userPanel/updateSurname";
    }

    @PostMapping("update/surname")
    public String updateSurname(@ModelAttribute @Valid User userFromForm,
                                BindingResult bindingResult,
                                @ModelAttribute("confirmationPassword") String confirmationPassword) {
        User userFromContext = getUserFromContext();

        if (!userService.isCorrectPassword(confirmationPassword, userFromContext))
            bindingResult.rejectValue("password", "error.update.oldPassword");

        if(bindingResult.hasErrors()) return "userPanel/updateSurname";

        userFromContext.setSurname(userFromForm.getSurname());
        updateUser(userFromContext);

        return "redirect:/profile/";
    }

    @GetMapping("update/email")
    public String getUpdateEmailPage(@ModelAttribute("user") User user) {
        return "userPanel/updateEmail";
    }
    
    @PostMapping("update/email")
    public String updateEmail(@ModelAttribute @Valid User userFromForm,
                              BindingResult bindingResult,
                              @ModelAttribute("confirmationPassword") String confirmationPassword) {
        User userFromContext = getUserFromContext();

        if (!userService.isCorrectPassword(confirmationPassword, userFromContext))
            bindingResult.rejectValue("password", "error.update.oldPassword");
        if (userService.userWithSuchEmailExists(userFromForm.getEmail()))
            bindingResult.rejectValue("email", "error.userExists");

        if(bindingResult.hasErrors()) return "userPanel/updateEmail";

        userFromContext.setEmail(userFromForm.getEmail());
        updateUser(userFromContext);

        return "redirect:/profile/";
    }

    @GetMapping("update/password")
    public String getUpdatePasswordPage(@ModelAttribute("user") User user) {
        return "userPanel/updatePassword";
    }
    
    @PostMapping("update/password")
    public String updatePassword(@ModelAttribute("user") @Valid User userFromForm,
                                 BindingResult bindingResult,
                                 @ModelAttribute("confirmationPassword") String confirmationPassword) {
        User userFromContext = getUserFromContext();

        if (!userService.isCorrectPassword(confirmationPassword, userFromContext))
            bindingResult.rejectValue("password", "error.update.oldPassword");

        if(bindingResult.hasErrors()) return "userPanel/updatePassword";

        String encryptedPassword = passwordEncoder.encode(userFromForm.getPassword());
        userFromContext.setPassword(encryptedPassword);
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

    private User getUserFromContext() {
        final String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(userEmail);
    }
}


