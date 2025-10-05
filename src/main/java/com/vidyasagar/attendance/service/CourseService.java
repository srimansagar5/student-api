package com.vidyasagar.attendance.service;

import com.vidyasagar.attendance.entity.Course;

import java.util.List;

public interface CourseService {
    String getCourseWelcomeMessage();
    Course saveCourse(Course course);
    List<Course> getAllCourses();
    Course getCourseById(Long id);
    Course updateCourse(Long id, Course courseDetails);
    void deleteCourse(Long id);
}
