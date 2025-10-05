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

# PostgreSQL – Fixing Permission Denied for Schema Public

This guide explains how to fix the common PostgreSQL error:

```
ERROR: permission denied for schema public
```

which occurs when creating tables using a non-superuser (e.g., `srimansagar`).

---

## Connecting to PostgreSQL (SQL Shell)

1. Open **SQL Shell (psql)**

```
Server [localhost]:
Database [postgres]:
Port [5432]:
Username [postgres]: srimansagar
Password for user srimansagar:
```

---

## Switching Database

```sql
postgres=> \c studentdb;
```

Output:

```
You are now connected to database "studentdb" as user "srimansagar".
```

---

## Attempt to Create Table

```sql
studentdb=> CREATE TABLE faculty(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    department VARCHAR(100) NOT NULL
);
```

If you get:

```
ERROR: permission denied for schema public
```

---

## 🧠 Root Cause

* The **database `studentdb`** was created by the **postgres superuser**.
* The user **srimansagar** is connected but does **not have permission** to create objects in the **public schema**.

By default, new users do **not** get `CREATE` privileges on schemas owned by another user.

---

## ✅ Fix: Grant Permissions

### 1. Switch to Superuser (postgres)

```sql
studentdb=# \c studentdb postgres
Password for user postgres:
```

### 2. Grant Usage + Create Privileges

```sql
studentdb=# GRANT USAGE, CREATE ON SCHEMA public TO srimansagar;
GRANT
```

### 3. (Optional but Recommended) Change Schema Ownership

Make the schema owned by `srimansagar`:

```sql
studentdb=# ALTER SCHEMA public OWNER TO srimansagar;
ALTER SCHEMA
```

---

## 🔁 Reconnect as Application User

```sql
studentdb=# \c studentdb srimansagar;
Password for user srimansagar:
You are now connected to database "studentdb" as user "srimansagar".
```

---

## ✅ Create Table Again

```sql
studentdb=# CREATE TABLE faculty(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    department VARCHAR(100) NOT NULL
);
CREATE TABLE
```

🎉 Success! Your user now has the proper privileges to create tables within the `studentdb` public schema.

---

## 🧩 Summary

| Step | Action                               | Purpose                       |
| ---- | ------------------------------------ | ----------------------------- |
| 1    | Switch to postgres                   | Gain superuser access         |
| 2    | Grant USAGE, CREATE on schema public | Allow table creation          |
| 3    | (Optional) Change ownership          | Make app user owner of schema |
| 4    | Reconnect as srimansagar             | Test privileges               |
| 5    | Create table                         | Verify fix                    |

✅ Always create and manage database objects using the **application user** (e.g., `srimansagar`) to prevent permission issues in future migrations or automation scripts.

# Spring Boot – Using `/api` Endpoints and ResponseEntity

This guide explains how to prefix your endpoints with `/api` and properly use `ResponseEntity` for clean, RESTful responses in your Spring Boot application.

---

## 1. Adding `/api` to Endpoints

By default, your controller endpoints might look like `/faculty` or `/hello-faculty`. To organize them under a common prefix (a best practice for versioned or modular APIs), use `@RequestMapping` at the class level:

```java
@RestController
@RequestMapping("/api")
public class FacultyController {
}
```

✅ Now your endpoints become:

* `/api/faculty`
* `/api/hello-faculty`
* `/api/faculty/{id}`

This keeps your API routes consistent and easier to maintain.

---

## 2. What is `ResponseEntity`?

`ResponseEntity<T>` is a Spring class that represents a **full HTTP response** — including:

* The **HTTP status code**
* The **response body** (e.g., a Faculty object)
* Any **HTTP headers**

Without `ResponseEntity`, you might write:

```java
@PostMapping("/faculty")
public Faculty createFaculty(@RequestBody Faculty faculty) {
    return facultyService.saveFaculty(faculty);
}
```

This works but always returns **HTTP 200 OK**, even when creating a new record — which should ideally return **HTTP 201 Created**.

---

## 3. Using `ResponseEntity` (Best Practice)

### ✅ Create (POST)

```java
@PostMapping("/faculty")
public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
    Faculty savedFaculty = facultyService.saveFaculty(faculty);
    return ResponseEntity
            .status(201) // or HttpStatus.CREATED
            .body(savedFaculty);
}
```

### ✅ Update (PUT)

```java
@PutMapping("/faculty/{id}")
public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty facultyDetails) {
    Faculty updatedFaculty = facultyService.updateFaculty(id, facultyDetails);
    return ResponseEntity.ok(updatedFaculty); // HTTP 200
}
```

---

## 4. Advantages of `ResponseEntity`

* **Proper HTTP Status:** Return 201 for new records, 200 for reads, 204 for deletes.
* **Custom Headers:** Add `Location` or other metadata easily.
* **Consistency:** Each response clearly communicates success/failure.
* **Maintainability:** Easier to extend or wrap responses later.

---

## 5. Adding a `Location` Header for Created Resources

When creating new resources, REST best practices recommend returning a **Location** header with the new resource’s URI.

```java
@PostMapping("/faculty")
public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
    Faculty savedFaculty = facultyService.saveFaculty(faculty);

    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedFaculty.getId())
            .toUri();

    return ResponseEntity
            .created(location)   // sets HTTP 201 + Location header
            .body(savedFaculty);
}
```

---

## 6. Summary of Recommended `ResponseEntity` Usage

| Situation     | HTTP Method | Recommended ResponseEntity                   | HTTP Status    |
| ------------- | ----------- | -------------------------------------------- | -------------- |
| **Create**    | POST        | `ResponseEntity.created(location).body(obj)` | 201 Created    |
| **Read**      | GET         | `ResponseEntity.ok(obj)`                     | 200 OK         |
| **Update**    | PUT         | `ResponseEntity.ok(obj)`                     | 200 OK         |
| **Delete**    | DELETE      | `ResponseEntity.noContent().build()`         | 204 No Content |
| **Not Found** | Any         | `throw new ResourceNotFoundException()`      | 404 Not Found  |

---

✅ Using `ResponseEntity` makes your Spring Boot APIs **cleaner, more RESTful, and easier to maintain**.
