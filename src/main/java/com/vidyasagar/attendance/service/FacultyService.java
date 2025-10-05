package com.vidyasagar.attendance.service;

import com.vidyasagar.attendance.entity.Faculty;

import java.util.List;

public interface FacultyService {
    String getFacultyMessage();
    Faculty saveFaculty(Faculty faculty);
    List<Faculty> getAllFaculty();
    Faculty updateFaculty(Long id, Faculty facultyDetails);
    void deleteFaculty(Long id);
}
