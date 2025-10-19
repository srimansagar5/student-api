package com.vidyasagar.attendance.entity;

import jakarta.persistence.*;

@Entity
@Table(name="course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private Integer credits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    // Constructor
    public Course() {}
    public Course(String title, Integer credits) {
        this.credits = credits;
        this.title = title;
    }

    // getters and setters
    public Long getId() { return id;}
    public void setId(Long id) {this.id = id;}

    public String getTitle() { return  title;}
    public void setTitle(String title) {this.title = title;}

    public Integer getCredits() {return  credits;}
    public void setCredits(Integer credits) { this.credits = credits;}

    public Student getStudent() { return  student;}
    public void setStudent(Student student) { this.student = student;}
}
