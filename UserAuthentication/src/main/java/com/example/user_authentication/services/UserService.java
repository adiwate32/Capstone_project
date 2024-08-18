package com.example.user_authentication.services;

import com.example.user_authentication.models.User;
import com.example.user_authentication.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public User getUser(Long id){
        Optional<User> userOptional = userRepo.findById(id);
        return userOptional.orElse(null);
    }
}
