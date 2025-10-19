package com.vidyasagar.attendance.api.v1.dto.response;

public class CourseDTO {
    private Long id;
    private String title;
    private Integer credits;
    private Long studentId;
    private String studentName;

    public CourseDTO() {}
    public CourseDTO(Long id, String title, Integer credits, Long studentId, String studentName) {
        this.id = id;
        this.title = title;
        this.credits = credits;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    // setters and getters
    public Long getId() { return id;}
    public void setId(Long id) { this.id = id;}

    public String getTitle() { return title;}
    public void setTitle(String title) { this.title = title;}

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits;}

    public Long getStudentId() { return studentId;}
    public void setStudentId(Long studentId) { this.studentId = studentId;}

    public String getStudentName() { return studentName;}
    public void setStudentName(String studentName) { this.studentName = studentName;}

}
