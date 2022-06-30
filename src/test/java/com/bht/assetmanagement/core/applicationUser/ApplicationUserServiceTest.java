package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.persistence.dto.ApplicationUserDto;
import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Role;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.liquibase.enabled=false"
        }
)
public class ApplicationUserServiceTest {
    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ApplicationUserMapper INSTANCE = Mappers.getMapper(ApplicationUserMapper.class);

    private UserAccount userAccount;

    @BeforeEach
    void deleteAll() {
        applicationUserRepository.deleteAll();
        userAccountRepository.deleteAll();
        userAccount = aValidUserAccount();
    }

    @Test
    @WithMockUser(username = "testtester", password = "12345678")
    void createApplicationUserTest() {
        ApplicationUser applicationUser = applicationUserService.createApplicationUser(aApplicationUserRequest());
        Assertions.assertEquals(applicationUserRepository.findAll().get(0).getId(), applicationUser.getId());
    }

    @Test
    @WithMockUser(username = "testtester", password = "12345678")
    void getCurrentApplicationUserTest() {
        ApplicationUser expected = aApplicationUser();
        ApplicationUser actual = applicationUserService.getCurrentApplicationUser();
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void saveApplicationUserTest() {
        ApplicationUser applicationUser = applicationUserService.saveApplicationUser(aApplicationUser());
        Assertions.assertEquals(applicationUserRepository.findAll().get(0).getId(), applicationUser.getId());
    }


    @Test
    @WithMockUser(username = "testtester", password = "12345678")
    void getUserDetailsTest() {
        ApplicationUser applicationUser = aApplicationUser();
        ApplicationUserDto expected = INSTANCE.mapEntityToApplicationUserResponse(applicationUser, applicationUser.getUserAccount().getUsername(), applicationUser.getUserAccount().getEmail());
        ApplicationUserDto actual = applicationUserService.getUserDetails();
        Assertions.assertEquals(expected, actual);
    }

    private ApplicationUserRequest aApplicationUserRequest() {
        return new ApplicationUserRequest("MA-12345", "Test", "Tester");
    }

    private ApplicationUser aApplicationUser() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(UUID.randomUUID());
        applicationUser.setEmployeeId("MA-12345");
        applicationUser.setFirstName("Test");
        applicationUser.setLastName("Tester");
        applicationUser.setUserAccount(userAccount);
        return applicationUserRepository.save(applicationUser);
    }

    private UserAccount aValidUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(UUID.randomUUID());
        userAccount.setUsername("testtester");
        userAccount.setEmail("test@mail.de");
        userAccount.setPassword("12345678");
        userAccount.setRole(Role.EMPLOYEE);
        userAccount.setEnabled(true);

        return userAccountRepository.save(userAccount);
    }
}
