package com.vidyasagar.attendance.service;

import com.vidyasagar.attendance.entity.Course;
import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.exception.ResourceNotFoundException;
import com.vidyasagar.attendance.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    // Constructor Injection
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public String getCourseWelcomeMessage(){
        return "Welcome to Course Section";
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    @Override
    public Course updateCourse(Long id, Course courseDetails){
        Course course = courseRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));

        course.setCredits(courseDetails.getCredits());
        course.setTitle(courseDetails.getTitle());

        return courseRepository.save(course);
    }

    @Override
    public void  deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));

        courseRepository.deleteById(id);
    }
}
