package com.vidyasagar.attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "student2s")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Column(unique = true, nullable = false)
    private String email;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 60, message = "Age must not exceed 60")
    @Column(nullable = false, length = 100)
    private int age;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = true, length = 150)
    private String password;

    //Constructor
    public Student() {}

    public Student(String name, String email, int age, String password) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    //getter and setter
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return  name;}
    public void setName(String name) {this.name = name;}

    public String getEmail() {return  email;}
    public void setEmail(String email) {this.email = email;}

    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}
