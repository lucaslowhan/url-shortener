package dev.lucaslowhan.urlshortener.service;

import dev.lucaslowhan.urlshortener.domain.User;
import dev.lucaslowhan.urlshortener.dto.request.LoginRequest;
import dev.lucaslowhan.urlshortener.dto.request.RegisterRequest;
import dev.lucaslowhan.urlshortener.dto.response.AuthResponse;
import dev.lucaslowhan.urlshortener.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        var userOptional = userRepository.findByEmail(registerRequest.getEmail());
        if(userOptional.isPresent()) {
            throw new RuntimeException("Email já registrado");
        }
        var user = User.builder()
                        .email(registerRequest.getEmail())
                                .password(passwordEncoder.encode(registerRequest.getPassword()))
                                        .build();
        userRepository.save(user);
        var token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        var user = userRepository.findByEmail(loginRequest.getEmail());
        var token = jwtService.generateToken(user.get());

        return new AuthResponse(token);

    }
}
