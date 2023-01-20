package com.bht.assetmanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetInquiryRequest {
    private String note;
    private String link;
    private AddressRequest addressRequest;
    private String assetName;
    private String assetCategory;
    private String memory;
}
