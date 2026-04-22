# 🏥 Samrat HMS - Comprehensive Module Testing & Verification Guide

This document provides a detailed, step-by-step blueprint for testing and verifying every module of the Samrat Hospital Management System. Follow these steps to ensure the system is production-ready.

---

## 🛠️ Phase 0: System Initialization & Super Admin Verification

The system is designed to self-initialize on the first run. This is critical for production deployment.

### **1. Initial Startup Verification**
- **Action**: Start the Spring Boot server for the first time on a clean database.
- **Verification**: Check the console logs. You should see a block of text confirming the creation of the Super Admin user.
- **Details**:
    - **Hospital**: "Samrat General Hospital" created.
    - **Branch**: "Main Branch" created.
    - **Super Admin**: Username `superadmin`, Password `admin123`.
- **Database Check**:
    - Query `users` table: Verify one entry with `username = 'superadmin'`.
    - Query `roles` and `permissions` tables: Verify `SUPER_ADMIN` entries exist.

---

## 🏥 Phase 1: Clinical Workflow Testing (The Core)

### **Module 1: Patient Management (Registration)**
- **Scenario**: New patient arrives at the hospital.
- **Steps**:
    1. Open Patient Registration.
    2. Enter details (Name, Phone, Age, Gender).
    3. **Verification**: System must generate a Unique Health ID (UHID) automatically (e.g., `UHID-2026-0001`).
- **"If-Then" Scenario**:
    - **Case**: If a patient with the same phone number already exists.
    - **Action**: The system should prompt for "Family Mapping" or show existing records.
    - **Test**: Register a sibling with the same phone; verify they are linked under the same family head but have different UHIDs.

### **Module 2: Doctor Management & Scheduling**
- **Action**: Create a Doctor profile and set availability.
- **Verification**:
    - Assign the doctor to a specific Department and Branch.
    - Set schedule (e.g., Monday 10:00 AM - 02:00 PM).
    - **Test**: Try to book an appointment outside these hours; the system should block it.

### **Module 3: OPD (Outpatient Department)**
- **Flow**: Appointment → Check-in → Vitals → Consultation.
- **Steps**:
    1. **Book Appointment**: Select Patient + Doctor + Date.
    2. **Check-in**: Mark patient as "Arrived."
    3. **Vitals Recording**: Nurse enters BP, SpO2, Temperature, Weight.
    4. **Doctor Consultation**: Doctor opens the patient dashboard.
- **"If-Then" Scenario**:
    - **Case**: If the doctor prescribes a medicine.
    - **Action**: Check if the prescription is visible in the **Pharmacy Module** for the patient.
    - **Case**: If the doctor orders a Lab Test.
    - **Action**: Check if the order appears in the **Laboratory Module**.

### **Module 4: IPD (Inpatient Department) & Bed Management**
- **Flow**: Admission → Bed Allocation → Daily Care → Discharge.
- **Steps**:
    1. **Admission**: Doctor recommends admission from OPD.
    2. **Bed Allocation**: Select Ward (e.g., General, Semi-Private, ICU) and allocate an "Available" bed.
    3. **Verification**: Query the `beds` table; verify status is now `OCCUPIED`.
- **"If-Then" Scenarios**:
    - **Case 1: Ward Shifting**: If the patient needs to be shifted from General Ward to ICU.
        - **Action**: Perform "Bed Transfer."
        - **Verification**: Ensure the previous bed is marked "Available" and the new ICU bed is "Occupied." Verify the billing system calculates different rates for the ICU period.
    - **Case 2: Emergency IPD**: If a patient is admitted via Emergency.
        - **Action**: Skip the OPD queue; go directly to Bed Allocation from the ER dashboard.
    - **Case 3: Pending Dues at Discharge**: If the patient is being discharged but has pending bills.
        - **Action**: The system must block the "Final Discharge" button until the Finance Department clears the "No Dues" flag.
    - **Case 4: Medicine Return**: If a patient was prescribed 10 tablets but only used 5 before discharge.
        - **Action**: Perform "Pharmacy Return."
        - **Verification**: Verify that the medicine stock increases and the patient's bill is credited/reduced.

### **Module 5: Emergency & Urgent Care**
- **Flow**: Triage → Immediate Action.
- **Steps**:
    1. **Quick Register**: Register patient with minimal info (Name, Gender, Triage Category).
    2. **Triage Tagging**:
        - **Red**: Critical (Immediate attention).
        - **Yellow**: Urgent.
        - **Green**: Non-urgent.
- **"If-Then" Scenarios**:
    - **Case 1: Red Category**: If a "Red" patient arrives.
        - **Action**: System must allow bypassing the billing queue to start immediate treatment/OT booking.
    - **Case 2: MLC (Medico-Legal Case)**: If the emergency is an accident or crime.
        - **Action**: Mark the "MLC" checkbox during registration.
        - **Verification**: The system must prompt for Police Station details and generate a mandatory MLC report.
    - **Case 3: Death in ER**: If the patient is brought dead or expires during triage.
        - **Action**: Mark "Death" status.
        - **Verification**: System must stop all active orders and trigger the "Mortuary Management" or "Death Certificate" flow.

