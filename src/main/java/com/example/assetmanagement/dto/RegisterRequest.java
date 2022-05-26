package com.example.assetmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username is necessary")
    private String username;

    @Email(message = "Email is incorrect")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Min(value = 8, message = "Password must be at least 8 characters")
    private String password;

    /*@NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;*/
}
