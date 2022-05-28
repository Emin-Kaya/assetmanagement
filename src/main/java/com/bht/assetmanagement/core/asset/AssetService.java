package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.persistence.dto.AssetDto;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    //private final AssetInquiryService assetInquiryService;
    private final ApplicationUserService applicationUserService;

    public Asset createAsset(AssetDto assetDto) {
        Asset asset = AssetMapper.INSTANCE.mapDtoToAsset(assetDto);
        ApplicationUser applicationUser = applicationUserService.getCurrentApplicationUser();

        return assetRepository.save(asset);
    }

   /* public List<Asset> getAllAssetsOfUser() {
        List<Asset> assetList = new ArrayList<>();
        assetInquiryService.findAssetInquriysByOwner().forEach(it -> assetList.add(it.getAsset()));
        return assetList;
    }*/



    /*public List<Asset> getAllAssets() {
        return assetRepository.findAll(); todo adminAssetService
    }*/

}
