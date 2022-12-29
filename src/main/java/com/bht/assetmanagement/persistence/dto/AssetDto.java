package com.bht.assetmanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetDto {
    private String id;
    private String serialnumber;
    private String name;
    private String memory;
    private String notes;
    private boolean enable;
    private String category;
}
