package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.persistence.dto.UserAccountDto;
import com.bht.assetmanagement.persistence.dto.UserAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public UserAccountDto createAssetManagerUserAccount(@RequestBody UserAccountRequest userAccountRequest) {
        return userAccountService.createAssetManagerUserAccount(userAccountRequest);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteUserAccount(@RequestParam String userAccountId) {
        userAccountService.delete(UUID.fromString(userAccountId));
    }
}
