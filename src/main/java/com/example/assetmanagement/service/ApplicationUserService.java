package com.example.assetmanagement.service;

import com.example.assetmanagement.dto.ApplicationUserRequest;
import com.example.assetmanagement.dto.ApplicationUserResponse;
import com.example.assetmanagement.entity.ApplicationUser;
import com.example.assetmanagement.entity.UserAccount;
import com.example.assetmanagement.mapper.ApplicationUserMapper;
import com.example.assetmanagement.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final UserAccountService userAccountService;


    public void saveUserAccount(ApplicationUserRequest applicationUserRequest) {
        ApplicationUser applicationUser = ApplicationUserMapper.INSTANCE.mapRequestToApplicationUser(applicationUserRequest);
        UserAccount userAccount = userAccountService.getCurrenUser();
        applicationUser.setUserAccount(userAccount);
        applicationUserRepository.save(applicationUser);
    }

    public ApplicationUserResponse getUserDetails() {
        UserAccount userAccount = userAccountService.getCurrenUser();
        ApplicationUser applicationUser = applicationUserRepository.findApplicationUserByUserAccount(userAccount).orElseThrow();

        return ApplicationUserResponse.builder()
                .firstName(applicationUser.getFirstName())
                .lastName(applicationUser.getLastName())
                .username(applicationUser.getUserAccount().getUsername())
                .email(applicationUser.getUserAccount().getEmail()).build();
    }
}
