package com.vidyasagar.attendance.api.v1.dto.response;

import java.util.List;

public class TeacherResponseDTO {

    private  Long id;
    private String name;
    private String email;
    private List<String> courses;

    //Getter and Setters

    public Long getId() { return id;}
    public void setId(Long id) { this.id = id;}

    public String getName() { return name; }
    public void setName(String name) { this.name = name;}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email;}

    public List<String> getCourses() { return courses; }
    public void setCourses(List<String> courses) { this.courses = courses; }
}
