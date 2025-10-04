package com.vidyasagar.attendance.service;

import com.vidyasagar.attendance.entity.Student;
import java.util.List;

public interface StudentService {
    String getHelloMessage();
    Student saveStudent(Student student);
    List<Student> getAllStudents();
    Student updateStudent(Long id, Student studentDetails);
}
