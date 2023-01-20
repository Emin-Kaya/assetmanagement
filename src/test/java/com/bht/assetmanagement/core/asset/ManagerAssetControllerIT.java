package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.dto.AssetRequest;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

public class ManagerAssetControllerIT extends IntegrationTestSetup {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private AssetService assetService;

    @BeforeEach
    public void setUp() {
        assetRepository.deleteAll();
        storageRepository.deleteAll();
    }

    @Test
    void getAllAssetsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/manager/asset")
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    @Transactional
    void getAllAssetsOfUserTest() throws Exception {

        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        Asset asset = testDataBuilder.aValidAsset();

        assetService.saveAssetToApplicationUser(asset.getId().toString(), userAccount.getApplicationUser().getId().toString());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/manager/asset/{userId}", userAccount.getId())
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

    @Test
    @Transactional
    void canNotGetAllAssetsOfUserTest() throws Exception {

        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        Asset asset = testDataBuilder.aValidAsset();

        assetService.saveAssetToApplicationUser(asset.getId().toString(), userAccount.getApplicationUser().getId().toString());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/manager/asset/{userId}", "invalidApplicationUserId")
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


    @Test
    @Transactional
    void canDeleteAssetTest() throws Exception {

        Asset asset = testDataBuilder.aValidAsset();
        asset.setEnable(true);
        assetRepository.save(asset);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/asset/{id}", asset.getId())
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isAccepted());
    }

    @Test
    @Transactional
    void canNotDeleteAssetTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/asset/{id}", "invalidAssetId")
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


    @Test
    @Transactional
    void canSaveAssetToStorageTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/manager/asset/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(testDataBuilder.aValidAssetRequest()))
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isAccepted());
    }

    @Test
    @Transactional
    void canNotSaveAssetToInvalidStorageTest() throws Exception {

        AssetRequest assetRequest = new AssetRequest(
                "123456",
                "iPhone 12",
                "Telefon",
                "keine Notizen",
                "64 GB",
                "invalidStorageId");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/manager/asset/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(assetRequest))
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


}
