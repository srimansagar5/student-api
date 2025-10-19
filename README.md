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


PostgreSQL Commands & Troubleshooting for Student API
This document provides useful psql commands, explains common issues, and details how to fix database permission errors when working with Spring Boot + PostgreSQL.

Common psql Commands
\c                -- connect to database
\d                -- describe tables

\c studentdb;     -- switch to your DB
\d;               -- list all tables
\d students       -- show table schema
Connecting to PostgreSQL (SQL Shell)
When you start SQL Shell (psql), you’ll see prompts:

Server [localhost]:
Database [postgres]:
Port [5432]:
Username [postgres]: postgres
Password for user postgres: ******
✅ What to do:

Start SQL Shell (psql).

At prompts, hit Enter for defaults (unless changed):

Server [localhost]: Enter
Database [postgres]: Enter
Port [5432]: Enter
Username [postgres]: postgres (or your DB username)
Password: enter your PostgreSQL password
Now you’re connected to the default database.

Switching Database
To switch to your project DB:

\c studentdb;
Output:

You are now connected to database "studentdb" as user "postgres".
studentdb=#
Checking Tables
\d;          -- list all tables
\d students  -- describe students table
If you see:

ERROR: relation "students" does not exist
👉 It means the table hasn’t been created yet.

Fix: Create Table
If Hibernate didn’t auto-create the table, create manually:

CREATE TABLE students (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
email VARCHAR(150) NOT NULL
);
Verify:

\d students;
Permission Errors
If you see:

ERROR: permission denied for table students
👉 The user your Spring Boot app uses doesn’t have INSERT privileges.

Check application.properties
spring.datasource.username=postgres
spring.datasource.password=yourpassword
Grant Permissions
\c studentdb;
GRANT ALL PRIVILEGES ON TABLE students TO postgres;
GRANT USAGE, SELECT, UPDATE, INSERT, DELETE ON SEQUENCE students_id_seq TO postgres;
If your app uses a custom user (e.g. srimansagar):

GRANT ALL PRIVILEGES ON TABLE students TO srimansagar;
GRANT USAGE, SELECT, UPDATE, INSERT, DELETE ON SEQUENCE students_id_seq TO srimansagar;
Verify:

\z students
Root Cause: User Ownership
You created the table students as postgres superuser.
But Spring Boot app connects as srimansagar.
Default privileges only apply to future tables, not existing ones.
So srimansagar cannot insert into students.
Fix for Existing Tables
Exit SQL shell and reopen.
Grant privileges explicitly:
\c studentdb;

-- Grant rights on table
GRANT ALL PRIVILEGES ON TABLE students TO srimansagar;

-- Grant rights on sequence (for auto-increment ID)
GRANT ALL PRIVILEGES ON SEQUENCE students_id_seq TO srimansagar;
Verify:
\z students
Best Practice
Always connect as the application user (srimansagar) before creating tables.
This ensures the user owns the tables → no need for extra grants.
Test with Spring Boot
Restart your Spring Boot app and test API:

POST http://localhost:8080/students
{
"name": "Robert",
"email": "robert@example.com"
}
✅ If insert succeeds, database + permissions are correctly configured.

📘 Student API – CRUD Endpoints
🔹 Endpoints Overview
Health Check
Endpoint: /hello-student
HTTP Method: GET
Description: Health check / Welcome API
Sample Request Body: -
Sample Response:
"Student API working fine!"
Create Student
Endpoint: /students
HTTP Method: POST
Description: Create a new student
Sample Request Body:
{
"name": "John Doe",
"email": "john@example.com"
}
Sample Response:
{
"id": 1,
"name": "John Doe",
"email": "john@example.com"
}
Get All Students
Endpoint: /students
HTTP Method: GET
Description: Retrieve all students
Sample Request Body: -
Sample Response:
[
{ "id": 1, "name": "John Doe", "email": "john@example.com" }
]
Update Student by ID
Endpoint: /students/{id}
HTTP Method: PUT
Description: Update student by ID
Sample Request Body:
{
"name": "Updated Name",
"email": "updated@example.com"
}
Sample Response:
{
"id": 1,
"name": "Updated Name",
"email": "updated@example.com"
}
Delete Student by ID
Endpoint: /students/{id}
HTTP Method: DELETE
Description: Delete student by ID
Sample Request Body: -
Sample Response (Success):
204 No Content
Sample Response (Not Found):
{
"status": 404,
"error": "Not Found",
"message": "Student not found with id 99"
}
🔹 Example Workflows
✅ Create Student (POST)
POST /students
Content-Type: application/json

{
"name": "Alice",
"email": "alice@example.com"
}
Response:

