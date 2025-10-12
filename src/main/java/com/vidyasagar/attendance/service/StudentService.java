package com.vidyasagar.attendance.service;

import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.entity.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    String getHelloMessage();
    StudentDTO saveStudent(StudentDTO studentDto);
    List<StudentDTO> getAllStudents();
    StudentDTO getStudentById(Long id);
    StudentDTO updateStudent(Long id, StudentDTO studentDetails);
    void deleteStudent(Long id);

    List<StudentDTO> findByName(String name);
    List<StudentDTO> findByAgeGreaterThan(Integer age);
    List<StudentDTO> findByEmailContains(String keyword);

    Page<StudentDTO> getAllStudentsPage(Pageable pageable);
}
