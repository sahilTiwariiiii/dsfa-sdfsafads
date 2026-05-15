package com.example.samrat.core.config;

import com.example.samrat.modules.admin.entity.*;
import com.example.samrat.modules.admin.repository.*;
import com.example.samrat.modules.appointment.entity.Appointment;
import com.example.samrat.modules.appointment.entity.DoctorSchedule;
import com.example.samrat.modules.appointment.repository.AppointmentRepository;
import com.example.samrat.modules.appointment.repository.DoctorScheduleRepository;
import com.example.samrat.modules.asset.entity.*;
import com.example.samrat.modules.asset.repository.*;
import com.example.samrat.modules.billing.entity.Invoice;
import com.example.samrat.modules.billing.entity.InvoiceItem;
import com.example.samrat.modules.billing.entity.ServiceCharge;
import com.example.samrat.modules.billing.repository.InvoiceRepository;
import com.example.samrat.modules.billing.repository.ServiceChargeRepository;
import com.example.samrat.modules.clinical.discharge.entity.DischargeSummary;
import com.example.samrat.modules.clinical.discharge.repository.DischargeSummaryRepository;
import com.example.samrat.modules.clinical.emergency.entity.ERVisit;
import com.example.samrat.modules.clinical.emergency.repository.ERVisitRepository;
import com.example.samrat.modules.clinical.emr.entity.*;
import com.example.samrat.modules.clinical.emr.repository.*;
import com.example.samrat.modules.clinical.nursing.entity.NursingNote;
import com.example.samrat.modules.clinical.nursing.repository.NursingNoteRepository;
import com.example.samrat.modules.clinical.ot.entity.OTBooking;
import com.example.samrat.modules.clinical.ot.repository.OTBookingRepository;
import com.example.samrat.modules.clinical.vitals.entity.PatientVital;
import com.example.samrat.modules.clinical.vitals.repository.PatientVitalRepository;
import com.example.samrat.modules.compliance.entity.AuditLog;
import com.example.samrat.modules.compliance.repository.AuditLogRepository;
import com.example.samrat.modules.diagnostics.bloodbank.entity.BloodDonation;
import com.example.samrat.modules.diagnostics.bloodbank.repository.BloodDonationRepository;
import com.example.samrat.modules.diagnostics.lab.entity.LabOrder;
import com.example.samrat.modules.diagnostics.lab.entity.LabResult;
import com.example.samrat.modules.diagnostics.lab.repository.LabOrderRepository;
import com.example.samrat.modules.diagnostics.lab.repository.LabResultRepository;
import com.example.samrat.modules.diagnostics.radiology.entity.RadiologyOrder;
import com.example.samrat.modules.diagnostics.radiology.repository.RadiologyOrderRepository;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.finance.accounts.entity.FinancialTransaction;
import com.example.samrat.modules.finance.accounts.repository.FinancialTransactionRepository;
import com.example.samrat.modules.finance.insurance.entity.TPACompany;
import com.example.samrat.modules.finance.insurance.repository.TPACompanyRepository;
import com.example.samrat.modules.inventory.assets.entity.HospitalAsset;
import com.example.samrat.modules.inventory.assets.repository.HospitalAssetRepository;
import com.example.samrat.modules.inventory.cssd.entity.CSSDCycle;
import com.example.samrat.modules.inventory.cssd.repository.CSSDCycleRepository;
import com.example.samrat.modules.inventory.pharmacy.entity.PharmacyStock;
import com.example.samrat.modules.inventory.pharmacy.repository.PharmacyStockRepository;
import com.example.samrat.modules.inventory.purchase.entity.PurchaseOrder;
import com.example.samrat.modules.inventory.purchase.entity.PurchaseOrderItem;
import com.example.samrat.modules.inventory.purchase.repository.PurchaseOrderRepository;
import com.example.samrat.modules.inventory.purchase.repository.PurchaseOrderItemRepository;
import com.example.samrat.modules.inventory.store.entity.StoreItem;
import com.example.samrat.modules.inventory.store.repository.StoreItemRepository;
import com.example.samrat.modules.ipd.entity.Admission;
import com.example.samrat.modules.ipd.entity.Bed;
import com.example.samrat.modules.ipd.entity.Ward;
import com.example.samrat.modules.ipd.repository.AdmissionRepository;
import com.example.samrat.modules.ipd.repository.BedRepository;
import com.example.samrat.modules.ipd.repository.WardRepository;
import com.example.samrat.modules.nurse.entity.Nurse;
import com.example.samrat.modules.nurse.repository.NurseRepository;
import com.example.samrat.modules.opd.entity.OPDVisit;
import com.example.samrat.modules.opd.repository.OPDVisitRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import com.example.samrat.modules.reception.entity.VisitorLog;
import com.example.samrat.modules.reception.repository.VisitorLogRepository;
import com.example.samrat.modules.reporting.entity.SystemReport;
import com.example.samrat.modules.reporting.repository.SystemReportRepository;
import com.example.samrat.modules.support.ambulance.entity.Ambulance;
import com.example.samrat.modules.support.ambulance.repository.AmbulanceRepository;
import com.example.samrat.modules.support.diet.entity.DietPlan;
import com.example.samrat.modules.support.diet.repository.DietPlanRepository;
import com.example.samrat.modules.support.hr.entity.Employee;
import com.example.samrat.modules.support.hr.repository.EmployeeRepository;
import com.example.samrat.modules.telemedicine.entity.TelemedicineConsultation;
import com.example.samrat.modules.telemedicine.repository.TelemedicineConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private final NurseRepository nurseRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final AssetSubCategoryRepository assetSubCategoryRepository;
    private final AssetVendorRepository assetVendorRepository;
    private final AssetLocationRepository assetLocationRepository;
    private final AssetRepository assetRepository;
    private final AssetAssignmentRepository assetAssignmentRepository;
    private final AssetMaintenanceRepository assetMaintenanceRepository;
    private final ServiceChargeRepository serviceChargeRepository;
    private final InvoiceRepository invoiceRepository;
    private final EMRRepository emrRepository;
    private final DoctorNoteRepository doctorNoteRepository;
    private final ClinicalDiagnosisRepository clinicalDiagnosisRepository;
    private final ClinicalPrescriptionRepository clinicalPrescriptionRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PersonalHistoryRepository personalHistoryRepository;
    private final SurgicalHistoryRepository surgicalHistoryRepository;
    private final NursingNoteRepository nursingNoteRepository;
    private final DischargeSummaryRepository dischargeSummaryRepository;
    private final ERVisitRepository erVisitRepository;
    private final OTBookingRepository otBookingRepository;
    private final PatientVitalRepository patientVitalRepository;
    private final AuditLogRepository auditLogRepository;
    private final LabOrderRepository labOrderRepository;
    private final RadiologyOrderRepository radiologyOrderRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final PharmacyStockRepository pharmacyStockRepository;
    private final VisitorLogRepository visitorLogRepository;
    private final SystemReportRepository systemReportRepository;
    private final EmployeeRepository employeeRepository;
    private final PublicPageRepository publicPageRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final BloodDonationRepository bloodDonationRepository;
    private final LabResultRepository labResultRepository;
    private final TPACompanyRepository tpaCompanyRepository;
    private final HospitalAssetRepository hospitalAssetRepository;
    private final CSSDCycleRepository cssdCycleRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final StoreItemRepository storeItemRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final DietPlanRepository dietPlanRepository;
    private final TelemedicineConsultationRepository telemedicineConsultationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        createInitialData();
    }

    private void createInitialData() {
        Hospital hospital = hospitalRepository.findAll().stream().findFirst().orElseGet(() -> {
            Hospital h = new Hospital();
            h.setName("Samrat General Hospital");
            h.setCode("SGH001");
            h.setAddress("Main Road, City Center");
            h.setPhone("0123456789");
            h.setEmail("admin@samrathospital.com");
            return hospitalRepository.save(h);
        });

        Branch branch = branchRepository.findAll().stream().findFirst().orElseGet(() -> {
            Branch b = new Branch();
            b.setName("Main Branch");
            b.setCode("SGH-MAIN");
            b.setHospital(hospital);
            b.setAddress("Main Road, City Center");
            b.setPhone("0123456789");
            return branchRepository.save(b);
        });

        ensurePermissionsAndRoles();

        User superAdmin = ensureUser(hospital, branch, "superadmin", "superadmin@samrathospital.com",
                "System Super Administrator", "admin123", "SUPER_ADMIN");
        ensureUser(hospital, branch, "reception.demo", "reception.demo@samrathospital.com",
                "Neha Frontdesk", "demo123", "RECEPTIONIST");
        ensureUser(hospital, branch, "nurse.demo", "nurse.demo@samrathospital.com",
                "Anita Nursing", "demo123", "NURSE");
        ensureUser(hospital, branch, "lab.demo", "lab.demo@samrathospital.com",
                "Rahul Labtech", "demo123", "LAB_TECHNICIAN");
        ensureUser(hospital, branch, "pharmacy.demo", "pharmacy.demo@samrathospital.com",
                "Kavita Pharmacist", "demo123", "PHARMACIST");

        Department medicine = ensureDepartment(hospital, branch, "General Medicine", "MED", "OPD and IPD medicine department", 30);
        Department cardiology = ensureDepartment(hospital, branch, "Cardiology", "CARD", "Cardiology services for OPD and IPD", 20);
        Department emergency = ensureDepartment(hospital, branch, "Emergency", "EMR", "Emergency and triage services", 15);
        Department laboratory = ensureDepartment(hospital, branch, "Laboratory", "LAB", "Clinical diagnostics and pathology", 5);
        Department radiology = ensureDepartment(hospital, branch, "Radiology", "RAD", "Diagnostic imaging services", 5);
        Department billing = ensureDepartment(hospital, branch, "Billing", "BILL", "Billing and finance desk", 2);

        Doctor drSharma = ensureDoctor(hospital, branch, "dr.sharma", "dr.sharma@samrathospital.com", "Dr. Rakesh Sharma",
                "Cardiologist", "REG-CARD-1001", 900.0, cardiology);
        Doctor drIyer = ensureDoctor(hospital, branch, "dr.iyer", "dr.iyer@samrathospital.com", "Dr. Priya Iyer",
                "General Physician", "REG-MED-1002", 700.0, medicine);
        Doctor drAli = ensureDoctor(hospital, branch, "dr.ali", "dr.ali@samrathospital.com", "Dr. Imran Ali",
                "Emergency Physician", "REG-EMR-1003", 800.0, emergency);

        User nurseUser = ensureUser(hospital, branch, "nurse.demo", "nurse.demo@samrathospital.com",
                "Anita Nursing", "demo123", "NURSE");
        User receptionistUser = ensureUser(hospital, branch, "reception.demo", "reception.demo@samrathospital.com",
                "Neha Frontdesk", "demo123", "RECEPTIONIST");
        User labTechUser = ensureUser(hospital, branch, "lab.demo", "lab.demo@samrathospital.com",
                "Rahul Labtech", "demo123", "LAB_TECHNICIAN");
        User pharmacyUser = ensureUser(hospital, branch, "pharmacy.demo", "pharmacy.demo@samrathospital.com",
                "Kavita Pharmacist", "demo123", "PHARMACIST");

        ensureNurse(hospital, branch, nurseUser, medicine, "GNM, B.Sc Nursing", "NURSE-001", "6 years", "Morning");
        ensureEmployee(hospital, branch, receptionistUser, "EMP-REC-001", "Receptionist", "Reception", 28000.0, 4500.0, 1200.0);
        ensureEmployee(hospital, branch, labTechUser, "EMP-LAB-001", "Lab Technician", "Laboratory", 32000.0, 5000.0, 1500.0);
        ensureEmployee(hospital, branch, pharmacyUser, "EMP-PHA-001", "Pharmacist", "Pharmacy", 35000.0, 5500.0, 1800.0);
        ensureEmployee(hospital, branch, superAdmin, "EMP-ADM-001", "Administrator", "Admin", 65000.0, 12000.0, 5000.0);

        Patient patientOne = ensurePatient(hospital, branch, "Amit", "Kumar", "M", LocalDate.of(1990, 5, 10), "9000000001", "UHID-OPD-1001");
        Patient patientTwo = ensurePatient(hospital, branch, "Pooja", "Singh", "F", LocalDate.of(1988, 9, 22), "9000000002", "UHID-IPD-1002");
        Patient patientThree = ensurePatient(hospital, branch, "Rohan", "Verma", "M", LocalDate.of(2001, 2, 14), "9000000003", "UHID-OPD-1003");
        Patient patientFour = ensurePatient(hospital, branch, "Sunita", "Mishra", "F", LocalDate.of(1976, 11, 3), "9000000004", "UHID-ER-1004");

        Appointment opdAppointmentOne = ensureAppointment(hospital, branch, patientOne, drIyer, medicine,
                "MED-1", LocalDate.now(), LocalTime.of(10, 0), "OPD", Appointment.AppointmentStatus.CONFIRMED);
        Appointment opdAppointmentTwo = ensureAppointment(hospital, branch, patientThree, drSharma, cardiology,
                "CAR-1", LocalDate.now(), LocalTime.of(11, 0), "OPD", Appointment.AppointmentStatus.PENDING);
        ensureAppointment(hospital, branch, patientTwo, drAli, emergency,
                "EMR-1", LocalDate.now().plusDays(1), LocalTime.of(9, 30), "IPD", Appointment.AppointmentStatus.PENDING);
        ensureAppointment(hospital, branch, patientFour, drAli, emergency,
                "EMR-2", LocalDate.now(), LocalTime.of(8, 45), "EMERGENCY", Appointment.AppointmentStatus.CONFIRMED);

        OPDVisit completedVisit = ensureOpdVisit(hospital, branch, opdAppointmentOne, patientOne, drIyer, medicine, "MED-1", OPDVisit.VisitStatus.COMPLETED);
        OPDVisit waitingVisit = ensureOpdVisit(hospital, branch, opdAppointmentTwo, patientThree, drSharma, cardiology, "CAR-1", OPDVisit.VisitStatus.WAITING);

        Ward generalWard = ensureWard(hospital, branch, "General Ward A", Ward.WardType.GENERAL, medicine, 10);
        Ward icuWard = ensureWard(hospital, branch, "ICU Ward 1", Ward.WardType.ICU, emergency, 5);

        Bed occupiedBed = ensureBed(hospital, branch, generalWard, "GA-01", Bed.BedStatus.OCCUPIED, 2500.0);
        ensureBed(hospital, branch, generalWard, "GA-02", Bed.BedStatus.AVAILABLE, 2500.0);
        ensureBed(hospital, branch, icuWard, "ICU-01", Bed.BedStatus.AVAILABLE, 6500.0);

        Admission activeAdmission = ensureAdmission(hospital, branch, patientTwo, drAli, occupiedBed, emergency, "IPD-1001");

        seedAssets(hospital, branch, medicine, radiology, laboratory, superAdmin);
        seedBilling(hospital, branch, patientOne, patientTwo, activeAdmission, drIyer, completedVisit);
        seedClinical(hospital, branch, patientOne, patientTwo, patientFour, drIyer, drAli, medicine, emergency, completedVisit, activeAdmission);
        seedDiagnostics(hospital, branch, patientOne, patientTwo, drIyer, drAli, laboratory, radiology);
        seedFinance(hospital, branch);
        seedInventory(hospital, branch);
        seedReception(hospital, branch, receptionistUser, patientOne, patientTwo);
        seedReporting(hospital, branch, superAdmin);
        seedCompliance(hospital, branch);
        seedPublicPages(hospital, branch);
        seedTelemedicine(hospital, branch, patientOne, drIyer);
        seedDiagnosticsExtra(hospital, branch, patientOne, patientTwo, drIyer, drAli, laboratory);
        seedInventoryExtra(hospital, branch, medicine, cardiology, superAdmin);
        seedFinanceExtra(hospital, branch);
        seedSupportExtra(hospital, branch, activeAdmission);
        seedAppointmentExtra(hospital, branch, drIyer, drSharma);

        System.out.println("----------------------------------------------------------");
        System.out.println("Dummy data ready for modules with persistence.");
        System.out.println("superadmin / admin123");
        System.out.println("reception.demo / demo123");
        System.out.println("nurse.demo / demo123");
        System.out.println("lab.demo / demo123");
        System.out.println("pharmacy.demo / demo123");
        System.out.println("----------------------------------------------------------");
    }

    private void ensurePermissionsAndRoles() {
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
                Permission permission = new Permission();
                permission.setName(pName);
                permission.setDescription("Access for " + pName);
                permissionRepository.save(permission);
            }
        }

        Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());
        ensureRoleExists("SUPER_ADMIN", allPermissions);
        ensureRolePermissions("SUPER_ADMIN", allPermissions);
        ensureRoleExists("DOCTOR", getPermissionsByNames(allPermissions,
                "DOCTOR_READ", "DOCTOR_WRITE", "PATIENT_READ", "EMR_READ", "EMR_WRITE", "APPOINTMENT_READ",
                "LAB_READ", "TELEMEDICINE_READ", "TELEMEDICINE_WRITE", "OPD_READ", "CLINICAL_READ"));
        ensureRoleExists("NURSE", getPermissionsByNames(allPermissions,
                "NURSE_READ", "NURSE_WRITE", "PATIENT_READ", "EMR_READ", "EMR_WRITE", "LAB_READ", "CLINICAL_READ"));
        ensureRoleExists("RECEPTIONIST", getPermissionsByNames(allPermissions,
                "RECEPTION_READ", "RECEPTION_WRITE", "PATIENT_READ", "PATIENT_CREATE", "APPOINTMENT_READ",
                "APPOINTMENT_WRITE", "BILLING_READ", "OPD_READ"));
        ensureRoleExists("PHARMACIST", getPermissionsByNames(allPermissions,
                "PHARMACY_READ", "PHARMACY_WRITE", "PATIENT_READ", "BILLING_READ", "INVENTORY_READ"));
        ensureRoleExists("LAB_TECHNICIAN", getPermissionsByNames(allPermissions,
                "LAB_READ", "LAB_WRITE", "PATIENT_READ", "DIAGNOSTICS_READ", "DIAGNOSTICS_WRITE"));
    }

    private void ensureRoleExists(String name, Set<Permission> permissions) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setPermissions(permissions);
            roleRepository.save(role);
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
            }
        });
    }

    private Set<Permission> getPermissionsByNames(Set<Permission> all, String... names) {
        Set<Permission> filtered = new HashSet<>();
        Set<String> nameSet = Set.of(names);
        for (Permission permission : all) {
            if (nameSet.contains(permission.getName())) {
                filtered.add(permission);
            }
        }
        return filtered;
    }

    private User ensureUser(Hospital hospital, Branch branch, String username, String email, String fullName,
                            String password, String... roleNames) {
        User user = userRepository.findByUsernameAndActiveTrue(username).orElseGet(() -> {
            User created = new User();
            created.setUsername(username);
            created.setEmail(email);
            created.setPhoneNumber("9" + String.format("%09d", Math.abs(username.hashCode()) % 1_000_000_000));
            created.setFullName(fullName);
            created.setPassword(passwordEncoder.encode(password));
            created.setHospitalId(hospital.getId());
            created.setBranchId(branch.getId());
            created.setActive(true);
            return userRepository.save(created);
        });

        user.setEmail(email);
        user.setFullName(fullName);
        user.setHospitalId(hospital.getId());
        user.setBranchId(branch.getId());
        user.setActive(true);
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            user.setPhoneNumber("9" + String.format("%09d", Math.abs(username.hashCode()) % 1_000_000_000));
        }
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        for (String roleName : roleNames) {
            roleRepository.findByName(roleName).ifPresent(user.getRoles()::add);
        }
        return userRepository.save(user);
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
        User doctorUser = ensureUser(hospital, branch, username, email, fullName, "doctor123", "DOCTOR");

        return doctorRepository.findAll().stream()
                .filter(d -> d.getUser() != null && d.getUser().getId().equals(doctorUser.getId()))
                .findFirst()
                .map(existing -> {
                    existing.setHospitalId(hospital.getId());
                    existing.setBranchId(branch.getId());
                    existing.setDepartment(department);
                    existing.setSpecialization(specialization);
                    existing.setRegistrationNumber(registrationNumber);
                    existing.setConsultationFee(consultationFee);
                    existing.setAvailable(true);
                    if (existing.getBranches() == null) {
                        existing.setBranches(new HashSet<>());
                    }
                    existing.getBranches().add(branch);
                    return doctorRepository.save(existing);
                })
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

    private Nurse ensureNurse(Hospital hospital, Branch branch, User user, Department department, String qualification,
                              String registrationNumber, String experience, String shift) {
        return nurseRepository.findByUserId(user.getId())
                .map(existing -> {
                    existing.setHospitalId(hospital.getId());
                    existing.setBranchId(branch.getId());
                    existing.setDepartmentId(department.getId());
                    existing.setQualification(qualification);
                    existing.setRegistrationNumber(registrationNumber);
                    existing.setExperience(experience);
                    existing.setShift(shift);
                    existing.setAvailable(true);
                    if (existing.getBranches() == null) {
                        existing.setBranches(new HashSet<>());
                    }
                    existing.getBranches().add(branch);
                    return nurseRepository.save(existing);
                })
                .orElseGet(() -> {
                    Nurse nurse = new Nurse();
                    nurse.setHospitalId(hospital.getId());
                    nurse.setBranchId(branch.getId());
                    nurse.setUser(user);
                    nurse.setQualification(qualification);
                    nurse.setRegistrationNumber(registrationNumber);
                    nurse.setDepartmentId(department.getId());
                    nurse.setExperience(experience);
                    nurse.setShift(shift);
                    nurse.setAvailable(true);
                    nurse.getBranches().add(branch);
                    return nurseRepository.save(nurse);
                });
    }

    private Employee ensureEmployee(Hospital hospital, Branch branch, User user, String employeeCode, String designation,
                                    String department, Double basicSalary, Double allowances, Double deductions) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getUser() != null && employee.getUser().getId().equals(user.getId()))
                .findFirst()
                .map(existing -> {
                    existing.setHospitalId(hospital.getId());
                    existing.setBranchId(branch.getId());
                    existing.setEmployeeCode(employeeCode);
                    existing.setDesignation(designation);
                    existing.setDepartment(department);
                    existing.setJoiningDate(LocalDate.now().minusMonths(18));
                    existing.setBasicSalary(basicSalary);
                    existing.setAllowances(allowances);
                    existing.setDeductions(deductions);
                    existing.setPanNumber("PAN" + employeeCode.replace("-", ""));
                    existing.setBankAccountNumber("110000" + employeeCode.replace("-", ""));
                    existing.setBankIfsc("SAMR0001234");
                    existing.setStatus(Employee.EmployeeStatus.ACTIVE);
                    return employeeRepository.save(existing);
                })
                .orElseGet(() -> {
                    Employee employee = new Employee();
                    employee.setHospitalId(hospital.getId());
                    employee.setBranchId(branch.getId());
                    employee.setUser(user);
                    employee.setEmployeeCode(employeeCode);
                    employee.setDesignation(designation);
                    employee.setDepartment(department);
                    employee.setJoiningDate(LocalDate.now().minusMonths(18));
                    employee.setBasicSalary(basicSalary);
                    employee.setAllowances(allowances);
                    employee.setDeductions(deductions);
                    employee.setPanNumber("PAN" + employeeCode.replace("-", ""));
                    employee.setBankAccountNumber("110000" + employeeCode.replace("-", ""));
                    employee.setBankIfsc("SAMR0001234");
                    employee.setStatus(Employee.EmployeeStatus.ACTIVE);
                    return employeeRepository.save(employee);
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
                    appointment.setNotes("Seeded appointment for flow testing");
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
        return wardRepository.findByHospitalIdAndBranchId(hospital.getId(), branch.getId()).stream()
                .filter(ward -> ward.getName().equalsIgnoreCase(name))
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
        return bedRepository.findByWardId(ward.getId()).stream()
                .filter(bed -> bed.getBedNumber().equalsIgnoreCase(bedNumber))
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

    private Admission ensureAdmission(Hospital hospital, Branch branch, Patient patient, Doctor doctor, Bed occupiedBed,
                                      Department department, String ipdNumber) {
        return admissionRepository.findByHospitalIdAndBranchId(hospital.getId(), branch.getId(), Pageable.unpaged())
                .stream()
                .filter(admission -> admission.getPatient() != null && admission.getPatient().getId().equals(patient.getId()))
                .findFirst()
                .map(existing -> {
                    existing.setHospitalId(hospital.getId());
                    existing.setBranchId(branch.getId());
                    existing.setAdmittingDoctor(doctor);
                    existing.setPrimaryDoctor(doctor);
                    existing.setDepartment(department);
                    existing.setBed(occupiedBed);
                    existing.setIpdNumber(ipdNumber);
                    existing.setAdmissionDate(LocalDateTime.now().minusHours(6));
                    existing.setStatus(Admission.AdmissionStatus.ADMITTED);
                    existing.setCaseType("NORMAL");
                    existing.setTriage("YELLOW");
                    existing.setGuardianName("Suresh Singh");
                    existing.setGuardianPhone("9000011111");
                    existing.setGuardianRelation("SPOUSE");
                    existing.setDiagnosis("Observation for chest pain");
                    existing.setAdmissionReason("Requires monitoring and serial diagnostics");
                    existing.setAdvanceAmount(15000.0);
                    return admissionRepository.save(existing);
                })
                .orElseGet(() -> {
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
                    return admissionRepository.save(admission);
                });
    }

    private void seedAssets(Hospital hospital, Branch branch, Department medicine, Department radiology,
                            Department laboratory, User adminUser) {
        AssetCategory equipmentCategory = ensureAssetCategory(hospital, branch, "Medical Equipment", "AST-CAT-EQP", "Clinical equipment and devices");
        AssetCategory itCategory = ensureAssetCategory(hospital, branch, "IT Equipment", "AST-CAT-IT", "Computers, printers, networking devices");
        AssetSubCategory monitorSubCategory = ensureAssetSubCategory(hospital, branch, equipmentCategory, "Patient Monitor", "AST-SUB-MON");
        AssetSubCategory imagingSubCategory = ensureAssetSubCategory(hospital, branch, equipmentCategory, "Imaging Console", "AST-SUB-IMG");
        ensureAssetSubCategory(hospital, branch, itCategory, "Printer", "AST-SUB-PRN");
        AssetVendor vendor = ensureAssetVendor(hospital, branch, "MedTech Supplies", "Sanjay Kapoor");
        AssetLocation icuLocation = ensureAssetLocation(hospital, branch, "ICU Store", "Block A", "1", "ICU-ST-01");
        AssetLocation radiologyLocation = ensureAssetLocation(hospital, branch, "Radiology Control Room", "Block B", "Ground", "RAD-CTRL");

        Asset monitorAsset = ensureAsset(hospital, branch, equipmentCategory, monitorSubCategory, medicine,
                "Bedside Monitor", "AST-0001", "SN-MON-1001", "PM-450", "Philips", LocalDate.now().minusYears(1),
                85000.0, LocalDate.now().plusYears(2), "ASSIGNED", "Assigned to medicine ward for vitals monitoring");
        Asset imagingAsset = ensureAsset(hospital, branch, equipmentCategory, imagingSubCategory, radiology,
                "Portable X-Ray Console", "AST-0002", "SN-RAD-2001", "XR-900", "Siemens", LocalDate.now().minusMonths(14),
                425000.0, LocalDate.now().plusYears(1), "AVAILABLE", "Portable console for emergency imaging");
        ensureAsset(hospital, branch, equipmentCategory, monitorSubCategory, laboratory,
                "Lab Analyzer", "AST-0003", "SN-LAB-3001", "LA-210", "Roche", LocalDate.now().minusMonths(18),
                310000.0, LocalDate.now().plusMonths(18), "UNDER_MAINTENANCE", "Biochemistry analyzer");

        ensureAssetAssignment(hospital, branch, monitorAsset, adminUser, icuLocation, "ASSIGNED",
                "Assigned for inpatient monitoring");
        ensureAssetAssignment(hospital, branch, imagingAsset, null, radiologyLocation, "RETURNED",
                "Stored after scheduled calibration");
        ensureAssetMaintenance(hospital, branch, imagingAsset, vendor, "PREVENTIVE", LocalDate.now().minusDays(10),
                LocalDate.now().plusMonths(6), 9500.0, "COMPLETED", "Calibration stable", "Routine preventive maintenance completed");
    }

    private AssetCategory ensureAssetCategory(Hospital hospital, Branch branch, String name, String code, String description) {
        return assetCategoryRepository.findAll().stream()
                .filter(category -> hospital.getId().equals(category.getHospitalId()) && branch.getId().equals(category.getBranchId()))
                .filter(category -> code.equalsIgnoreCase(category.getCode()))
                .findFirst()
                .orElseGet(() -> {
                    AssetCategory category = new AssetCategory();
                    category.setHospitalId(hospital.getId());
                    category.setBranchId(branch.getId());
                    category.setName(name);
                    category.setCode(code);
                    category.setDescription(description);
                    category.setStatus("ACTIVE");
                    return assetCategoryRepository.save(category);
                });
    }

    private AssetSubCategory ensureAssetSubCategory(Hospital hospital, Branch branch, AssetCategory category, String name, String code) {
        return assetSubCategoryRepository.findAll().stream()
                .filter(subCategory -> hospital.getId().equals(subCategory.getHospitalId()) && branch.getId().equals(subCategory.getBranchId()))
                .filter(subCategory -> code.equalsIgnoreCase(subCategory.getCode()))
                .findFirst()
                .orElseGet(() -> {
                    AssetSubCategory subCategory = new AssetSubCategory();
                    subCategory.setHospitalId(hospital.getId());
                    subCategory.setBranchId(branch.getId());
                    subCategory.setCategory(category);
                    subCategory.setName(name);
                    subCategory.setCode(code);
                    subCategory.setDescription("Seeded sub category");
                    subCategory.setStatus("ACTIVE");
                    return assetSubCategoryRepository.save(subCategory);
                });
    }

    private AssetVendor ensureAssetVendor(Hospital hospital, Branch branch, String name, String contactPerson) {
        return assetVendorRepository.findAll().stream()
                .filter(vendor -> hospital.getId().equals(vendor.getHospitalId()) && branch.getId().equals(vendor.getBranchId()))
                .filter(vendor -> name.equalsIgnoreCase(vendor.getName()))
                .findFirst()
                .orElseGet(() -> {
                    AssetVendor vendor = new AssetVendor();
                    vendor.setHospitalId(hospital.getId());
                    vendor.setBranchId(branch.getId());
                    vendor.setName(name);
                    vendor.setContactPerson(contactPerson);
                    vendor.setPhone("9012301234");
                    vendor.setEmail("vendor@" + name.toLowerCase().replace(" ", "") + ".com");
                    vendor.setAddress("Industrial Area, City Center");
                    vendor.setTinNumber("TIN-" + Math.abs(name.hashCode()));
                    vendor.setStatus("ACTIVE");
                    return assetVendorRepository.save(vendor);
                });
    }

    private AssetLocation ensureAssetLocation(Hospital hospital, Branch branch, String name, String building, String floor, String room) {
        return assetLocationRepository.findAll().stream()
                .filter(location -> hospital.getId().equals(location.getHospitalId()) && branch.getId().equals(location.getBranchId()))
                .filter(location -> name.equalsIgnoreCase(location.getName()))
                .findFirst()
                .orElseGet(() -> {
                    AssetLocation location = new AssetLocation();
                    location.setHospitalId(hospital.getId());
                    location.setBranchId(branch.getId());
                    location.setName(name);
                    location.setBuilding(building);
                    location.setFloor(floor);
                    location.setRoomNumber(room);
                    location.setStatus("ACTIVE");
                    return assetLocationRepository.save(location);
                });
    }

    private Asset ensureAsset(Hospital hospital, Branch branch, AssetCategory category, AssetSubCategory subCategory,
                              Department department, String name, String assetCode, String serialNumber, String modelNumber,
                              String manufacturer, LocalDate purchaseDate, Double purchaseCost, LocalDate warrantyExpiry,
                              String status, String description) {
        return assetRepository.findAll().stream()
                .filter(asset -> hospital.getId().equals(asset.getHospitalId()) && branch.getId().equals(asset.getBranchId()))
                .filter(asset -> assetCode.equalsIgnoreCase(asset.getAssetCode()))
                .findFirst()
                .map(existing -> {
                    existing.setCategory(category);
                    existing.setSubCategory(subCategory);
                    existing.setDepartment(department);
                    existing.setName(name);
                    existing.setSerialNumber(serialNumber);
                    existing.setModelNumber(modelNumber);
                    existing.setManufacturer(manufacturer);
                    existing.setPurchaseDate(purchaseDate);
                    existing.setPurchaseCost(purchaseCost);
                    existing.setWarrantyExpiry(warrantyExpiry);
                    existing.setStatus(status);
                    existing.setDescription(description);
                    return assetRepository.save(existing);
                })
                .orElseGet(() -> {
                    Asset asset = new Asset();
                    asset.setHospitalId(hospital.getId());
                    asset.setBranchId(branch.getId());
                    asset.setCategory(category);
                    asset.setSubCategory(subCategory);
                    asset.setDepartment(department);
                    asset.setName(name);
                    asset.setAssetCode(assetCode);
                    asset.setSerialNumber(serialNumber);
                    asset.setModelNumber(modelNumber);
                    asset.setManufacturer(manufacturer);
                    asset.setPurchaseDate(purchaseDate);
                    asset.setPurchaseCost(purchaseCost);
                    asset.setWarrantyExpiry(warrantyExpiry);
                    asset.setStatus(status);
                    asset.setDescription(description);
                    return assetRepository.save(asset);
                });
    }

    private void ensureAssetAssignment(Hospital hospital, Branch branch, Asset asset, User assignedTo, AssetLocation location,
                                       String status, String notes) {
        boolean exists = assetAssignmentRepository.findAll().stream()
                .filter(assignment -> hospital.getId().equals(assignment.getHospitalId()) && branch.getId().equals(assignment.getBranchId()))
                .anyMatch(assignment -> assignment.getAsset() != null && assignment.getAsset().getId().equals(asset.getId()));
        if (exists) {
            return;
        }

        AssetAssignment assignment = new AssetAssignment();
        assignment.setHospitalId(hospital.getId());
        assignment.setBranchId(branch.getId());
        assignment.setAsset(asset);
        assignment.setAssignedTo(assignedTo);
        assignment.setLocation(location);
        assignment.setAssignedDate(LocalDateTime.now().minusDays(5));
        assignment.setReturnDate("RETURNED".equals(status) ? LocalDateTime.now().minusDays(1) : null);
        assignment.setStatus(status);
        assignment.setNotes(notes);
        assetAssignmentRepository.save(assignment);
    }

    private void ensureAssetMaintenance(Hospital hospital, Branch branch, Asset asset, AssetVendor vendor,
                                        String maintenanceType, LocalDate maintenanceDate, LocalDate nextDueDate,
                                        Double cost, String status, String findings, String actionTaken) {
        boolean exists = assetMaintenanceRepository.findAll().stream()
                .filter(maintenance -> hospital.getId().equals(maintenance.getHospitalId()) && branch.getId().equals(maintenance.getBranchId()))
                .anyMatch(maintenance -> maintenance.getAsset() != null
                        && maintenance.getAsset().getId().equals(asset.getId())
                        && maintenanceDate.equals(maintenance.getMaintenanceDate()));
        if (exists) {
            return;
        }

        AssetMaintenance maintenance = new AssetMaintenance();
        maintenance.setHospitalId(hospital.getId());
        maintenance.setBranchId(branch.getId());
        maintenance.setAsset(asset);
        maintenance.setVendor(vendor);
        maintenance.setMaintenanceType(maintenanceType);
        maintenance.setMaintenanceDate(maintenanceDate);
        maintenance.setNextDueDate(nextDueDate);
        maintenance.setCost(cost);
        maintenance.setStatus(status);
        maintenance.setFindings(findings);
        maintenance.setActionTaken(actionTaken);
        assetMaintenanceRepository.save(maintenance);
    }

    private void seedBilling(Hospital hospital, Branch branch, Patient patientOne, Patient patientTwo, Admission admission,
                             Doctor doctor, OPDVisit visit) {
        ensureServiceCharge(hospital, branch, "General Consultation", "CONSULTATION", 700.0, 12.0);
        ensureServiceCharge(hospital, branch, "Complete Blood Count", "LAB", 450.0, 5.0);
        ensureServiceCharge(hospital, branch, "Chest X-Ray", "RADIOLOGY", 900.0, 5.0);

        ensureInvoice(hospital, branch, patientOne, null, "INV-OPD-1001", 700.0, 84.0, 0.0, 784.0,
                784.0, 0.0, Invoice.InvoiceStatus.PAID, "CASH",
                List.of(buildInvoiceItem("General Consultation", 700.0, 1.0, 84.0, 784.0, "CONSULTATION", visit.getId())));

        ensureInvoice(hospital, branch, patientTwo, admission, "INV-IPD-1002", 17450.0, 450.0, 500.0, 17400.0,
                10000.0, 7400.0, Invoice.InvoiceStatus.PARTIALLY_PAID, "CARD",
                List.of(
                        buildInvoiceItem("Bed Charges - 4 days", 2500.0, 4.0, 0.0, 10000.0, "IPD", admission.getId()),
                        buildInvoiceItem("Emergency Observation Package", 7450.0, 1.0, 450.0, 7900.0, "EMERGENCY", admission.getId())
                ));
    }

    private ServiceCharge ensureServiceCharge(Hospital hospital, Branch branch, String name, String category,
                                              Double standardCharge, Double taxPercentage) {
        return serviceChargeRepository.findAll().stream()
                .filter(charge -> hospital.getId().equals(charge.getHospitalId()) && branch.getId().equals(charge.getBranchId()))
                .filter(charge -> name.equalsIgnoreCase(charge.getName()) && category.equalsIgnoreCase(charge.getCategory()))
                .findFirst()
                .orElseGet(() -> {
                    ServiceCharge charge = new ServiceCharge();
                    charge.setHospitalId(hospital.getId());
                    charge.setBranchId(branch.getId());
                    charge.setName(name);
                    charge.setCategory(category);
                    charge.setStandardCharge(standardCharge);
                    charge.setTaxPercentage(taxPercentage);
                    charge.setActive(true);
                    return serviceChargeRepository.save(charge);
                });
    }

    private InvoiceItem buildInvoiceItem(String itemName, Double unitPrice, Double quantity, Double taxAmount,
                                         Double totalAmount, String category, Long itemId) {
        InvoiceItem item = new InvoiceItem();
        item.setItemName(itemName);
        item.setUnitPrice(unitPrice);
        item.setQuantity(quantity);
        item.setTaxAmount(taxAmount);
        item.setTotalAmount(totalAmount);
        item.setCategory(category);
        item.setItemId(itemId);
        return item;
    }

    private void ensureInvoice(Hospital hospital, Branch branch, Patient patient, Admission admission, String invoiceNumber,
                               Double totalAmount, Double taxAmount, Double discountAmount, Double netAmount,
                               Double paidAmount, Double dueAmount, Invoice.InvoiceStatus status, String paymentMethod,
                               List<InvoiceItem> items) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber).orElseGet(() -> {
            Invoice created = new Invoice();
            created.setHospitalId(hospital.getId());
            created.setBranchId(branch.getId());
            created.setPatient(patient);
            created.setAdmission(admission);
            created.setInvoiceNumber(invoiceNumber);
            created.setInvoiceDate(LocalDateTime.now().minusDays(1));
            created.setTotalAmount(totalAmount);
            created.setTaxAmount(taxAmount);
            created.setDiscountAmount(discountAmount);
            created.setNetAmount(netAmount);
            created.setPaidAmount(paidAmount);
            created.setDueAmount(dueAmount);
            created.setStatus(status);
            created.setPaymentMethod(paymentMethod);
            created.setItems(new ArrayList<>());
            return created;
        });

        invoice.setHospitalId(hospital.getId());
        invoice.setBranchId(branch.getId());
        invoice.setPatient(patient);
        invoice.setAdmission(admission);
        invoice.setInvoiceDate(LocalDateTime.now().minusDays(1));
        invoice.setTotalAmount(totalAmount);
        invoice.setTaxAmount(taxAmount);
        invoice.setDiscountAmount(discountAmount);
        invoice.setNetAmount(netAmount);
        invoice.setPaidAmount(paidAmount);
        invoice.setDueAmount(dueAmount);
        invoice.setStatus(status);
        invoice.setPaymentMethod(paymentMethod);
        invoice.getItems().clear();
        for (InvoiceItem item : items) {
            item.setInvoice(invoice);
            invoice.getItems().add(item);
        }
        invoiceRepository.save(invoice);
    }

    private void seedClinical(Hospital hospital, Branch branch, Patient patientOne, Patient patientTwo, Patient patientFour,
                              Doctor doctorOne, Doctor emergencyDoctor, Department medicine, Department emergency,
                              OPDVisit visit, Admission admission) {
        ensurePatientVital(hospital, branch, patientOne, visit.getId(), null, LocalDateTime.now().minusHours(4),
                68.5, 172.0, "120/80", 98.4, 78, 18, 99, "Nurse Anita", "Stable OPD vitals");
        ensurePatientVital(hospital, branch, patientTwo, null, admission.getId(), LocalDateTime.now().minusHours(2),
                71.2, 165.0, "134/86", 99.1, 84, 20, 97, "Nurse Anita", "IPD review vitals");

        ensureEmrRecord(hospital, branch, patientOne, doctorOne, medicine, "Fever with productive cough for 3 days",
                "120/80", 99.2, 82, 19, 98, 68.5, 172.0, 23.2,
                "Cough and intermittent fever for 72 hours", "Type 2 diabetes for 3 years", "Penicillin",
                "Mild crepitations, no distress", "Acute bronchitis", "Paracetamol, Azithromycin",
                "CBC, CRP", "Chest X-ray PA", "ACTIVE");

        ensureDoctorNote(hospital, branch, patientOne, doctorOne, "Initial Consultation", "Consultation",
                LocalDateTime.now().minusHours(3), "Patient oriented, symptoms consistent with acute lower respiratory infection.", "Finalized");
        ensureClinicalDiagnosis(hospital, branch, patientOne, doctorOne, visit, "J20.9", "Acute Bronchitis",
                "Final", "Moderate", "Active", LocalDateTime.now().minusHours(3), "Treat symptomatically and review in 3 days");
        ensureClinicalPrescription(hospital, branch, patientOne, doctorOne, visit, "Azithromycin",
                "500mg", "1-0-0", "Oral", "3 days", "After food", "0",
                LocalDateTime.now().minusHours(3), "ACTIVE");
        ensureMedicalHistory(hospital, branch, patientOne, "Type 2 Diabetes", "2022-03-18", "Chronic",
                "Metformin 500mg BD", "Sugar usually controlled");
        ensurePersonalHistory(hospital, branch, patientOne, "Exercise", "4 days/week", "5 years", "Current",
                "Walks 30 minutes daily");
        ensureSurgicalHistory(hospital, branch, patientOne, "Appendectomy", "2015-08-11", "Dr. Mehta",
                "City Surgical Center", "Recovered well");

        ensureNursingNote(hospital, branch, admission, LocalDateTime.now().minusHours(1),
                "Patient under observation for chest discomfort. No active distress.", "NORMAL",
                98.9, 80.0, 130.0, 84.0, 98.0);
        ensureDischargeSummary(hospital, branch, admission, LocalDateTime.now().plusDays(1),
                "Atypical chest pain under observation", "Stable inpatient course with serial ECG monitoring.",
                "Observation, IV fluids, analgesics", "Pantoprazole, analgesic SOS",
                "Cardiology review after 5 days", DischargeSummary.DischargeType.NORMAL);

        ensureErVisit(hospital, branch, patientFour, emergencyDoctor, emergency, LocalDateTime.now().minusMinutes(90),
                ERVisit.TriageStatus.YELLOW, ERVisit.ERStatus.OBSERVED, "Roadside fall with dizziness");
        ensureOtBooking(hospital, branch, patientTwo, emergencyDoctor, doctorOne, emergency,
                "Coronary Angiography Prep", LocalDateTime.now().plusDays(2).withHour(9).withMinute(0),
                90, OTBooking.OTStatus.SCHEDULED, "Pre-procedure evaluation completed");
    }

    private void ensurePatientVital(Hospital hospital, Branch branch, Patient patient, Long opdVisitId, Long admissionId,
                                    LocalDateTime recordedAt, Double weight, Double height, String bloodPressure,
                                    Double temperature, Integer pulseRate, Integer respiratoryRate, Integer spo2,
                                    String recordedBy, String remark) {
        boolean exists = patientVitalRepository.findByHospitalIdAndBranchIdAndPatientIdOrderByRecordedAtDesc(
                        hospital.getId(), branch.getId(), patient.getId())
                .stream()
                .anyMatch(vital -> remark.equalsIgnoreCase(vital.getRemark()));
        if (exists) {
            return;
        }

        PatientVital vital = new PatientVital();
        vital.setHospitalId(hospital.getId());
        vital.setBranchId(branch.getId());
        vital.setPatient(patient);
        vital.setRecordedAt(recordedAt);
        vital.setWeight(weight);
        vital.setHeight(height);
        vital.setBloodPressure(bloodPressure);
        vital.setTemperature(temperature);
        vital.setPulseRate(pulseRate);
        vital.setRespiratoryRate(respiratoryRate);
        vital.setSpo2(spo2);
        vital.setRecordedBy(recordedBy);
        vital.setRemark(remark);
        vital.setOpdVisitId(opdVisitId);
        vital.setAdmissionId(admissionId);
        patientVitalRepository.save(vital);
    }

    private void ensureEmrRecord(Hospital hospital, Branch branch, Patient patient, Doctor doctor, Department department,
                                 String chiefComplaint, String bloodPressure, Double bodyTemperature, Integer heartRate,
                                 Integer respiratoryRate, Integer oxygenSaturation, Double weight, Double height,
                                 Double bmi, String historyOfPresentIllness, String pastMedicalHistory, String allergies,
                                 String physicalExamination, String diagnosis, String prescription, String labOrders,
                                 String radiologyOrders, String status) {
        boolean exists = emrRepository.findByHospitalIdAndBranchId(hospital.getId(), branch.getId(), Pageable.unpaged())
                .stream()
                .anyMatch(record -> record.getPatient() != null
                        && record.getPatient().getId().equals(patient.getId())
                        && chiefComplaint.equalsIgnoreCase(record.getChiefComplaint()));
        if (exists) {
            return;
        }

        EMRRecord record = new EMRRecord();
        record.setHospitalId(hospital.getId());
        record.setBranchId(branch.getId());
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setDepartment(department);
        record.setChiefComplaint(chiefComplaint);
        record.setBloodPressure(bloodPressure);
        record.setBodyTemperature(bodyTemperature);
        record.setHeartRate(heartRate);
        record.setRespiratoryRate(respiratoryRate);
        record.setOxygenSaturation(oxygenSaturation);
        record.setWeight(weight);
        record.setHeight(height);
        record.setBmi(bmi);
        record.setHistoryOfPresentIllness(historyOfPresentIllness);
        record.setPastMedicalHistory(pastMedicalHistory);
        record.setAllergies(allergies);
        record.setPhysicalExamination(physicalExamination);
        record.setDiagnosis(diagnosis);
        record.setPrescription(prescription);
        record.setLabOrders(labOrders);
        record.setRadiologyOrders(radiologyOrders);
        record.setStatus(status);
        emrRepository.save(record);
    }

    private void ensureDoctorNote(Hospital hospital, Branch branch, Patient patient, Doctor doctor, String title,
                                  String type, LocalDateTime noteTime, String content, String status) {
        boolean exists = doctorNoteRepository.findAll().stream()
                .filter(note -> hospital.getId().equals(note.getHospitalId()) && branch.getId().equals(note.getBranchId()))
                .anyMatch(note -> note.getPatient() != null
                        && note.getPatient().getId().equals(patient.getId())
                        && title.equalsIgnoreCase(note.getTitle()));
        if (exists) {
            return;
        }

        DoctorNote note = new DoctorNote();
        note.setHospitalId(hospital.getId());
        note.setBranchId(branch.getId());
        note.setPatient(patient);
        note.setDoctor(doctor);
        note.setTitle(title);
        note.setType(type);
        note.setNoteTime(noteTime);
        note.setContent(content);
        note.setStatus(status);
        doctorNoteRepository.save(note);
    }

    private void ensureClinicalDiagnosis(Hospital hospital, Branch branch, Patient patient, Doctor doctor, OPDVisit visit,
                                         String icdCode, String diagnosisName, String type, String severity, String status,
                                         LocalDateTime diagnosisTime, String notes) {
        boolean exists = clinicalDiagnosisRepository.findAll().stream()
                .filter(diagnosis -> hospital.getId().equals(diagnosis.getHospitalId()) && branch.getId().equals(diagnosis.getBranchId()))
                .anyMatch(diagnosis -> diagnosis.getPatient() != null
                        && diagnosis.getPatient().getId().equals(patient.getId())
                        && diagnosisName.equalsIgnoreCase(diagnosis.getDiagnosisName()));
        if (exists) {
            return;
        }

        ClinicalDiagnosis diagnosis = new ClinicalDiagnosis();
        diagnosis.setHospitalId(hospital.getId());
        diagnosis.setBranchId(branch.getId());
        diagnosis.setPatient(patient);
        diagnosis.setDoctor(doctor);
        diagnosis.setVisit(visit);
        diagnosis.setIcdCode(icdCode);
        diagnosis.setDiagnosisName(diagnosisName);
        diagnosis.setType(type);
        diagnosis.setSeverity(severity);
        diagnosis.setStatus(status);
        diagnosis.setDiagnosisTime(diagnosisTime);
        diagnosis.setNotes(notes);
        clinicalDiagnosisRepository.save(diagnosis);
    }

    private void ensureClinicalPrescription(Hospital hospital, Branch branch, Patient patient, Doctor doctor, OPDVisit visit,
                                            String medicineName, String dosage, String frequency, String route,
                                            String duration, String instructions, String refillCount,
                                            LocalDateTime prescriptionTime, String status) {
        boolean exists = clinicalPrescriptionRepository.findAll().stream()
                .filter(prescription -> hospital.getId().equals(prescription.getHospitalId()) && branch.getId().equals(prescription.getBranchId()))
                .anyMatch(prescription -> prescription.getPatient() != null
                        && prescription.getPatient().getId().equals(patient.getId())
                        && medicineName.equalsIgnoreCase(prescription.getMedicineName()));
        if (exists) {
            return;
        }

        ClinicalPrescription prescription = new ClinicalPrescription();
        prescription.setHospitalId(hospital.getId());
        prescription.setBranchId(branch.getId());
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setVisit(visit);
        prescription.setMedicineName(medicineName);
        prescription.setDosage(dosage);
        prescription.setFrequency(frequency);
        prescription.setRoute(route);
        prescription.setDuration(duration);
        prescription.setInstructions(instructions);
        prescription.setRefillCount(refillCount);
        prescription.setPrescriptionTime(prescriptionTime);
        prescription.setStatus(status);
        clinicalPrescriptionRepository.save(prescription);
    }

    private void ensureMedicalHistory(Hospital hospital, Branch branch, Patient patient, String conditionName,
                                      String diagnosisDate, String status, String medications, String notes) {
        boolean exists = medicalHistoryRepository.findAll().stream()
                .filter(history -> hospital.getId().equals(history.getHospitalId()) && branch.getId().equals(history.getBranchId()))
                .anyMatch(history -> history.getPatient() != null
                        && history.getPatient().getId().equals(patient.getId())
                        && conditionName.equalsIgnoreCase(history.getConditionName()));
        if (exists) {
            return;
        }

        MedicalHistory history = new MedicalHistory();
        history.setHospitalId(hospital.getId());
        history.setBranchId(branch.getId());
        history.setPatient(patient);
        history.setConditionName(conditionName);
        history.setDiagnosisDate(diagnosisDate);
        history.setStatus(status);
        history.setMedications(medications);
        history.setNotes(notes);
        medicalHistoryRepository.save(history);
    }

    private void ensurePersonalHistory(Hospital hospital, Branch branch, Patient patient, String type, String frequency,
                                       String duration, String status, String notes) {
        boolean exists = personalHistoryRepository.findAll().stream()
                .filter(history -> hospital.getId().equals(history.getHospitalId()) && branch.getId().equals(history.getBranchId()))
                .anyMatch(history -> history.getPatient() != null
                        && history.getPatient().getId().equals(patient.getId())
                        && type.equalsIgnoreCase(history.getType()));
        if (exists) {
            return;
        }

        PersonalHistory history = new PersonalHistory();
        history.setHospitalId(hospital.getId());
        history.setBranchId(branch.getId());
        history.setPatient(patient);
        history.setType(type);
        history.setFrequency(frequency);
        history.setDuration(duration);
        history.setStatus(status);
        history.setNotes(notes);
        personalHistoryRepository.save(history);
    }

    private void ensureSurgicalHistory(Hospital hospital, Branch branch, Patient patient, String surgeryName,
                                       String surgeryDate, String surgeon, String facility, String notes) {
        boolean exists = surgicalHistoryRepository.findAll().stream()
                .filter(history -> hospital.getId().equals(history.getHospitalId()) && branch.getId().equals(history.getBranchId()))
                .anyMatch(history -> history.getPatient() != null
                        && history.getPatient().getId().equals(patient.getId())
                        && surgeryName.equalsIgnoreCase(history.getSurgeryName()));
        if (exists) {
            return;
        }

        SurgicalHistory history = new SurgicalHistory();
        history.setHospitalId(hospital.getId());
        history.setBranchId(branch.getId());
        history.setPatient(patient);
        history.setSurgeryName(surgeryName);
        history.setSurgeryDate(surgeryDate);
        history.setSurgeon(surgeon);
        history.setHospital(facility);
        history.setNotes(notes);
        surgicalHistoryRepository.save(history);
    }

    private void ensureNursingNote(Hospital hospital, Branch branch, Admission admission, LocalDateTime noteTime,
                                   String description, String status, Double temperature, Double pulse,
                                   Double bpSystolic, Double bpDiastolic, Double spo2) {
        boolean exists = nursingNoteRepository.findAll().stream()
                .filter(note -> hospital.getId().equals(note.getHospitalId()) && branch.getId().equals(note.getBranchId()))
                .anyMatch(note -> note.getAdmission() != null
                        && note.getAdmission().getId().equals(admission.getId())
                        && description.equalsIgnoreCase(note.getDescription()));
        if (exists) {
            return;
        }

        NursingNote note = new NursingNote();
        note.setHospitalId(hospital.getId());
        note.setBranchId(branch.getId());
        note.setAdmission(admission);
        note.setNoteTime(noteTime);
        note.setDescription(description);
        note.setStatus(status);
        note.setTemperature(temperature);
        note.setPulse(pulse);
        note.setBpSystolic(bpSystolic);
        note.setBpDiastolic(bpDiastolic);
        note.setSpo2(spo2);
        nursingNoteRepository.save(note);
    }

    private void ensureDischargeSummary(Hospital hospital, Branch branch, Admission admission, LocalDateTime dischargeTime,
                                        String dischargeDiagnosis, String summaryOfCase, String treatmentGiven,
                                        String medicationsAtDischarge, String followUpInstructions,
                                        DischargeSummary.DischargeType dischargeType) {
        if (dischargeSummaryRepository.findByAdmissionId(admission.getId()).isPresent()) {
            return;
        }

        DischargeSummary summary = new DischargeSummary();
        summary.setHospitalId(hospital.getId());
        summary.setBranchId(branch.getId());
        summary.setAdmission(admission);
        summary.setDischargeTime(dischargeTime);
        summary.setDischargeDiagnosis(dischargeDiagnosis);
        summary.setSummaryOfCase(summaryOfCase);
        summary.setTreatmentGiven(treatmentGiven);
        summary.setMedicationsAtDischarge(medicationsAtDischarge);
        summary.setFollowUpInstructions(followUpInstructions);
        summary.setDischargeType(dischargeType);
        dischargeSummaryRepository.save(summary);
    }

    private void ensureErVisit(Hospital hospital, Branch branch, Patient patient, Doctor doctor, Department department,
                               LocalDateTime arrivalTime, ERVisit.TriageStatus triage, ERVisit.ERStatus status,
                               String chiefComplaint) {
        boolean exists = erVisitRepository.findAll().stream()
                .filter(visit -> hospital.getId().equals(visit.getHospitalId()) && branch.getId().equals(visit.getBranchId()))
                .anyMatch(visit -> visit.getPatient() != null
                        && visit.getPatient().getId().equals(patient.getId())
                        && chiefComplaint.equalsIgnoreCase(visit.getChiefComplaint()));
        if (exists) {
            return;
        }

        ERVisit visit = new ERVisit();
        visit.setHospitalId(hospital.getId());
        visit.setBranchId(branch.getId());
        visit.setPatient(patient);
        visit.setAssignedDoctor(doctor);
        visit.setDepartment(department);
        visit.setArrivalTime(arrivalTime);
        visit.setTriage(triage);
        visit.setStatus(status);
        visit.setChiefComplaint(chiefComplaint);
        erVisitRepository.save(visit);
    }

    private void ensureOtBooking(Hospital hospital, Branch branch, Patient patient, Doctor surgeon, Doctor anesthetist,
                                 Department department, String procedureName, LocalDateTime scheduleDate,
                                 Integer durationInMinutes, OTBooking.OTStatus status, String notes) {
        boolean exists = otBookingRepository.findAll().stream()
                .filter(booking -> hospital.getId().equals(booking.getHospitalId()) && branch.getId().equals(booking.getBranchId()))
                .anyMatch(booking -> booking.getPatient() != null
                        && booking.getPatient().getId().equals(patient.getId())
                        && procedureName.equalsIgnoreCase(booking.getProcedureName()));
        if (exists) {
            return;
        }

        OTBooking booking = new OTBooking();
        booking.setHospitalId(hospital.getId());
        booking.setBranchId(branch.getId());
        booking.setPatient(patient);
        booking.setSurgeon(surgeon);
        booking.setAnesthetist(anesthetist);
        booking.setDepartment(department);
        booking.setProcedureName(procedureName);
        booking.setScheduleDate(scheduleDate);
        booking.setDurationInMinutes(durationInMinutes);
        booking.setStatus(status);
        booking.setNotes(notes);
        booking.setVideoRecordingUrl("https://example.local/ot/" + Math.abs(procedureName.hashCode()));
        otBookingRepository.save(booking);
    }

    private void seedDiagnostics(Hospital hospital, Branch branch, Patient patientOne, Patient patientTwo,
                                 Doctor doctorOne, Doctor doctorTwo, Department laboratory, Department radiology) {
        ensureLabOrder(hospital, branch, patientOne, doctorOne, laboratory, "LAB-1001", "Complete Blood Count", "CBC",
                "BLOOD", LocalDateTime.now().minusHours(5), LocalDateTime.now().minusHours(4),
                LocalDateTime.now().minusHours(3), LabOrder.LabStatus.COMPLETED,
                "Hemoglobin 13.8 g/dL, TLC 7900 /cmm", "Within acceptable range");
        ensureLabOrder(hospital, branch, patientTwo, doctorTwo, laboratory, "LAB-1002", "Troponin I", "TROP-I",
                "BLOOD", LocalDateTime.now().minusHours(2), LocalDateTime.now().minusMinutes(100),
                null, LabOrder.LabStatus.PROCESSING,
                null, "Urgent cardiac marker evaluation");

        ensureRadiologyOrder(hospital, branch, patientOne, doctorOne, radiology, "RAD-1001", "X-RAY",
                "Chest X-Ray PA View", LocalDateTime.now().minusHours(4), LocalDateTime.now().minusHours(3),
                LocalDateTime.now().minusHours(2), RadiologyOrder.RadiologyStatus.COMPLETED,
                "No focal consolidation seen.", "Mild bronchitic changes", "https://pacs.local/rad-1001");
        ensureRadiologyOrder(hospital, branch, patientTwo, doctorTwo, radiology, "RAD-1002", "CT",
                "CT Chest Contrast", LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(1),
                null, RadiologyOrder.RadiologyStatus.SCHEDULED,
                null, null, null);
    }

    private void ensureLabOrder(Hospital hospital, Branch branch, Patient patient, Doctor doctor, Department department,
                                String orderNumber, String testName, String testCode, String sampleType,
                                LocalDateTime orderTime, LocalDateTime sampleCollectionTime, LocalDateTime resultTime,
                                LabOrder.LabStatus status, String result, String remarks) {
        boolean exists = labOrderRepository.findAll().stream()
                .filter(order -> hospital.getId().equals(order.getHospitalId()) && branch.getId().equals(order.getBranchId()))
                .anyMatch(order -> orderNumber.equalsIgnoreCase(order.getOrderNumber()));
        if (exists) {
            return;
        }

        LabOrder order = new LabOrder();
        order.setHospitalId(hospital.getId());
        order.setBranchId(branch.getId());
        order.setPatient(patient);
        order.setOrderingDoctor(doctor);
        order.setDepartment(department);
        order.setOrderNumber(orderNumber);
        order.setTestName(testName);
        order.setTestCode(testCode);
        order.setSampleType(sampleType);
        order.setOrderTime(orderTime);
        order.setSampleCollectionTime(sampleCollectionTime);
        order.setResultTime(resultTime);
        order.setStatus(status);
        order.setResult(result);
        order.setRemarks(remarks);
        labOrderRepository.save(order);
    }

    private void ensureRadiologyOrder(Hospital hospital, Branch branch, Patient patient, Doctor doctor, Department department,
                                      String orderNumber, String modality, String procedureName, LocalDateTime orderTime,
                                      LocalDateTime procedureTime, LocalDateTime reportTime,
                                      RadiologyOrder.RadiologyStatus status, String report,
                                      String impression, String imagesUrl) {
        boolean exists = radiologyOrderRepository.findAll().stream()
                .filter(order -> hospital.getId().equals(order.getHospitalId()) && branch.getId().equals(order.getBranchId()))
                .anyMatch(order -> orderNumber.equalsIgnoreCase(order.getOrderNumber()));
        if (exists) {
            return;
        }

        RadiologyOrder order = new RadiologyOrder();
        order.setHospitalId(hospital.getId());
        order.setBranchId(branch.getId());
        order.setPatient(patient);
        order.setOrderingDoctor(doctor);
        order.setDepartment(department);
        order.setOrderNumber(orderNumber);
        order.setModality(modality);
        order.setProcedureName(procedureName);
        order.setOrderTime(orderTime);
        order.setProcedureTime(procedureTime);
        order.setReportTime(reportTime);
        order.setStatus(status);
        order.setReport(report);
        order.setImpression(impression);
        order.setImagesUrl(imagesUrl);
        radiologyOrderRepository.save(order);
    }

    private void seedFinance(Hospital hospital, Branch branch) {
        ensureFinancialTransaction(hospital, branch, LocalDateTime.now().minusDays(1), "CREDIT", 784.0,
                "OPD_BILLING", "Received OPD bill payment INV-OPD-1001", "CASH", "INV-OPD-1001", "SUCCESS");
        ensureFinancialTransaction(hospital, branch, LocalDateTime.now().minusHours(20), "CREDIT", 10000.0,
                "IPD_BILLING", "Partial IPD payment INV-IPD-1002", "CARD", "INV-IPD-1002", "SUCCESS");
        ensureFinancialTransaction(hospital, branch, LocalDateTime.now().minusHours(12), "DEBIT", 18000.0,
                "PURCHASE", "Vendor payment for pharmacy replenishment", "ONLINE", "PO-5001", "SUCCESS");
    }

    private void ensureFinancialTransaction(Hospital hospital, Branch branch, LocalDateTime transactionTime,
                                            String transactionType, Double amount, String category, String description,
                                            String paymentMethod, String referenceNumber, String status) {
        boolean exists = financialTransactionRepository.findByHospitalIdAndBranchId(hospital.getId(), branch.getId())
                .stream()
                .anyMatch(transaction -> description.equalsIgnoreCase(transaction.getDescription()));
        if (exists) {
            return;
        }

        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setHospitalId(hospital.getId());
        transaction.setBranchId(branch.getId());
        transaction.setTransactionTime(transactionTime);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setDescription(description);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setReferenceNumber(referenceNumber);
        transaction.setStatus(status);
        financialTransactionRepository.save(transaction);
    }

    private void seedInventory(Hospital hospital, Branch branch) {
        ensurePharmacyStock(hospital, branch, "Azithromycin 500", "BATCH-AZI-001", "Azithromycin",
                "Sun Pharma", 12.5, 18.0, 240, LocalDate.now().plusMonths(18), 40, true, "TABLET");
        ensurePharmacyStock(hospital, branch, "Pantoprazole Injection", "BATCH-PAN-002", "Pantoprazole",
                "Cipla", 32.0, 45.0, 75, LocalDate.now().plusMonths(10), 20, true, "INJECTION");
        ensurePharmacyStock(hospital, branch, "Paracetamol Syrup", "BATCH-PAR-003", "Paracetamol",
                "Mankind", 24.0, 36.0, 55, LocalDate.now().plusMonths(8), 15, true, "SYRUP");
    }

    private void ensurePharmacyStock(Hospital hospital, Branch branch, String medicineName, String batchNumber,
                                     String genericName, String manufacturer, Double unitPrice, Double mrp,
                                     Integer quantity, LocalDate expiryDate, Integer reorderLevel, boolean active,
                                     String category) {
        boolean exists = pharmacyStockRepository.findAll().stream()
                .filter(stock -> hospital.getId().equals(stock.getHospitalId()) && branch.getId().equals(stock.getBranchId()))
                .anyMatch(stock -> batchNumber.equalsIgnoreCase(stock.getBatchNumber()));
        if (exists) {
            return;
        }

        PharmacyStock stock = new PharmacyStock();
        stock.setHospitalId(hospital.getId());
        stock.setBranchId(branch.getId());
        stock.setMedicineName(medicineName);
        stock.setBatchNumber(batchNumber);
        stock.setGenericName(genericName);
        stock.setManufacturer(manufacturer);
        stock.setUnitPrice(unitPrice);
        stock.setMrp(mrp);
        stock.setQuantity(quantity);
        stock.setExpiryDate(expiryDate);
        stock.setReorderLevel(reorderLevel);
        stock.setActive(active);
        stock.setCategory(category);
        pharmacyStockRepository.save(stock);
    }

    private void seedReception(Hospital hospital, Branch branch, User receptionistUser, Patient patientOne, Patient patientTwo) {
        ensureVisitorLog(hospital, branch, "Suresh Kumar", "9000012345", "Patient Visit", "Brother",
                patientOne.getUhid(), LocalDateTime.now().minusHours(2), null, "Aadhaar", "AAD-1001", receptionistUser.getId());
        ensureVisitorLog(hospital, branch, "Meena Singh", "9000012346", "Billing Inquiry", "Spouse",
                patientTwo.getUhid(), LocalDateTime.now().minusHours(4), LocalDateTime.now().minusHours(1),
                "PAN", "PAN-2201", receptionistUser.getId());
    }

    private void ensureVisitorLog(Hospital hospital, Branch branch, String visitorName, String phoneNumber, String purpose,
                                  String relationToPatient, String patientUhid, LocalDateTime entryTime,
                                  LocalDateTime exitTime, String idProofType, String idProofNumber, Long receptionistId) {
        boolean exists = visitorLogRepository.findAll().stream()
                .filter(log -> hospital.getId().equals(log.getHospitalId()) && branch.getId().equals(log.getBranchId()))
                .anyMatch(log -> visitorName.equalsIgnoreCase(log.getVisitorName())
                        && phoneNumber.equalsIgnoreCase(log.getPhoneNumber())
                        && patientUhid.equalsIgnoreCase(log.getPatientUhid()));
        if (exists) {
            return;
        }

        VisitorLog log = new VisitorLog();
        log.setHospitalId(hospital.getId());
        log.setBranchId(branch.getId());
        log.setVisitorName(visitorName);
        log.setPhoneNumber(phoneNumber);
        log.setPurpose(purpose);
        log.setRelationToPatient(relationToPatient);
        log.setPatientUhid(patientUhid);
        log.setEntryTime(entryTime);
        log.setExitTime(exitTime);
        log.setIdProofType(idProofType);
        log.setIdProofNumber(idProofNumber);
        log.setReceptionistId(receptionistId);
        visitorLogRepository.save(log);
    }

    private void seedReporting(Hospital hospital, Branch branch, User generatedBy) {
        ensureSystemReport(hospital, branch, "Daily Revenue Snapshot", "FINANCIAL", "PDF",
                LocalDateTime.now().minusHours(6), generatedBy.getUsername(),
                "https://reports.local/financial/daily-revenue", "{\"date\":\"today\"}", "COMPLETED");
        ensureSystemReport(hospital, branch, "Clinical Caseload Overview", "CLINICAL", "EXCEL",
                LocalDateTime.now().minusHours(3), generatedBy.getUsername(),
                "https://reports.local/clinical/caseload", "{\"department\":\"ALL\"}", "COMPLETED");
    }

    private void ensureSystemReport(Hospital hospital, Branch branch, String reportName, String reportType,
                                    String reportFormat, LocalDateTime generatedAt, String generatedBy,
                                    String reportUrl, String reportParameters, String status) {
        boolean exists = systemReportRepository.findByHospitalIdAndBranchId(hospital.getId(), branch.getId())
                .stream()
                .anyMatch(report -> reportName.equalsIgnoreCase(report.getReportName()));
        if (exists) {
            return;
        }

        SystemReport report = new SystemReport();
        report.setHospitalId(hospital.getId());
        report.setBranchId(branch.getId());
        report.setReportName(reportName);
        report.setReportType(reportType);
        report.setReportFormat(reportFormat);
        report.setGeneratedAt(generatedAt);
        report.setGeneratedBy(generatedBy);
        report.setReportUrl(reportUrl);
        report.setReportParameters(reportParameters);
        report.setStatus(status);
        systemReportRepository.save(report);
    }

    private void seedCompliance(Hospital hospital, Branch branch) {
        ensureAuditLog(hospital, branch, "superadmin", "LOGIN", "AUTH",
                "Seeded login audit event for admin user", LocalDateTime.now().minusHours(8), "127.0.0.1");
        ensureAuditLog(hospital, branch, "dr.iyer", "READ_EMR", "CLINICAL",
                "Viewed EMR for UHID-OPD-1001", LocalDateTime.now().minusHours(3), "127.0.0.1");
        ensureAuditLog(hospital, branch, "reception.demo", "CREATE_VISITOR_LOG", "RECEPTION",
                "Created visitor log for Suresh Kumar", LocalDateTime.now().minusHours(2), "127.0.0.1");
    }

    private void ensureAuditLog(Hospital hospital, Branch branch, String username, String action, String module,
                                String details, LocalDateTime timestamp, String ipAddress) {
        boolean exists = auditLogRepository.findAll().stream()
                .filter(log -> hospital.getId().equals(log.getHospitalId()) && branch.getId().equals(log.getBranchId()))
                .anyMatch(log -> username.equalsIgnoreCase(log.getUsername())
                        && action.equalsIgnoreCase(log.getAction())
                        && module.equalsIgnoreCase(log.getModule()));
        if (exists) {
            return;
        }

        AuditLog auditLog = new AuditLog();
        auditLog.setHospitalId(hospital.getId());
        auditLog.setBranchId(branch.getId());
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setModule(module);
        auditLog.setDetails(details);
        auditLog.setTimestamp(timestamp);
        auditLog.setIpAddress(ipAddress);
        auditLogRepository.save(auditLog);
    }

    private void seedPublicPages(Hospital hospital, Branch branch) {
        ensurePublicPage(hospital, branch, "Home", "home", "Welcome to Samrat HMS", true);
        ensurePublicPage(hospital, branch, "About Us", "about", "Leading healthcare provider", true);
        ensurePublicPage(hospital, branch, "Services", "services", "Our clinical services", true);
    }

    private void ensurePublicPage(Hospital hospital, Branch branch, String title, String slug, String content, boolean published) {
        if (publicPageRepository.findBySlug(slug).isPresent()) return;
        PublicPage page = new PublicPage();
        page.setHospitalId(hospital.getId());
        page.setBranchId(branch.getId());
        page.setTitle(title);
        page.setSlug(slug);
        page.setContent(content);
        page.setPublished(published);
        publicPageRepository.save(page);
    }

    private void seedTelemedicine(Hospital hospital, Branch branch, Patient patient, Doctor doctor) {
        ensureTelemedicineConsultation(hospital, branch, patient, doctor, 1L, LocalDateTime.now().plusHours(2),
                "https://zoom.us/j/123456789", TelemedicineConsultation.ConsultationStatus.SCHEDULED, "Follow-up for hypertension");
    }

    private void ensureTelemedicineConsultation(Hospital hospital, Branch branch, Patient patient, Doctor doctor,
                                               Long appointmentId, LocalDateTime startTime, String meetingLink, TelemedicineConsultation.ConsultationStatus status, String notes) {
        if (telemedicineConsultationRepository.findByAppointmentId(appointmentId).isPresent()) return;
        TelemedicineConsultation consultation = new TelemedicineConsultation();
        consultation.setHospitalId(hospital.getId());
        consultation.setBranchId(branch.getId());
        consultation.setPatientUhid(patient.getUhid());
        consultation.setDoctorId(doctor.getId());
        consultation.setAppointmentId(appointmentId);
        consultation.setStartTime(startTime);
        consultation.setEndTime(startTime.plusMinutes(30));
        consultation.setMeetingUrl(meetingLink);
        consultation.setStatus(status);
        telemedicineConsultationRepository.save(consultation);
    }

    private void seedDiagnosticsExtra(Hospital hospital, Branch branch, Patient p1, Patient p2, Doctor d1, Doctor d2, Department lab) {
        ensureBloodDonation(hospital, branch, p1, "A+", 350.0, LocalDateTime.now().minusDays(2), BloodDonation.BloodStatus.AVAILABLE, "BAG-12345");

        LabOrder order = labOrderRepository.findAll().stream().findFirst().orElse(null);
        if (order != null) {
            ensureLabResult(hospital, branch, order, "Hemoglobin", "14.2", "g/dL", "13.0-17.0", "Normal");
        }
    }

    private void ensureBloodDonation(Hospital hospital, Branch branch, Patient donor, String bloodGroup, Double volume,
                                    LocalDateTime donationDate, BloodDonation.BloodStatus status, String bagId) {
        if (bloodDonationRepository.findByBagId(bagId).isPresent()) return;
        BloodDonation donation = new BloodDonation();
        donation.setHospitalId(hospital.getId());
        donation.setBranchId(branch.getId());
        donation.setDonor(donor);
        donation.setBloodGroup(bloodGroup);
        donation.setVolumeInMl(volume);
        donation.setDonationDate(donationDate);
        donation.setExpiryDate(donationDate.plusDays(35));
        donation.setBagId(bagId);
        donation.setStatus(status);
        bloodDonationRepository.save(donation);
    }

    private void ensureLabResult(Hospital hospital, Branch branch, LabOrder order, String parameter, String value,
                                String unit, String referenceRange, String flag) {
        boolean exists = labResultRepository.findAll().stream()
                .filter(r -> r.getLabOrder().getId().equals(order.getId()))
                .anyMatch(r -> parameter.equalsIgnoreCase(r.getParameterName()));
        if (exists) return;
        LabResult result = new LabResult();
        result.setHospitalId(hospital.getId());
        result.setBranchId(branch.getId());
        result.setLabOrder(order);
        result.setParameterName(parameter);
        result.setValue(value);
        result.setUnit(unit);
        result.setReferenceRange(referenceRange);
        result.setFlag(flag);
        labResultRepository.save(result);
    }

    private void seedInventoryExtra(Hospital hospital, Branch branch, Department med, Department card, User admin) {
        ensureHospitalAsset(hospital, branch, "Ventilator V1", "VENT-001", "Drager", "V-500", HospitalAsset.AssetStatus.IN_USE);
        ensureCSSDCycle(hospital, branch, "OT Set A", "STEAM", LocalDateTime.now().minusHours(4), CSSDCycle.CSSDStatus.COMPLETED, "CYC-12345");

        PurchaseOrder po = ensurePurchaseOrder(hospital, branch, "PO-2026-001", "MedLife Supplies", LocalDate.now(), 50000.0, PurchaseOrder.POStatus.APPROVED);
        ensurePurchaseOrderItem(po, "Gloves Box", 100.0, 50.0, 5000.0);

        ensureStoreItem(hospital, branch, "Syringe 5ml", "STR-001", "Disposable", 500, 100, "Shelf A-1");
    }

    private void ensureHospitalAsset(Hospital hospital, Branch branch, String name, String code, String manufacturer,
                                    String model, HospitalAsset.AssetStatus status) {
        if (hospitalAssetRepository.findByAssetTag(code).isPresent()) return;
        HospitalAsset asset = new HospitalAsset();
        asset.setHospitalId(hospital.getId());
        asset.setBranchId(branch.getId());
        asset.setAssetName(name);
        asset.setAssetTag(code);
        asset.setCategory("MEDICAL_EQUIPMENT");
        asset.setManufacturer(manufacturer);
        asset.setModel(model);
        asset.setPurchaseDate(LocalDate.now().minusYears(1));
        asset.setPurchaseValue(50000.0);
        asset.setStatus(status);
        hospitalAssetRepository.save(asset);
    }

    private void ensureCSSDCycle(Hospital hospital, Branch branch, String loadName, String method, LocalDateTime startTime, CSSDCycle.CSSDStatus status, String cycleNumber) {
        if (cssdCycleRepository.findByCycleNumber(cycleNumber).isPresent()) return;
        CSSDCycle cycle = new CSSDCycle();
        cycle.setHospitalId(hospital.getId());
        cycle.setBranchId(branch.getId());
        cycle.setLoadName(loadName);
        cycle.setSterilizationMethod(method);
        cycle.setStartTime(startTime);
        cycle.setEndTime(startTime.plusHours(2));
        cycle.setTemperature(121.0);
        cycle.setPressure(15.0);
        cycle.setCycleNumber(cycleNumber);
        cycle.setStatus(status);
        cssdCycleRepository.save(cycle);
    }

    private PurchaseOrder ensurePurchaseOrder(Hospital hospital, Branch branch, String poNumber, String vendor,
                                             LocalDate poDate, Double totalAmount, PurchaseOrder.POStatus status) {
        return purchaseOrderRepository.findByPoNumber(poNumber).orElseGet(() -> {
            PurchaseOrder po = new PurchaseOrder();
            po.setHospitalId(hospital.getId());
            po.setBranchId(branch.getId());
            po.setPoNumber(poNumber);
            po.setSupplierName(vendor);
            po.setOrderDate(poDate);
            po.setTotalAmount(totalAmount);
            po.setStatus(status);
            return purchaseOrderRepository.save(po);
        });
    }

    private void ensurePurchaseOrderItem(PurchaseOrder po, String itemName, Double unitPrice, Double quantity, Double total) {
        boolean exists = purchaseOrderItemRepository.findAll().stream()
                .filter(i -> i.getPurchaseOrder().getId().equals(po.getId()))
                .anyMatch(i -> itemName.equalsIgnoreCase(i.getItemName()));
        if (exists) return;
        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setPurchaseOrder(po);
        item.setItemName(itemName);
        item.setUnitPrice(unitPrice);
        item.setQuantity(quantity);
        item.setTotalAmount(total);
        purchaseOrderItemRepository.save(item);
    }

    private void ensureStoreItem(Hospital hospital, Branch branch, String name, String code, String category,
                                Integer stock, Integer reorderLevel, String location) {
        if (storeItemRepository.findByItemCode(code).isPresent()) return;
        StoreItem item = new StoreItem();
        item.setHospitalId(hospital.getId());
        item.setBranchId(branch.getId());
        item.setItemName(name);
        item.setItemCode(code);
        item.setCategory(category);
        item.setCurrentStock(stock);
        item.setReorderPoint(reorderLevel);
        item.setUnitOfMeasure("BOX");
        item.setAverageCost(50.0);
        item.setShelfLocation(location);
        storeItemRepository.save(item);
    }

    private void seedFinanceExtra(Hospital hospital, Branch branch) {
        ensureTPACompany(hospital, branch, "Star Health", "STAR-001", "9000000000", "claims@starhealth.com", true);
    }

    private void ensureTPACompany(Hospital hospital, Branch branch, String name, String code, String phone, String email, boolean active) {
        if (tpaCompanyRepository.findByCode(code).isPresent()) return;
        TPACompany tpa = new TPACompany();
        tpa.setHospitalId(hospital.getId());
        tpa.setBranchId(branch.getId());
        tpa.setName(name);
        tpa.setCode(code);
        tpa.setPhone(phone);
        tpa.setEmail(email);
        tpa.setActive(active);
        tpaCompanyRepository.save(tpa);
    }

    private void seedSupportExtra(Hospital hospital, Branch branch, Admission admission) {
        ensureAmbulance(hospital, branch, "MH-01-AX-1234", "ALS", Ambulance.AmbulanceStatus.AVAILABLE, "9876543210");
        ensureDietPlan(hospital, branch, admission, "DIABETIC", "Oats", "Salad", "Soup", "Nuts", "No sugar");
    }

    private void ensureAmbulance(Hospital hospital, Branch branch, String number, String type, Ambulance.AmbulanceStatus status, String driverPhone) {
        if (ambulanceRepository.findByRegistrationNumber(number).isPresent()) return;
        Ambulance amb = new Ambulance();
        amb.setHospitalId(hospital.getId());
        amb.setBranchId(branch.getId());
        amb.setRegistrationNumber(number);
        amb.setModel("Force Traveller");
        amb.setType(type);
        amb.setStatus(status);
        amb.setDriverPhone(driverPhone);
        ambulanceRepository.save(amb);
    }

    private void ensureDietPlan(Hospital hospital, Branch branch, Admission admission, String type, String breakfast, String lunch, String dinner, String snacks, String notes) {
        if (dietPlanRepository.findByAdmissionId(admission.getId()).isPresent()) return;
        DietPlan diet = new DietPlan();
        diet.setHospitalId(hospital.getId());
        diet.setBranchId(branch.getId());
        diet.setAdmission(admission);
        diet.setDietType(type);
        diet.setBreakfast(breakfast);
        diet.setLunch(lunch);
        diet.setDinner(dinner);
        diet.setSnacks(snacks);
        diet.setNotes(notes);
        dietPlanRepository.save(diet);
    }

    private void seedAppointmentExtra(Hospital hospital, Branch branch, Doctor d1, Doctor d2) {
        ensureDoctorSchedule(hospital, branch, d1, "MONDAY", LocalTime.of(9, 0), LocalTime.of(13, 0), 15);
        ensureDoctorSchedule(hospital, branch, d2, "TUESDAY", LocalTime.of(10, 0), LocalTime.of(14, 0), 20);
    }

    private void ensureDoctorSchedule(Hospital hospital, Branch branch, Doctor doctor, String day, LocalTime start, LocalTime end, Integer avgTime) {
        if (doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctor.getId(), day).isPresent()) return;
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setHospitalId(hospital.getId());
        schedule.setBranchId(branch.getId());
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(day);
        schedule.setStartTime(start);
        schedule.setEndTime(end);
        schedule.setAvgConsultationTime(avgTime);
        schedule.setMaxPatients(20);
        schedule.setActive(true);
        doctorScheduleRepository.save(schedule);
    }
}