---

## 🧪 Phase 2: Diagnostic & Support Testing

### **Module 6: Laboratory (LIS)**
- **Steps**: Lab Technician receives sample → Enters results → Parameter-wise verification → Approves report.
- **Verification**: 
    - Check if specific parameters (e.g., HB, RBC) can be entered via `LabResult` entity.
    - Patient should receive an SMS/Notification, and the Doctor should see the report in the EMR.

### **Module 7: Nurse & Ward Management**
- **Action**: Assign Nurse to a shift and department.
- **Verification**:
    - Verify nurse can record vitals and nursing notes in `NursingNote`.
    - Check shift-wise availability listing in the dashboard.

### **Module 8: Reception & Visitor Management**
- **Action**: Log a visitor entry for a patient.
- **Verification**:
    - Ensure `entryTime` is auto-populated.
    - Test "Log Exit" and verify `exitTime` is set.
    - Check "Active Visitors" list to see who is currently in the hospital.

### **Module 9: Telemedicine & Remote Care**
- **Action**: Initiate a video consultation for a remote appointment.
- **Verification**:
    - Verify a unique meeting URL (e.g., Jitsi/Zoom link) is generated.
    - Check if status changes from `SCHEDULED` to `IN_PROGRESS`.

### **Module 10: Dashboards & Patient Portal**
- **Action**: Log in as a Patient/Doctor and view the dashboard.
- **Verification**:
    - **Doctor**: Verify counts for today's appointments and admitted patients are accurate.
    - **Patient**: Verify latest prescriptions and lab reports are visible in the portal.

### **Module 11: Compliance & Audit Logs**
- **Action**: Perform sensitive actions like "Delete Patient" or "View EMR."
- **Verification**:
    - Query `audit_logs` table; verify an entry exists with the username, action, and timestamp.
    - Ensure IP address and module name are correctly captured.

---

## 💰 Phase 3: Financial & Administrative Verification

### **Module 8: Billing & Invoicing**
- **Verification**:
    - **OPD Bill**: Includes Consultation Fee + Registration Fee + Lab Tests.
    - **IPD Bill**: Includes Bed Charges (per day) + Nursing Charges + Medicine + OT Charges.
- **Test**: Apply a discount or Insurance/TPA claim. Verify the "Net Payable" is calculated correctly.

### **Module 9: Multi-Tenancy (Hospital Isolation)**
- **Critical Test**:
    1. Log in as Admin of **Hospital A**.
    2. Try to search for a Patient registered in **Hospital B**.
    3. **Result**: System MUST return "Not Found." Data must be strictly isolated.

---

## 🚀 Production Readiness Checklist (27 Modules Summary)

| # | Module | Key Verification Point |
|---|---|---|
| 1 | Admin | Hospital/Branch hierarchy is correct. |
| 2 | Roles | `X-Hospital-Id` header is enforced in all APIs. |
| 3 | Users | Passwords are BCrypt hashed in DB. |
| 4 | Patient | UHID is unique across the entire system. |
| 5 | Doctor | Daily token sequence resets at midnight. |
| 6 | Appointment | No double-booking of the same time slot. |
| 7 | OPD | Vitals history is plotted on a graph correctly. |
| 8 | IPD | Bed charges stop exactly at the discharge time. |
| 9 | EMR | Clinical notes cannot be edited after "Finalize." |
| 10 | Nursing | 2-hourly vitals alerts are triggered. |
| 11 | OT | Surgeon and Anesthetist schedules don't overlap. |
| 12 | Emergency | Triage priority reflects in the doctor's queue. |
| 13 | Discharge | Summary includes all medicines and follow-up dates. |
| 14 | Lab | Result values outside "Normal Range" are highlighted Red. |
| 15 | Radiology | PACS/DICOM link is accessible from EMR. |
| 16 | Pharmacy | Stock is deducted automatically on billing. |
| 17 | Inventory | PO (Purchase Order) requires manager approval. |
| 18 | Blood Bank | Bag expiry triggers a system-wide alert. |
| 19 | Asset | Maintenance schedule alerts for ventilators/MRI. |
| 20 | CSSD | Sterilization batch ID is linked to OT instruments. |
| 21 | Billing | GST/Tax calculation matches local regulations. |
| 22 | Insurance | TPA approval amount is capped during billing. |
| 23 | Finance | Day-end cash report matches system total. |
| 24 | HR/Payroll | Salary calculation includes OT and Deductions. |
| 25 | Reports | PDF/Excel exports are formatted and readable. |
| 26 | Ambulance | Real-time GPS/Status (Available/Busy) works. |
| 27 | Diet | Patient diet plan excludes allergens listed in EMR. |

---

## 🏁 Final Step: The "Production Ready" Sign-off
Before going live:
1. **Stress Test**: Simulate 100 concurrent patient registrations.
2. **Security Scan**: Verify no API is accessible without a valid JWT token.
3. **Backup Test**: Perform a database backup and restore it to verify data integrity.
4. **Super Admin Access**: Change the default `admin123` password immediately after the first login.
