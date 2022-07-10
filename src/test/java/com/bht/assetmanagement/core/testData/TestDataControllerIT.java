package com.bht.assetmanagement.core.testData;

import com.bht.assetmanagement.IntegrationTestSetup;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDataControllerIT extends IntegrationTestSetup {

    @Test
    void getAllTestDataTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/testdata/all"))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

    }
}
