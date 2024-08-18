package com.example.user_authentication.services;

import com.example.user_authentication.models.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.util.MultiValueMap;

public interface IAuthService {
    User sigUp(String email, String password);
    Pair<User, MultiValueMap<String, String>> login(String email, String password);
}
