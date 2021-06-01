package de.telran.businesstracker.service;

import de.telran.businesstracker.data.User;
import de.telran.businesstracker.repositories.UserRepository;
import org.springframework.stereotype.Service;

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
}

