package com.vidyasagar.attendance.service;

import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.entity.StudentDTO;
import com.vidyasagar.attendance.exception.ResourceNotFoundException;
import com.vidyasagar.attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    // Constructor Injection
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private StudentDTO convertToDTO(Student student) {
        return new StudentDTO(student.getId(), student.getName(), student.getEmail());
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
    public List<StudentDTO> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO studentGeById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        return convertToDTO(student);
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
