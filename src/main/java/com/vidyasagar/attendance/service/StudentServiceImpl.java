package com.vidyasagar.attendance.service;

import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.exception.ResourceNotFoundException;
import com.vidyasagar.attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    // Constructor Injection
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public String getHelloMessage() {
        return "Student API working fine!";
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student updateStudent(Long id, Student studentDetails) {
        // Find existing student
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));

        //update fields
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());

        //Save updated student
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        // Find existing student
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));

        studentRepository.deleteById(id);
    }
    @Override
    public List<Student> findByName(String name) {
        return studentRepository.findByName(name);
    }

    @Override
    public List<Student> findByAgeGreaterThan(int age) {
        return studentRepository.findByAgeGreaterThan(age);
    }

    @Override
    public List<Student> findByEmailContains(String keyword) {
        return studentRepository.findByEmailContains(keyword);
    }


}
