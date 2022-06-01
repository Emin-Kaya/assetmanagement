package com.bht.assetmanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUserRequest {
    private String employeeId;
    private String firstName;
    private String lastName;
}
