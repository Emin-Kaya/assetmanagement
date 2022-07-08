package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.core.assetUserHistory.AssetUserHistoryService;
import com.bht.assetmanagement.core.storage.StorageService;
import com.bht.assetmanagement.persistence.dto.AssetDto;
import com.bht.assetmanagement.persistence.dto.AssetRequest;
import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final ApplicationUserService applicationUserService;
    private final StorageService storageService;
    private final AssetUserHistoryService assetUserHistoryService;

    public List<AssetDto> getAllAssetsOfUser() {
        List<Asset> assetsOfApplicationUser = applicationUserService.getCurrentUser().getAssetUserHistoryList()
                .stream()
                .filter(it -> it.getLendStatus() == LendStatus.RENTED)
                .map(AssetUserHistory::getAsset)
                .collect(Collectors.toList());

        return assetsOfApplicationUser.stream().map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
    }

    public List<AssetDto> getAll() {
        return assetRepository.findAll().stream().map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
    }

    public Asset create(AssetRequest assetRequest) {
        Asset asset = AssetMapper.INSTANCE.mapRequestToAsset(assetRequest);
        return assetRepository.save(asset);
    }

    public Optional<Asset> getAsset(AssetRequest assetRequest) {
        return assetRepository.findByName(assetRequest.getName());
    }

    public void saveRequestToStorage(AssetRequest assetRequest) {
        Asset asset = new Asset();
        if (getAsset(assetRequest).isEmpty()) {
            asset = create(assetRequest);
        }
        saveAssetToStorage(asset, storageService.findStorage(assetRequest.getStorageId()));
    }

    public void saveAssetToApplicationUser(Asset asset, ApplicationUser applicationUser) {
        if (!applicationUserService.existsUser(applicationUser.getId())) {
            throw new EntryNotFoundException("Application user with id: " + applicationUser.getId() + " does not exist.");
        }
        applicationUser.getAssetUserHistoryList().add(assetUserHistoryService.create(applicationUser, asset));
        applicationUserService.save(applicationUser);
    }

    public void saveAssetToStorage(Asset asset, Storage storage) {
        if (!storageService.existsStorage(storage.getName())) {
            throw new EntryNotFoundException("Storage with id: " + storage.getId() + " does not exist.");
        }
        storage.getAssets().add(asset);
        storageService.save(storage);
    }

    public void removeAssetFromUser(String assetId, String userId) {
        Asset asset = findAsset(assetId);
        ApplicationUser applicationUser = applicationUserService.getCurrentUser();

        assetUserHistoryService.update(applicationUser, asset);
        saveAssetToStorage(asset, storageService.findStorage(userId));
        applicationUserService.save(applicationUser);
    }

    public void removeAssetFromStorage(String assetId, String stroageId) {
        Asset asset = findAsset(assetId);
        Storage storage = storageService.findStorage(stroageId);
        storage.getAssets().remove(asset);
        storageService.save(storage);
    }

    public Asset getAssetFromAssetInquiry(AssetInquiry assetInquiry) {
        AssetRequest assetRequest = AssetRequest.builder()
                .name(assetInquiry.getAssetName())
                .category(assetInquiry.getAssetCategory()).build();

        Optional<Asset> asset = getAsset(assetRequest);
        if (getAsset(assetRequest).isEmpty()) {
            asset = Optional.ofNullable(create(assetRequest));
        }
        return asset.orElseThrow();
    }

    public List<Storage> getAllStoragesContainsAsset(String assetId) {
        Asset asset = findAsset(assetId);
        return storageService.findAll()
                .stream()
                .filter(storage ->
                        storage.getAssets().contains(asset)
                ).collect(Collectors.toList());
    }

    private Asset findAsset(String id) {
        return assetRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntryNotFoundException("Asset with id " + id + " does not exist."));
    }

}
