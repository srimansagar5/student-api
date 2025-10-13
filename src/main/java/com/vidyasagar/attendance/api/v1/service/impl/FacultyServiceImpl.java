package com.vidyasagar.attendance.api.v1.service.impl;

import com.vidyasagar.attendance.api.v1.service.FacultyService;
import com.vidyasagar.attendance.entity.Faculty;
import com.vidyasagar.attendance.exception.ResourceNotFoundException;
import com.vidyasagar.attendance.api.v1.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    // Constructor Injection
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public String getFacultyMessage() { return "Faculty API is working fine!"; }

    @Override
    public Faculty saveFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    @Override
    public Faculty updateFaculty(Long id, Faculty facultyDetails) {
        // Find existing faculty
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id " + id));

        faculty.setName(facultyDetails.getName());
        faculty.setEmail(facultyDetails.getEmail());
        faculty.setDepartment(facultyDetails.getDepartment());

        return facultyRepository.save(faculty);
    }

    @Override
    public void deleteFaculty(Long id) {
        // Find existing faculty
        // This avoids fetching the entity if not needed
        if(!facultyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Faculty not found with id " + id);
        }

        //        Faculty faculty = facultyRepository.findById(id)
        //                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id " + id));

        facultyRepository.deleteById(id);
    }
}
