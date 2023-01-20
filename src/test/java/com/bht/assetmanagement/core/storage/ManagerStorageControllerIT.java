package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.Storage;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManagerStorageControllerIT extends IntegrationTestSetup {

    @Autowired
    private StorageRepository storageRepository;

    @BeforeEach
    public void setUp() {
        storageRepository.deleteAll();
    }

    @Test
    void createStorageTest() throws Exception {
        long beforeCount = storageRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/manager/storage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(testDataBuilder.aValidStorageRequest()))
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());

        assertEquals(storageRepository.count(), beforeCount + 1);
    }

    @Test
    void canNotCreateDuplicateStorageTest() throws Exception {
        testDataBuilder.aValidStorage();
        long beforeCount = storageRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/manager/storage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(testDataBuilder.aValidStorageRequest()))
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isConflict());

        assertEquals(storageRepository.count(), beforeCount);
    }

    @Test
    void deleteStorageTest() throws Exception {
        Storage storage = testDataBuilder.aValidStorage();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/storage")
                        .with(getAuthentication("MANAGER"))
                        .param("id", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent());

    }

    @Test
    void caNotDeleteStorageTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/storage")
                        .with(getAuthentication("MANAGER"))
                        .param("id", "invalidStorageID"))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());

    }

    @Test
    void caNotDeleteStorageWhichContainsAssetsTest() throws Exception {
        Storage storage = testDataBuilder.aValidStorage();

        Asset asset = testDataBuilder.aValidAsset();

        storage.setAssets(List.of(asset));
        storageRepository.save(storage);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/storage")
                        .with(getAuthentication("MANAGER"))
                        .param("id", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());

    }

    @Test
    void getAllStoragesTest() throws Exception {
        testDataBuilder.aValidStorage();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/manager/storage")
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

    }

    /*@Test
    void getAllStorageDtosTest() throws Exception {
        testDataBuilder.aValidStorage();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/manager/storage/dto")
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

    }*/
}
