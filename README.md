# REST ACME Medical - Group Project

## Project Description
This project involves creating a RESTful application for ACME Medical Corp. using Jakarta EE. The system maps medical database schemas to Java POJOs, develops RESTful APIs for CRUD operations, implements role-based security, and validates APIs with JUnit tests. The project focuses on secure, scalable, and efficient resource management.

---

## Key Features
1. **JPA Entity Mapping**  
   - Mapped all database tables to Java POJOs using JPA annotations.  
   - Entities include: `Physician`, `Patient`, `Medicine`, `MedicalSchool`, `MedicalTraining`, `MedicalCertificate`, etc.

2. **RESTful APIs**  
   - Developed APIs to support CRUD operations for each resource.  
   - Secured REST endpoints with role-based access control using JEE security.

3. **Role-Based Security**  
   - Implemented custom authentication and authorization mechanisms with roles `ADMIN_ROLE` and `USER_ROLE`.

4. **JUnit Testing**  
   - Created a total of 50+ test cases to validate REST API functionality.  
   - Included both positive and negative test scenarios.  
   - Automated test execution using Maven Surefire plugin.

5. **Postman Collections**  
   - Provided a comprehensive Postman collection for API testing, including valid REST requests for all supported operations.

---

## Contributors

This project was a collaborative effort between the following members, with contributions distributed as follows:

| **Contributor**   | **GitHub ID**  | **Commits** | **Lines Added** | **Lines Removed** | **Contribution** |
|-------------------|----------------|-------------|-----------------|-------------------|------------------|
| Milo Tse          | MiloTse        | 22          | 7,876           | 4,899             | 30%              |
| Ryan Ren          | RyanRen2023    | 13          | 8,137           | 877               | 30%              |
| Shaoxian          | shaoxian423    | 6           | 2,953           | 563               | 20%              |
| Yaozhou Xie       | (GitHub ID TBD)| TBD         | TBD             | TBD               | 20%              |

---

---

## Technologies Used
- **Jakarta EE**: For RESTful API development and security.
- **JPA**: For mapping Java objects to database tables.
- **JUnit 5**: For automated testing of REST endpoints.
- **Maven Surefire**: For generating pass-fail reports for test cases.
- **Postman**: For manual API testing.
- **Swagger**: For documenting and testing RESTful APIs.

---

## Instructions to Run the Project
1. **Download and Import**  
   - Unzip the project folder and import it as an existing Maven project in Eclipse.

2. **Build the Project**  
   - Use Maven to clean, build, and test the project:
     ```bash
     mvn clean install test surefire-report:report site -DgenerateReports=true
     ```

3. **Run the Application**  
   - Deploy the application on Payara Server.

4. **Test the Application**  
   - Use Postman or Swagger to test REST endpoints.  
   - Execute JUnit tests to validate the system.

---

## Postman Collection
The updated Postman collection file is located in:  
`/postman/REST-ACMEMedical-Sample.postman_collection.json`

---

## JUnit Test Suite
- **Test Coverage**:
  - CRUD operations for all entities.
  - Role-based access control tests.
  - Negative test cases for invalid inputs or unauthorized actions.
- **Running Tests**:
  - Ensure the application is running on Payara Server before executing tests.

---

## Maven Surefire Report
The Maven Surefire report is generated at:  
`/target/site/surefire-report.html`

---

## Security Rules
- **Physicians**:
  - Only `ADMIN_ROLE` can view all physicians.
  - Both `ADMIN_ROLE` and `USER_ROLE` can view a specific physician (limited by ownership for `USER_ROLE`).
  - Only `ADMIN_ROLE` can add new physicians.
- **Medical Schools and Trainings**:
  - Any user can retrieve these resources.
  - Only `ADMIN_ROLE` can perform CRUD operations.
- **Medical Certificates**:
  - Only `USER_ROLE` can read their own certificates.
  - Only `ADMIN_ROLE` can manage certificates.
- **General Rules**:
  - Only `ADMIN_ROLE` can associate entities like `Medicine` or `Patient` with a `Physician`.
  - Only `ADMIN_ROLE` can delete any entity.

---

## Known Issues
- **LazyInitializationException**:
  Resolved using `LEFT JOIN FETCH` in named queries for `MedicalSchool` and `MedicalTraining`.

---

