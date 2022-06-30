package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.persistence.dto.UserAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/admin/userAccount")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserAccountService userAccountService;

    @PostMapping
    public ResponseEntity<String> createAssetManagerUserAccount(@RequestBody UserAccountRequest userAccountRequest) {
        userAccountService.createAssetManagerUserAccount(userAccountRequest);
        return status(HttpStatus.CREATED).body("New UserAccount for Asset-Manager saved.");
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<String> deleteUserAccount(@RequestParam String userAccountId) {
        userAccountService.deleteUserAccount(UUID.fromString(userAccountId));
        return status(HttpStatus.ACCEPTED).body("UserAccount with useraccount id: " + userAccountId + " is deleted.");
    }
}
