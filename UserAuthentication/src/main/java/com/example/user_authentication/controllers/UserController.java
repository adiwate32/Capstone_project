package com.example.user_authentication.controllers;

import com.example.user_authentication.dtos.UserDto;
import com.example.user_authentication.models.User;
import com.example.user_authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public UserDto getUserDetails(@PathVariable long id){
        return from(userService.getUser(id));
    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        //userDto.setRole(userDto.getRole());
        return userDto;
    }
}
