package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationUserControllerIT extends IntegrationTestSetup {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Test
    void createApplicationUserTest() throws Exception {
        long beforeCount = applicationUserRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/applicationUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(aValidApplicationRequest()))
                        .with(getAuthentication("EMPLOYEE")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());

        assertEquals(applicationUserRepository.count(), beforeCount + 1);
    }

    @Test
    void canNotCreateApplicationUserTest() throws Exception {
        long beforeCount = applicationUserRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/applicationUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(aValidApplicationRequest()))
                        .with(getAuthentication(aValidEmployeeApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isConflict());
    }


    @Test
    void getUserDetailsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/applicationUser")
                        .with(getAuthentication(aValidEmployeeApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }
}
