package com.bht.assetmanagement.persistence.dto;

import com.bht.assetmanagement.persistence.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetInquiryResponse {
    private UUID assetInquiryId;
    private String note;
    private Double price;
    private String link;
    private boolean enable;
    private Status status;
    private ApplicationUserResponse owner;
    private AddressResponse addressResponse;
    private AssetResponse assetResponse;
}

