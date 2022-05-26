package com.example.assetmanagement.controller;

import com.example.assetmanagement.dto.ApplicationUserRequest;
import com.example.assetmanagement.dto.ApplicationUserResponse;
import com.example.assetmanagement.entity.ApplicationUser;
import com.example.assetmanagement.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/applicationUser")
@RequiredArgsConstructor
public class ApplicationUserController {
    private final ApplicationUserService applicationUserService;

    @PostMapping
    public ResponseEntity<String> createApplicationUser(@RequestBody ApplicationUserRequest applicationUserRequest){
        applicationUserService.saveUserAccount(applicationUserRequest);
        return status(HttpStatus.CREATED).body("User data saved.");
    }

    @GetMapping
    public ResponseEntity<ApplicationUserResponse> getUserDetails() {
        return status(HttpStatus.CREATED).body(applicationUserService.getUserDetails());
    }

}
