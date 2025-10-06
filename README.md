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
