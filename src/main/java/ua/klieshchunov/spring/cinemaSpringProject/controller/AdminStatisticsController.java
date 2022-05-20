package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminStatisticsController {
    @GetMapping("/statistics")
    public String getStatisticPage() {
        return "adminPanel/statistics";
    }
}
