package com.bht.assetmanagement.persistence.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Username is necessary")
    private String username;

    @Email(message = "Email is incorrect")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Min(value = 8, message = "Password must be at least 8 characters")
    private String password;

    private ApplicationUserRequest applicationUserRequest;
}
