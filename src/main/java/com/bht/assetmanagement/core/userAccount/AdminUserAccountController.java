package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.persistence.dto.UserAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/userAccount")
@RequiredArgsConstructor
public class AdminUserAccountController {
    private final UserAccountService userAccountService;

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteUserAccount(@RequestParam String id) {
        userAccountService.delete(UUID.fromString(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserAccountRequest userAccountRequest) throws MalformedURLException {
        userAccountService.createUserAccount(userAccountRequest);
    }
}
