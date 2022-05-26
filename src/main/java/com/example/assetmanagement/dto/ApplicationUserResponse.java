package com.example.assetmanagement.dto;

import lombok.*;

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
