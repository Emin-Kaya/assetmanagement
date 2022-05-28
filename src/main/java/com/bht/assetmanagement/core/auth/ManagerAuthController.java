package com.bht.assetmanagement.core.auth;

import com.bht.assetmanagement.persistence.dto.RegisterRequest;
import com.bht.assetmanagement.persistence.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/auth/manager")
@RequiredArgsConstructor
public class ManagerAuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody RegisterRequest loginRequest) {
        authService.signUp(loginRequest, Role.MANAGER);
        return status(HttpStatus.CREATED).body("Registration succesful.");
    }
}
