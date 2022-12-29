package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.persistence.dto.StorageDto;
import com.bht.assetmanagement.persistence.dto.StorageResponse;
import com.bht.assetmanagement.persistence.dto.StorageRequest;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.Storage;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import com.bht.assetmanagement.shared.exception.DublicateEntryException;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final StorageRepository storageRepository;

    public List<StorageResponse> getAll() {
        return storageRepository.findAll().stream().map(StorageMapper.INSTANCE::mapEntityToStorageResponse).collect(Collectors.toList());
    }

    public List<StorageDto> getAllStorages() {
        return storageRepository.findAll().stream().map(StorageMapper.INSTANCE::mapEntityToStorageDto).collect(Collectors.toList());
    }

    public void save(Storage storage) {
        storageRepository.save(storage);
    }

    public StorageResponse create(StorageRequest storageRequest) {
        Storage storage = StorageMapper.INSTANCE.mapStoragRequestToEntity(storageRequest.getName());
        if (existsStorage(storageRequest.getName())) {
            throw new DublicateEntryException("This storage exists already.");
        } else {
            save(storage);
        }
        return StorageMapper.INSTANCE.mapEntityToStorageResponse(storage);
    }

    public Boolean existsStorage(String name) {
        return storageRepository.existsByName(name);
    }

    public Storage findStorage(String id) {
        return storageRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntryNotFoundException("Storage with id " + id + " does not exist."));
    }

    public List<Storage> findAll() {
        return storageRepository.findAll();
    }

    public List<StorageDto> getAllStoragesContainsAsset(String name, String assetCategory) {
        return findAll()
                .stream()
                .filter(storage -> containsAsset(storage, name, assetCategory)
                ).map(StorageMapper.INSTANCE::mapEntityToStorageDto).collect(Collectors.toList());
    }


    private boolean containsAsset(Storage storage, String name, String assetCategory){
        boolean filter = false;

        for (Asset asset: storage.getAssets()) {
            if (asset.getName().equals(name) && asset.getCategory().equals(assetCategory)) {
                filter = true;
                break;
            }
        }

        return filter;
    }

}

