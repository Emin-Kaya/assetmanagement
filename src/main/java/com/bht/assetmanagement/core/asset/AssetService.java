package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.core.assetUserHistory.AssetUserHistoryService;
import com.bht.assetmanagement.core.email.EmailService;
import com.bht.assetmanagement.core.storage.StorageService;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
import com.bht.assetmanagement.persistence.dto.AssetDto;
import com.bht.assetmanagement.persistence.dto.AssetRequest;
import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import com.bht.assetmanagement.shared.email.EmailUtils;
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

    private final UserAccountService userAccountService;
    private final StorageService storageService;
    private final AssetUserHistoryService assetUserHistoryService;
    private final EmailService emailService;
    private final EmailUtils emailUtils;

    public List<AssetDto> getAllAssetsOfUser() {
        UserAccount userAccount = userAccountService.getCurrenUser();
        List<Asset> assetsOfApplicationUser = applicationUserService.getApplicationUserByUserAccount(userAccount).getAssetUserHistoryList()
                .stream()
                .filter(it -> it.getLendStatus() == LendStatus.RENTED)
                .map(AssetUserHistory::getAsset)
                .collect(Collectors.toList());

        return assetsOfApplicationUser.stream().map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
    }

    public List<AssetDto> getAllAssetsOfUserByID(UUID id) {
        List<Asset> assetsOfApplicationUser = userAccountService.getUserAccountById(id).getApplicationUser().getAssetUserHistoryList()
                .stream()
                .filter(it -> it.getLendStatus() == LendStatus.RENTED)
                .map(AssetUserHistory::getAsset)
                .collect(Collectors.toList());

        return assetsOfApplicationUser.stream().map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
    }

    public List<AssetDto> getAll() {
        return assetRepository
                .findAll()
                .stream()
                .map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
    }

    public List<AssetDto> getFreeAssets() {
        return assetRepository
                .findAll()
                .stream()
                .filter(it -> !it.isEnable())
                .map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
    }

    public List<AssetDto> getFreeAssetsByAttributes(String assetName, String assetCategory) {
        return assetRepository
                .findAll()
                .stream()
                .filter(it -> it.isEnable())
                .filter(it -> it.getName().equals(assetName))
                .filter(it -> it.getCategory().equals(assetCategory))
                .map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
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

    public void saveAssetToApplicationUser(String assetId, String applicationUserId) {
        ApplicationUser applicationUser = applicationUserService.existsUser(applicationUserId);
        Asset asset = findAsset(assetId);

        applicationUser.getAssetUserHistoryList().add(assetUserHistoryService.create(applicationUser, asset));

        asset.setEnable(false);

        assetRepository.save(asset);
        applicationUserService.save(applicationUser);
    }

    public void saveAssetToStorage(Asset asset, Storage storage) {
        if (!storageService.existsStorage(storage.getName())) {
            throw new EntryNotFoundException("Storage with id: " + storage.getId() + " does not exist.");
        }
        storage.getAssets().add(asset);
        storageService.save(storage);
    }

    public void removeAssetFromUser(String assetId, String storageId) {
        Asset asset = findAsset(assetId);
        UserAccount userAccount = userAccountService.getCurrenUser();
        ApplicationUser applicationUser = applicationUserService.getApplicationUserByUserAccount(userAccount);

        assetUserHistoryService.update(applicationUser, asset);
        saveAssetToStorage(asset, storageService.findStorage(storageId));
        applicationUserService.save(applicationUser);
    }

    public void removeAssetFromStorage(String assetId, String stroageId) {
        UserAccount userAccount = userAccountService.getCurrenUser();
        ApplicationUser applicationUser = applicationUserService.getApplicationUserByUserAccount(userAccount);
        Asset asset = findAsset(assetId);
        Storage storage = storageService.findStorage(stroageId);
        if (!storage.getAssets().contains(asset)) {
            throw new EntryNotFoundException("Asset with id: " + assetId + " does not exists in storage.");
        }
        emailService.sendMessage(applicationUser.getUserAccount().getEmail(), emailUtils.getSubjectRomeveAsset(), emailUtils.getBodyRemoveAsset(asset, storage));
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


    private Asset findAsset(String id) {
        return assetRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntryNotFoundException("Asset with id " + id + " does not exist."));
    }

}
