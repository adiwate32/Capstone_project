package com.example.productcatalogueservice.dtos;

import com.example.productcatalogueservice.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String email;
    private Set<Role> role;
}
