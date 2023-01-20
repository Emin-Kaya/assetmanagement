package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.AssetInquiryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssetInquiryControllerIT extends IntegrationTestSetup {

    @Autowired
    private AssetInquiryRepository assetInquiryRepository;

    @Test
    void createAssetInquiryTest() throws Exception {
        UserAccount userAccount = testDataBuilder.aValidEmployeeUserAccount();
        long beforeCount = assetInquiryRepository.count();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/assetInquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBody(testDataBuilder.aValidAssetInquiryRequest()))
                        .with(getAuthentication(userAccount.getApplicationUser())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());

        assertEquals(assetInquiryRepository.count(), beforeCount + 1);
    }
}
