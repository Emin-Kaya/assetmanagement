package com.bht.assetmanagement.core.auth;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.core.refreshToken.RefreshTokenService;
import com.bht.assetmanagement.core.verificationToken.VerificationTokenService;
import com.bht.assetmanagement.persistence.dto.*;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.RefreshTokenRepository;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthControllerIT extends IntegrationTestSetup {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;



    @BeforeEach
    public void setUp() {
        verificationTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userAccountRepository.deleteAll();
    }

    @Test
    void signupTest() throws Exception {
        long beforeCount = userAccountRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/signup")
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
                        .get("/api/v1/auth/activate/account/{token}", token))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

    }

    @Test
    void signinTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(testDataBuilder.aValidEmployeeloginRequest())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void canNotSignInTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        LoginRequest loginRequest = testDataBuilder.aValidEmployeeloginRequest();
        loginRequest.setPassword("invalidPassword");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(loginRequest)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @Test
    void signOutTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        LoginRequest loginRequest = testDataBuilder.aValidEmployeeloginRequest();

        AuthenticationResponse authenticationResponse = authService.signIn(loginRequest);
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(authenticationResponse.getRefreshToken(), userAccount.getUsername());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(refreshTokenRequest)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void canNotSignOutTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        LoginRequest loginRequest = testDataBuilder.aValidEmployeeloginRequest();

        AuthenticationResponse authenticationResponse = authService.signIn(loginRequest);
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("invalidRefreshtoken", userAccount.getUsername());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(refreshTokenRequest)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @Test
    void changeUserPasswordTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        refreshTokenService.generateRefreshToken(userAccount.getUsername());

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("12345678", "newPassword");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/auth/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(passwordChangeRequest))
                        .with(getAuthentication(userAccount.getApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void canNotchangeUserPasswordTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        refreshTokenService.generateRefreshToken(userAccount.getUsername());
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("invalidPassword", "newPassword");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/auth/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(passwordChangeRequest))
                        .with(getAuthentication(userAccount.getApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    void changeUserEmailTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        EmailChangeRequest emailChangeRequest = new EmailChangeRequest(userAccount.getEmail(), "newEmail@mail.de");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/auth/change/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(emailChangeRequest))
                        .with(getAuthentication(userAccount.getApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void canNotchangeUserEmailTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        EmailChangeRequest emailChangeRequest = new EmailChangeRequest("invalidOldEmail", "newEmail@mail.de");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/auth/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(emailChangeRequest))
                        .with(getAuthentication(userAccount.getApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }
}
