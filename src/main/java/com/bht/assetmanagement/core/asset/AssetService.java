package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.persistence.dto.AssetDto;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;

    public Asset createAsset(AssetDto assetDto) {
        Asset asset = AssetMapper.INSTANCE.mapDtoToAsset(assetDto);
        return assetRepository.save(asset);
    }
}