{
"id": 2,
"name": "Alice",
"email": "alice@example.com"
}
✅ Get All Students (GET)
GET /students
Response:

[
{ "id": 1, "name": "John Doe", "email": "john@example.com" },
{ "id": 2, "name": "Alice", "email": "alice@example.com" }
]
✅ Update Student (PUT)
PUT /students/2
Content-Type: application/json

{
"name": "Alice Updated",
"email": "alice.updated@example.com"
}
Response:

{
"id": 2,
"name": "Alice Updated",
"email": "alice.updated@example.com"
}
✅ Delete Student (DELETE)
DELETE /students/2
Response (Success):

HTTP 204 No Content
Response (Not Found):

{
"timestamp": "2025-10-04T15:00:00",
"status": 404,
"error": "Not Found",
"message": "Student not found with id 99",
"path": "/students/99"
}
✅ This README.md serves as both API documentation and a Postman reference.

# PostgreSQL Commands & Troubleshooting for Student API

This document provides useful **psql commands**, explains common database issues, and details how to fix permission errors when working with **Spring Boot + PostgreSQL**.

---

## 🧩 Common psql Commands

```sql
\c                -- connect to database
\d                -- describe tables

\c studentdb;     -- switch to your DB
\d;               -- list all tables
\d students       -- show table schema
```

---

## ⚙️ Connecting to PostgreSQL (SQL Shell)

When you start **SQL Shell (psql)**, you’ll see prompts:

```
Server [localhost]:
Database [postgres]:
Port [5432]:
Username [postgres]: postgres
Password for user postgres: ******
```

✅ **What to do:**

1. Start SQL Shell (psql).
2. At prompts, hit **Enter** for defaults (unless changed):

    * Server [localhost]: Enter
    * Database [postgres]: Enter
    * Port [5432]: Enter
    * Username [postgres]: postgres (or your DB username)
    * Password: enter your PostgreSQL password

You’ll now be connected to the default database.

---

## 🔄 Switching Database

```sql
\c studentdb;
```

Output:

```
You are now connected to database "studentdb" as user "postgres".
studentdb=#
```

---

## 📋 Checking Tables

```sql
\d;          -- list all tables
\d students  -- describe students table
```

If you see:

```
ERROR: relation "students" does not exist
```

👉 It means the table hasn’t been created yet.

---

## 🧱 Fix: Create Table

If Hibernate didn’t auto-create the table, create it manually:

```sql
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL
);
```

Verify:

```sql
\d students;
```

---

## 🚫 Permission Errors

If you see:

```
ERROR: permission denied for table students
```

👉 The user your Spring Boot app uses doesn’t have INSERT privileges.

### Check `application.properties`

```properties
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

### Grant Permissions

```sql
\c studentdb;
GRANT ALL PRIVILEGES ON TABLE students TO postgres;
GRANT USAGE, SELECT, UPDATE, INSERT, DELETE ON SEQUENCE students_id_seq TO postgres;
```

If your app uses a custom user (e.g. `srimansagar`):

```sql
GRANT ALL PRIVILEGES ON TABLE students TO srimansagar;
GRANT USAGE, SELECT, UPDATE, INSERT, DELETE ON SEQUENCE students_id_seq TO srimansagar;
```

Verify:

```sql
\z students
```

---

## ⚠️ Root Cause: User Ownership

* You created the table `students` as **postgres superuser**.
* Spring Boot connects as **srimansagar**.
* Default privileges only apply to future tables, not existing ones.
* Therefore, `srimansagar` cannot insert into `students`.

---

## 🔧 Fix for Existing Tables

1. Exit SQL shell and reopen.
2. Grant privileges explicitly:

```sql
\c studentdb;

-- Grant rights on table
GRANT ALL PRIVILEGES ON TABLE students TO srimansagar;

