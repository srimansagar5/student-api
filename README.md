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

# PostgreSQL Commands & Troubleshooting for Student API

This document provides useful **psql commands**, explains common issues, and details how to fix database permission errors when working with **Spring Boot + PostgreSQL**.

---

## Common psql Commands

```sql
\c                -- connect to database
\d                -- describe tables

\c studentdb;     -- switch to your DB
\d;               -- list all tables
\d students       -- show table schema
```

---

## Connecting to PostgreSQL (SQL Shell)

When you start **SQL Shell (psql)**, you’ll see prompts:

```
Server [localhost]:
Database [postgres]:
Port [5432]:
Username [postgres]: postgres
Password for user postgres: ******
```

✅ What to do:

1. Start SQL Shell (psql).
2. At prompts, hit **Enter** for defaults (unless changed):

    * Server [localhost]: **Enter**
    * Database [postgres]: **Enter**
    * Port [5432]: **Enter**
    * Username [postgres]: **postgres** (or your DB username)
    * Password: enter your PostgreSQL password

Now you’re connected to the default database.

---

## Switching Database

To switch to your project DB:

```sql
\c studentdb;
```

Output:

```
You are now connected to database "studentdb" as user "postgres".
studentdb=#
```

---

## Checking Tables

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

## Fix: Create Table

If Hibernate didn’t auto-create the table, create manually:

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

## Permission Errors

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

## Root Cause: User Ownership

* You created the table `students` as **postgres** superuser.
* But Spring Boot app connects as **srimansagar**.
* Default privileges only apply to future tables, not existing ones.
* So `srimansagar` cannot insert into `students`.

---

## Fix for Existing Tables

1. Exit SQL shell and reopen.
2. Grant privileges explicitly:

```sql
\c studentdb;

-- Grant rights on table
GRANT ALL PRIVILEGES ON TABLE students TO srimansagar;

-- Grant rights on sequence (for auto-increment ID)
GRANT ALL PRIVILEGES ON SEQUENCE students_id_seq TO srimansagar;
```

3. Verify:

```sql
\z students
```

---

## Best Practice

* Always connect as the **application user** (`srimansagar`) before creating tables.
* This ensures the user owns the tables → no need for extra grants.

---

## Test with Spring Boot

Restart your Spring Boot app and test API:

```http
POST http://localhost:8080/students
{
  "name": "Robert",
  "email": "robert@example.com"
}
```

✅ If insert succeeds, database + permissions are correctly configured.

# 📘 Student API – CRUD Endpoints

## 🔹 Endpoints Overview

### Health Check

* **Endpoint:** `/hello-student`
* **HTTP Method:** `GET`
* **Description:** Health check / Welcome API
* **Sample Request Body:** -
* **Sample Response:**

```json
"Student API working fine!"
```

---

### Create Student

* **Endpoint:** `/students`
* **HTTP Method:** `POST`
* **Description:** Create a new student
* **Sample Request Body:**

```json
{
  "name": "John Doe",
  "email": "john@example.com"
}
```

* **Sample Response:**

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

---

### Get All Students

* **Endpoint:** `/students`
* **HTTP Method:** `GET`
* **Description:** Retrieve all students
* **Sample Request Body:** -
* **Sample Response:**

```json
[
  { "id": 1, "name": "John Doe", "email": "john@example.com" }
]
```

---

### Update Student by ID

* **Endpoint:** `/students/{id}`
* **HTTP Method:** `PUT`
* **Description:** Update student by ID
* **Sample Request Body:**

```json
{
  "name": "Updated Name",
  "email": "updated@example.com"
}
```

* **Sample Response:**

```json
{
  "id": 1,
  "name": "Updated Name",
  "email": "updated@example.com"
}
```

---

### Delete Student by ID

* **Endpoint:** `/students/{id}`
* **HTTP Method:** `DELETE`
* **Description:** Delete student by ID
* **Sample Request Body:** -
* **Sample Response (Success):**

```
204 No Content
```

* **Sample Response (Not Found):**

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

✅ This README.md serves as both **API documentation** and a **Postman reference**.

