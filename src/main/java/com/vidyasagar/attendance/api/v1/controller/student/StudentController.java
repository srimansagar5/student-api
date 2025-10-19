package com.vidyasagar.attendance.api.v1.controller.student;

import com.vidyasagar.attendance.api.v1.dto.request.StudentSearchRequest;
import com.vidyasagar.attendance.api.v1.dto.response.CourseDTO;
import com.vidyasagar.attendance.api.v1.service.StudentService;
import com.vidyasagar.attendance.api.v1.service.CourseService;

import com.vidyasagar.attendance.api.v1.dto.response.StudentDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;
    // Constructor Injection
    public  StudentController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/hello-student")
    public String helloStudent() {
        return studentService.getHelloMessage();
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDto) {

        StudentDTO savedStudent = studentService.saveStudent(studentDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStudent.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedStudent);
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> getAllStudents = studentService.getAllStudents();
        return ResponseEntity.ok(getAllStudents);
    }

    @GetMapping("/pages")
    public ResponseEntity<Page<StudentDTO>> getAllStudentsPage(
            @PageableDefault(page =0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<StudentDTO> studentDTOPage = studentService.getAllStudentsPage(pageable);
        return ResponseEntity.ok(studentDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }


    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDetails) {
        StudentDTO updateStudent = studentService.updateStudent(id, studentDetails);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updateStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<StudentDTO>> getStudentsByName(@PathVariable String name) {
        List<StudentDTO> students = studentService.findByName(name);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<List<StudentDTO>> getStudentsByAgeGreaterThan(@PathVariable int age) {
        List<StudentDTO> students = studentService.findByAgeGreaterThan(age);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/email/{keyword}")
    public ResponseEntity<List<StudentDTO>> getStudentsByEmailKeyword(@PathVariable String keyword) {
        List<StudentDTO> students = studentService.findByEmailContains(keyword);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<StudentDTO>> searchStudents(@RequestBody StudentSearchRequest request) {
        Page<StudentDTO> result = studentService.searchStudents(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseDTO>> getCoursesByStudent(@PathVariable Long id) {
        List<CourseDTO> courses = courseService.getCoursesByStudentId(id);
        return ResponseEntity.ok(courses);
    }
}
