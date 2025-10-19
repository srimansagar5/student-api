package com.vidyasagar.attendance.api.v1.service;

import com.vidyasagar.attendance.api.v1.dto.response.CourseDTO;
import com.vidyasagar.attendance.entity.Course;

import java.util.List;

public interface CourseService {
    String getCourseWelcomeMessage();
    CourseDTO saveCourse(CourseDTO courseDTO);
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseById(Long id);
    CourseDTO updateCourse(Long id, CourseDTO courseDetails);
    void deleteCourse(Long id);
    List<CourseDTO> getCoursesByStudentId(Long studentId);
}
