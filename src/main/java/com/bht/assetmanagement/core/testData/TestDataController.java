package com.bht.assetmanagement.core.testData;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("testdata")
@RestController
@RequestMapping("/api/v1/testdata")
@RequiredArgsConstructor
public class TestDataController {
    private final TestDataService testDataService;

    @GetMapping("/all")
    public void resetAll() {
        testDataService.resetAll();
    }

}
