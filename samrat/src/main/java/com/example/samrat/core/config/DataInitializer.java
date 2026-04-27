package com.example.samrat.core.config;

import com.example.samrat.modules.appointment.entity.Appointment;
import com.example.samrat.modules.appointment.repository.AppointmentRepository;
import com.example.samrat.modules.admin.entity.*;
import com.example.samrat.modules.admin.repository.*;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.entity.Bed;
import com.example.samrat.modules.ipd.entity.Ward;
import com.example.samrat.modules.ipd.repository.AdmissionRepository;
import com.example.samrat.modules.ipd.repository.BedRepository;
import com.example.samrat.modules.ipd.repository.WardRepository;
import com.example.samrat.modules.opd.entity.OPDVisit;
import com.example.samrat.modules.opd.repository.OPDVisitRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final OPDVisitRepository opdVisitRepository;
    private final WardRepository wardRepository;
    private final BedRepository bedRepository;
    private final AdmissionRepository admissionRepository;
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
            "PATIENT_UPDATE", "PATIENT_DELETE",
            "DOCTOR_READ", "DOCTOR_WRITE", "DOCTOR_CREATE", "DOCTOR_UPDATE", "DOCTOR_DELETE",
            "NURSE_READ", "NURSE_WRITE",
            "RECEPTION_READ", "RECEPTION_WRITE",
            "APPOINTMENT_READ", "APPOINTMENT_WRITE", "APPOINTMENT_CREATE", "APPOINTMENT_UPDATE", "APPOINTMENT_DELETE",
            "EMR_READ", "EMR_WRITE",
            "LAB_READ", "LAB_WRITE",
            "PHARMACY_READ", "PHARMACY_WRITE",
            "BILLING_READ", "BILLING_WRITE", "BILLING_DELETE",
            "INVENTORY_READ", "INVENTORY_WRITE", "INVENTORY_DELETE",
            "HR_READ", "HR_WRITE",
            "REPORT_READ", "REPORT_WRITE",
            "TELEMEDICINE_READ", "TELEMEDICINE_WRITE",
            "DEPARTMENT_READ",
            "DIAGNOSTICS_READ", "DIAGNOSTICS_WRITE",
            "IPD_READ", "IPD_WRITE", "IPD_DELETE",
            "OPD_READ", "OPD_WRITE", "OPD_DELETE",
            "CLINICAL_READ", "CLINICAL_WRITE",
            "FINANCE_READ", "FINANCE_WRITE"
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
        ensureRolePermissions("SUPER_ADMIN", allPermissions);
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

        seedOpdAndIpdTestData(hospital, branch);
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

    private void ensureRolePermissions(String roleName, Set<Permission> desiredPermissions) {
        roleRepository.findByName(roleName).ifPresent(role -> {
            if (role.getPermissions() == null) {
                role.setPermissions(new HashSet<>());
            }
            if (!role.getPermissions().containsAll(desiredPermissions) || role.getPermissions().size() != desiredPermissions.size()) {
                role.setPermissions(desiredPermissions);
                roleRepository.save(role);
                System.out.println("Updated Role permissions: " + roleName);
            }
        });
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

    private void seedOpdAndIpdTestData(Hospital hospital, Branch branch) {
        Department medicine = ensureDepartment(hospital, branch, "General Medicine", "MED", "OPD and IPD medicine department", 30);
        Department cardiology = ensureDepartment(hospital, branch, "Cardiology", "CARD", "Cardiology services for OPD and IPD", 20);
        Department emergency = ensureDepartment(hospital, branch, "Emergency", "EMR", "Emergency and triage services", 15);

        Doctor drSharma = ensureDoctor(hospital, branch, "dr.sharma", "dr.sharma@samrathospital.com", "Dr. Rakesh Sharma", "Cardiologist", "REG-CARD-1001", 900.0, cardiology);
        Doctor drIyer = ensureDoctor(hospital, branch, "dr.iyer", "dr.iyer@samrathospital.com", "Dr. Priya Iyer", "General Physician", "REG-MED-1002", 700.0, medicine);
        Doctor drAli = ensureDoctor(hospital, branch, "dr.ali", "dr.ali@samrathospital.com", "Dr. Imran Ali", "Emergency Physician", "REG-EMR-1003", 800.0, emergency);

        Patient patientOne = ensurePatient(hospital, branch, "Amit", "Kumar", "M", LocalDate.of(1990, 5, 10), "9000000001", "UHID-OPD-1001");
        Patient patientTwo = ensurePatient(hospital, branch, "Pooja", "Singh", "F", LocalDate.of(1988, 9, 22), "9000000002", "UHID-IPD-1002");
        Patient patientThree = ensurePatient(hospital, branch, "Rohan", "Verma", "M", LocalDate.of(2001, 2, 14), "9000000003", "UHID-OPD-1003");

        Appointment opdAppointmentOne = ensureAppointment(hospital, branch, patientOne, drIyer, medicine, "MED-1", LocalDate.now(), LocalTime.of(10, 0), "OPD", Appointment.AppointmentStatus.CONFIRMED);
        Appointment opdAppointmentTwo = ensureAppointment(hospital, branch, patientThree, drSharma, cardiology, "CAR-1", LocalDate.now(), LocalTime.of(11, 0), "OPD", Appointment.AppointmentStatus.PENDING);
        ensureAppointment(hospital, branch, patientTwo, drAli, emergency, "EMR-1", LocalDate.now().plusDays(1), LocalTime.of(9, 30), "IPD", Appointment.AppointmentStatus.PENDING);

        ensureOpdVisit(hospital, branch, opdAppointmentOne, patientOne, drIyer, medicine, "MED-1", OPDVisit.VisitStatus.COMPLETED);
        ensureOpdVisit(hospital, branch, opdAppointmentTwo, patientThree, drSharma, cardiology, "CAR-1", OPDVisit.VisitStatus.WAITING);

        Ward generalWard = ensureWard(hospital, branch, "General Ward A", Ward.WardType.GENERAL, medicine, 10);
        Ward icuWard = ensureWard(hospital, branch, "ICU Ward 1", Ward.WardType.ICU, emergency, 5);

        Bed occupiedBed = ensureBed(hospital, branch, generalWard, "GA-01", Bed.BedStatus.OCCUPIED, 2500.0);
        ensureBed(hospital, branch, generalWard, "GA-02", Bed.BedStatus.AVAILABLE, 2500.0);
        ensureBed(hospital, branch, icuWard, "ICU-01", Bed.BedStatus.AVAILABLE, 6500.0);

        ensureAdmission(hospital, branch, patientTwo, drAli, occupiedBed, emergency, "IPD-1001");
    }

    private Department ensureDepartment(Hospital hospital, Branch branch, String name, String code, String description, int totalBeds) {
        return departmentRepository.findByHospitalIdAndBranchIdAndCode(hospital.getId(), branch.getId(), code).orElseGet(() -> {
            Department department = new Department();
            department.setHospitalId(hospital.getId());
            department.setBranchId(branch.getId());
            department.setName(name);
            department.setCode(code);
            department.setDescription(description);
            department.setActive(true);
            department.setTotalBeds(totalBeds);
            department.setAvailableBeds(totalBeds);
            department.setLocation("Block A");
            department.setContactNumber("0123456789");
            department.setEmail(code.toLowerCase() + "@samrathospital.com");
            return departmentRepository.save(department);
        });
    }

    private Doctor ensureDoctor(Hospital hospital, Branch branch, String username, String email, String fullName,
                                String specialization, String registrationNumber, Double consultationFee, Department department) {
        User doctorUser = userRepository.findByUsernameAndActiveTrue(username).orElseGet(() -> {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPhoneNumber("99999" + (10000 + Math.abs(username.hashCode() % 89999)));
            user.setFullName(fullName);
            user.setPassword(passwordEncoder.encode("doctor123"));
            user.setHospitalId(hospital.getId());
            user.setBranchId(branch.getId());
            roleRepository.findByName("DOCTOR").ifPresent(role -> user.getRoles().add(role));
            return userRepository.save(user);
        });

        return doctorRepository.findAll()
                .stream()
                .filter(d -> d.getUser() != null && d.getUser().getId().equals(doctorUser.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Doctor doctor = new Doctor();
                    doctor.setHospitalId(hospital.getId());
                    doctor.setBranchId(branch.getId());
                    doctor.setUser(doctorUser);
                    doctor.setSpecialization(specialization);
                    doctor.setQualification("MBBS, MD");
                    doctor.setExperience("8 years");
                    doctor.setRegistrationNumber(registrationNumber);
                    doctor.setConsultationFee(consultationFee);
                    doctor.setDepartment(department);
                    doctor.setAvailable(true);
                    doctor.getBranches().add(branch);
                    return doctorRepository.save(doctor);
                });
    }

    private Patient ensurePatient(Hospital hospital, Branch branch, String firstName, String lastName, String gender,
                                  LocalDate dob, String phone, String uhid) {
        return patientRepository.findByHospitalIdAndBranchIdAndUhid(hospital.getId(), branch.getId(), uhid).orElseGet(() -> {
            Patient patient = new Patient();
            patient.setHospitalId(hospital.getId());
            patient.setBranchId(branch.getId());
            patient.setFirstName(firstName);
            patient.setLastName(lastName);
            patient.setGender(gender);
            patient.setDateOfBirth(dob);
            patient.setPhoneNumber(phone);
            patient.setUhid(uhid);
            patient.setAddress("City Center");
            patient.setBloodGroup("O+");
            patient.setMaritalStatus("MARRIED");
            patient.setGuardianName("Guardian of " + firstName);
            patient.setGuardianPhone("9000009999");
            patient.setEmergencyContactName("Emergency Contact");
            patient.setEmergencyContactPhone("9000008888");
            patient.setNationality("Indian");
            patient.setActive(true);
            return patientRepository.save(patient);
        });
    }

    private Appointment ensureAppointment(Hospital hospital, Branch branch, Patient patient, Doctor doctor, Department department,
                                          String tokenNumber, LocalDate date, LocalTime time, String visitType,
                                          Appointment.AppointmentStatus status) {
        return appointmentRepository.findAll().stream()
                .filter(a -> a.getHospitalId().equals(hospital.getId()) && a.getBranchId().equals(branch.getId()))
                .filter(a -> a.getPatient() != null && a.getPatient().getId().equals(patient.getId()))
                .filter(a -> a.getDoctor() != null && a.getDoctor().getId().equals(doctor.getId()))
                .filter(a -> a.getAppointmentDate() != null && a.getAppointmentDate().equals(date))
                .findFirst()
                .orElseGet(() -> {
                    Appointment appointment = new Appointment();
                    appointment.setHospitalId(hospital.getId());
                    appointment.setBranchId(branch.getId());
                    appointment.setPatient(patient);
                    appointment.setDoctor(doctor);
                    appointment.setDepartment(department);
                    appointment.setAppointmentDate(date);
                    appointment.setAppointmentTime(time);
                    appointment.setTokenNumber(tokenNumber);
                    appointment.setVisitType(visitType);
                    appointment.setStatus(status);
                    appointment.setPriority(Appointment.AppointmentPriority.NORMAL);
                    appointment.setSource("WALK_IN");
                    appointment.setNotes("Seeded appointment for OPD/IPD flow testing");
                    return appointmentRepository.save(appointment);
                });
    }

    private OPDVisit ensureOpdVisit(Hospital hospital, Branch branch, Appointment appointment, Patient patient, Doctor doctor,
                                    Department department, String token, OPDVisit.VisitStatus status) {
        return opdVisitRepository.findByHospitalIdAndBranchIdAndAppointmentId(hospital.getId(), branch.getId(), appointment.getId())
                .orElseGet(() -> {
                    OPDVisit visit = new OPDVisit();
                    visit.setHospitalId(hospital.getId());
                    visit.setBranchId(branch.getId());
                    visit.setAppointment(appointment);
                    visit.setPatient(patient);
                    visit.setDoctor(doctor);
                    visit.setDepartment(department);
                    visit.setVisitTime(LocalDateTime.now().minusHours(2));
                    visit.setTokenNumber(token);
                    visit.setVisitType("OPD");
                    visit.setSlot("MORNING");
                    visit.setFee(doctor.getConsultationFee());
                    visit.setPaymentMode("CASH");
                    visit.setStatus(status);
                    visit.setWeight(68.5);
                    visit.setHeight(172.0);
                    visit.setBloodPressure("120/80");
                    visit.setTemperature(98.4);
                    visit.setPulseRate(78);
                    visit.setRespiratoryRate(18);
                    visit.setSpo2(99);
                    visit.setSource("WALK_IN");
                    visit.setRemark("Seed data");
                    return opdVisitRepository.save(visit);
                });
    }

    private Ward ensureWard(Hospital hospital, Branch branch, String name, Ward.WardType type, Department department, int capacity) {
        return wardRepository.findByHospitalIdAndBranchId(hospital.getId(), branch.getId())
                .stream()
                .filter(w -> w.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    Ward ward = new Ward();
                    ward.setHospitalId(hospital.getId());
                    ward.setBranchId(branch.getId());
                    ward.setName(name);
                    ward.setType(type);
                    ward.setDepartment(department);
                    ward.setCapacity(capacity);
                    ward.setActive(true);
                    return wardRepository.save(ward);
                });
    }

    private Bed ensureBed(Hospital hospital, Branch branch, Ward ward, String bedNumber, Bed.BedStatus status, Double chargePerDay) {
        return bedRepository.findByWardId(ward.getId())
                .stream()
                .filter(b -> b.getBedNumber().equalsIgnoreCase(bedNumber))
                .findFirst()
                .map(existing -> {
                    existing.setStatus(status);
                    existing.setBedChargePerDay(chargePerDay);
                    return bedRepository.save(existing);
                })
                .orElseGet(() -> {
                    Bed bed = new Bed();
                    bed.setHospitalId(hospital.getId());
                    bed.setBranchId(branch.getId());
                    bed.setWard(ward);
                    bed.setBedNumber(bedNumber);
                    bed.setStatus(status);
                    bed.setBedChargePerDay(chargePerDay);
                    return bedRepository.save(bed);
                });
    }

    private void ensureAdmission(Hospital hospital, Branch branch, Patient patient, Doctor doctor, Bed occupiedBed, Department department, String ipdNumber) {
        boolean exists = admissionRepository.findByHospitalIdAndBranchId(hospital.getId(), branch.getId(), Pageable.unpaged())
                .stream()
                .anyMatch(a -> a.getPatient() != null && a.getPatient().getId().equals(patient.getId())
                        && a.getStatus() == Admission.AdmissionStatus.ADMITTED);
        if (exists) {
            return;
        }

        Admission admission = new Admission();
        admission.setHospitalId(hospital.getId());
        admission.setBranchId(branch.getId());
        admission.setPatient(patient);
        admission.setAdmittingDoctor(doctor);
        admission.setPrimaryDoctor(doctor);
        admission.setDepartment(department);
        admission.setBed(occupiedBed);
        admission.setIpdNumber(ipdNumber);
        admission.setAdmissionDate(LocalDateTime.now().minusHours(6));
        admission.setStatus(Admission.AdmissionStatus.ADMITTED);
        admission.setCaseType("NORMAL");
        admission.setTriage("YELLOW");
        admission.setGuardianName("Suresh Singh");
        admission.setGuardianPhone("9000011111");
        admission.setGuardianRelation("SPOUSE");
        admission.setDiagnosis("Observation for chest pain");
        admission.setAdmissionReason("Requires monitoring and serial diagnostics");
        admission.setAdvanceAmount(15000.0);
        admissionRepository.save(admission);
    }
}