-- Grant rights on the sequence (auto-increment ID)
GRANT ALL PRIVILEGES ON SEQUENCE students_id_seq TO srimansagar;
```

3. Verify:

```sql
\z students
```

---

## 💡 Best Practice

Always connect as the **application user (srimansagar)** before creating tables.
That way, the user automatically owns the tables → no manual grants required.

---

## 🧪 Test with Spring Boot

Restart your Spring Boot app and test API:

```http
POST http://localhost:8080/students
{
  "name": "Robert",
  "email": "robert@example.com"
}
```

✅ If insert succeeds, database + permissions are correctly configured.

---

# 📘 Student API – CRUD Endpoints

## 🔹 Endpoints Overview

### 🩺 Health Check

**Endpoint:** `/hello-student`
**Method:** `GET`
**Description:** Health check / Welcome API
**Response:**

```json
"Student API working fine!"
```

---

### ➕ Create Student

**Endpoint:** `/students`
**Method:** `POST`
**Description:** Create a new student
**Request Body:**

```json
{
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Response:**

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

---

### 📚 Get All Students

**Endpoint:** `/students`
**Method:** `GET`
**Description:** Retrieve all students
**Response:**

```json
[
  { "id": 1, "name": "John Doe", "email": "john@example.com" }
]
```

---

### ✏️ Update Student by ID

**Endpoint:** `/students/{id}`
**Method:** `PUT`
**Description:** Update student by ID
**Request Body:**

```json
{
  "name": "Updated Name",
  "email": "updated@example.com"
}
```

**Response:**

```json
{
  "id": 1,
  "name": "Updated Name",
  "email": "updated@example.com"
}
```

---

### ❌ Delete Student by ID

**Endpoint:** `/students/{id}`
**Method:** `DELETE`
**Description:** Delete student by ID
**Response (Success):**

```
204 No Content
```

**Response (Not Found):**

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with id 99"
}
```

---

## 🔹 Example Workflows

### ✅ Create Student (POST)

```http
POST /students
Content-Type: application/json

{
  "name": "Alice",
  "email": "alice@example.com"
}
```

**Response:**

```json
{
  "id": 2,
  "name": "Alice",
  "email": "alice@example.com"
}
```

---

### ✅ Get All Students (GET)

```http
GET /students
```

**Response:**

```json
[
  { "id": 1, "name": "John Doe", "email": "john@example.com" },
  { "id": 2, "name": "Alice", "email": "alice@example.com" }
]
```

---

### ✅ Update Student (PUT)

```http
PUT /students/2
Content-Type: application/json

{
  "name": "Alice Updated",
  "email": "alice.updated@example.com"
}
```

**Response:**

```json
{
  "id": 2,
  "name": "Alice Updated",
  "email": "alice.updated@example.com"
}
```

---

### ✅ Delete Student (DELETE)

```http
DELETE /students/2
```

**Response (Success):**

```
HTTP 204 No Content
```

**Response (Not Found):**

```json
{
  "timestamp": "2025-10-04T15:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with id 99",
  "path": "/students/99"
}
```

---

✅ This README.md serves as both **API documentation** and a **Postman reference** for your Student CRUD API.



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


# Recommended Improvements (Scalability + Clarity)

This document provides best practices to improve scalability, readability, and maintainability for your **Course API** in Spring Boot.

---

## 1. Use Versioned API Endpoints

Instead of using a generic `/api` prefix, version your endpoints for future compatibility.

```java
@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    @GetMapping
    public List<Course> getAllCourses() {
        ...
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        ...
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        ...
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        ...
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        ...
    }
}
```

✅ Benefits:

* Adds version control to your API (`v1`, `v2`, etc.)
* Makes endpoint purpose clear: `/api/v1/courses`
* Prevents breaking changes when API evolves

---

## 2. Add Consistent Logging (Optional but Useful)

Logging helps track application flow, debug issues, and monitor usage.

Use **SLF4J Logger** from Spring Boot:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        logger.info("Creating new course: {}", course.getTitle());
        Course savedCourse = courseService.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }
}
```

✅ Benefits:

* Adds visibility into operations
* Aids debugging and audit trails
* Can be enhanced with AOP or centralized logging later

---

## 3. API Endpoints Summary

| **Method** | **Endpoint**           | **Description**        | **Request Body**                                        | **Response**               |
| ---------- | ---------------------- | ---------------------- | ------------------------------------------------------- | -------------------------- |
| **GET**    | `/api/v1/courses`      | Get all courses        | -                                                       | `200 OK`                   |
| **GET**    | `/api/v1/courses/{id}` | Get course by ID       | -                                                       | `200 OK` / `404 Not Found` |
| **POST**   | `/api/v1/courses`      | Add a new course       | `{ "title": "Data Structures", "credits": 4 }`          | `201 Created`              |
| **PUT**    | `/api/v1/courses/{id}` | Update existing course | `{ "title": "Advanced Data Structures", "credits": 5 }` | `200 OK`                   |
| **DELETE** | `/api/v1/courses/{id}` | Delete course          | -                                                       | `204 No Content`           |

---

✅ **Additional Recommendations:**

* Use `ResponseEntity` for proper HTTP status control.
* Add exception handling via `@ControllerAdvice` for clean error responses.
* Version your APIs consistently (`/api/v1`, `/api/v2`) to handle future changes smoothly.
* Consider returning pagination in the GET endpoint for large datasets.

---

