# Student API with Spring Boot and PostgreSQL

This guide will help you set up IntelliJ IDEA, Spring Boot, and PostgreSQL for a basic **Student CRUD API** project.

---

## IntelliJ IDEA Setup

1. Go to [IntelliJ IDEA Download Page](https://www.jetbrains.com/idea/download/?section=windows)
2. Select **Community Edition** and download the `.exe` installer.
3. Double-click the `.exe` file and click **Yes**.
4. Click **Next**.
5. (Optional) Choose a destination folder, then click **Next**.
6. Installation Options:
    - Create Desktop Shortcut: ✅
    - Update PATH variable (restart needed): ✅
    - Add "Open Folder as Project": ✅
    - Create Associations:
        - `.java`: ✅
        - `.gradle`: ✅
        - `.groovy`: ✅
        - `.pom`: ✅
7. Click **Next** → **Install**.
8. Select **Reboot now** (in newer versions, restart may not be required).

**First Launch:**
1. Open IntelliJ, accept the **User Agreement** → Continue.
2. Disable sending reports (optional).
3. Import Settings: **Skip Import**.
4. You’ll see **Welcome to IntelliJ IDEA**.

---

## Spring Boot Setup

1. Go to [Spring Initializr](https://start.spring.io/).
2. Configure project:
    - **Project**: Maven
    - **Language**: Java
    - **Spring Boot**: 3.5.6
    - **Project Metadata**:
        - Group: `com.example`
        - Artifact: `student-api`
        - Name: `student-api`
        - Description: `Student CRUD API with Spring Boot and PostgreSQL`
        - Package: `com.example.demo`
    - **Packaging**: JAR
    - **Java**: 21
    - **Dependencies**: Spring Web, Spring Data JPA, PostgreSQL Driver
3. Click **GENERATE** → download ZIP → extract → open in IntelliJ.

### Project Structure
```
controller/   → Handles HTTP requests (@RestController)
service/      → Business logic layer (interfaces + implementations)
repository/   → Extends JpaRepository for CRUD DB operations
entity/       → Student entity mapped to PostgreSQL table
dto/ (opt.)   → Decouple entity from API contract
exception/    → Centralized error handling
config/ (opt.)→ Security, Swagger, etc.
```

---

## PostgreSQL Installation

1. Download PostgreSQL installer from **EDB**.
2. Setup Wizard Steps:
    - Installation Directory: `C:\Program Files\PostgreSQL\18`
    - Components: PostgreSQL Server, pgAdmin 4, Stack Builder, CLI tools
    - Data Directory: `C:\Program Files\PostgreSQL\18\data`
    - Superuser Password: `Admin@123`
    - Port: `5432`
    - Leave Advanced Options default.
    - Confirm Installation Summary → **Next → Install**.

**First Run:**
- Open **SQL Shell (psql)**
- Enter details:
  ```
  Server: localhost
  Database: postgres
  Port: 5432
  Username: postgres
  Password: Admin@123
  ```
- Check version:
  ```sql
  SELECT version();
  ```

---

## Create Database & User

```sql
-- Create a database
CREATE DATABASE studentdb;

-- Create application user
CREATE USER srimansagar WITH PASSWORD 'Admin@123';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE studentdb TO srimansagar;

-- Connect to studentdb
\c studentdb

-- Default privileges
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO srimansagar;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO srimansagar;
```

---

## Spring Boot Configuration

Edit `src/main/resources/application.properties`:
```properties
spring.application.name=student-api
server.port=8080

# PostgreSQL DataSource
spring.datasource.url=jdbc:postgresql://localhost:5432/studentdb
spring.datasource.username=srimansagar
spring.datasource.password=Admin@123

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## Running the Application

1. In IntelliJ, open `StudentApiApplication.java` (`com.example.demo`).
2. Run the application.
3. Console output should show:
   ```
   HikariPool-1 - Start completed.
   Tomcat started on port(s): 8080 …
   ```
4. Test endpoint in browser/Postman:
   ```
   http://localhost:8080/hello-student
   ```

✅ If you see **“Student API working fine!”**, setup is successful! 🚀

## Application Flow:
### End-to-End Flow
1. User sends GET request → http://localhost:8080/hello-student.
2. Spring routes it to StudentController.helloStudent().
3. Controller delegates to StudentServiceImpl.getHelloMessage().
4. Service asks StudentRepository.getMessage().
5. Repository returns "Student API working fine!".
6. Response goes back → Browser/Postman shows: "Student API working fine!"

### ✅ This is textbook Layered Architecture:
1. Controller = Request handling (Web Layer).
2. Service = Business logic (Service Layer).
3. Repository = Database access (Data Layer).
4. Entity = Data model (Persistence Layer).
5. Exception = Error handling.

## Each File Type Role:
### Controller = Handles HTTP requests & responses only. Doesn’t contain business logic:
1. **@RestController:** Marks this class as a REST API controller → methods return JSON/text directly as HTTP responses.
2. **Dependency injection:**
```
// Constructor Injection
public  StudentController(StudentService studentService) {
this.studentService = studentService;
}
```

This is called constructor injection → safe, testable, and recommended.
3. **@GetMapping("/hello-student"):**
    - When you hit http://localhost:8080/hello-student, this method runs.
    - It delegates the actual logic to the service layer (studentService.getHelloMessage()).

### Entity = data structure that represents a DB table row:
1. fields like id, name, email, course with JPA annotations (@Entity, @Id, @GeneratedValue).
2. Entities map to database tables in Spring Data JPA.

### Exception = Helps return clear error messages (e.g., HTTP 404):
1. Typically used when a student isn’t found in DB.
2. you’ll make it extend RuntimeException and use it in service/repository layer.

### Repository = communicates with the database:
1. **@Repository:** Marks it as a persistence/data layer class. Spring manages it as a bean.
2. it will extend JpaRepository<Student, Long>, giving you full CRUD methods (save, findById, findAll, deleteById).

### Service interface = Business logic contract:
1. Declares what services should provide (a contract).
2. **Why interface?** → Helps in loose coupling. You can swap implementations (mock service for testing, real one for prod).
3. Later: will grow with methods like createStudent, getStudentById, updateStudent, deleteStudent.

### Service implementation = Business logic + Orchestration (calls repositories, applies rules, processes data):
1. **@Service:** Marks this as a business/service layer bean.
2. Implements the StudentService interface.
3. Uses StudentRepository (injected via constructor) to fetch data.

**Delegation flow:**
**Controller → Service → Repository → return value.** 

**Example:**
1. **Controller**(StudentController): Controller required service.
2. **Service**(StudentService(interface) and StudentServiceImpl(Business logic and Orchestration)
3. **Repository**(StudentRepository): communicates with database.
4. **return value**(return messages)
