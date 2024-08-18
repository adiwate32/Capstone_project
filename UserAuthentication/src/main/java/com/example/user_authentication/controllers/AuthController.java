package com.example.user_authentication.controllers;

import com.example.user_authentication.dtos.*;
import com.example.user_authentication.exceptions.UserAlreadyExists;
import com.example.user_authentication.models.User;
import com.example.user_authentication.services.AuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    //SignUp
    //Login
    //ForgetPassword
    //Logout
    //...
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> SignUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        try {
            User user = authService.sigUp(signUpRequestDto.getEmail(), signUpRequestDto.getPassword());
            if (user == null) {
                throw new UserAlreadyExists("Please Try out with different email");
            }
            return new ResponseEntity<>(from(user), HttpStatus.OK);
        }catch (UserAlreadyExists ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto){
        Pair<User, MultiValueMap<String, String>> userwithHeaders = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        User user = userwithHeaders.a;
        if(user == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(from(user), userwithHeaders.b, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateResponseDto> validate(@RequestBody ValidateTokenRequestDto validateTokenRequestDto){
        Boolean response = authService.validateToken(validateTokenRequestDto.getToken(), validateTokenRequestDto.getUserId());
        return new ResponseEntity<>(from(response), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        return null;
    }

    @PostMapping("/forget_password")
    public ResponseEntity<String> forgetPassword(@RequestBody ForgetPasswordDto forgetPasswordDto){
        return null;
    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRole(userDto.getRole());
        return userDto;
    }

    private ValidateResponseDto from(Boolean valid){
        ValidateResponseDto validateResponseDto = new ValidateResponseDto();
        validateResponseDto.setValid(valid);
        return validateResponseDto;
    }

}
