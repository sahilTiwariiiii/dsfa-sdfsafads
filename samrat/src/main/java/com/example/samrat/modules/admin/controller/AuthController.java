package com.example.samrat.modules.admin.controller;

import com.example.samrat.core.config.JwtTokenProvider;
import com.example.samrat.core.context.TenantContext;
import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.admin.dto.LoginRequest;
import com.example.samrat.modules.admin.dto.RegisterRequest;
import com.example.samrat.modules.admin.entity.User;
import com.example.samrat.modules.admin.repository.RoleRepository;
import com.example.samrat.modules.admin.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "V1 - UserRoute", description = "User authentication and management APIs")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
                            description = "Invalid input or missing tenant context",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "RegisterError",
                                            value = "{\"success\":false,\"message\":\"Cannot resolve hospital/branch for this request. Login first or provide tenant headers.\",\"error\":\"TENANT_CONTEXT_MISSING\",\"data\":null}"
                                    )
                            )
                    )
              }
    )
    public ResponseEntity<BaseResponse<User>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(false, "Username already exists", null, null));
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(false, "Email already exists", null, null));
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());
        user.setActive(true);
        user.setHospitalId(resolveHospitalId());
        user.setBranchId(resolveBranchId());

        if (user.getHospitalId() == null || user.getBranchId() == null) {
            return ResponseEntity.badRequest().body(
                    new BaseResponse<>(false,
                            "Cannot resolve hospital/branch for this request. Login first or provide tenant headers.",
                            "TENANT_CONTEXT_MISSING",
                            null)
            );
        }

        // Handle roles
        if (registerRequest.getRoles() != null && !registerRequest.getRoles().isEmpty()) {
            Set<com.example.samrat.modules.admin.entity.Role> roles = registerRequest.getRoles().stream()
                    .map(roleName -> {
                        String name = roleName.startsWith("ROLE_") ? roleName.substring(5) : roleName;
                        return roleRepository.findByName(name)
                                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                    })
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new BaseResponse<>(true, "User registered successfully", null, savedUser));
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
                            description = "Unauthorized - Invalid credentials",
                            content = @Content(
                                    schema = @Schema(implementation = BaseResponse.class),
                                    examples = @ExampleObject(
                                            name = "InvalidCredentials",
                                            value = "{\"success\":false,\"message\":\"Invalid username or password\",\"error\":\"BAD_CREDENTIALS\",\"data\":null}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<BaseResponse<Map<String, String>>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new BaseResponse<>(true, "Login successful", null, Map.of("token", jwt)));
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
    public ResponseEntity<BaseResponse<List<User>>> getUsers() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Users retrieved", null, userRepository.findAll()));
    }

    private Long resolveHospitalId() {
        return resolveCurrentUser()
                .map(User::getHospitalId)
                .orElseGet(TenantContext::getHospitalId);
    }

    private Long resolveBranchId() {
        return resolveCurrentUser()
                .map(User::getBranchId)
                .orElseGet(TenantContext::getBranchId);
    }

    private java.util.Optional<User> resolveCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return java.util.Optional.empty();
        }
        String username = authentication.getName();
        return userRepository.findByUsernameAndActiveTrue(username);
    }
}
