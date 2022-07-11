package com.bht.assetmanagement.core.auth;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.core.verificationToken.VerificationTokenService;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.Valid;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthControllerIT extends IntegrationTestSetup {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @BeforeEach
    public void setUp() {
        userAccountRepository.deleteAll();
    }

    @Test
    void signupTest() throws Exception {
        long beforeCount = userAccountRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRequestBody(testDataBuilder.aValidRegisterRequest())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());

        assertEquals(userAccountRepository.count(), beforeCount + 1);
    }

    @Test
    void activateAccountTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        userAccount.setEnabled(false);
        userAccountRepository.save(userAccount);
        String token = verificationTokenService.generateVerificationToken(userAccount);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/auth/activate/account/{token}", token))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

    }

    @Test
    void signinTest() throws Exception{
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(testDataBuilder.aValidEmployeeloginRequest())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());
    }
}
