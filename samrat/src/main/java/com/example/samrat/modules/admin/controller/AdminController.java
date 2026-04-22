package com.example.samrat.modules.admin.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.admin.entity.Branch;
import com.example.samrat.modules.admin.entity.Role;
import com.example.samrat.modules.admin.entity.User;
import com.example.samrat.modules.admin.repository.BranchRepository;
import com.example.samrat.modules.admin.repository.RoleRepository;
import com.example.samrat.modules.admin.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "APIs for hospital administrative tasks, users, and roles")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @Operation(summary = "Get all users", description = "Retrieves all user accounts in the hospital system")
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Users found", null, userRepository.findAll()));
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @Operation(summary = "Get all roles", description = "Retrieves all security roles defined in the system")
    public ResponseEntity<BaseResponse<List<Role>>> getAllRoles() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Roles found", null, roleRepository.findAll()));
    }

    @GetMapping("/branches")
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @Operation(summary = "Get all branches", description = "Retrieves all branches associated with the hospital")
    public ResponseEntity<BaseResponse<List<Branch>>> getAllBranches() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Branches found", null, branchRepository.findAll()));
    }
}
