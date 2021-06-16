package de.telran.businesstracker.controller;

import de.telran.businesstracker.data.User;
import de.telran.businesstracker.dto.UserDto;
import de.telran.businesstracker.mapper.UserMapper;
import de.telran.businesstracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) throws URISyntaxException {
        User user = userService.add();
        userDto.id = user.getId();
        return ResponseEntity
                .created(new URI("/api/users/" + user.getId()))
                .body(userDto);
    }

    @GetMapping("")
    public List<UserDto> getAllTasks() {
        return userService.getAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
