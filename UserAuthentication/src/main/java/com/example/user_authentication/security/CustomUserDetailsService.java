package com.example.user_authentication.security;

import com.example.user_authentication.models.CustomUserDetails;
import com.example.user_authentication.models.User;
import com.example.user_authentication.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("Bad credentials");
        }

        User user = userOptional.get();
        return new CustomUserDetails(user);
    }
}
