package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.dto.ApplicationUserResponse;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Role;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final UserAccountService userAccountService;


    public void createApplicationUser(ApplicationUserRequest applicationUserRequest) {
        ApplicationUser applicationUser = ApplicationUserMapper.INSTANCE.mapRequestToApplicationUser(applicationUserRequest);
        UserAccount userAccount = userAccountService.getCurrenUser();
        applicationUser.setUserAccount(userAccount);
        saveApplicationUser(applicationUser);
    }

    public void saveApplicationUser(ApplicationUser applicationUser) {
        applicationUserRepository.save(applicationUser);
    }

    public ApplicationUser getCurrentApplicationUser(){
        UserAccount userAccount = userAccountService.getCurrenUser();
        return applicationUserRepository.findApplicationUserByUserAccount(userAccount).orElseThrow();
    }


    public ApplicationUserResponse getUserDetails() {
        ApplicationUser applicationUser = getCurrentApplicationUser();

        return ApplicationUserResponse.builder()
                .firstName(applicationUser.getFirstName())
                .lastName(applicationUser.getLastName())
                .username(applicationUser.getUserAccount().getUsername())
                .email(applicationUser.getUserAccount().getEmail()).build();
    }
}
