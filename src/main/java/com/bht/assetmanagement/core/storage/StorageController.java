package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.persistence.dto.StorageEmployeeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    @GetMapping
    public List<StorageEmployeeDto> getAllStorages() {
        return storageService.getAllStorages();
    }
}
