package com.bht.assetmanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private String id;
    private String streetName;
    private String streetNumber;
    private String postalCode;
    private String city;
    private String country;
}
