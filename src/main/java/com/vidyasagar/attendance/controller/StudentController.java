package com.vidyasagar.attendance.controller;

import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.entity.StudentDTO;
import com.vidyasagar.attendance.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

    @PostMapping("/students")
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @GetMapping("/students")
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.studentGeById(id);
        return ResponseEntity.ok(student);
    }


    @PutMapping("/students/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        return studentService.updateStudent(id, studentDetails);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students/name/{name}")
    public List<Student> getStudentsByName(@PathVariable String name) {
        return studentService.findByName(name);
    }

    @GetMapping("/students/age/{age}")
    public List<Student> getStudentsByAgeGreaterThan(@PathVariable int age) {
        return studentService.findByAgeGreaterThan(age);
    }

    @GetMapping("/students/email/{keyword}")
    public List<Student> getStudentsByEmailKeyword(@PathVariable String keyword) {
        return studentService.findByEmailContains(keyword);
    }

}
