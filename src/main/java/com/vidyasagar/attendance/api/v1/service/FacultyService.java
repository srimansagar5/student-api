package com.vidyasagar.attendance.api.v1.service;

import com.vidyasagar.attendance.api.v1.dto.response.FacultyDTO;
import com.vidyasagar.attendance.entity.Faculty;

import java.util.List;

public interface FacultyService {
    String getFacultyMessage();
    FacultyDTO saveFaculty(FacultyDTO facultyDTO);
    List<FacultyDTO> getAllFaculty();
    FacultyDTO updateFaculty(Long id, FacultyDTO facultyDetails);
    void deleteFaculty(Long id);
}
