package com.bht.assetmanagement.core.auth;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.core.refreshToken.RefreshTokenService;
import com.bht.assetmanagement.core.verificationToken.VerificationTokenService;
import com.bht.assetmanagement.persistence.dto.*;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.RefreshTokenRepository;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    void signinTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/signin")
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
                        .post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(loginRequest)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @Test
    void signOutTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        AuthenticationResponse authenticationResponse = authService.signIn(testDataBuilder.aValidEmployeeloginRequest());
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(authenticationResponse.getRefreshToken(), authenticationResponse.getUsername());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(refreshTokenRequest)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void canNotsignOutTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        AuthenticationResponse authenticationResponse = authService.signIn(testDataBuilder.aValidEmployeeloginRequest());
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("invalidRefreshtoken", authenticationResponse.getUsername());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(refreshTokenRequest)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @Test
    void changeUserPasswordTest() throws Exception {
        ApplicationUser applicationUser = testDataBuilder.aValidEmployeeApplicationUser();
        refreshTokenService.generateRefreshToken(applicationUser.getUserAccount().getUsername());

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("12345678", "newPassword");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/auth/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(passwordChangeRequest))
                        .with(getAuthentication(applicationUser)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void canNotchangeUserPasswordTest() throws Exception {
        ApplicationUser applicationUser = testDataBuilder.aValidEmployeeApplicationUser();
        refreshTokenService.generateRefreshToken(applicationUser.getUserAccount().getUsername());
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("invalidPassword", "newPassword");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/auth/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(passwordChangeRequest))
                        .with(getAuthentication(applicationUser)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    void changeUserEmailTest() throws Exception {
        ApplicationUser applicationUser = testDataBuilder.aValidEmployeeApplicationUser();
        EmailChangeRequest emailChangeRequest = new EmailChangeRequest(applicationUser.getUserAccount().getEmail(), "newEmail@mail.de");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/auth/change/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(emailChangeRequest))
                        .with(getAuthentication(applicationUser)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void canNotchangeUserEmailTest() throws Exception {
        ApplicationUser applicationUser = testDataBuilder.aValidEmployeeApplicationUser();
        EmailChangeRequest emailChangeRequest = new EmailChangeRequest("invalidOldEmail", "newEmail@mail.de");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/auth/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(emailChangeRequest))
                        .with(getAuthentication(applicationUser)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


}
