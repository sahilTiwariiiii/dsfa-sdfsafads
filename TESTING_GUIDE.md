# Samrat HMS - API Testing & Flow Guide

This guide provides a comprehensive walkthrough for testing the Samrat Hospital Management System (HMS) using Swagger/OpenAPI. It covers the complete lifecycle of a patient from registration to discharge.

## 🚀 Getting Started with Testing

The easiest way to test the application is through the **Swagger UI**.
- **URL:** `http://localhost:8080/swagger-ui/index.html` (default)
- **API Spec:** `http://localhost:8080/v3/api-docs`

---

## 🛠️ Phase 1: Authentication & Setup

Before making any calls, you need to authenticate to get a JWT token.

1. **Register/Login:**
   - **Endpoint:** `POST /api/v1/auth/login`
   - **Request:**
     ```json
     {
       "username": "admin",
       "password": "password"
     }
     ```
   - **Action:** Copy the token from the response and use it in the "Authorize" button in Swagger.

---

## 🏥 Phase 2: Complete Patient Flow (The "Golden Path")

Follow these steps to test a full patient lifecycle:

### Step 1: Patient Registration
- **Endpoint:** `POST /api/v1/patients/register`
- **Purpose:** Creates a patient record and generates a Unique Health ID (UHID).
- **Test Data:** Provide name, age, gender, and phone number.
- **Reference:** [PatientController.java](file:///c%3A/Users/P%20cc/Desktop/devs/dsfa-sdfsafads/samrat/src/main/java/com/example/samrat/modules/patient/controller/PatientController.java)

### Step 2: Book an Appointment
- **Endpoint:** `POST /api/v1/appointments`
- **Purpose:** Schedule a visit with a specific doctor.
- **Test Data:** Use the `patientId` from Step 1 and a valid `doctorId`.
- **Reference:** [AppointmentController.java](file:///c%3A/Users/P%20cc/Desktop/devs/dsfa-sdfsafads/samrat/src/main/java/com/example/samrat/modules/appointment/controller/AppointmentController.java)

### Step 3: OPD Check-in
- **Endpoint:** `POST /api/v1/opd/check-in/{appointmentId}`
- **Purpose:** Converts an appointment into an active OPD visit.
- **Action:** This starts the OPD workflow.
- **Reference:** [OPDController.java](file:///c%3A/Users/P%20cc/Desktop/devs/dsfa-sdfsafads/samrat/src/main/java/com/example/samrat/modules/opd/controller/OPDController.java)

### Step 4: Record Vitals (Nursing)
- **Endpoint:** `POST /api/v1/opd/vitals/{opdVisitId}`
- **Purpose:** Record weight, height, BP, and temperature.
- **Test Data:** Use the `opdVisitId` from Step 3.

### Step 5: Clinical Consultation
- **Endpoint:** `POST /api/v1/clinical`
- **Purpose:** Doctor records diagnosis, symptoms, and prescriptions (EMR).
- **Reference:** [ClinicalController.java](file:///c%3A/Users/P%20cc/Desktop/devs/dsfa-sdfsafads/samrat/src/main/java/com/example/samrat/modules/clinical/controller/ClinicalController.java)

### Step 6: IPD Admission (If needed)
- **Endpoint:** `POST /api/v1/ipd/admissions`
- **Purpose:** Admit the patient to a ward if the condition is serious.
- **Test Data:** Requires `patientId`, `doctorId`, and an available `bedId`.
- **Reference:** [IPDController.java](file:///c%3A/Users/P%20cc/Desktop/devs/dsfa-sdfsafads/samrat/src/main/java/com/example/samrat/modules/ipd/controller/IPDController.java)

### Step 7: Billing & Discharge
- **Endpoint:** `POST /api/v1/billing/invoices`
- **Purpose:** Generate a final bill for all services rendered.
- **Endpoint:** `POST /api/v1/ipd/discharges`
- **Purpose:** Mark the patient as discharged and free the bed.

---

## 🔍 Understanding the OPD & IPD Flow

### 1. Outpatient (OPD) Flow
The OPD flow is designed for speed and efficiency:
- **Queue Management:** Appointments are converted to "Waiting" status upon check-in.
- **Workflow:** Registration -> Triage (Vitals) -> Consultation -> Billing/Pharmacy.
- **Best Practice:** The use of `TenantContext` ensures that patient visits are isolated by Hospital/Branch, preventing data leakage.

### 2. Inpatient (IPD) Flow
The IPD flow is more complex and involves resource management:
- **Bed Management:** Beds are tracked by status (AVAILABLE, OCCUPIED, CLEANING).
- **Admission:** Links a patient to a bed, a primary doctor, and a ward.
- **Continuous Care:** Nursing notes and clinical updates are recorded against the `AdmissionID`.
- **Best Practice:** The system uses `@Transactional` for admissions to ensure that if a bed assignment fails, the admission record is not created (maintaining data integrity).

---

## 🏨 Detailed Inpatient (IPD) API Flow (With Examples)

Use this section to test IPD end-to-end in Swagger without confusion.

### 0) Prerequisites (create once)
- Have a valid `patientId` (from `POST /api/v1/patients/register`).
- Have a valid `doctorId` (from doctor module).
- Authorize Swagger with JWT token.

### 1) Create/Check Ward
- **Create Ward (if not already present):** `POST /api/v1/ipd/wards?departmentId=5`
- **Body example:**
  ```json
  {
    "name": "General Ward A",
    "code": "GW-A",
    "floor": "2",
    "description": "General inpatient ward"
  }
  ```
- **List Wards:** `GET /api/v1/ipd/wards`
- Save `wardId` from response.

### 2) Create/Check Bed
- **Create Bed:** `POST /api/v1/ipd/beds?wardId={wardId}`
- **Body example:**
  ```json
  {
    "bedNumber": "A-201",
    "bedType": "GENERAL",
    "status": "AVAILABLE",
    "dailyRate": 1500
  }
  ```
- **Get Available Beds:** `GET /api/v1/ipd/beds/available?wardId={wardId}`
- Save available `bedId`.

### 3) Admit Patient (Core IPD Entry)
- **Endpoint:** `POST /api/v1/ipd/admit?patientId={patientId}&doctorId={doctorId}&bedId={bedId}&departmentId=5`
- **Body example:**
  ```json
  {
    "admissionReason": "High fever with dehydration",
    "provisionalDiagnosis": "Viral fever",
    "attendantName": "Rahul Sharma",
    "attendantContact": "9876543210",
    "notes": "Needs close monitoring for 24 hours"
  }
  ```
- Save `admissionId` from response.

### 4) Verify Admission + Bed Occupancy
- **Search admissions:** `GET /api/v1/ipd/admissions/search?patientId={patientId}`
- **Get admission by id:** `GET /api/v1/ipd/{admissionId}`
- **Expected:** admission is active and bed should be marked occupied.

### 5) IPD Clinical/Nursing Updates During Stay
- **Create nursing note:** `POST /api/v1/clinical/nursing-note?admissionId={admissionId}`
- **Body example:**
  ```json
  {
    "noteText": "Vitals stable. IV fluids started."
  }
  ```
- **Create doctor EMR record (optional during admission):** `POST /api/v1/clinical/emr?patientId={patientId}&doctorId={doctorId}&departmentId=5`
- **Body example:**
  ```json
  {
    "chiefComplaint": "Weakness and fever",
    "bloodPressure": "118/76",
    "bodyTemperature": 99.1,
    "diagnosis": "Viral fever",
    "prescription": "Paracetamol and hydration"
  }
  ```

### 6) Bed Transfer (if needed)
- **Endpoint:** `POST /api/v1/ipd/transfer-bed/{admissionId}?newBedId={newBedId}`
- **Expected:** old bed released, new bed occupied.

### 7) Generate Bill (common before discharge)
- Use billing module endpoints (invoice creation/line items) based on your setup:
  - e.g. `POST /api/v1/billing/invoices`
- Link invoice with patient/admission as per response model in Swagger.

### 8) Discharge Patient (IPD Exit)
- **Endpoint:** `POST /api/v1/ipd/discharge/{admissionId}`
- **Expected:** admission marked discharged and current bed released to available.

### 9) Post-Discharge Validation Checklist
- `GET /api/v1/ipd/{admissionId}` shows discharge status/time.
- `GET /api/v1/ipd/beds/available?wardId={wardId}` includes released bed.
- Billing/invoice status is finalized (if applicable).

### Common IPD Test Mistakes
- Admitting on a non-available bed -> admission fails.
- Missing tenant context/JWT -> authorization or tenant errors.
- Trying discharge twice -> should return error or no-op based on service logic.
- Using wrong IDs across hospitals/branches -> records not found due to tenant isolation.

---

## ✅ Why this architecture follows Best Practices?

1. **Separation of Concerns:**
   - **Controllers:** Handle API requests and documentation (Swagger).
   - **Services:** Contain business logic (e.g., generating UHIDs, calculating fees).
   - **Repositories:** Manage database interactions.
   - *Why?* This makes the code modular and easy to test individually.

2. **Multi-Tenancy (SaaS Ready):**
   - Every entity extends `BaseEntity` which automatically injects `hospitalId` and `branchId` via `@PrePersist`.
   - *Why?* This is the "Gold Standard" for healthcare apps, allowing one instance to serve multiple hospitals securely.

3. **Audit Trails:**
   - `BaseEntity` tracks `createdAt`, `createdBy`, `updatedAt`, and `updatedBy`.
   - *Why?* Essential for medical compliance (HIPAA/GDPR) to know who accessed/modified patient data.

4. **Security:**
   - Method-level security using `@PreAuthorize("hasAuthority('...')")`.
   - *Why?* Ensures that a nurse can record vitals but only a doctor can write prescriptions.

---

## 🛑 How to ensure "100% working fine"?

1. **Compilations:** Always run `./mvnw compile` to catch syntax errors.
2. **Unit Tests:** Create tests in `src/test/java` to verify individual service logic.
3. **Integration Tests:** Use Swagger to run the "Golden Path" flow from registration to billing.
4. **Data Integrity:** Check the database after an admission to ensure the bed status changed to `OCCUPIED`.
