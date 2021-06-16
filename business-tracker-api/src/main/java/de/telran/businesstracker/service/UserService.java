package de.telran.businesstracker.service;

import de.telran.businesstracker.model.User;
import de.telran.businesstracker.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User add() {
        User user = User.builder().build();
        userRepository.save(user);
        return user;
    }

    public List<User> getAll() {
        return new ArrayList<>(userRepository.findAll());
    }

}

