package ua.klieshchunov.spring.cinemaSpringProject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketService;
import ua.klieshchunov.spring.cinemaSpringProject.service.UserService;
import ua.klieshchunov.spring.cinemaSpringProject.utils.UserDetailsImpl;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("profile")
public class ProfileController {
    private final UserService userService;
    private final TicketService ticketService;
    private final ModelFiller<Ticket> ticketModelFiller;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserService userService,
                             TicketService ticketService,
                             @Qualifier("ticketModelFiller") ModelFiller<Ticket> modelFiller,
                             PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.ticketModelFiller = modelFiller;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String index(@RequestParam(defaultValue = "0") Integer pageNum,
                        @RequestParam(defaultValue = "3") Integer pageSize,
                        Model model) {
        User user = getUserFromContext();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Ticket> page = ticketService.findTicketsByUserPaginatedAndSorted(user, pageable);

        model.addAttribute("user", user);
        ticketModelFiller.fillModelForPaginatedItems(page, model);

        return "userPanel/index";
    }

    @DeleteMapping("/delete")
    public String deleteUser() {
        userService.deleteUser(getUserFromContext());
        clearSession();
        return "redirect:/";
    }

    private void clearSession() {
        SecurityContextHolder.clearContext();
    }

    @GetMapping("update/name")
    public String getUpdateNamePage(@ModelAttribute("user") User user) {
        return "userPanel/updateName";
    }
    
    @PatchMapping("update/name")
    public String updateName(@ModelAttribute @Valid User userFromForm,
                             BindingResult bindingResult,
                             @ModelAttribute("confirmationPassword") String confirmationPassword) {
        User userFromContext = getUserFromContext();

        if (wrongConfirmationPasswordGiven(confirmationPassword, userFromContext)) {
            log.warn(String.format("Wrong confirmation password given when changing name " +
                    "for User{'id': '%d'}",userFromContext.getId()));
            bindingResult.rejectValue("password", "error.update.oldPassword");
        }
        if(bindingResult.hasErrors()) return "userPanel/updateName";

        userFromContext.setName(userFromForm.getName());
        updateUser(userFromContext);

        return "redirect:/profile/";
    }

    @GetMapping("update/surname")
    public String getUpdateSurnamePage(@ModelAttribute("user") User user) {
        return "userPanel/updateSurname";
    }

    @PatchMapping("update/surname")
    public String updateSurname(@ModelAttribute @Valid User userFromForm,
                                BindingResult bindingResult,
                                @ModelAttribute("confirmationPassword") String confirmationPassword) {
        User userFromContext = getUserFromContext();

        if (wrongConfirmationPasswordGiven(confirmationPassword, userFromContext)) {
            log.warn(String.format("Wrong confirmation password given when changing surname " +
                    "for User{'id': '%d'}",userFromContext.getId()));
            bindingResult.rejectValue("password", "error.update.oldPassword");
        }
        if(bindingResult.hasErrors()) return "userPanel/updateSurname";

        userFromContext.setSurname(userFromForm.getSurname());
        updateUser(userFromContext);

        return "redirect:/profile/";
    }

    @GetMapping("update/email")
    public String getUpdateEmailPage(@ModelAttribute("user") User user) {
        return "userPanel/updateEmail";
    }
    
    @PatchMapping("update/email")
    public String updateEmail(@ModelAttribute @Valid User userFromForm,
                              BindingResult bindingResult,
                              @ModelAttribute("confirmationPassword") String confirmationPassword) {
        User userFromContext = getUserFromContext();

        if (wrongConfirmationPasswordGiven(confirmationPassword, userFromContext)) {
            log.warn(String.format("Wrong confirmation password given when changing email " +
                    "for User{'id': '%d'}",userFromContext.getId()));
            bindingResult.rejectValue("password", "error.update.oldPassword");
        }
        if (userService.userWithSuchEmailExists(userFromForm.getEmail())) {
            log.warn("User with the same email already exists");
            bindingResult.rejectValue("email", "error.userExists");
        }

        if(bindingResult.hasErrors()) return "userPanel/updateEmail";

        userFromContext.setEmail(userFromForm.getEmail());
        updateUser(userFromContext);

        return "redirect:/profile/";
    }

    @GetMapping("update/password")
    public String getUpdatePasswordPage(@ModelAttribute("user") User user) {
        return "userPanel/updatePassword";
    }
    
    @PatchMapping("update/password")
    public String updatePassword(@ModelAttribute("user") @Valid User userFromForm,
                                 BindingResult bindingResult,
                                 @ModelAttribute("confirmationPassword") String confirmationPassword) {
        User userFromContext = getUserFromContext();

        if (wrongConfirmationPasswordGiven(confirmationPassword, userFromContext)) {
            log.warn(String.format("Wrong confirmation password given when changing password " +
                    "for User{'id': '%d'}",userFromContext.getId()));
            bindingResult.rejectValue("password", "error.update.oldPassword");
        }

        if(bindingResult.hasErrors()) return "userPanel/updatePassword";

        String encryptedPassword = passwordEncoder.encode(userFromForm.getPassword());
        userFromContext.setPassword(encryptedPassword);
        updateUser(userFromContext);

        return "redirect:/profile/";
    }

    private boolean wrongConfirmationPasswordGiven(String confirmationPassword, User userFromContext) {
        return !userService.isCorrectPassword(confirmationPassword, userFromContext);
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


