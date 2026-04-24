package com.example.samrat.modules.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request body for user login")
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Schema(description = "User username", example = "johndoe")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "password_value")
    private String password;
}
