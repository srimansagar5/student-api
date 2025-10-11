package com.vidyasagar.attendance.controller;

import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.entity.StudentDTO;
import com.vidyasagar.attendance.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    // Constructor Injection
    public  StudentController(StudentService studentService) {
        this.studentService = studentService;
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

}
