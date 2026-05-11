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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Method Summary", description = "Retrieves a paginated list of all user accounts")
    public ResponseEntity<BaseResponse<Page<User>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Users found", null, userRepository.findAll(pageable)));
    }

    @GetMapping("/users/search")
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @Operation(summary = "Search users", description = "Filters users by username, full name, email, and active status")
    public ResponseEntity<BaseResponse<Page<User>>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new BaseResponse<>(true, "Search results found", null, userRepository.searchUsers(username, fullName, email, active, pageable)));
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves all security roles defined in the system")
    public ResponseEntity<BaseResponse<List<Role>>> getAllRoles() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Roles found", null, roleRepository.findAll()));
    }

    @GetMapping("/branches")
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves all branches associated with the hospital")
    public ResponseEntity<BaseResponse<List<Branch>>> getAllBranches() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Branches found", null, branchRepository.findAll()));
    }
}

