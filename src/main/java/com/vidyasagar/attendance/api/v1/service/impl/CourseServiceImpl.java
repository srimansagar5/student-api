package com.vidyasagar.attendance.api.v1.service.impl;

import com.vidyasagar.attendance.api.v1.dto.response.CourseDTO;
import com.vidyasagar.attendance.api.v1.mapper.CourseMapper;
import com.vidyasagar.attendance.api.v1.repository.StudentRepository;
import com.vidyasagar.attendance.api.v1.service.CourseService;
import com.vidyasagar.attendance.entity.Course;
import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.exception.ResourceNotFoundException;
import com.vidyasagar.attendance.api.v1.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper courseMapper;

    // Constructor Injection
    public CourseServiceImpl(
            CourseRepository courseRepository,
            StudentRepository studentRepository,
            CourseMapper courseMapper
    ) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public String getCourseWelcomeMessage(){
        return "Welcome to Course Section";
    }

    @Override
    public CourseDTO saveCourse(CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);

        // If student is attached, ensure it's loaded from DB(best practice)
        if(courseDTO.getStudentId() != null) {
            Student student = studentRepository.findById(courseDTO.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            course.setStudent(student);
        }

        Course saved = courseRepository.save(course);
        return courseMapper.toDTO(saved);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseMapper.toDTOList(courseRepository.findAll());
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
        return courseMapper.toDTO(course);
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDetails){
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));

        course.setCredits(courseDetails.getCredits());
        course.setTitle(courseDetails.getTitle());

        if(courseDetails.getStudentId() != null) {
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            course.setStudent(student);
        }

        Course updated = courseRepository.save(course);
        return courseMapper.toDTO(updated);
    }

    @Override
    public void  deleteCourse(Long id) {
        if(!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id " + id);
        }
        courseRepository.deleteById(id);
    }
}
