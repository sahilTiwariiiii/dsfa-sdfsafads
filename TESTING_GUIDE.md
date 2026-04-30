# Samrat HMS - Enterprise API & End-to-End Testing Guide

This guide is designed for developers and QA engineers to test the **Samrat Hospital Management System (HMS)** with accuracy and speed. It covers internal logic, multi-tenant isolation, and detailed API workflows for OPD and IPD modules.

---

## � 1. Authentication & Security Context

Every API request requires a valid JWT token and a Hospital/Branch context.

1. **Login to get Token:**
   - **Endpoint:** `POST /api/v1/auth/login`
   - **Body:**
     ```json
     {
       "username": "admin",
       "password": "password"
     }
     ```
2. **Authorize Swagger:**
   - Click the **"Authorize"** button in Swagger and paste the `accessToken`.
   - The system automatically resolves your `HospitalId` and `BranchId` from this token using `TenantContext`.

---

## 🏥 2. OPD (Outpatient) Workflows: Two Distinct Approaches

The system provides two ways to handle OPD visits depending on the hospital's operational style.

### Approach A: The "Fast-Track" Direct OPD (Walk-in)
*Best for: Small clinics or emergency walk-ins where registration and consultation happen immediately.*

1. **Unified Registration & Visit:**
   - **Endpoint:** `POST /api/v1/patients/patient-register`
   - **Description:** This is the "Solution 1". It creates a patient record AND an OPD visit in a single atomic transaction.
   - **Body Example:**
     ```json
     {
       "patientName": "John Doe",
       "mobile": "9876543210",
       "gender": "MALE",
       "visitType": "OPD",
       "doctorId": 1,
       "departmentId": 5,
       "fee": 500.0,
       "slot": "MORNING"
     }
     ```
   - **Internal Logic:** The system checks if the mobile number exists. If yes, it reuses the patient; if no, it creates a new UHID. It then creates an `OPDVisit` with status `WAITING`.

### Approach B: The "Traditional" Appointment-Based OPD
*Best for: Large hospitals where patients book in advance and check-in upon arrival.*

