package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminUserAccountControllerIT extends IntegrationTestSetup {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    public void setUp() {
        userAccountRepository.deleteAll();
    }

    @Test
    void createAssetUserAccountTest() throws Exception {
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
    void canNotCreateDublicatUserAccountTest() throws Exception {
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

    @Test
    void deleteUserAccountTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/admin/userAccount" )
                        .with(getAuthentication("ADMIN"))
                        .param("id", userAccount.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent());
    }

    @Test
    void canNotDeleteUserAccountTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/admin/userAccount" )
                        .with(getAuthentication("ADMIN"))
                        .param("id", "invalidUserId"))

                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }
}
