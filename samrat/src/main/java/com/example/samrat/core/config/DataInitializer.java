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
        createInitialData();
    }

    private void createInitialData() {
        // 1. Create Default Hospital if not exists
        Hospital hospital = hospitalRepository.findAll().stream().findFirst().orElseGet(() -> {
            Hospital h = new Hospital();
            h.setName("Samrat General Hospital");
            h.setCode("SGH001");
            h.setAddress("Main Road, City Center");
            h.setPhone("0123456789");
            h.setEmail("admin@samrathospital.com");
            return hospitalRepository.save(h);
        });

        // 2. Create Default Branch if not exists
        Branch branch = branchRepository.findAll().stream().findFirst().orElseGet(() -> {
            Branch b = new Branch();
            b.setName("Main Branch");
            b.setCode("SGH-MAIN");
            b.setHospital(hospital);
            b.setAddress("Main Road, City Center");
            b.setPhone("0123456789");
            return branchRepository.save(b);
        });

        // 3. Create Permissions if they don't exist
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
            if (permissionRepository.findByName(pName).isEmpty()) {
                Permission p = new Permission();
                p.setName(pName);
                p.setDescription("Access for " + pName);
                permissionRepository.save(p);
            }
        }

        Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());

        // 4. Create Roles if they don't exist
        ensureRoleExists("SUPER_ADMIN", allPermissions);
        ensureRoleExists("DOCTOR", getPermissionsByNames(allPermissions, "DOCTOR_READ", "DOCTOR_WRITE", "PATIENT_READ", "EMR_READ", "EMR_WRITE", "APPOINTMENT_READ", "LAB_READ", "TELEMEDICINE_READ", "TELEMEDICINE_WRITE"));
        ensureRoleExists("NURSE", getPermissionsByNames(allPermissions, "NURSE_READ", "NURSE_WRITE", "PATIENT_READ", "EMR_READ", "EMR_WRITE", "LAB_READ"));
        ensureRoleExists("RECEPTIONIST", getPermissionsByNames(allPermissions, "RECEPTION_READ", "RECEPTION_WRITE", "PATIENT_READ", "PATIENT_CREATE", "APPOINTMENT_READ", "APPOINTMENT_WRITE", "BILLING_READ"));
        ensureRoleExists("PHARMACIST", getPermissionsByNames(allPermissions, "PHARMACY_READ", "PHARMACY_WRITE", "PATIENT_READ", "BILLING_READ"));
        ensureRoleExists("LAB_TECHNICIAN", getPermissionsByNames(allPermissions, "LAB_READ", "LAB_WRITE", "PATIENT_READ"));

        // 5. Create Super Admin User if no users exist
        if (userRepository.count() == 0) {
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
            System.out.println(" Initialized with full RBAC!");
            System.out.println("Super Admin: superadmin / admin123");
            System.out.println("----------------------------------------------------------");
        }
    }

    private void ensureRoleExists(String name, Set<Permission> permissions) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setPermissions(permissions);
            roleRepository.save(role);
            System.out.println("Created Role: " + name);
        }
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