1. **Step 1: Standalone Registration**
   - **Endpoint:** `POST /api/v1/patients/register`
   - **Purpose:** Only creates the patient profile.
   - **Reference:** [PatientController.java](file:///c%3A/Users/P%20cc/Desktop/devs/dsfa-sdfsafads/samrat/src/main/java/com/example/samrat/modules/patient/controller/PatientController.java)

2. **Step 2: Book Appointment**
   - **Endpoint:** `POST /api/v1/appointments`
   - **Params:** `patientId=X`, `doctorId=Y`, `date=2024-04-27`, `visitType=OPD`
   - **Reference:** [AppointmentController.java](file:///c%3A/Users/P%20cc/Desktop/devs/dsfa-sdfsafads/samrat/src/main/java/com/example/samrat/modules/appointment/controller/AppointmentController.java)

3. **Check-in (The "Bridge")**
   - **Endpoint:** `POST /api/v1/opd/check-in/{appointmentId}`
   - **Description:** This is the "Solution 2". It converts a scheduled appointment into an active OPD visit.
   - **Internal Logic:** Updates appointment status to `CONFIRMED` and creates an `OPDVisit` record linked to that appointment.

---

## 📊 3. Vitals Tracking & Historical Trends

The system now supports dedicated vitals tracking, allowing multiple records per visit and a complete history of patient health trends.

### Recording Vitals (Legacy & Real-time)
1. **Option A: During OPD Flow**
   - **Endpoint:** `POST /api/v1/opd/vitals/{opdVisitId}`
   - **Behavior:** Updates the visit record AND automatically creates a entry in the historical `patient_vitals` table.

2. **Option B: Dedicated Vitals API (Recommended for IPD/Repeated checks)**
   - **Endpoint:** `POST /api/v1/clinical/vitals`
   - **Body:**
     ```json
     {
       "patientId": 1,
       "weight": 72.5,
       "height": 178.0,
       "bloodPressure": "120/80",
       "temperature": 98.6,
       "pulseRate": 72,
       "respiratoryRate": 18,
       "spo2": 98,
       "remark": "Morning checkup",
       "recordedBy": "Nurse Joy"
     }
     ```

### Viewing Vitals History
- **Endpoint:** `GET /api/v1/clinical/vitals/history/{patientId}`
- **Response:** Returns a list of all recorded vitals for the patient, ordered from newest to oldest.
- **Use Case:** Perfect for generating trend charts or checking if BP is improving over multiple days.

---

## 🩺 4. Post-Check-in Clinical Flow (Common for both A & B)

Once a patient is in the `OPD_VISIT` table, follow these steps:

1. **Record Vitals (Nurse Module):**
   - **Endpoint:** `POST /api/v1/opd/vitals/{opdVisitId}`
   - **Params:** `weight=70`, `height=175`, `bp=120/80`, `temp=98.6`, `pulse=72`, `resp=18`, `spo2=98`
   - **Reference:** [OPDController.java](file:///c%3A/Users/P%20cc/Desktop/devs/dsfa-sdfsafads/samrat/src/main/java/com/example/samrat/modules/opd/controller/OPDController.java)

2. **Doctor Consultation (EMR):**
   - **Endpoint:** `POST /api/v1/clinical/emr`
   - **Body:**
     ```json
     {
       "patientId": 1,
       "doctorId": 1,
       "chiefComplaint": "Persistent cough",
       "diagnosis": "Acute Bronchitis",
       "prescription": "Amoxicillin 500mg, 1-0-1 for 5 days"
     }
     ```

---

## 🛌 5. IPD (Inpatient) Lifecycle: From Admission to Discharge

IPD testing requires careful management of resources (Wards and Beds).

### Phase 1: Resource Setup (Required Once)
1. **Create Ward:** `POST /api/v1/ipd/wards?departmentId=5`
2. **Create Bed:** `POST /api/v1/ipd/beds?wardId={wardId}` (Set status to `AVAILABLE`)

### Phase 2: The Admission Flow
1. **Admit Patient:**
   - **Endpoint:** `POST /api/v1/ipd/admit`
   - **Query Params:** `patientId`, `doctorId`, `bedId`, `departmentId`
   - **Internal Logic:** The system marks the `Bed` as `OCCUPIED` and creates an `Admission` record.
2. **Verify Bed Status:**
   - `GET /api/v1/ipd/beds/{bedId}` should now show `status: OCCUPIED`.

### Phase 3: Daily Care & Discharge (Surgery Patient Example - 8 Days)
This covers a complete surgery admission workflow:

**Day 1-3: Pre-Operative Care**
1. **Daily Nurse Vitals:**
   - **Endpoint:** `POST /api/v1/clinical/vitals`
   - Record BP, Temp, SpO2, Pulse twice daily (morning & evening)
   - Link to admission via `admissionId` parameter
2. **Daily Doctor Check:**
   - Doctor adds notes via DoctorNote / EMR
3. **Nursing Notes:**
   - **Endpoint:** `POST /api/v1/clinical/nursing-note?admissionId={id}`

**Day 4: Operation Day**
1. **Book OT:**
   - **Endpoint:** `POST /api/v1/clinical/ot/book` (or similar)
   - Create OTBooking with surgeon, anesthetist, procedure name, schedule
2. **Update OT Status:**
   - Mark as IN_PROGRESS during surgery
   - Mark as COMPLETED after surgery

**Day 5-7: Post-Operative Care**
1. **Daily Medications/Injections:**
   - **Endpoint:** Doctor prescribes via ClinicalPrescription
   - Set frequency (1-0-1, 0-1-0, etc.)
   - Set route (Oral, IV, IM)
2. **Continue Daily Vitals & Nursing Notes**
   - Monitor recovery vitals more frequently if needed

**Day 8: Discharge**
1. **Discharge Summary:**
   - Create DischargeSummary with all medications, follow-up dates
2. **Final Discharge:**
   - **Endpoint:** `POST /api/v1/ipd/discharge/{admissionId}`
   - **Internal Logic:** Marks admission as `DISCHARGED` and automatically sets the bed back to `AVAILABLE`.

---

## 👨‍👩‍👧 6. Advanced Scenarios: Family Mapping & Duplicates

The system handles patient identity with intelligence to prevent duplicate records.

### Case 1: Returning Patient (Same Phone Number)
- **Scenario:** A patient who visited 6 months ago returns.
- **System Behavior:** When using `POST /api/v1/patients/patient-register` with the same mobile number, the system will **not** create a new patient. It will link the new `OPD_VISIT` to the existing `Patient` record.
- **Verification:** Check that the `UHID` remains the same in the response.

### Case 2: Family Mapping (Siblings/Spouse)
- **Scenario:** Registering a child whose father is already a patient.
- **Step 1:** Get the `patientId` of the father (Family Head).
- **Step 2:** Use `POST /api/v1/patients/register` with `familyHeadId` in the body.
- **Body Example:**
  ```json
  {
    "firstName": "Junior",
    "lastName": "Doe",
    "phoneNumber": "9876543210",
    "familyHeadId": 1
  }
  ```
- **Verification:** Call `GET /api/v1/patients/{id}/family` to see all members linked to the head.

---

## 🔍 7. Internal Workings & Data Integrity

### Multi-Tenancy (SaaS Isolation)
The system uses a **Shared Database, Shared Schema** approach with Discriminator Columns.
- Every table has `hospital_id` and `branch_id`.
- The `TenantFilter` intercepts every request, extracts the tenant info from JWT, and stores it in `TenantContext`.
- **Testing Tip:** Try to access a `patientId` belonging to Hospital A using a token from Hospital B. The system should return `404 Not Found` or `Access Denied`.

### The "Two Ways" OPD Solution Resolved
The user interface usually chooses the flow:
- **Reception Desk:** Uses **Approach A** (`patient-register`) for new walk-ins to save time.
- **Call Center/Online:** Uses **Approach B** (`appointments`) for future bookings.
Both flows converge at the `OPD_VISIT` entity, ensuring that the Doctor's Dashboard looks the same regardless of how the patient arrived.

---

## 🛠️ 8. Interactive Step-by-Step Test Scenarios

### Scenario 1: The "New Walk-in" (Zero to Hero)
1. **Action:** Call `POST /api/v1/patients/patient-register`
   - **Data:** Name: "Alice Smith", Mobile: "1122334455", VisitType: "OPD", DoctorId: 1
2. **Expectation:** Response contains a `UHID` and an `opdVisitId`.
3. **Action:** Call `POST /api/v1/opd/vitals/{opdVisitId}`
   - **Data:** BP: "110/70", Temp: "98.4"
4. **Action:** Call `POST /api/v1/clinical/emr`
   - **Data:** Diagnosis: "Seasonal Allergy"

### Scenario 2: The "Planned Appointment"
1. **Action:** Call `POST /api/v1/patients/register`
   - **Data:** Name: "Bob Brown", Mobile: "5544332211" (Save `patientId`)
2. **Action:** Call `POST /api/v1/appointments`
   - **Data:** PatientId: {Bob's ID}, DoctorId: 1, Date: {Today} (Save `appointmentId`)
3. **Action:** Call `POST /api/v1/opd/check-in/{appointmentId}`
4. **Expectation:** An `OPDVisit` is created. Appointment status changes to `CONFIRMED`.

### Scenario 3: Emergency to IPD
1. **Action:** Call `POST /api/v1/patients/patient-register`
   - **Data:** VisitType: "Emergency"
2. **Action:** Call `POST /api/v1/ipd/admit`
   - **Data:** PatientId, DoctorId, BedId (Make sure bed is `AVAILABLE` first)
3. **Action:** Verify bed status is now `OCCUPIED`.

---

## 🧪 9. Testing Checklist for Accuracy
- [ ] **Auth:** Is the JWT token present in the header?
- [ ] **Vitals:** Are vitals appearing in the EMR after being saved in OPD?
- [ ] **Bed Lock:** Does admitting a patient to Bed #101 prevent another admission to the same bed?
- [ ] **UHID Consistency:** Does registering the same mobile number twice reuse the existing UHID? (Check `PatientRegistrationRequest` logic).

---

## 🚀 10. Pro-Tip: Fast Testing with Seed Data
We have already run the migrations and seeded the database with dummy data.
- **Default Admin:** `admin` / `password`
- **Check Seeded Patients:** `GET /api/v1/patients`
- **Check Seeded Doctors:** `GET /api/v1/doctors` (if available)

Use these existing IDs to skip the registration steps and jump straight to **OPD Check-in** or **IPD Admission**.
