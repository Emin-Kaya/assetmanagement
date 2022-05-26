package com.bht.assetmanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationUserResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
}