**Example Future Extension:**

```java
@GetMapping
public ResponseEntity<Page<Course>> getAllCourses(Pageable pageable) {
    Page<Course> courses = courseService.findAll(pageable);
    return ResponseEntity.ok(courses);
}
```

---

These improvements make your Course API **scalable, predictable, and production-ready**.


### 🔍 Advanced Queries — Student API
```java
// Derived queries (auto-generated by Spring)
    List<Student> findByName(String name);
    List<Student> findByAgeGreaterThan(int age);

    // Custom JPQL query
    @Query("SELECT s FROM Student s WHERE s.email LIKE %?1%")
    List<Student> findByEmailContains(String keyword);

    // Native SQL example
    @Query(value = "SELECT * FROM students WHERE name ILIKE %?1%", nativeQuery = true)
    List<Student> searchByName(String namePart);
   ```


| HTTP Method | Endpoint | Description | Example | Response |
|--------------|-----------|-------------|----------|-----------|
| GET | `/students/name/{name}` | Find by exact name | `/students/name/John` | Students with name "John" |
| GET | `/students/age/{age}` | Find students older than given age | `/students/age/18` | Students aged > 18 |
| GET | `/students/email/{keyword}` | Find students whose email contains keyword | `/students/email/gmail` | All gmail users |

#### Example JSON Response:
```json
[
  {"id": 1, "name": "John", "email": "john@gmail.com", "age": 22},
  {"id": 2, "name": "Alice", "email": "alice@gmail.com", "age": 25}
]


# Week 3: Advanced Spring Data JPA Queries & DTOs

This guide focuses on **Advanced Querying in Spring Data JPA** and implementing **DTOs (Data Transfer Objects)** for better API performance, security, and clarity.

---

## 🔍 Concept Recap — What’s Advanced Querying?

In **Spring Data JPA**, you can write powerful database queries without manually coding SQL. These queries can be auto-generated based on method names or explicitly defined using JPQL (`@Query`).

### ✳️ Examples

**Repository method naming convention:**

```java
List<Student> findByEmail(String email);
List<Student> findByAgeGreaterThan(int age);
List<Student> findByNameContaining(String keyword);
```

**JPQL Example using @Query:**

```java
@Query("SELECT s FROM Student s WHERE s.age > :age")
List<Student> findStudentsOlderThan(@Param("age") int age);
```

---

## 🧠 What is a DTO?

A **DTO (Data Transfer Object)** is a simple Java class used to:

* Transfer only required data between layers or over APIs.
* Hide sensitive/internal fields (e.g., passwords, IDs, or metadata).
* Reduce payload size for performance.
* Prevent lazy-loading issues or circular references with JPA entities.

DTOs help ensure that API responses are **lightweight, secure, and well-structured.**

---

## 🧩 Example Implementation — `StudentDTO`

### **1. StudentService.java**

```java
public interface StudentService {
    List<StudentDTO> getAllStudents();
    StudentDTO studentGeById(Long id);
}
```

### **2. StudentServiceImpl.java**

```java
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    private StudentDTO convertToDTO(Student student) {
        return new StudentDTO(student.getId(), student.getName(), student.getEmail());
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO studentGeById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        return convertToDTO(student);
    }
}
```

### **3. StudentController.java**

```java
@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.studentGeById(id);
        return ResponseEntity.ok(student);
    }
}
```

---

## 🧮 Example Request & Response

### **Request**

```http
GET /students
```

### **Response Before DTO:**

```json
[
  {
    "id": 1,
    "name": "John",
    "email": "john@gmail.com",
    "age": 22,
    "password": "Admin@123"
  }
]
```

### **Response After DTO:**

```json
[
  {
    "id": 1,
    "name": "John",
    "email": "john@gmail.com"
  }
]
```

✅ Cleaner, safer, and faster response!

---

## 🎯 Day 16 — DTOs (Data Transfer Objects)

| **Purpose**                    | **Implementation**                             | **Example**                   |
| ------------------------------ | ---------------------------------------------- | ----------------------------- |
| Hide sensitive/internal fields | Created `StudentDTO` (id, name, email)         | Returns only required data    |
| Convert Entity → DTO           | Added `convertToDTO()` in `StudentServiceImpl` | Uses Java Stream + map        |
| Updated Controller             | Returns `List<StudentDTO>` instead of entity   | Lightweight, secure responses |

**Sample Response:**

```json
[
  {"id": 1, "name": "Alice", "email": "alice@gmail.com"},
  {"id": 2, "name": "Robert", "email": "robert@gmail.com"}
]
```

---

## 🗃️ PostgreSQL Table Setup

Create the `student2s` table under user **srimansagar**:

```sql
CREATE TABLE student2s (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    age INT NOT NULL
);

