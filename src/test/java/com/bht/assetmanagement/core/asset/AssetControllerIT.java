package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.IntegrationTestSetup;
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

public class AssetControllerIT extends IntegrationTestSetup {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private AssetService assetService;

    @BeforeEach
    public void setUp() {
        storageRepository.deleteAll();
        assetRepository.deleteAll();
    }

    @Test
    void saveAssetToStorageTest() throws Exception {
        long beforeCount = assetRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/asset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(aValidAssetRequest()))
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isAccepted());

        assertEquals(assetRepository.count(), beforeCount + 1);
    }


    @Test
    void canNotSaveAssetToStorageTest() throws Exception {
        AssetRequest assetRequest = new AssetRequest("iPhone 12", "Telefon", "invalidStorageID");


        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/asset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(assetRequest))
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


    @Test
    void getAllAssetsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/asset")
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void removeAssetFromStorageTest() throws Exception {
        Storage storage = aValidStorage();
        Asset asset = aValidAsset();
        assetService.saveAssetToStorage(asset, storage);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/asset/{assetId}", asset.getId())
                        .with(getAuthentication("MANAGER")).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void canNotRemoveAssetFromStorageTest() throws Exception {
        Storage storage = aValidStorage();
        Asset asset = aValidAsset();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/asset/{assetId}", asset.getId())
                        .with(getAuthentication("MANAGER")).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    void canNotRemoveInvalidAssetFromStorageTest() throws Exception {
        Storage storage = aValidStorage();
        Asset asset = new Asset();
        asset.setId(UUID.randomUUID());
        asset.setName("invalidAssetName");
        asset.setCategory("invalidAssetCategory");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/asset/{assetId}", asset.getId())
                        .with(getAuthentication("MANAGER")).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    void canNotRemoveAssetFromInvalidStorageTest() throws Exception {
        Asset asset = aValidAsset();
        Storage storage = new Storage();
        storage.setId(UUID.randomUUID());
        storage.setName("invalidStorage");
        storage.getAssets().add(asset);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/asset/{assetId}", asset.getId())
                        .with(getAuthentication("MANAGER")).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }
}
