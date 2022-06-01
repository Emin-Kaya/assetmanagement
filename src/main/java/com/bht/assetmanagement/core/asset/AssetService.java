package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.persistence.dto.AssetDto;
import com.bht.assetmanagement.persistence.dto.AssetRequest;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
        Set<Asset> assetsOfApplicationUser = applicationUserService.getCurrentApplicationUser().getAssets();

        List<AssetDto> assetDtoList = new ArrayList<>();
        assetsOfApplicationUser.forEach(it -> assetDtoList.add(AssetMapper.INSTANCE.mapEntityToAssetDto(it)));
        return assetDtoList;
    }

    public void removeAssetFromUser(String id) {
        ApplicationUser applicationUser = applicationUserService.getCurrentApplicationUser();
        applicationUser.getAssets().remove(assetRepository.findById(UUID.fromString(id)).orElseThrow());
        applicationUserService.saveApplicationUser(applicationUser);
    }

    public List<AssetDto> getAllAssets() {

        return assetRepository.findAll().stream().map(AssetMapper.INSTANCE::mapEntityToAssetDto).collect(Collectors.toList());
    }
}
