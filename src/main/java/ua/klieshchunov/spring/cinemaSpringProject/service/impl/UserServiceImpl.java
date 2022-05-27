package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.config.security.ApplicationUserRole;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.UserRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.UserService;

import javax.transaction.Transactional;
//@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean userWithSuchEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    @Transactional
    public void createUser(User user) {
        user.setRole(ApplicationUserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }

    @Override
    public boolean isCorrectPassword(String password, User userFromDb) {
        if (password == null)
            return false;

        return passwordEncoder.matches(password, userFromDb.getPassword());
    }

    @Override
    public void updateUser(User userFromDb) {
        userRepository.updateUser(userFromDb);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.deleteUser(user);
    }

    @Override
    public Page<User> findAllUsersPaginatedAndSorted(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
