package com.example.user_authentication.services;

import com.example.user_authentication.clients.KafkaProducer;
import com.example.user_authentication.dtos.EmailDto;
import com.example.user_authentication.models.Session;
import com.example.user_authentication.models.SessionState;
import com.example.user_authentication.models.User;
import com.example.user_authentication.repositories.SessionRepo;
import com.example.user_authentication.repositories.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public User sigUp(String email, String password){
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isPresent()){
            return null;
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(user);

        try {
            EmailDto emailDto = new EmailDto();
            emailDto.setTo(email);
            emailDto.setSubject("Welcome to Scaler !!" );
            emailDto.setBody("Hope you have great stay" );
            emailDto.setFrom("diwate.abhishek8@gmail.com" );
            kafkaProducer.sendMessage("signUp", objectMapper.writeValueAsString(emailDto));
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public Pair<User, MultiValueMap<String, String>> login(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isEmpty()){
            return null;
        }
        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            return null;
        }

        // Token generation
//        String message = "{\n" +
//                " \"email\": \"anurag@scaler.com\",\n" +
//                " \"roles\": [\n" +
//                " \"instructor\",\n" +
//                " \"buddy\"\n" +
//                " ],\n" +
//                " \"expirationDate\": \"25thJuly2024\"\n" +
//                "}";
        long nowInMillis = System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());
        claims.put("iat", nowInMillis);
        claims.put("exp", nowInMillis + (24 * 60 * 60 * 1000));

//        byte [] content = message.getBytes(StandardCharsets.UTF_8);

//        MacAlgorithm algorithm = Jwts.SIG.HS256;
//        SecretKey secretKey = algorithm.key().build();
//        String token = Jwts.builder().content(content).signWith(secretKey).compact();
        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);

        Session session = new Session();
        session.setUser(user);
        session.setSessionState(SessionState.ACTIVE);
        session.setToken(token);
        sessionRepo.save(session);

        return new Pair<>(user, headers);
    }

    public Boolean validateToken(String token, Long userId) {
        Optional<Session> optionalSession = sessionRepo.findByToken(token);
        if(optionalSession.isEmpty()){
            System.out.println("Token mismatch");
            return false;
        }

        Session session = optionalSession.get();
        String storedToken = session.getToken();

        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = parser.parseSignedClaims(storedToken).getPayload();

        Long tokenExp = claims.get("exp", Long.class);
        Long currentTime = System.currentTimeMillis();

        System.out.println(tokenExp);
        System.out.println(currentTime);

        if(currentTime > tokenExp){
            System.out.println("Token expired");
            //set status to expired in db
            return false;
        }

        //Till this point is enough. It's good to go
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()){
            System.out.println("User not found");
            return false;
        }

        String email = user.get().getEmail();
        String tokenEmail = claims.get("email", String.class);

        if(!tokenEmail.equals(email)){
            System.out.println("Email does not match");
            return false;
        }
        return true;
    }
}
