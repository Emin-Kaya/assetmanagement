package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.core.userAccount.UserAccountService;
import com.bht.assetmanagement.persistence.dto.ApplicationUserDto;
import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import com.bht.assetmanagement.shared.exception.DublicateEntryException;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final UserAccountService userAccountService;


    public ApplicationUserDto create(ApplicationUserRequest applicationUserRequest) {
        ApplicationUser applicationUser = ApplicationUserMapper.INSTANCE.mapRequestToApplicationUser(applicationUserRequest);
        UserAccount userAccount = userAccountService.getCurrenUser();
        if (userAccount.getApplicationUser() != null) {
            throw new DublicateEntryException("User account data already saved."); //TODO duplicate  tnry exce
        }
        applicationUser.setUserAccount(userAccount);
        save(applicationUser);
        return ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(applicationUser, applicationUser.getUserAccount().getUsername(), applicationUser.getUserAccount().getUsername());
    }

    public ApplicationUser save(ApplicationUser applicationUser) {
        return applicationUserRepository.save(applicationUser);
    }

    public ApplicationUser getCurrentUser() {
        UserAccount userAccount = userAccountService.getCurrenUser();
        return applicationUserRepository.findApplicationUserByUserAccount(userAccount).orElseThrow(() -> new EntryNotFoundException("Application user not found."));
    }

    public ApplicationUserDto getProfileInformation() {
        ApplicationUser applicationUser = getCurrentUser();

        return ApplicationUserDto.builder()
                .id(applicationUser.getId().toString())
                .firstName(applicationUser.getFirstName())
                .lastName(applicationUser.getLastName())
                .username(applicationUser.getUserAccount().getUsername())
                .email(applicationUser.getUserAccount().getEmail()).build();
    }

    public void delete(UUID id) {
        ApplicationUser applicationUser = applicationUserRepository.findById(id).orElseThrow(() -> new EntryNotFoundException("ApplicationUser not found with id " + id));
        applicationUserRepository.delete(applicationUser);
    }

    public List<ApplicationUserDto> getAll() {
        List<ApplicationUserDto> applicationUserDto = new ArrayList<>();
        applicationUserRepository.findAll().forEach(it -> applicationUserDto.add(ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(it, it.getUserAccount().getUsername(), it.getUserAccount().getEmail())));
        return applicationUserDto;
    }

    public boolean existsUser(UUID id) {
        return applicationUserRepository.existsById(id);
    }
}
