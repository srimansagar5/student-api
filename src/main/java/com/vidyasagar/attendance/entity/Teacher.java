package com.vidyasagar.attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name="teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Teacher name is required")
    @Column(nullable = false, length = 250)
    private String name;

    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true, length = 250)
    private String email;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;

    //Constructor
    public Teacher() {}

    public Teacher(String name, String email) {
        this.name = name;
        this.email = email;
    }
    // Getters and Setters

    public Long getId() { return id;}
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }

}
