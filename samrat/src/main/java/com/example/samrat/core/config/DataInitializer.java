package com.example.samrat.core.config;

import com.example.samrat.modules.admin.entity.*;
import com.example.samrat.modules.admin.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final HospitalRepository hospitalRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            createInitialData();
        }
    }

    private void createInitialData() {
        // 1. Create Default Hospital
        Hospital hospital = new Hospital();
        hospital.setName("Samrat General Hospital");
        hospital.setCode("SGH001");
        hospital.setAddress("Main Road, City Center");
        hospital.setPhone("0123456789");
        hospital.setEmail("admin@samrathospital.com");
        hospital = hospitalRepository.save(hospital);

        // 2. Create Default Branch
        Branch branch = new Branch();
        branch.setName("Main Branch");
        branch.setCode("SGH-MAIN");
        branch.setHospital(hospital);
        branch.setAddress("Main Road, City Center");
        branch.setPhone("0123456789");
        branch = branchRepository.save(branch);

        // 3. Create Permissions
        Set<Permission> allPermissions = new HashSet<>();
        String[] permissionNames = {
            "SUPER_ADMIN", "ADMIN_READ", "ADMIN_WRITE",
            "PATIENT_READ", "PATIENT_WRITE", "PATIENT_CREATE",
            "DOCTOR_READ", "DOCTOR_WRITE",
            "NURSE_READ", "NURSE_WRITE",
            "RECEPTION_READ", "RECEPTION_WRITE",
            "APPOINTMENT_READ", "APPOINTMENT_WRITE",
            "EMR_READ", "EMR_WRITE",
            "LAB_READ", "LAB_WRITE",
            "PHARMACY_READ", "PHARMACY_WRITE",
            "BILLING_READ", "BILLING_WRITE",
            "INVENTORY_READ", "INVENTORY_WRITE",
            "HR_READ", "HR_WRITE",
            "REPORT_READ", "TELEMEDICINE_READ", "TELEMEDICINE_WRITE"
        };

        for (String pName : permissionNames) {
            Permission p = new Permission();
            p.setName(pName);
            p.setDescription("Access for " + pName);
            allPermissions.add(permissionRepository.save(p));
        }

        // 4. Create Roles
        createRole("SUPER_ADMIN", allPermissions);
        createRole("DOCTOR", getPermissionsByNames(allPermissions, "DOCTOR_READ", "DOCTOR_WRITE", "PATIENT_READ", "EMR_READ", "EMR_WRITE", "APPOINTMENT_READ", "LAB_READ", "TELEMEDICINE_READ", "TELEMEDICINE_WRITE"));
        createRole("NURSE", getPermissionsByNames(allPermissions, "NURSE_READ", "NURSE_WRITE", "PATIENT_READ", "EMR_READ", "EMR_WRITE", "LAB_READ"));
        createRole("RECEPTIONIST", getPermissionsByNames(allPermissions, "RECEPTION_READ", "RECEPTION_WRITE", "PATIENT_READ", "PATIENT_CREATE", "APPOINTMENT_READ", "APPOINTMENT_WRITE", "BILLING_READ"));
        createRole("PHARMACIST", getPermissionsByNames(allPermissions, "PHARMACY_READ", "PHARMACY_WRITE", "PATIENT_READ", "BILLING_READ"));
        createRole("LAB_TECHNICIAN", getPermissionsByNames(allPermissions, "LAB_READ", "LAB_WRITE", "PATIENT_READ"));

        // 5. Create Super Admin User
        Role superAdminRole = roleRepository.findByName("SUPER_ADMIN").orElseThrow();
        User superAdmin = new User();
        superAdmin.setUsername("superadmin");
        superAdmin.setPassword(passwordEncoder.encode("admin123")); // Default password
        superAdmin.setEmail("superadmin@samrathospital.com");
        superAdmin.setFullName("System Super Administrator");
        superAdmin.setHospitalId(hospital.getId());
        superAdmin.setBranchId(branch.getId());
        superAdmin.getRoles().add(superAdminRole);
        
        userRepository.save(superAdmin);
        
        System.out.println("----------------------------------------------------------");
        System.out.println("HMS Initialized with full RBAC!");
        System.out.println("Super Admin: superadmin / admin123");
        System.out.println("----------------------------------------------------------");
    }

    private void createRole(String name, Set<Permission> permissions) {
        Role role = new Role();
        role.setName(name);
        role.setPermissions(permissions);
        roleRepository.save(role);
    }

    private Set<Permission> getPermissionsByNames(Set<Permission> all, String... names) {
        Set<Permission> filtered = new HashSet<>();
        Set<String> nameSet = Set.of(names);
        for (Permission p : all) {
            if (nameSet.contains(p.getName())) {
                filtered.add(p);
            }
        }
        return filtered;
    }
}
