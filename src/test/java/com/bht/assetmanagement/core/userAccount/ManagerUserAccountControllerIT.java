package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class ManagerUserAccountControllerIT extends IntegrationTestSetup {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @BeforeEach
    public void setUp() {

        userAccountRepository.deleteAll();
        applicationUserRepository.deleteAll();
    }

    @Test
    void getAllUserAccountsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/manager/userAccount")
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void getUserByIdTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/manager/userAccount/{id}", userAccount.getId() )
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void canNotGetUserByIdTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/manager/userAccount/{id}", "invalidUserId" )
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }
}
