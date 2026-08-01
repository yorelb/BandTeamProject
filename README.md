# Band Management System (Team 28)

## Overview
The Band Management System is a full-stack enterprise application designed to streamline the administrative, logistical, and operational workflows of a musical organisation. Built with **Java** and **Spring Boot**, the platform provides a secure and centralised hub for managing members, performance schedules, sheet music libraries, and equipment loans.

## Key Features
*   **Role-Based Access Control (RBAC):** Secure authentication and authorization using **Spring Security**. Distinct access levels and dashboards for Directors, Committee Members, standard Members, and Child Members.
*   **Inventory & Loan Management:** Complete lifecycle tracking for band assets. Track instrument assignments, sheet music distribution, and miscellaneous items (e.g., uniforms) with a formal checkout/request system.
*   **Performance Scheduling:** Schedule upcoming gigs and rehearsals, and link specific sheet music and setlists to each performance.
*   **Member Hierarchies & Tracking:** Manage detailed member profiles, including participation tracking, skill levels, and parent/guardian linking for child members.
*   **Approval Workflows:** Dedicated dashboards for committee members to review, approve, or reject equipment loan requests and system changes.

## Technology Stack
*   **Backend:** Java, Spring Boot (Web, Security, Data JPA)
*   **Database:** Relational database integration via Hibernate/Spring Data JPA
*   **Frontend:** HTML5, CSS3, JavaScript, Thymeleaf (Server-side rendering)
*   **Testing:** JUnit, Mockito (Test-Driven Development approach)
*   **Build Tool:** Gradle

## 📂 Architecture & Project Structure
The application strictly adheres to the **Model-View-Controller (MVC)** architectural pattern to ensure separation of concerns and maintainability:
*   `controller/` - REST endpoints and web request handlers routing data to Thymeleaf views.
*   `service/` - Core business logic, transaction management, and data processing.
*   `repository/` - Data Access Layer extending `JpaRepository` for CRUD operations.
*   `model/` - JPA Entities representing database tables (`Member`, `Instrument`, `Loan`, `Performance`, etc.).
*   `dto/` - Data Transfer Objects for secure and decoupled data exchange between frontend forms and backend services.
*   `security/` - Custom user details services and authentication failure handlers.
