package com.bht.assetmanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetInquiryDto {
    private String note;
    private Double price;
    private String link;
    private AddressDto addressDto;
    private AssetDto assetDto;
}
