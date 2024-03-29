package com.bht.assetmanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetRequest {
    private String serialnumber;
    private String name;
    private String category;
    private String notes ;
    private String memory ;
    private String storageId = null;
}
