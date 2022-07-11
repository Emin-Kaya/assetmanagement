package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminUserControllerIT extends IntegrationTestSetup {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    void createAssetManagerUserAccountTest() throws Exception {
        long beforeCount = userAccountRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/admin/userAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(testDataBuilder.aValidUserAccountRequest()))
                        .with(getAuthentication("ADMIN")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());

        assertEquals(userAccountRepository.count(), beforeCount + 2);
    }

    @Test
    void canNotCreateDublicateAssetManagerUserAccountTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidManagerUserAccount();
        userAccount.setUsername("aValidUsername");
        userAccount.setEmail("aEmail@mail.de");
        userAccountRepository.save(userAccount);
        long beforeCount = userAccountRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/admin/userAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(testDataBuilder.aValidUserAccountRequest()))
                        .with(getAuthentication("ADMIN")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isConflict());

        assertEquals(userAccountRepository.count(), beforeCount + 1);
    }
}
