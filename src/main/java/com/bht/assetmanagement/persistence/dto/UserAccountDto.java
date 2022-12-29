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
public class UserAccountDto {
    private String id;
    private String email;
    private String username;
    private Role role;
    private Boolean enabled;
    private ApplicationUserDto applicationUser;
}
