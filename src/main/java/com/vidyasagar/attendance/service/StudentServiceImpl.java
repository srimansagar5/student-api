package com.vidyasagar.attendance.service;

import com.vidyasagar.attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    // Constructor Injection
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public String getHelloMessage() {
        return studentRepository.getMessage();
    }
}
