package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.persistence.dto.StorageResponse;
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
    public StorageResponse createStorage(@RequestBody StorageRequest storageRequest) {
        return storageService.create(storageRequest);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteStorafe(@RequestParam String id) {
        storageService.delete(id);
    }

    @GetMapping
    public List<StorageResponse> getAllStorages() {
        return storageService.getAll();
    }
}
