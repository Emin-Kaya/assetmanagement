package com.bht.assetmanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String authenticationToken; //jwtToken
    private String username; //TODO delete
    private String refreshToken;
    private LocalDateTime expiresAt;
}
