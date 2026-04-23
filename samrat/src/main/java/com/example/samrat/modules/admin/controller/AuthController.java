package com.example.samrat.modules.admin.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.admin.dto.LoginRequest;
import com.example.samrat.modules.admin.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "V1 - UserRoute", description = "User authentication and management APIs")
public class AuthController {

    @PostMapping("/register")
    @Operation(
            summary = "POST /api/v1/register",
            description = "Registers a new user account in the system",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    )
            }
    )
    public ResponseEntity<BaseResponse<RegisterRequest>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(new BaseResponse<>(true, "User registered successfully", null, registerRequest));
    }

    @PostMapping("/login")
    @Operation(
            summary = "POST /api/v1/login",
            description = "Authenticates a user and returns a JWT token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            content = @Content(schema = @Schema(implementation = BaseResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Invalid credentials"
                    )
            }
    )
    public ResponseEntity<BaseResponse<Map<String, String>>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Login successful", null, Map.of("token", "dummy-jwt-token")));
    }

    @GetMapping("/users")
    @Operation(
            summary = "GET /api/v1/users",
            description = "Retrieves a list of all registered users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users retrieved successfully",
                            content = @Content(schema = @Schema(implementation = BaseResponse.class))
                    )
            }
    )
    public ResponseEntity<BaseResponse<List<Map<String, Object>>>> getUsers() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Users retrieved", null, List.of()));
    }
}
