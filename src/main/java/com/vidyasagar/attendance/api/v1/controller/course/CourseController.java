package com.vidyasagar.attendance.api.v1.controller.course;

import com.vidyasagar.attendance.api.v1.dto.response.CourseDTO;
import com.vidyasagar.attendance.api.v1.service.CourseService;
import com.vidyasagar.attendance.entity.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(com.vidyasagar.attendance.api.v1.controller.course.CourseController.class);

    private final CourseService courseService;

    // Constructor Injection
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course-welcome")
    public String getCourseWelcomeMessage() {
        return courseService.getCourseWelcomeMessage();
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO saved = courseService.saveCourse(courseDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(saved);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        CourseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDetails) {
        CourseDTO updateCourse  = courseService.updateCourse(id, courseDetails);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updateCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
