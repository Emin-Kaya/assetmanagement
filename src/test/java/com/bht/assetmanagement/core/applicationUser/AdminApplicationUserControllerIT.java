package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AdminApplicationUserControllerIT extends IntegrationTestSetup {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Test
    void deleteUserAccountTest() throws Exception {
        ApplicationUser employee = aValidEmployeeApplicationUser();
        long beforeCount = applicationUserRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/admin/applicationUser/{id}", employee.getId())
                        .with(getAuthentication("ADMIN")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent());

        assertEquals(applicationUserRepository.count(), beforeCount - 1);
    }

    @Test
    void canNotDeleteUserAccountTest() throws Exception {
        UUID employeID = UUID.randomUUID();
        long beforeCount = applicationUserRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/admin/applicationUser/{id}", employeID)
                        .with(getAuthentication("ADMIN")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());

        assertEquals(applicationUserRepository.count(), beforeCount);
    }


    @Test
    void getAllApplicationUserTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/admin/applicationUser")
                        .with(getAuthentication("ADMIN")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }
}
