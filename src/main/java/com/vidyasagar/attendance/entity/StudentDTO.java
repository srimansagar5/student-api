package com.vidyasagar.attendance.entity;

import jakarta.validation.constraints.*;

public class StudentDTO {
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 60, message = "Age must not exceed 60")
    @NotNull(message = "Age cannot be null")
    private Integer age;

    // Constructor
    public StudentDTO() {}

    public StudentDTO(Long id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    //Getters and Setter
    public Long getId() {return id;}
    public void setId(Long id) { this.id = id;}

    public String getName() { return name;}
    public void setName(String name) { this.name = name;}

    public String getEmail() { return email;}
    public void setEmail(String email) { this.email = email;}

    public int getAge() { return age; }
    public void setAge(int age) {this.age = age; }
}
