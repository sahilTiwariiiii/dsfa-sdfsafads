# Enterprise Hospital Management System (HMS) - Backend Documentation

This document provides a comprehensive overview of the **Samrat HMS Backend**, a multi-hospital, multi-branch enterprise-level system built using **Spring Boot 3.4.4**, **Java 21**, and **SQL Server**.

---

## 🏗️ Core Architecture

### **Multi-Tenancy & Data Isolation**
- **Architecture**: Shared Database, Shared Schema with Discriminator columns (`hospital_id`, `branch_id`).
- **Implementation**: Uses `TenantContext` (ThreadLocal) and `TenantFilter` to automatically extract hospital and branch context from request headers (`X-Hospital-Id`, `X-Branch-Id`).
- **Base Entity**: All domain entities inherit from `BaseEntity`, which automatically manages tenant IDs and auditing fields (`createdAt`, `createdBy`, etc.).

### **Security & Access Control**
- **Authentication**: JWT (JSON Web Token) with standard Bearer token flow.
- **RBAC**: Role-Based Access Control implemented using Spring Security. Permissions are granular (e.g., `PATIENT_READ`, `APPOINTMENT_CREATE`).
- **Password Security**: BCrypt password encoding for all user credentials.

### **Production-Ready Features**
- **Global Exception Handling**: Unified error response format via `GlobalExceptionHandler`.
- **Validation**: Strict input validation using Jakarta Validation (`@NotBlank`, `@NotNull`, etc.).
- **Auditing**: Spring Data JPA Auditing enabled to track every database change.
- **DTO Pattern**: Clean separation between persistence models and API response models using `ModelMapper`.

---

## 🏥 Module Overview (27 Modules)

### **1. Access & Admin Domain**
- **Admin**: Manage Hospitals and Branches.
- **Departments**: Create and manage clinical/non-clinical departments within each branch.
- **Role & Permission (RBAC)**: Granular permissions for Super Admin, Admin, Doctor, Nurse, Receptionist, Pharmacist, and Lab Technician.
- **User Management**: Multi-hospital user accounts with hospital-level isolation.
- **Website Management**: Manage public-facing pages and content via `PublicPage` module.

### **2. Clinical Domain**
- **Patient Management**: Registration, UHID generation, and family mapping.
- **Doctor Management**: Specialization, availability, and branch assignments.
- **Nurse Management**: Shift management and department-wise availability.
- **Appointment & Queue**: Sequential token system for OPD visits.
- **EMR / Medical Records**: Comprehensive records including vitals (BP, Temp, SpO2), history, diagnosis, and prescriptions.
- **Telemedicine**: Video consultation support via meeting link generation.
- **IPD / OPD**: Complete outpatient and inpatient lifecycle management.
- **Nursing Management**: Nursing notes and patient monitoring.
- **OT & Emergency**: Surgery tracking and triage system.

### **3. Diagnostics Domain**
- **Laboratory (LIS)**: Detailed lab orders and parameter-wise results (Hemoglobin, RBC, etc.).
- **Radiology (RIS)**: X-Ray, CT, MRI order and reporting flow.

### **4. Support & Dashboards**
- **Reception / Front Desk**: Visitor logging (entry/exit tracking).
- **Patient Portal**: Patient view of records, appointments, and billing.
- **Doctor Dashboard**: Summary of today's appointments, admitted patients, and surgeries.
- **Integrations**: Mock services for SMS (notifications) and Payments (Stripe/Razorpay style).
- **Compliance**: Automated audit logging of sensitive actions (READ_EMR, LOGIN, etc.).

### **4. Inventory & Supply Chain Domain**
- **Pharmacy Management**: Medicine stock, batch tracking, expiry alerts, and reorder levels.
- **Store Management**: Medical supply and stationery inventory tracking with shelf locations.
- **Purchase Management**: Purchase Order (PO) lifecycle from draft to approval and receiving.
- **Asset Management**: Hospital equipment tracking, service dates, and maintenance scheduling.
- **CSSD Management**: Sterilization cycle tracking (Steam, ETO, Plasma) with result validation.

### **5. Finance Domain**
- **Billing & Invoicing**: Automated invoice generation for OPD fees and IPD services.
- **Insurance / TPA**: Management of insurance companies, TPA codes, and handling charges.
- **Accounts & Finance**: Double-entry financial transaction tracking for all hospital revenue and expenses.

### **6. Support & Reporting Domain**
- **Ambulance Management**: Real-time vehicle status (Available, Busy, Maintenance) and type tracking (ALS, BLS).
- **Diet & Kitchen**: Patient-specific diet plans (Diabetic, Liquid, Regular) integrated with IPD.
- **HR & Payroll**: Employee profiles, designations, department tracking, and salary details.
- **MIS & Reports**: Automated generation of system reports in PDF, Excel, and CSV formats.

---

## 🛠️ Tech Stack
- **Framework**: Spring Boot 3.4.4
- **Language**: Java 21
- **Database**: MS SQL Server (Dialect: `SQLServerDialect`)
- **Persistence**: Spring Data JPA (Hibernate)
- **Security**: Spring Security + JJWT
- **Mapping**: ModelMapper
- **Utilities**: Lombok, Jakarta Validation

---

## 🚀 API Request Flow
1. **Client** sends request with `Authorization` and `X-Hospital-Id`/`X-Branch-Id` headers.
2. **JwtAuthenticationFilter** validates user token and sets security context.
3. **TenantFilter** sets the hospital/branch context in `TenantContext`.
4. **Controller** validates DTOs and calls **Service**.
5. **Service** executes business logic and calls **Repository**.
6. **BaseEntity** ensures all DB queries are filtered by the current tenant context.
7. **GlobalExceptionHandler** catches any errors and returns a unified `BaseResponse`.

---
*Documentation generated for Samrat HMS Enterprise Backend - 2026-04-05*
i want to test my each and every module of my hms in such a way so that i can vreify all the things working correctly all modules first just tell me  all the modules which just number and also all the module wise testing like in first what all things we had to test like how should i test each and every module from ip to opd to urget patient and alll that stuff and also our super admin will be crated by our system migration only when we start oru server in that case all all the things should be done and verified correctly so that it will becoem the production ready 