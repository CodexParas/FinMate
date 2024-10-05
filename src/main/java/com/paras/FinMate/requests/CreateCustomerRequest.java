package com.paras.FinMate.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateCustomerRequest {
    @NotEmpty(message = "First Name is required")
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotEmpty(message = "Last Name is required")
    @NotBlank(message = "Last Name is required")
    private String lastName;
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    @NotEmpty(message = "Phone Number is required")
    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;
}
