package com.vidyasagar.attendance.service;

import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.entity.StudentDTO;

import java.util.List;

public interface StudentService {
    String getHelloMessage();
    Student saveStudent(Student student);
    List<StudentDTO> getAllStudents();
    StudentDTO getStudentById(Long id);
    Student updateStudent(Long id, Student studentDetails);
    void deleteStudent(Long id);

    List<Student> findByName(String name);
    List<Student> findByAgeGreaterThan(int age);
    List<Student> findByEmailContains(String keyword);
}
