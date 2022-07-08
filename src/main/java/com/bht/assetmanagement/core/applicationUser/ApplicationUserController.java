package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.persistence.dto.ApplicationUserDto;
import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationUserDto createApplicationUser(@RequestBody ApplicationUserRequest applicationUserRequest) {
        return applicationUserService.create(applicationUserRequest);
    }

    @GetMapping
    public ResponseEntity<ApplicationUserDto> getUserDetails() {
        return status(HttpStatus.CREATED).body(applicationUserService.getProfileInformation());
    }
}
