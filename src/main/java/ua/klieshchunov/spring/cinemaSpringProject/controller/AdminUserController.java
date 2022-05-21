package ua.klieshchunov.spring.cinemaSpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.klieshchunov.spring.cinemaSpringProject.controller.util.ModelFiller;
import ua.klieshchunov.spring.cinemaSpringProject.dto.PaginationDto;
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
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<User> page = userService
                .findAllUsersPaginatedAndSorted(pageable);

        PaginationDto paginationDto = new PaginationDto();
        paginationDto.pageNumber = pageNum;

        userModelFiller.fillModelForPaginatedItems(page, paginationDto, model);

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
    public String getUserUpdatePage(@PathVariable("id") int id) {
        return "adminPanel/user/edit";
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
