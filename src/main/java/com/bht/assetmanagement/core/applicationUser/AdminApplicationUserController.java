package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.persistence.dto.ApplicationUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/applicationUser")
@RequiredArgsConstructor
public class AdminApplicationUserController {
    private final ApplicationUserService applicationUserService;


    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserAccount(@PathVariable String id) {
        applicationUserService.delete(id);
    }

    @GetMapping
    public List<ApplicationUserDto> getAllApplicationUser() {
        return applicationUserService.getAll();
    }
}
