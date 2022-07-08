package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.persistence.dto.StorageDto;
import com.bht.assetmanagement.persistence.dto.StorageRequest;
import com.bht.assetmanagement.persistence.entity.Storage;
import com.bht.assetmanagement.persistence.repository.StorageRepository;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final StorageRepository storageRepository;

    public List<StorageDto> getAll() {
        return storageRepository.findAll().stream().map(StorageMapper.INSTANCE::mapEntityToStorageDto).collect(Collectors.toList());
    }

    public void save(Storage storage) {
        storageRepository.save(storage);
    }

    public StorageDto create(StorageRequest storageRequest) {
        Storage storage = StorageMapper.INSTANCE.mapStoragRequestToEntity(storageRequest.getName());
        if (existsStorage(storageRequest.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This storage exists already.");
        } else {
            save(storage);
        }
        return StorageMapper.INSTANCE.mapEntityToStorageDto(storage);
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

}

