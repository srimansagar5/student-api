package com.vidyasagar.attendance.controller;

import com.vidyasagar.attendance.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
