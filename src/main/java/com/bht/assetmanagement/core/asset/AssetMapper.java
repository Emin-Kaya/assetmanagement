package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.persistence.dto.AssetRequest;
import com.bht.assetmanagement.persistence.dto.AssetResponse;
import com.bht.assetmanagement.persistence.entity.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);

    Asset mapRequestToAsset(AssetRequest assetRequest);
    AssetResponse mapEntityToAssetResponse(Asset asset);
}
