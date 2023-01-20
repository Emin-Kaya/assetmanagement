package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.core.asset.AssetMapper;
import com.bht.assetmanagement.persistence.dto.AssetDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final StorageRepository storageRepository;

    public List<StorageResponse> getAll() {
        List<Storage> storageList = findAll();
        List<StorageResponse> storageResponses = new ArrayList<>();

        for (Storage storage:storageList ) {
            storageResponses.add(
                    StorageResponse
                            .builder()
                            .id(storage.getId().toString())
                            .name(storage.getName())
                            .assetDtos(assetDtoListNotArchived(storage))
                            .build()
            );
        }

        return  storageResponses;
    }

    public List<StorageDto> getAllStorages() {
        return storageRepository.findAll().stream().filter(it->!it.isArchived()).map(StorageMapper.INSTANCE::mapEntityToStorageDto).collect(Collectors.toList());
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

    public void delete(String id) {
        Storage storage = findStorage(id);

        List<Asset> list = storage.getAssets().stream().filter(it->!it.isArchived()).collect(Collectors.toList());

        if (list.isEmpty()){
            storage.setArchived(true);
            save(storage);
        }else throw new RuntimeException("Storage cannot be deleted because there are assets inside.");
    }

    public Boolean existsStorage(String name) {
        return storageRepository.existsByName(name);
    }

    public Storage findStorage(String id) {
        return storageRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntryNotFoundException("Storage with id " + id + " does not exist."));
    }

    public List<Storage> findAll() {
        return storageRepository.findAll().stream().filter(it->!it.isArchived()).collect(Collectors.toList());
    }

    public List<StorageResponse> getAllStoragesContainsAssets(String name, String assetCategory) {
        return findAll()
                .stream()
                .filter(storage -> containsAsset(storage, name, assetCategory)
                ).map(StorageMapper.INSTANCE::mapEntityToStorageResponse).collect(Collectors.toList());
    }


    public List<StorageResponse> getAllStoragesContainsAsset(String name, String assetCategory) {
         List<Storage> storageList = findAll()
                .stream()
                .filter(storage -> containsAsset(storage, name, assetCategory))
                 .collect(Collectors.toList());



         List<StorageResponse> storageResponses = new ArrayList<>();


        for (Storage storage:storageList ) {
            List<AssetDto> assetDtoList = storage
                    .getAssets()
                    .stream()
                    .filter(it -> it.getName().equals(name))
                    .filter(it -> !it.isArchived())
                    .map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());

            storageResponses.add(
                    StorageResponse
                            .builder()
                            .id(storage.getId().toString())
                            .name(storage.getName())
                            .assetDtos(assetDtoList)
                            .build()
            );
        }

         return  storageResponses;
    }


    public Storage getStorageIdByAsset(String assetId) {
        return findAll()
                .stream()
                .filter(storage -> containsAssetId(storage, assetId)).findFirst().orElseThrow();
    }

    public StorageResponse getStorageById(String id) {
        Storage storage = findStorage(id);

        return StorageResponse
                .builder()
                .id(storage.getId().toString())
                .name(storage.getName())
                .assetDtos(assetDtoListNotArchived(storage))
                .build();
    }


    private boolean containsAsset(Storage storage, String name, String assetCategory){
        boolean filter = false;

        for (Asset asset: storage.getAssets()) {
            if (asset.getName().equals(name) && asset.getCategory().equals(assetCategory) && !asset.isArchived()) {
                filter = true;
                break;
            }
        }

        return filter;
    }

    private boolean containsAssetId(Storage storage, String assetId){
        boolean filter = false;

        for (Asset asset: storage.getAssets()) {
            if (asset.getId().equals(UUID.fromString(assetId))) {
                filter = true;
                break;
            }
        }

        return filter;
    }

    private List<AssetDto> assetDtoListNotArchived(Storage storage){
        return storage
                .getAssets()
                .stream()
                .filter(it -> !it.isArchived())
                .map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
    }
}

