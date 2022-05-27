package ua.klieshchunov.spring.cinemaSpringProject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.UserService;

import javax.validation.Valid;

@Slf4j
@Controller
public class SignInSignUpController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "common/login";
    }

    @GetMapping("/signUp")
    public String getsignUpPage(@ModelAttribute("user") User user) { return "common/signUp"; }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "common/signUp";

        if (userService.userWithSuchEmailExists(user.getEmail())) {
            log.debug(String.format("User:{'email': '%s'} already exists", user.getEmail()));
            model.addAttribute("userAlreadyExists",true);
            return "common/signUp";
        }

        model.addAttribute("userAlreadyExists",false);
        userService.createUser(user);
        return "redirect:/";
    }
}
