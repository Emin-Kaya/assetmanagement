package com.bht.assetmanagement.core.auth;

import com.bht.assetmanagement.persistence.dto.*;
import com.bht.assetmanagement.persistence.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.MalformedURLException;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody RegisterRequest loginRequest) throws MalformedURLException {
        authService.signUp(loginRequest, Role.EMPLOYEE);
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
    public void changeEmail(@RequestParam String email) {
        authService.changeUserEmail(email);
    }
}
