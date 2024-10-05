package com.paras.FinMate.services;

import com.paras.FinMate.common.Response;
import com.paras.FinMate.entities.User;
import com.paras.FinMate.repositories.RoleRepo;
import com.paras.FinMate.repositories.UserRepo;
import com.paras.FinMate.requests.LoginRequest;
import com.paras.FinMate.requests.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

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
        JSONObject response = new JSONObject();
        response.put("token", jwtToken);
        return Response.builder()
                       .message("Login Successful")
                       .data(response.toString())
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
                       .enabled(false)
                       .roles(List.of(userRole))
                       .build();
        userRepository.save(user);
        return Response.builder()
                       .message("User Registered Successfully")
                       .data(user)
                       .build();
    }
}
