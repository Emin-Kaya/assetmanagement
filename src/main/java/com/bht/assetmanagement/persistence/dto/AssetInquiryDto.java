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
public class AssetInquiryDto {
    private UUID id;
    private String note;
    private Double price;
    private String link;
    private boolean enable;
    private Status status;
    private ApplicationUserDto owner;
    private AddressDto addressDTO;
    private AssetDto assetDto;
}

