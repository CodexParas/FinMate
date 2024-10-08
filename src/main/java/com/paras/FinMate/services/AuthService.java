package com.paras.FinMate.services;

import com.paras.FinMate.DTO.LoginRequest;
import com.paras.FinMate.DTO.RegisterRequest;
import com.paras.FinMate.common.Response;
import com.paras.FinMate.entities.User;
import com.paras.FinMate.repositories.RoleRepo;
import com.paras.FinMate.repositories.UserRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Response login (@Valid LoginRequest loginRequest) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
                                                     );
        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getName());
        var jwtToken = jwtService.generateToken(claims, user);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("token", jwtToken);
        return Response.builder()
                       .message("Login Successful")
                       .data(responseMap)
                       .build();
    }

    public Response register (@Valid RegisterRequest registerRequest) {
        var userRole = roleRepository.findByName("USER").orElseThrow(
                () -> new IllegalStateException("Role USER was not initiated")
                                                                    );
        var user = User.builder()
                       .firstName(registerRequest.getFirstName())
                       .lastName(registerRequest.getLastName())
                       .email(registerRequest.getEmail())
                       .password(passwordEncoder.encode(registerRequest.getPassword()))
                       .accountLocked(false)
                       .enabled(true)
                       .roles(List.of(userRole))
                       .build();
        userRepository.save(user);
        return Response.builder()
                       .message("User Registered Successfully")
                       .data(user)
                       .build();
    }
}
