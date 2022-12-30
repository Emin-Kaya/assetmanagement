package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.persistence.dto.UserAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/userAccount")
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService userAccountService;

    @GetMapping
    public UserAccountDto getCurrentUser() {
        return userAccountService.getCurrentUserDto();
    }
}
