package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.core.userAccount.UserAccountService;
import com.bht.assetmanagement.persistence.dto.ApplicationUserDto;
import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public ApplicationUser getCurrentApplicationUser() {
        UserAccount userAccount = userAccountService.getCurrenUser();
        return applicationUserRepository.findApplicationUserByUserAccount(userAccount).orElseThrow();
    }

    public ApplicationUserDto getUserDetails() {
        ApplicationUser applicationUser = getCurrentApplicationUser();

        return ApplicationUserDto.builder()
                .id(applicationUser.getId())
                .firstName(applicationUser.getFirstName())
                .lastName(applicationUser.getLastName())
                .username(applicationUser.getUserAccount().getUsername())
                .email(applicationUser.getUserAccount().getEmail()).build();
    }
}
