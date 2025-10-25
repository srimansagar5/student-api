package com.vidyasagar.attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student2s")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = true, length = 100)
    private Integer age;

    @Column(nullable = true, length = 150)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();

    //Constructor
    public Student() {}

    public Student(String name, String email, Integer age, String password) {
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

    public Integer getAge() {return age;}
    public void setAge(Integer age) {this.age = age;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public List<Course> getCourses() { return courses;}
    public void setCourses(List<Course> courses) { this.courses = courses;}
}
