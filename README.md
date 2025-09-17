# Healthcare Appointment System
## Overview
The **Healthcare Appointment System** is a Spring Boot–based application designed to manage interactions between doctors and patients.  
It supports user registration, authentication, doctor scheduling, appointment booking, prescriptions, and medical records management.  
The system integrates both relational (MySQL) and NoSQL databases (MongoDB).

## Code Structure
```text
src/main/java/com/example/healthcareappointmentsystem
├── aop/                  # Logging (AOP annotations & aspects)   HealthcareLoggingAspect
├── collection/           # MongoDB collections (Prescriptions, Records)
├── controller/           # REST controllers (Admin, Doctor, Patient)
├── dto/                  # Data Transfer Objects
├── entity/               # JPA entities (Appointment, Doctor, Patient, etc.)
├── exception/            # Custom exception handling
├── repository/           # Spring Data repositories
├── security/             # SecurityConfig, Role definitions
├── service/              # Business logic layer
└── HealthcareApplication # Main Spring Boot application
```

## Architecture
- **Controller Layer** Containing REST APIs and endpoints
- **Service Layer** Handles all business logic
- **Repository Layer** Connected directly with the entities and collections (Spring Data JPA + MongoDB)
- **Security Layer** Basic authentication with role based access control
- **AOP** Logs operations using custom annotations

### Data Storage
- **Relational DB (MySQL):**
        Users (Doctors, Patients, Admins)
        Appointments
        Doctor Schedules
- **NoSQL DB (MongoDB):**
        Medical Records
        Prescriptions

## Authentication & Roles

### Authentication
The system uses **Spring Security with HTTP Basic Authentication**.
- Users log in by sending their **email** and **password** using the `Authorization` header.
- Passwords are securely stored using **BCrypt hashing**.
- Access is granted based on role.  

### Roles & Permissions
- **ADMIN** → Manage users, doctors, and system settings
- **DOCTOR** → Manage schedules, view patients, create prescriptions
- **PATIENT** → Book appointments, view medical records & prescriptions

### Access Control
| Endpoint Pattern          | Required Role |
|---------------------------|---------------|
| `/api/admin/**`           | `ADMIN`       |
| `/api/doctor/**`          | `DOCTOR`      |
| `/api/patient/**`         | `PATIENT`     |

## API Documentation
### Admin Endpoints
#### Doctor Management

| Method | Endpoint                   | Description          | Parameters                  |
|--------|----------------------------|--------------------|----------------------------|
| GET    | /api/admin/doctors          | Get all doctors     | -                          |
| GET    | /api/admin/doctors/{id}     | Get doctor by ID    | id (Long)                  |
| POST   | /api/admin/doctors          | Create new doctor   | CreateDoctorRequest        |
| PUT    | /api/admin/doctors/{id}     | Update doctor       | id (Long), UpdateDoctorRequest |
| DELETE | /api/admin/doctors/{id}     | Delete doctor (soft delete) | id (Long)            |

#### Patient Management

| Method | Endpoint                   | Description          | Parameters                  |
|--------|----------------------------|--------------------|----------------------------|
| GET    | /api/admin/patients         | Get all patients    | -                          |
| GET    | /api/admin/patients/{id}    | Get patient by ID   | id (Long)                  |
| POST   | /api/admin/patients         | Create new patient  | CreatePatientRequest       |


### Doctor Endpoints
#### Appointment Management

| Method | Endpoint                                    | Description                        | Parameters |
|--------|--------------------------------------------|-----------------------------------|------------|
| GET    | /api/doctor/appointments                   | Get doctor's appointments          | -          |
| PATCH  | /api/doctor/appointments/{id}/complete     | Mark appointment as completed      | id (Long)  |

#### Prescription Management

| Method | Endpoint                           | Description                  | Parameters            |
|--------|-----------------------------------|------------------------------|----------------------|
| POST   | /api/doctor/prescriptions         | Create prescription          | CreatePrescriptionRequest |
| GET    | /api/doctor/prescriptions         | Get doctor's prescriptions   | -                    |

#### Schedule Management

| Method | Endpoint                   | Description                  | Parameters     |
|--------|----------------------------|------------------------------|----------------|
| POST   | /api/doctor/schedule       | Add availability schedule    | ScheduleRequest |
| GET    | /api/doctor/schedule       | Get doctor's schedule        | -              |


###  Patient Endpoints

#### Doctor Search

| Method | Endpoint                                      | Description                  | Parameters    |
|--------|-----------------------------------------------|------------------------------|---------------|
| GET    | /api/patient/doctors/specialty/{specialty}   | Get doctors by specialty     | specialty (String) |
| GET    | /api/patient/doctors/specialties             | Get all specialties          | -             |

#### Appointment Management

| Method | Endpoint                                    | Description                  | Parameters             |
|--------|--------------------------------------------|------------------------------|-----------------------|
| POST   | /api/patient/appointments                  | Book new appointment         | BookAppointmentRequest |
| GET    | /api/patient/appointments                  | Get patient's appointments   | -                     |
| PATCH  | /api/patient/appointments/{id}/cancel      | Cancel appointment           | id (Long)             |

#### Medical Records

| Method | Endpoint                           | Description                  | Parameters |
|--------|-----------------------------------|------------------------------|------------|
| GET    | /api/patient/prescriptions         | Get patient's prescriptions  | -          |
| GET    | /api/patient/medical-record        | Get medical record           | -          |

#### Profile Management

| Method | Endpoint                    | Description                  | Parameters |
|--------|-----------------------------|------------------------------|------------|
| GET    | /api/patient/profile         | Get patient profile          | -          |
| PUT    | /api/patient/profile         | Update patient profile       | -          |

## Dependencies

The project relies on the following key dependencies:

### Core Frameworks
- **Spring Boot Starter Web** → REST API development
- **Spring Boot Starter Security** → Basic authentication & role-based access control
- **Spring Boot Starter Data JPA** → Interaction with MySQL using JPA
- **Spring Boot Starter Data MongoDB** → Interaction with MongoDB collections
- **Spring Boot Starter Validation** → Request validation annotations
- **Spring Boot Starter AOP** → Aspect-oriented programming for logging

### Database Drivers
- **MySQL Connector/J** → MySQL database connection

### Caching
- **Hibernate JCache** → JPA second-level caching
- **Ehcache 3** → Cache provider for Hibernate

### Utility
- **Lombok** → Reduce boilerplate code (getters, setters, constructors, etc.)

### Testing
- **Spring Boot Starter Test** → Unit and integration testing
- **Mockito Core & Mockito JUnit Jupiter** → Mocking for unit tests
- **Spring Security Test** → Security testing utilities

## Build & Run Instructions
-**Clone the repository** 
```text
git clone git@github.com:mai-zaied/healthcareManagementSystem.git
cd healthcare-appointment-system
```
-**Build the Docker image**
```text
docker compose build
```
-**Start the containers**
```text
docker compose up -d
```
-**Stop the containers**
```text
docker compose stop
```

