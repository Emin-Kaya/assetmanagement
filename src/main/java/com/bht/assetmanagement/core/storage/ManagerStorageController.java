package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.persistence.dto.StorageDto;
import com.bht.assetmanagement.persistence.dto.StorageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/storage")
@RequiredArgsConstructor
public class ManagerStorageController {
    private final StorageService storageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StorageDto createStorage(@RequestBody StorageRequest storageRequest) {
        return storageService.create(storageRequest);
    }

    @GetMapping
    public List<StorageDto> getAllStorages() {
        return storageService.getAll();
    }
}
