package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.persistence.dto.ApplicationUserDto;
import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import com.bht.assetmanagement.shared.exception.DublicateEntryException;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;


    public ApplicationUserDto create(ApplicationUserRequest applicationUserRequest, UserAccount userAccount) {
        ApplicationUser applicationUser = ApplicationUserMapper.INSTANCE.mapRequestToApplicationUser(applicationUserRequest);
        if (existsApplicationUser(applicationUserRequest.getEmployeeId())) {
            throw new DublicateEntryException("This application user exists already.");
        }
        applicationUser.setUserAccount(userAccount);
        save(applicationUser);
        return ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(applicationUser, applicationUser.getUserAccount().getUsername(), applicationUser.getUserAccount().getUsername());
    }

    public boolean existsApplicationUser(String employeeID) {
        return applicationUserRepository.existsByEmployeeId(employeeID);
    }

    public ApplicationUser save(ApplicationUser applicationUser) {
        return applicationUserRepository.save(applicationUser);
    }

    public ApplicationUser getApplicationUserByUserAccount(UserAccount userAccount) {
        return applicationUserRepository.findApplicationUserByUserAccount(userAccount).orElseThrow(() -> new EntryNotFoundException("Application user not found."));
    }

    public ApplicationUser existsUser(String id) {
        return applicationUserRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntryNotFoundException("Asset with id " + id + " does not exist."));
    }
}
