package com.vidyasagar.attendance.entity;

import jakarta.persistence.*;

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

    @Column(nullable = false, length = 100)
    private int age;

    //Constructor
    public Student() {}

    public Student(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
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
}
