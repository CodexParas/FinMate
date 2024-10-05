package com.paras.FinMate.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.paras.FinMate.DTO.LoginRequest;
import com.paras.FinMate.DTO.RegisterRequest;
import com.paras.FinMate.common.Response;
import com.paras.FinMate.services.AuthService;
import com.paras.FinMate.services.CredentialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth", produces = "application/json")
public class AuthController {

    private final AuthService authService;
    private final CredentialService credentialService;
    private final GoogleAuthorizationCodeFlow flow;
    @Value("${callback_url}")
    private String callbackUrl;

    @PostMapping("/login")
    public ResponseEntity<Response> login (@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<Response> register (@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @GetMapping("/callback")
    public String handleCallback (@RequestParam("code") String code) {
        try {
            GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                                                    .setRedirectUri(callbackUrl)
                                                    .execute();
            credentialService.storeCredential(tokenResponse);
            return "Authorization successful!";
        } catch (Exception e) {
            return "Error during OAuth2 callback: " + e.getMessage();
        }
    }
}
