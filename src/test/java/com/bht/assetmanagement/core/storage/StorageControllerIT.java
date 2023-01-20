package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class StorageControllerIT extends IntegrationTestSetup {

    @Autowired
    private StorageRepository storageRepository;

    @BeforeEach
    public void setUp() {
        storageRepository.deleteAll();
    }


    @Test
    void getAllStoragesTest() throws Exception {
        testDataBuilder.aValidStorage();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/storage")
                        .with(getAuthentication("EMPLOYEE")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

    }

}