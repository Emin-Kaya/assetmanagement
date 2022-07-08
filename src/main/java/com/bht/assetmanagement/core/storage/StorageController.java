package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.persistence.dto.StorageDto;
import com.bht.assetmanagement.persistence.dto.StorageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    @PostMapping
    public StorageDto createStorage(@RequestBody StorageRequest storageRequest) {
        return storageService.create(storageRequest);
    }

    @GetMapping
    public List<StorageDto> getAllStorages() {
        return storageService.getAll();
    }
}
