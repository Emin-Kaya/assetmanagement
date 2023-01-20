package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.Storage;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.UUID;

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
    void getAllAssetsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/asset")
                        .with(getAuthentication("EMPLOYEE")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    @Transactional
    void removeAssetFromStorageUserAndSaveToStorage() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidUserAccount("EMPLOYEE");
        Asset asset = testDataBuilder.aValidAsset();
        assetService.saveAssetToApplicationUser(asset.getId().toString(), userAccount.getApplicationUser().getId().toString());


        Storage storage = testDataBuilder.aValidStorage();


        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/asset/{id}", asset.getId())
                        .with(getAuthentication(userAccount.getApplicationUser())).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isAccepted());
    }

    @Test
    @Transactional
    void canNotRemoveInvalidAssetFromUserAndSaveToStorage() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidUserAccount("EMPLOYEE");
        Asset asset = testDataBuilder.aValidAsset();
        assetService.saveAssetToApplicationUser(asset.getId().toString(), userAccount.getApplicationUser().getId().toString());


        Storage storage = testDataBuilder.aValidStorage();


        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/asset/{id}", "invalidId")
                        .with(getAuthentication(userAccount.getApplicationUser()))
                        .param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @Test
    void canNotRemoveAssetFromUserAndSaveToInvalidStorage() throws Exception {
        Storage storage = testDataBuilder.aValidStorage();
        Asset asset = new Asset();
        asset.setId(UUID.randomUUID());
        asset.setName("invalidAssetName");
        asset.setCategory("invalidAssetCategory");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/asset/{assetId}", asset.getId())
                        .with(getAuthentication("MANAGER"))
                        .param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    void canNotRemoveAssetFromInvalidStorageTest() throws Exception {
        Asset asset = testDataBuilder.aValidAsset();
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
