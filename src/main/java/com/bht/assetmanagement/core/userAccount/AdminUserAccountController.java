package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.persistence.dto.UserAccountDto;
import com.bht.assetmanagement.persistence.dto.UserAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/userAccount")
@RequiredArgsConstructor
public class AdminUserAccountController {
    private final UserAccountService userAccountService;

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteUserAccount(@RequestParam String id) {
        userAccountService.delete(UUID.fromString(id));
    }

    @GetMapping()
    public List<UserAccountDto> getAllUserAccount() {
        return userAccountService.getAll();
    }

    @GetMapping("/{id}")
    public UserAccountDto getUserDetails(@PathVariable String id) {
        return userAccountService.getUserAccountDtoById(UUID.fromString(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserAccountRequest userAccountRequest) throws MalformedURLException {
        userAccountService.createUserAccount(userAccountRequest);
    }
}
