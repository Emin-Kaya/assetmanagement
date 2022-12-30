package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.persistence.dto.UserAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/manager/userAccount")
@RequiredArgsConstructor
public class ManagerUserAccountController {
    private final UserAccountService userAccountService;

    @GetMapping()
    public List<UserAccountDto> getAllUserAccount() {
        return userAccountService.getAll();
    }

    @GetMapping("/{id}")
    public UserAccountDto getUserDetails(@PathVariable String id) {
        return userAccountService.getUserAccountDtoById(UUID.fromString(id));
    }
}
