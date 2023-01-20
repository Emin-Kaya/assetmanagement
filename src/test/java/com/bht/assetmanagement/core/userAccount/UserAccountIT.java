package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class UserAccountIT extends IntegrationTestSetup {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    public void setUp() {
        userAccountRepository.deleteAll();
    }


    @Test
    void getCurrentUserTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidUserAccount("EMPLOYEE");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/userAccount")
                        .with(getAuthentication(userAccount.getApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

}
