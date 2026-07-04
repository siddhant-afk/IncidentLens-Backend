package com.incidentlens.backend.service;


import com.incidentlens.backend.entity.User;
import com.incidentlens.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User registerUser(String name, String email, String rawPassword){

        if(userRepository.existsBy(email)){

            throw new RuntimeException("Email is already in use");
        }

        User user = new User();
        user.setEmail(email.toLowerCase());
        user.setName(name);
        user.setPassword(passwordEncoder.encode(rawPassword));

        return userRepository.save(user);

    }

    public String loginAndGetToken(String email, String rawPassword){

        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if(!passwordEncoder.matches(rawPassword, user.getPassword())){
            throw new RuntimeException("Invalid user or password");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
