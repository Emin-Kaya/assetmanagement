package com.bht.assetmanagement.core.testData;

import com.bht.assetmanagement.IntegrationTestSetup;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class TestDataControllerIT extends IntegrationTestSetup {

    @Test
    void getAllTestDataTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/testdata/all"))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

    }
}
