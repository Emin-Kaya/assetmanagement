package com.example.assetmanagement.controller;

import com.example.assetmanagement.dto.*;
import com.example.assetmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody RegisterRequest loginRequest) {
        authService.signUp(loginRequest);
        return status(HttpStatus.CREATED).body("Registration succesful.");
    }

    @GetMapping("/activate/account/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) {
        return status(HttpStatus.OK).body(authService.activateAccount(token));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody LoginRequest loginRequest) {
        return status(HttpStatus.ACCEPTED).body(authService.signIn(loginRequest));
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOut(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return status(HttpStatus.OK).body(authService.signOut(refreshTokenRequest));
    }

    @PutMapping("/change/password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        authService.changeUserPassword(passwordChangeRequest);
        return status(HttpStatus.OK).body("Password changed!");
    }

    @PutMapping("/change/email")
    public ResponseEntity<String> changeEmail(@RequestParam String email) {
        authService.changeUserEmail(email);
        return status(HttpStatus.OK).body("Email changed!");
    }

}
