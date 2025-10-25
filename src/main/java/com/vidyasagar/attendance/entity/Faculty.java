package com.vidyasagar.attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="faculty")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(unique = true, nullable = false, length = 150)
    @Email(message = "Enter valid email address")
    private String email;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Department is required")
    private String department;

    //Constructor
    public Faculty() {}

    public Faculty(String name, String email, String department) {
        this.name = name;
        this.email = email;
        this.department = department;
    }

    //getter and setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name; }
    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}
}
