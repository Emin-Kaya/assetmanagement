package com.bht.assetmanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetInquiryResponse {
    private String id;
    private List<AssetDto> assetDtos;
    private List<StorageDto> storageDto;
}

