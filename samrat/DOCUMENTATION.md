# Samrat Enterprise HMS - Software Flow & Architecture Documentation

## 1. Overview
Samrat is a comprehensive Enterprise Hospital Management System (HMS) designed with a **multi-tenant architecture**. It supports multiple hospitals, each with multiple branches, providing isolation and hierarchical management of data.

---

## 2. Hierarchical Structure & Data Isolation
The system follows a strict hierarchy for data organization and security.

### **Hierarchy:**
1.  **Hospital (Tenant):** The top-level organization (e.g., "Apollo Hospitals"). All data belongs to a specific hospital.
2.  **Branch:** A physical location or unit of a hospital (e.g., "Apollo Bangalore", "Apollo Delhi").
3.  **Department:** A functional unit within a branch (e.g., "Cardiology", "Radiology", "Billing").
4.  **User/Staff:** Assigned to a specific Hospital and Branch.

### **Multi-Tenancy Implementation:**
The system uses a **ThreadLocal-based context** to track the current tenant during a request.
-   **Headers:** Every API request should include `X-Hospital-Id` and `X-Branch-Id`.
-   **TenantFilter:** Intercepts requests and populates `TenantContext`.
-   **BaseEntity:** Almost all entities extend `BaseEntity`, which automatically includes `hospital_id` and `branch_id` fields for data isolation.

---

## 3. Security & Permission Model (RBAC)
Samrat uses a robust Role-Based Access Control (RBAC) system.

### **Components:**
-   **Permission:** The smallest unit of access (e.g., `PATIENT_READ`, `BILLING_WRITE`, `INVENTORY_DELETE`).
-   **Role:** A collection of permissions (e.g., `DOCTOR` role might have `PATIENT_READ`, `EMR_WRITE`). Roles can be global or specific to a Hospital.
-   **User:** Can be assigned multiple roles.

### **Flow:**
1.  **Authentication:** Users log in with a username and password.
2.  **Authorization:** Spring Security checks the user's permissions (authorities) before allowing access to specific API endpoints using `@PreAuthorize`.

---

## 4. Module Descriptions

The software is divided into several specialized modules:

### **Core Modules:**
-   **Admin Module:** Manages the foundation—Hospitals, Branches, Departments, Users, Roles, and Permissions.
-   **Patient Module:** Handles patient registration, Unique Health ID (UHID) generation, and family mapping.
-   **Doctor Module:** Manages doctor profiles, specializations, and availability.

### **Clinical Modules:**
-   **OPD (Outpatient):** Manages outpatient visits and consultations.
-   **IPD (Inpatient):** Handles admissions, bed management, and ward assignments.
-   **EMR (Electronic Medical Records):** Centralized repository for all clinical data of a patient.
-   **OT (Operation Theatre):** Manages OT bookings and surgical schedules.
-   **Nursing:** Records nursing notes and daily patient care.

### **Support & Diagnostics:**
-   **Diagnostics:** Includes Laboratory, Radiology, and Blood Bank management.
-   **Pharmacy:** Manages medicine stocks, sales, and prescriptions.
-   **Inventory:** Tracks hospital assets, store items, and purchase orders.
-   **Billing:** Generates invoices, manages service charges, and handles payments.

### **Finance & Reporting:**
-   **Finance:** Tracks all financial transactions and TPA (Insurance) mappings.
-   **Reporting:** Generates system-wide reports for management and analysis.

---

## 5. Example Flow: Patient Journey

### **Step 1: Hospital Setup (One-time)**
An admin creates a **Hospital** (ID: 1) and adds two **Branches** (ID: 101 for Delhi, ID: 102 for Mumbai). They then create a **Department** "General Medicine" in Branch 101.

### **Step 2: Patient Registration**
A patient visits the Delhi Branch (ID: 101).
1.  The Receptionist (Role: RECEPTIONIST) registers the patient.
2.  The system generates a **UHID** and saves the patient record with `hospital_id=1` and `branch_id=101`.
3.  The request includes `X-Hospital-Id: 1` and `X-Branch-Id: 101`.

### **Step 3: Appointment & Consultation**
1.  An **Appointment** is booked for the patient with a Doctor.
2.  The Doctor logs in, sees the patient in their dashboard, and opens the **EMR**.
3.  The Doctor writes a **Nursing Note** or prescribes medicine. These records are linked to the patient's UHID and the current branch.

### **Step 4: Billing & Discharge**
1.  If the patient is admitted (IPD), a **Bed** is assigned.
2.  Upon completion of treatment, the **Billing Module** aggregates all service charges, pharmacy bills, and room rents into a single **Invoice**.
3.  The patient is discharged after payment, and a **Discharge Summary** is generated.

---

## 6. Technical Stack
-   **Backend:** Spring Boot 3.4.4, Java 21.
-   **Security:** Spring Security 6 (RBAC).
-   **Database:** MySQL with Hibernate/JPA.
-   **Context Management:** ThreadLocal for multi-tenancy.
-   **DTO Mapping:** ModelMapper for converting entities to response objects.
