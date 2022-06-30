package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.persistence.dto.ApplicationUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/admin/applicationUser")
@RequiredArgsConstructor
public class AdminApplicationUserController {
    private final ApplicationUserService applicationUserService;


    @DeleteMapping()
    @Transactional
    public ResponseEntity<String> deleteUserAccount(@RequestParam String applicationUserId) {
        applicationUserService.deleteUserAccount(UUID.fromString(applicationUserId));
        return status(HttpStatus.ACCEPTED).body("ApplicationUser with id: " + applicationUserId + " is deleted.");
    }

    @GetMapping
    public List<ApplicationUserDto> getAllApplicationUser() {
        return applicationUserService.getApplicationUsers();
    }
}
