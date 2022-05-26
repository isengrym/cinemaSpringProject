package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.UserService;

@Controller
@RequestMapping("admin/users")
public class AdminUserController {
    private final ModelFiller<User> userModelFiller;
    private final UserService userService;

    @Autowired
    public AdminUserController(@Qualifier("userModelFiller") ModelFiller<User> seanceModelFiller,
                                 UserService seanceService) {
        this.userModelFiller = seanceModelFiller;
        this.userService = seanceService;
    }

    @GetMapping
    public String getUsers(@RequestParam(defaultValue = "0") Integer pageNum,
                            @RequestParam(defaultValue = "15") Integer pageSize,
                            Model model) {
        User user = getUserFromContext();

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<User> page = userService.findAllUsersPaginatedAndSorted(pageable);

        model.addAttribute("currentUser", user);
        userModelFiller.fillModelForPaginatedItems(page, model);

        return "adminPanel/users/index";
    }

    @GetMapping("/{id}")
    public String getUserUpdatePage(@PathVariable("id") int id) {
        return "adminPanel/user/edit";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable("id") int id) {
        return "adminPanel/users/index";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id,
                             Model model) {
        User user = userService.getUserById(id);
        System.out.println(user);

        userService.deleteUser(user);

        return "redirect:/admin/users";
    }

    private User getUserFromContext() {
        final String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(userEmail);
    }
}
