package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.config.security.ApplicationUserRole;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.UserRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.UserService;

import javax.transaction.Transactional;

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
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    @Transactional
    public boolean createUser(User user) {
        user.setRole(ApplicationUserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
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
}
