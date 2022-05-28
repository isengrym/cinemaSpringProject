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
import ua.klieshchunov.spring.cinemaSpringProject.config.security.ApplicationUserRole;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.service.UserService;

import java.util.ArrayList;
import java.util.List;

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
    public String getUserUpdatePage(@PathVariable("id") int id,
                                    Model model) {
        List<ApplicationUserRole> roles = new ArrayList<>();
        User user = new User();
        user.setId(id);
        user.setRole(userService.getUserById(id).getRole());

        for (ApplicationUserRole role : ApplicationUserRole.values()) {
            if (!role.equals(user.getRole())) {
                roles.add(role);
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return "adminPanel/users/edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @PathVariable("id") int id) {
        User currentUser = userService.getUserById(id);
        currentUser.setRole(user.getRole());
        System.out.println(currentUser);
        userService.updateUser(user);

        return "redirect:/admin/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        User user = userService.getUserById(id);
        userService.deleteUser(user);

        return "redirect:/admin/users";
    }

    private User getUserFromContext() {
        final String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(userEmail);
    }
}
