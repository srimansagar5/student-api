package com.vidyasagar.attendance.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    //Constructor
    public Student() {}

    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }

    //getter and setter
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return  name;}
    public void setName(String name) {this.name = name;}

    public String getEmail() {return  email;}
    public void setEmail(String email) {this.email = email;}
}
