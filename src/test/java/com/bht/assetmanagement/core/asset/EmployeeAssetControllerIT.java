package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.Storage;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

public class EmployeeAssetControllerIT extends IntegrationTestSetup {

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
    void getAllEmployeeAssetsTest() throws Exception {
        ApplicationUser applicationUser = testDataBuilder.aValidEmployeeApplicationUser();
        Asset asset = testDataBuilder.aValidAsset();

      //  assetService.saveAssetToApplicationUser(asset, applicationUser);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/employee/asset")
                        .with(getAuthentication(applicationUser)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    void removeAssetFromUserTest() throws Exception {
        ApplicationUser applicationUser = testDataBuilder.aValidEmployeeApplicationUser();
        Asset asset = testDataBuilder.aValidAsset();
      //  assetService.saveAssetToApplicationUser(asset, applicationUser);

        Storage storage = testDataBuilder.aValidStorage();


        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/employee/asset/{assetId}", asset.getId())
                        .with(getAuthentication(applicationUser)).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent());
    }


    @Test
    void canNotRemoveAssetFromUserTest() throws Exception {
        ApplicationUser applicationUser = testDataBuilder.aValidEmployeeApplicationUser();
        Asset asset = testDataBuilder.aValidAsset();

        Storage storage = testDataBuilder.aValidStorage();


        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/employee/asset/{assetId}", asset.getId())
                        .with(getAuthentication(applicationUser)).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    void canNotRemoveInvalidAssetFromUserTest() throws Exception {
        ApplicationUser applicationUser = testDataBuilder.aValidEmployeeApplicationUser();
        Storage storage = testDataBuilder.aValidStorage();
        Asset asset = new Asset();
        asset.setId(UUID.randomUUID());
        asset.setName("invalidAssetName");
        asset.setCategory("invalidAssetCategory");


        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/employee/asset/{assetId}", asset.getId())
                        .with(getAuthentication(applicationUser)).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }


    @Test
    void canNotRemoveAssetFromUserAndSaveIntoInvalidAssetTest() throws Exception {
        ApplicationUser applicationUser = testDataBuilder.aValidEmployeeApplicationUser();
        Asset asset = testDataBuilder.aValidAsset();
        Storage storage = new Storage();
        storage.setId(UUID.randomUUID());
        storage.setName("invalidStorage");
        storage.getAssets().add(asset);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/employee/asset/{assetId}", asset.getId())
                        .with(getAuthentication(applicationUser)).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }
}
