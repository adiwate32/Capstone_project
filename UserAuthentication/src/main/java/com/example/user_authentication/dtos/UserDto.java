package com.example.user_authentication.dtos;

import com.example.user_authentication.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String email;
    private     Set<Role> role;
}
