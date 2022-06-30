package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.persistence.dto.AssetDto;
import com.bht.assetmanagement.persistence.dto.AssetRequest;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final ApplicationUserService applicationUserService;

    public Asset createAsset(AssetRequest assetRequest) {
        Asset asset = AssetMapper.INSTANCE.mapRequestToAsset(assetRequest);
        return assetRepository.save(asset);
    }

    public Asset getAsset(AssetRequest assetRequest) {
        return assetRepository.findByName(assetRequest.getName()).orElseGet(() -> createAsset(assetRequest));
    }

    public void saveAssetToApplicationUser(Asset asset, ApplicationUser applicationUser) {
        applicationUser.getAssets().add(asset);
        applicationUserService.saveApplicationUser(applicationUser);
    }

    public List<AssetDto> getAllAssetsOfUser() {
        List<Asset> assetsOfApplicationUser = applicationUserService.getCurrentApplicationUser().getAssets();
        return assetsOfApplicationUser.stream().map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
    }

    public void removeAssetFromUser(String assetId) {
        ApplicationUser applicationUser = applicationUserService.getCurrentApplicationUser();
        applicationUser.getAssets().remove(assetRepository.findById(UUID.fromString(assetId)).orElseThrow(() -> new EntryNotFoundException("Asset with id " + assetId + " does not exist.")));

        //TODO EMAIL SERVICE SEND EMAIL TO ASSETMANAGER + speichere in unternehmer storage
        applicationUserService.saveApplicationUser(applicationUser);
    }

    public List<AssetDto> getAllAssets() {
        return assetRepository.findAll().stream().map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
    }

    public void deleteAsset(String assetId) {
        Asset asset = assetRepository.findById(assetId).orElseThrow(() -> new EntryNotFoundException("Asset with id " + assetId + "does not exist."));

        //TODO was mit assets die user haben ?
        assetRepository.deleteById(assetId);
    }
}
