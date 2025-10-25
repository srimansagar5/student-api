package com.vidyasagar.attendance.api.v1.dto.response;

import java.util.List;

public class CourseDTO {
    private Long id;
    private String title;
    private Integer credits;
    private List<Long> studentIds;
    private List<String> studentNames;

    public CourseDTO() {}

    public CourseDTO(Long id, String title, Integer credits, List<Long> studentIds, List<String> studentNames){
        this.id = id;
        this.title = title;
        this.credits = credits;
        this.studentIds = studentIds;
        this.studentNames = studentNames;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public List<Long> getStudentIds() { return studentIds; }
    public void setStudentIds(List<Long> studentIds) { this.studentIds = studentIds; }

    public List<String> getStudentNames() { return studentNames; }
    public void setStudentNames(List<String> studentNames) { this.studentNames = studentNames; }
}

