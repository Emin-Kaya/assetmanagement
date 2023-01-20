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
import com.bht.assetmanagement.shared.exception.DublicateEntryException;
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
                .filter(it -> !it.isArchived())
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


    public Boolean existsAsset(String serialnumber) {
        return assetRepository.existsBySerialnumber(serialnumber);
    }


    public Asset create(AssetRequest assetRequest, Boolean enable) {
        Asset asset = AssetMapper.INSTANCE.mapRequestToAsset(assetRequest);

        if (existsAsset(assetRequest.getSerialnumber()))
            throw new DublicateEntryException("This asset exists already.");

        asset.setEnable(enable);
        return assetRepository.save(asset);
    }

    public Optional<Asset> getAsset(AssetRequest assetRequest) {
        return assetRepository.findBySerialnumber(assetRequest.getSerialnumber()).filter(it -> !it.isArchived());
    }

    public void saveRequestToStorage(AssetRequest assetRequest) {
        Asset asset;
        if (getAsset(assetRequest).isEmpty()) {
            asset = create(assetRequest, true);
        } else throw new DublicateEntryException("Asset with serialnumber already exists.");
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
        Storage storage = storageService.findStorage(storageId);
        ApplicationUser applicationUser = userAccountService.getCurrenUser().getApplicationUser();
        assetUserHistoryService.update(applicationUser, asset);


        saveAssetToStorage(asset, storage);
        asset.setEnable(true);
        assetRepository.save(asset);


        emailService.sendMessage(applicationUser.getUserAccount().getEmail(), emailUtils.getSubjectRomeveAsset(), emailUtils.getBodyRemoveAsset(asset, storage));

        List<UserAccount> listOfAssetManagers = userAccountService.getAllUsersByRole(Role.MANAGER);


        listOfAssetManagers.forEach(it -> emailService.sendMessage(
                applicationUser.getUserAccount().getEmail(),
                it.getEmail(),
                emailUtils.getSubjectNotificationRomeveAsset(),
                emailUtils.getBodyNotificationRemoveAsset(asset, storage)));


        applicationUserService.save(applicationUser);
    }

    public void removeAssetFromStorage(String assetId, String stroageId) {
        Asset asset = findAsset(assetId);
        Storage storage = storageService.findStorage(stroageId);
        if (!storage.getAssets().contains(asset)) {
            throw new EntryNotFoundException("Asset with id: " + assetId + " does not exists in storage.");
        }
        storage.getAssets().remove(asset);
        storageService.save(storage);
    }

    private Asset findAsset(String id) {
        return assetRepository.findById(UUID.fromString(id)).filter(it -> !it.isArchived()).orElseThrow(() -> new EntryNotFoundException("Asset with id " + id + " does not exist."));
    }

    public void delete(String id) {
        Asset asset = findAsset(id);
        if (!asset.isEnable()) {
            throw new RuntimeException("Asset kann nicht gelöscht werden, weil es von einem User genutzt wird.");
        }
        asset.setArchived(true);
        assetRepository.save(asset);
    }
}
