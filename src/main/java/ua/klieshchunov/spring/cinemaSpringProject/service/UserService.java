package ua.klieshchunov.spring.cinemaSpringProject.service;

import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;


public interface UserService {
    boolean existsByEmail(String email);
    boolean create(User user);
}
