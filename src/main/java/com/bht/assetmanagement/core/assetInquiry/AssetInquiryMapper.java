package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.entity.AssetInquiry;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssetInquiryMapper {
    AssetInquiryMapper INSTANCE = Mappers.getMapper(AssetInquiryMapper.class);

    AssetInquiry mapDtoToAssetInquiry(AssetInquiryDto assetInquiryDto);
}
