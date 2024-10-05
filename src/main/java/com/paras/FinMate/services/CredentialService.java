package com.paras.FinMate.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CredentialService {
    private static final String USER_ID = "user"; // Hardcoded user ID
    private final GoogleAuthorizationCodeFlow flow;
    @Value("${callback_url}")
    private String callbackUrl;

    public void storeCredential (GoogleTokenResponse tokenResponse) {
        try {
            Credential credential = flow.createAndStoreCredential(tokenResponse, USER_ID);
            log.info("Stored Refresh Token: {}", credential.getRefreshToken());
        } catch (Exception e) {
            log.error("Error while storing credential: {}", e.getMessage());
        }
    }

    public Credential getCredential () {
        try {
            Credential credential = flow.loadCredential(USER_ID);
            if (credential != null && credential.getRefreshToken() != null) {
                credential.refreshToken();
                return credential;
            } else {
                flow.getCredentialDataStore().delete(USER_ID);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error while loading credential: " + e.getMessage());
            return null;
        }
    }

    public Credential refreshCredential () throws IOException {
        Credential credential = getCredential();
        if (credential != null) {
            log.info("Credential is not null: {}", credential);
            if (credential.getRefreshToken() == null) {
                log.info("Refresh token is null");
            } else {
                credential.refreshToken();
                log.info("Refreshed token: {}", credential.getAccessToken());
            }
            return credential;
        } else {
            log.info("Credential is null or refresh token is null");
            log.info("Authorize the application again");
            log.info("Redirect to the URL: {}", flow.newAuthorizationUrl().setRedirectUri(callbackUrl).build());
        }
        return null;
    }
}
