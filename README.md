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

