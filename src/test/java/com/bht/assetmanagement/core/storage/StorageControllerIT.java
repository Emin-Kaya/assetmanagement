package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.core.asset.AssetService;
import com.bht.assetmanagement.persistence.dto.AssetRequest;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.Storage;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageControllerIT extends IntegrationTestSetup {

    @Autowired
    private  StorageRepository storageRepository;

    @BeforeEach
    public void setUp() {
        storageRepository.deleteAll();
    }

    @Test
    void createStorageTest() throws Exception {
        long beforeCount = storageRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/storage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(aValidStorageRequest()))
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());

        assertEquals(storageRepository.count(), beforeCount + 1);
    }

    @Test
    void canNotCreateDuplicateStorageTest() throws Exception {
        aValidStorage();
        long beforeCount = storageRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/storage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(aValidStorageRequest()))
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isConflict());

        assertEquals(storageRepository.count(), beforeCount);
    }


    @Test
    void getAllStoragesTest() throws Exception {
        aValidStorage();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/storage")
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

    }
}
