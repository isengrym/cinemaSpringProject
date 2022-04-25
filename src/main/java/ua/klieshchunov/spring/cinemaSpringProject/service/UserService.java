package ua.klieshchunov.spring.cinemaSpringProject.service;

import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;


public interface UserService {
    boolean userWithSuchEmailExists(String email);
    boolean createUser(User user);
}
