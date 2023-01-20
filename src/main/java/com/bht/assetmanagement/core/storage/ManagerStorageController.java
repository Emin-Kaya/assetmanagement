package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.persistence.dto.StorageRequest;
import com.bht.assetmanagement.persistence.dto.StorageResponse;
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
    public StorageResponse createStorage(@RequestBody StorageRequest storageRequest) {
        return storageService.create(storageRequest);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStorage(@RequestParam String id) {
        storageService.delete(id);
    }

    @GetMapping
    public List<StorageResponse> getAllStorages() {
        return storageService.getAll();
    }

    @GetMapping("/{id}")
    public StorageResponse getById(@PathVariable String id) {
        return storageService.getStorageById(id);
    }
}
