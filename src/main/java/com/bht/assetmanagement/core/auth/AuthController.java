package com.bht.assetmanagement.core.auth;

import com.bht.assetmanagement.persistence.dto.*;
import com.bht.assetmanagement.persistence.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody RegisterRequest registerRequest) throws MalformedURLException {
        authService.signUp(registerRequest, Role.EMPLOYEE);
    }

    @GetMapping("/activate/account/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void activateAccount(@PathVariable String token) throws MalformedURLException {
        authService.activateAccount(token);
    }

    @PostMapping("/signin")
    public AuthenticationResponse signIn(@RequestBody LoginRequest loginRequest) {
        return authService.signIn(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/signout")
    @ResponseStatus(HttpStatus.OK)
    public void signOut(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        authService.signOut(refreshTokenRequest);
    }

    @PutMapping("/change/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        authService.changeUserPassword(passwordChangeRequest);
    }

    @PutMapping("/change/email")
    @ResponseStatus(HttpStatus.OK)
    public void changeEmail(@RequestBody EmailChangeRequest emailChangeRequest) {
        authService.changeUserEmail(emailChangeRequest);
    }
}
