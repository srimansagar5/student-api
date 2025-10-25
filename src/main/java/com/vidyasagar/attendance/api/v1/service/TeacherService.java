package com.vidyasagar.attendance.api.v1.service;

import com.vidyasagar.attendance.api.v1.dto.request.TeacherRequestDTO;
import com.vidyasagar.attendance.api.v1.dto.response.TeacherResponseDTO;

import java.util.List;

public interface TeacherService {
    List<TeacherResponseDTO> getAllTeachers();
    TeacherResponseDTO getTeacherById(Long id);
    TeacherResponseDTO createTeacher(TeacherRequestDTO teacher);
    TeacherResponseDTO updateTeacher(Long id, TeacherRequestDTO teacher);
    void deleteTeacher(Long id);
}
