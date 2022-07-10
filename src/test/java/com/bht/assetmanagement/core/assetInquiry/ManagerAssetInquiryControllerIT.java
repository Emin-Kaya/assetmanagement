package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.core.asset.AssetService;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.AssetInquiry;
import com.bht.assetmanagement.persistence.entity.Storage;
import com.bht.assetmanagement.persistence.repository.AddressRepository;
import com.bht.assetmanagement.persistence.repository.AssetInquiryRepository;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

public class ManagerAssetInquiryControllerIT extends IntegrationTestSetup {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AssetInquiryRepository assetInquiryRepository;


    @BeforeEach
    public void setUp() {
        assetInquiryRepository.deleteAll();
        addressRepository.deleteAll();
        storageRepository.deleteAll();
        assetRepository.deleteAll();
    }



    @Test
    void cancelAssetInquiryTest() throws Exception {
        AssetInquiry assetInquiry = aValidAssetInquiry();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/assetInquiry/{id}", assetInquiry.getId())
                        .with(getAuthentication(aValidManagerApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent());

    }

    @Test
    void canNotCancelInvalidAssetInquiryTest() throws Exception {
        AssetInquiry assetInquiry = new AssetInquiry();
        assetInquiry.setId(UUID.randomUUID());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/assetInquiry/{id}", assetInquiry.getId())
                        .with(getAuthentication(aValidManagerApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    void confirmAssetInquiryTest() throws Exception {
        AssetInquiry assetInquiry = aValidAssetInquiry();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/assetInquiry/confirm/{id}", assetInquiry.getId())
                        .with(getAuthentication(aValidManagerApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isAccepted());

    }

    @Test
    void canNotConfirmAssetInquiryTest() throws Exception {
        AssetInquiry assetInquiry = new AssetInquiry();
        assetInquiry.setId(UUID.randomUUID());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/assetInquiry/confirm/{id}", assetInquiry.getId())
                        .with(getAuthentication(aValidManagerApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    void handleAssetInquriyInStorageTest() throws Exception {
        Asset asset = aValidAsset();
        Storage storage = aValidStorage();
        storage.getAssets().add(asset);
        storageRepository.save(storage);

        AssetInquiry assetInquiry = aValidAssetInquiry();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/assetInquiry/confirm/handle/{assetInquiryId}", assetInquiry.getId())
                        .with(getAuthentication(aValidManagerApplicationUser())).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isAccepted());
    }

    @Test
    void canNotHandleInvalidAssetInquriyInStorageTest() throws Exception {
        Asset asset = aValidAsset();
        Storage storage = aValidStorage();
        storage.getAssets().add(asset);
        storageRepository.save(storage);

        AssetInquiry assetInquiry = aValidAssetInquiry();
        assetInquiry.setId(UUID.randomUUID());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/assetInquiry/confirm/handle/{assetInquiryId}", assetInquiry.getId())
                        .with(getAuthentication(aValidManagerApplicationUser())).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    void canNotHandleAssetInquriyInInvalidStorageTest() throws Exception {
        Asset asset = aValidAsset();
        Storage storage = aValidStorage();
        storage.setId(UUID.randomUUID());
        storage.getAssets().add(asset);
        storageRepository.save(storage);

        AssetInquiry assetInquiry = aValidAssetInquiry();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/manager/assetInquiry/confirm/handle/{assetInquiryId}", assetInquiry.getId())
                        .with(getAuthentication(aValidManagerApplicationUser())).param("storageId", storage.getId().toString()))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    void getAllAssetInquirysTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/manager/assetInquiry")
                        .with(getAuthentication("MANAGER")))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }

}
