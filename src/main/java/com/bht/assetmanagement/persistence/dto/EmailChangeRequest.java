package com.bht.assetmanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailChangeRequest {
    private String oldEmail;
    private String newEmail;
}
