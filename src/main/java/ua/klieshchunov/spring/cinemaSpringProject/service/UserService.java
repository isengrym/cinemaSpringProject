package ua.klieshchunov.spring.cinemaSpringProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;


public interface UserService {
    boolean userWithSuchEmailExists(String email);
    void createUser(User user);
    User getUserById(int id);
    User getUserByEmail(String email);
    boolean isCorrectPassword(String password, User userFromDb);
    void updateUser(User user);
    void deleteUser(User user);
    Page<User> findAllUsersPaginatedAndSorted(Pageable pageable);
}
