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
    private Double price;
    private String link;
    private AddressRequest addressRequest;
    private AssetRequest assetRequest;
}
