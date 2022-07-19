package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.dto.AssetInquiryRequest;
import com.bht.assetmanagement.persistence.entity.AssetInquiry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssetInquiryMapper {
    AssetInquiryMapper INSTANCE = Mappers.getMapper(AssetInquiryMapper.class);

    AssetInquiry mapRequestToAssetInquiry(AssetInquiryRequest assetInquiryRequest);

    @Mapping(source = "assetName", target = "assetName")
    @Mapping(source = "assetCategory", target = "assetCategory")
    AssetInquiryDto mapEntityToAssetInquiryDto(AssetInquiry assetInquiry);
}
