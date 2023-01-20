package com.bht.assetmanagement.persistence.dto;

import com.bht.assetmanagement.persistence.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetInquiryDto {
    private String id;
    private String note;
    private String link;
    private boolean enable;
    private Status status;
    private ApplicationUserDto owner;
    private AddressDto address;
    private String assetName;
    private String assetCategory;
    private String memory;
}

