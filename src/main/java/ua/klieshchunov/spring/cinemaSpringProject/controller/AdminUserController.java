package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/users")
public class AdminUserController {
    @GetMapping
    public String getMovies() {
        return "adminPanel/users/index";
    }


    @GetMapping("/new")
    public String newUserPage() {
        return "adminPanel/users/new";
    }


    @PostMapping("/new")
    public String addUser() {
        return "adminPanel/users/index";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") int id) {
        return "edit";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable("id") int id) {
        return "adminPanel/users/index";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        return "adminPanel/users/index";
    }
}
