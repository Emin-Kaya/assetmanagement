package com.bht.assetmanagement.persistence.dto;

import com.bht.assetmanagement.persistence.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountRequest {
    String email;
    String username;
    String password;
    Role role;
    @Builder.Default
    Boolean enabled = true;
    ApplicationUserRequest applicationUserRequest;
}