ALTER TABLE student2s
ADD COLUMN password VARCHAR(150);
```

✅ Table created successfully with columns: `id`, `name`, `email`, `age`, `password`.

---

## 💡 Optional Improvements

* Use **ModelMapper** or **MapStruct** to automate Entity ↔ DTO conversion.
* Add filtering or pagination to `getAllStudents()`.
* Implement **custom JPQL queries** for advanced search functionality.

---

📘 **Summary:**
This week focused on **advanced JPA querying**, introducing **DTOs** to ensure secure, minimal, and efficient API responses — a crucial step toward building scalable backend systems.

# 📘 Mapping Entities → DTOs in student-api

This document explains how to simplify object mapping in Spring Boot — converting between **Entity** and **DTO** classes efficiently using tools like **ModelMapper** and **MapStruct**.

---

## 💡 Why Mapping Matters

When building REST APIs, you often need to send **DTOs (Data Transfer Objects)** instead of full entities to:

* 🔒 Hide sensitive fields (e.g., passwords)
* 🧱 Avoid exposing database structure
* 📦 Control API response shape
* ⚙️ Improve maintainability

Manually mapping these can get repetitive — here are cleaner options.

---

## ⚙️ Mapping Approaches

### 🔹 Option 1: Use ModelMapper (Simple, runtime reflection)

**Add dependency in `pom.xml`:**

```xml
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.2.0</version>
</dependency>
```

**Usage:**

```java
import org.modelmapper.ModelMapper;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    private StudentDTO convertToDTO(Student student) {
        return modelMapper.map(student, StudentDTO.class);
    }

    private Student convertToEntity(StudentDTO dto) {
        return modelMapper.map(dto, Student.class);
    }
}
```

✅ Automatically maps fields with the same names.
💡 You can also define custom mappings (e.g., `emailAddress → email`).

---

### 🔹 Option 2: Use MapStruct (Recommended — compile-time, type-safe)

**MapStruct** generates mappers at compile time — faster, safer, and ideal for enterprise projects.

#### 🧠 Advantages:

* ⚡ **Faster** – no reflection
* 🧱 **Type-safe** – compiler validates mappings
* 🧩 **Maintainable** – easily scales with project size

---

## 🧠 Integrating MapStruct into student-api

### Step 1: Add Maven Dependencies

Add inside `<dependencies>`:

```xml
<!-- MapStruct -->
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
</dependency>

<!-- MapStruct Processor -->
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct-processor</artifactId>
    <version>1.5.5.Final</version>
    <scope>provided</scope>
</dependency>
```

Add compiler plugin under `<build>`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <source>21</source>
                <target>21</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>1.5.5.Final</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

✅ Now Maven will generate mapper implementations at compile time.

---

### Step 2: Create the Mapper Interface

`StudentMapper.java`

```java
package com.vidyasagar.attendance.mapper;

import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.entity.StudentDTO;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDTO toDTO(Student student);
    Student toEntity(StudentDTO dto);
    List<StudentDTO> toDTOList(List<Student> students);
}
```

---

### Step 3: Use the Mapper in the Service

`StudentServiceImpl.java`

```java
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentMapper.toDTOList(studentRepository.findAll());
    }

    @Override
    public StudentDTO studentGeById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        return studentMapper.toDTO(student);
    }
}
```

---

### Step 4: Generated Code (for Reference)

After building, MapStruct auto-generates:

```
target/generated-sources/annotations/com/vidyasagar/attendance/mapper/StudentMapperImpl.java
```

Example:

```java
@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public StudentDTO toDTO(Student student) {
        if (student == null) return null;
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        return dto;
    }

    @Override
    public Student toEntity(StudentDTO dto) {
        if (dto == null) return null;
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        return student;
    }

    @Override
    public List<StudentDTO> toDTOList(List<Student> students) {
        if (students == null) return null;
        return students.stream().map(this::toDTO).toList();
    }
}
```

✅ Generated automatically — no manual maintenance required.

---

## 🚨 Common Issues During Integration

If you see errors like:

```
Dependency 'org.mapstruct:mapstruct:1.5.5.Final' not found
Plugin 'org.apache.maven.plugins:maven-compiler-plugin:3.11.0' not found
```

### Fix — Configure Maven Settings

If `settings.xml` is missing:

1. Right-click project → **Maven → Create settings.xml**
2. Maven generates it at:

```
C:\Users\Admin\.m2\settings.xml
```

Replace contents with:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <mirrors>
        <mirror>
            <id>central</id>
            <name>Maven Central Mirror</name>
            <url>https://repo.maven.apache.org/maven2</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors>

    <profiles>
        <profile>
            <id>default</id>
            <repositories>
                <repository>
                    <id>central</id>
                    <url>https://repo.maven.apache.org/maven2</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
            </repositories>
        </profile>
    </profiles>

    <activeProfiles>
        <activeProfile>default</activeProfile>
    </activeProfiles>
</settings>
```

