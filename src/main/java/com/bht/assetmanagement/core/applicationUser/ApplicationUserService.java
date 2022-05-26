package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.dto.ApplicationUserResponse;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
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
