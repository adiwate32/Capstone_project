package com.example.user_authentication.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequestDto {
    String token;
    long userId;
}