Then sync project:

```
Right-click → Maven → Reload Project
mvn clean compile -U
```

✅ This downloads all dependencies freshly.

---

### 📘 Option 3: Keep Manual Mapping (for small APIs)

For simple projects, manual mapping is fine. You can move logic into a helper class:

```java
public class StudentMapperUtil {
    public static StudentDTO toDTO(Student student) {
        return new StudentDTO(student.getId(), student.getName(), student.getEmail());
    }
}
```

✅ Simple, readable, and no dependencies.

---

## ✅ Summary: Mapping Options Comparison

| **Option**     | **Type**             | **Performance** | **Setup** | **Best For**                   |
| -------------- | -------------------- | --------------- | --------- | ------------------------------ |
| Manual Mapping | Code-based           | ⚡ Fast          | Minimal   | Learning & small apps          |
| ModelMapper    | Runtime (Reflection) | 🐢 Slower       | Easy      | Quick demos                    |
| MapStruct      | Compile-time         | ⚡⚡ Fastest      | Medium    | Enterprise & scalable projects |

---

## 🧾 Example API Response

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@gmail.com"
}
```

---

## 🧠 Benefits of Using MapStruct

| **Feature**   | **Advantage**                      |
| ------------- | ---------------------------------- |
| 🧠 Type-safe  | Compiler catches mismatched fields |
| ⚡ Fast        | No reflection overhead             |
| 🧩 Clean      | Removes manual boilerplate         |
| 🔁 Reusable   | Works for all entities             |
| 🔄 Extensible | Handles nested mappings & lists    |

---

## ✅ Verification Commands

```bash
mvn clean install
```

If build succeeds and you see:

```
MapStruct: generating mapper implementation for StudentMapper
BUILD SUCCESS
```

🎉 Integration complete!


# 📘 Project Folder Structure – `student-api`

This document explains the **modular folder structure** for the `com.vidyasagar.attendance` package, built for scalability, clarity, and maintainability.

---

## 📂 Updated Folder Structure

```
com.vidyasagar.attendance
│
├── api
│   └── v1
│       ├── controller
│       │   ├── student
│       │   │   └── StudentController.java
│       │   ├── course
│       │   │   └── CourseController.java
│       │   └── faculty
│       │       └── FacultyController.java
│       │
│       ├── dto
│       │   ├── request
│       │   │   ├── StudentSearchRequest.java
│       │   │   ├── StudentRequest.java
│       │   │   ├── CourseRequest.java
│       │   │   └── FacultyRequest.java
│       │   │
│       │   ├── response
│       │   │   ├── StudentDTO.java
│       │   │   ├── CourseDTO.java
│       │   │   └── FacultyDTO.java
│       │   │
│       │   └── common
│       │       └── PageResponse.java
│       │
│       ├── mapper
│       │   ├── StudentMapper.java
│       │   ├── CourseMapper.java
│       │   └── FacultyMapper.java
│       │
│       ├── service
│       │   ├── impl
│       │   │   ├── StudentServiceImpl.java
│       │   │   ├── CourseServiceImpl.java
│       │   │   └── FacultyServiceImpl.java
│       │   │
│       │   ├── StudentService.java
│       │   ├── CourseService.java
│       │   └── FacultyService.java
│       │
│       ├── specification
│       │   ├── StudentSpecification.java
│       │   ├── CourseSpecification.java
│       │   └── FacultySpecification.java
│       │
│       └── repository
│           ├── StudentRepository.java
│           ├── CourseRepository.java
│           └── FacultyRepository.java
│
├── entity
│   ├── Student.java
│   ├── Course.java
│   ├── Faculty.java
│   ├── Department.java
│   └── Subject.java
│
├── exception
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── InvalidRequestException.java
│   └── ErrorResponse.java
│
├── config
│   ├── SwaggerConfig.java
│   ├── WebConfig.java
│   └── AppProperties.java
│
├── util
│   ├── Constants.java
│   ├── ValidationUtils.java
│   └── DateUtils.java
│
└── StudentApiApplication.java
```

---

## 🧠 Why This Structure Works

### 🔹 **api/v1 – Versioning at the Folder Level**

* Keeps APIs organized by version.
* Future-proof: `/v2` can coexist with `/v1` without breaking older clients.
* Example endpoints:

    * `/api/v1/students`
    * `/api/v1/courses`
    * `/api/v1/faculties`

---

### 🔹 **controller/**

Each domain module (Student, Course, Faculty) has its own dedicated controller.

✅ Example routes:

* `/api/v1/students`
* `/api/v1/courses`
* `/api/v1/faculties`

📁 Example: `controller/student/StudentController.java`
Keeps the controller focused on a single responsibility.

---

### 🔹 **dto/request & dto/response**

Separating **request** and **response** DTOs improves clarity and security.

| Type                   | Purpose                           |
| ---------------------- | --------------------------------- |
| `StudentRequest`       | Incoming data for POST/PUT        |
| `StudentDTO`           | Outgoing API response             |
| `StudentSearchRequest` | Used for pagination/filter/search |

✅ Clear data flow:
**Frontend → Request DTO → Controller → Entity → Response DTO → Frontend**

📘 Example common wrapper:

```java
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
```

This standardizes pagination across all APIs.

---

### 🔹 **mapper/**

Contains `MapStruct` mappers for converting between entities and DTOs.

| Mapper          | Responsibility       |
| --------------- | -------------------- |
| `StudentMapper` | Student ↔ StudentDTO |
| `CourseMapper`  | Course ↔ CourseDTO   |
| `FacultyMapper` | Faculty ↔ FacultyDTO |

Encapsulates all conversion logic in one place.

---

### 🔹 **service/** & **service/impl/**

Separates **interfaces** from **implementations**:

| Folder          | Description                    |
| --------------- | ------------------------------ |
| `service/`      | Defines contracts (interfaces) |
| `service/impl/` | Implements business logic      |

✅ Example:

* `StudentService` → defines operations
* `StudentServiceImpl` → contains logic using repository & mapper

This improves testability and loose coupling.

---

### 🔹 **specification/**

Holds classes for **dynamic filtering**, **search**, and **sorting** using Spring JPA Specifications.

| Example Files               |
| --------------------------- |
| `StudentSpecification.java` |
| `CourseSpecification.java`  |
| `FacultySpecification.java` |

Encourages reusable and composable query logic.

---

### 🔹 **repository/**

Holds JPA repositories for data persistence.

| Repository          | Entity  |
| ------------------- | ------- |
| `StudentRepository` | Student |
| `CourseRepository`  | Course  |
| `FacultyRepository` | Faculty |

---

### 🔹 **entity/**

Contains database entity models mapped via JPA.

| Entity       | Description                |
| ------------ | -------------------------- |
| `Student`    | Represents student records |
| `Course`     | Represents course details  |
| `Faculty`    | Represents faculty details |
| `Department` | Represents departments     |
| `Subject`    | Represents subjects        |

---

### 🔹 **exception/**

Centralized error handling and custom exceptions.

| File                        | Purpose                            |
| --------------------------- | ---------------------------------- |
| `GlobalExceptionHandler`    | Handles all application exceptions |
| `ResourceNotFoundException` | Thrown when record not found       |
| `InvalidRequestException`   | Used for invalid input cases       |
| `ErrorResponse`             | Standardized JSON error format     |

---

### 🔹 **config/**

Configuration files for application-wide settings.

| File            | Purpose                   |
| --------------- | ------------------------- |
| `SwaggerConfig` | OpenAPI/Swagger setup     |
| `WebConfig`     | CORS and Web MVC settings |
| `AppProperties` | Custom property mappings  |

---

### 🔹 **util/**

Reusable utility classes and constants.

| File              | Purpose                           |
| ----------------- | --------------------------------- |
| `Constants`       | Common static messages and values |
| `ValidationUtils` | Input validation helpers          |
| `DateUtils`       | Date/time formatting methods      |

---

## ✅ Benefits of This Structure

* 🧩 **Modular:** Each feature is self-contained.
* 🚀 **Scalable:** Easy to extend new modules like Attendance, Department, etc.
* 🧠 **Readable:** Follows clear naming and separation of concerns.
* 🔁 **Maintainable:** Simplifies debugging and testing.

---

**Summary:**
This structure follows enterprise-grade standards — clear layering, versioned APIs, reusable components, and consistent DTO management. Perfect foundation for scalable Spring Boot applications.



📘 Week 3 – Day 19
Topic: Add Related Entity – Course
Focus: One Student → Many Courses + CRUD using DTO
🧩 Overview

Today’s focus was on expanding the existing Student API by adding a related entity — Course — and implementing a One-to-Many relationship between Student and Course.

Each student can be linked to multiple courses, but each course belongs to only one student.
We also implemented DTO mapping for Course to maintain clean JSON structures and avoid infinite recursion during serialization.

🧱 Database Schema
Student Table (student2s)
Column	Type	Constraints
id	bigint	Primary Key, Auto Increment
name	varchar(100)	Not Null
email	varchar(255)	Not Null
age	int	Not Null
password	varchar(150)	Optional
Course Table (course)
Column	Type	Constraints
id	bigint	Primary Key, Auto Increment
title	varchar(100)	Not Null
credits	int	Optional
student_id	bigint	Foreign Key → student2s(id)
🧠 Entity Relationship

Relationship:

One Student → Many Courses
Each Course → Belongs to one Student

Student.java
@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference
private List<Course> courses = new ArrayList<>();

Course.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "student_id")
@JsonBackReference
private Student student;


✅ Why:
@JsonManagedReference and @JsonBackReference prevent infinite JSON recursion between Student and Course.

🧩 DTO Layer

To maintain a clean API structure and prevent exposing JPA entities directly,
we used CourseDTO for all request/response data.

CourseDTO.java
public class CourseDTO {
private Long id;
private String title;
private Integer credits;
private Long studentId;
private String studentName;
}

⚙️ Mapper Layer (MapStruct Integration)
CourseMapper.java
@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mappings({
        @Mapping(source = "student.id", target = "studentId"),
        @Mapping(source = "student.name", target = "studentName")
    })
    CourseDTO toDTO(Course course);

    @Mappings({
        @Mapping(source = "studentId", target = "student.id")
    })
    Course toEntity(CourseDTO courseDTO);

    List<CourseDTO> toDTOList(List<Course> courses);
}


✅ Automatically maps between Entity ↔ DTO using MapStruct.
✅ Simplifies conversion logic in service layer.

💼 Service Layer
CourseService.java
public interface CourseService {
String getCourseWelcomeMessage();
CourseDTO saveCourse(CourseDTO courseDTO);
List<CourseDTO> getAllCourses();
CourseDTO getCourseById(Long id);
CourseDTO updateCourse(Long id, CourseDTO courseDTO);
void deleteCourse(Long id);
}

CourseServiceImpl.java
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper courseMapper;

    public CourseServiceImpl(CourseRepository courseRepository, 
                             StudentRepository studentRepository, 
                             CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public CourseDTO saveCourse(CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);

        if (courseDTO.getStudentId() != null) {
            Student student = studentRepository.findById(courseDTO.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            course.setStudent(student);
        }

        Course saved = courseRepository.save(course);
        return courseMapper.toDTO(saved);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseMapper.toDTOList(courseRepository.findAll());
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
        return courseMapper.toDTO(course);
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));

        course.setTitle(courseDTO.getTitle());
        course.setCredits(courseDTO.getCredits());

        if (courseDTO.getStudentId() != null) {
            Student student = studentRepository.findById(courseDTO.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            course.setStudent(student);
        }

        Course updated = courseRepository.save(course);
        return courseMapper.toDTO(updated);
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id " + id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    public String getCourseWelcomeMessage() {
        return "Welcome to Course Section";
    }
}

🧭 Controller Layer
CourseController.java
@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO saved = courseService.saveCourse(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id,
                                                  @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}

🌐 API Endpoints
Method	Endpoint	Description
POST	/api/v1/courses	Create a new course
GET	/api/v1/courses	Fetch all courses
GET	/api/v1/courses/{id}	Fetch a single course by ID
PUT	/api/v1/courses/{id}	Update course details
DELETE	/api/v1/courses/{id}	Delete a course by ID
🧪 Example Request & Response
POST /api/v1/courses

Request:

{
"title": "Spring Boot Basics",
"credits": 4,
"studentId": 1
}


Response:

{
"id": 10,
"title": "Spring Boot Basics",
"credits": 4,
"studentId": 1,
"studentName": "John"
}

GET /api/v1/courses

Response:

[
{
"id": 10,
"title": "Spring Boot Basics",
"credits": 4,
"studentId": 1,
"studentName": "John"
},
{
"id": 11,
"title": "React Fundamentals",
"credits": 3,
"studentId": 2,
"studentName": "Robert"
}
]

✅ Outcome of the Day
Area	Achievement
Entity Design	Created Course entity with a foreign key to Student
JPA Mapping	Implemented One-to-Many & Many-to-One relationship
DTO & Mapper	Introduced CourseDTO + MapStruct for clean conversions
CRUD APIs	Created, Read, Updated, and Deleted Courses via REST
Serialization Fix	Eliminated circular JSON dependency between Student ↔ Course