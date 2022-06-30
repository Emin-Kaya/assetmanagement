package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.core.asset.AssetMapper;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
import com.bht.assetmanagement.persistence.dto.ApplicationUserDto;
import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.dto.AssetDto;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final UserAccountService userAccountService;


    public ApplicationUser createApplicationUser(ApplicationUserRequest applicationUserRequest) {
        ApplicationUser applicationUser = ApplicationUserMapper.INSTANCE.mapRequestToApplicationUser(applicationUserRequest);
        UserAccount userAccount = userAccountService.getCurrenUser();
        if(userAccount.getApplicationUser() != null){
            throw new RuntimeException("UserAccuntd data already saved.");
        }
        applicationUser.setUserAccount(userAccount);
        return saveApplicationUser(applicationUser);
    }

    public ApplicationUser saveApplicationUser(ApplicationUser applicationUser) {
       return applicationUserRepository.save(applicationUser);
    }

    public ApplicationUser getCurrentApplicationUser() {
        UserAccount userAccount = userAccountService.getCurrenUser();
        return applicationUserRepository.findApplicationUserByUserAccount(userAccount).orElseThrow(() -> new RuntimeException("ApplicationUser not found."));
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

    public void deleteUserAccount(UUID id) {
        ApplicationUser applicationUser = applicationUserRepository.findById(id).orElseThrow(() -> new EntryNotFoundException("ApplicationUser not found with id " + id ));
        userAccountService.deleteUserAccount(applicationUser.getUserAccount().getId());
        applicationUserRepository.deleteById(id);
    }

    public List<ApplicationUserDto> getApplicationUsers() {
        List<ApplicationUserDto> applicationUserDto = new ArrayList<>();
        applicationUserRepository.findAll().forEach(it -> applicationUserDto.add(ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(it, it.getUserAccount().getUsername(), it.getUserAccount().getEmail())));
        return applicationUserDto;
    }
}
