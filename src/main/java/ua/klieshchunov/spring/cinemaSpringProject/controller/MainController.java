package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String getMainPage() {
        return "common/main";
    }
    @GetMapping("/admin") String getAdminPanel() { return "adminPanel/movies/index"; }
}
