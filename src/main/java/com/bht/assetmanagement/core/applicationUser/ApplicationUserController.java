package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.dto.ApplicationUserResponse;
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
    public ResponseEntity<String> createApplicationUser(@RequestBody ApplicationUserRequest applicationUserRequest) {
        applicationUserService.saveUserAccount(applicationUserRequest);
        return status(HttpStatus.CREATED).body("User data saved.");
    }

    @GetMapping
    public ResponseEntity<ApplicationUserResponse> getUserDetails() {
        return status(HttpStatus.CREATED).body(applicationUserService.getUserDetails());
    }

}
