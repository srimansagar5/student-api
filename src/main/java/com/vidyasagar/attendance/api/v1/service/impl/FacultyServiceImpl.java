package com.vidyasagar.attendance.api.v1.service.impl;

import com.vidyasagar.attendance.api.v1.dto.response.FacultyDTO;
import com.vidyasagar.attendance.api.v1.mapper.FacultyMapper;
import com.vidyasagar.attendance.api.v1.service.FacultyService;
import com.vidyasagar.attendance.entity.Faculty;
import com.vidyasagar.attendance.exception.ResourceNotFoundException;
import com.vidyasagar.attendance.api.v1.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {
    private static final Logger log = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;

    public FacultyServiceImpl(FacultyRepository facultyRepository, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
    }

    @Override
    public String getFacultyMessage() {
        return "Faculty API is working fine!";
    }

    @Override
    public FacultyDTO saveFaculty(FacultyDTO facultyDTO) {
        log.info("Saving new faculty: {}", facultyDTO.getName());
        Faculty faculty = facultyMapper.toEntity(facultyDTO);
        Faculty saved = facultyRepository.save(faculty);
        return facultyMapper.toDTO(saved);
    }

    @Override
    public List<FacultyDTO> getAllFaculty() {
        return facultyMapper.toDTOList(facultyRepository.findAll());
    }

    @Override
    public FacultyDTO updateFaculty(Long id, FacultyDTO facultyDetails) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id " + id));
        facultyMapper.updateFacultyFromDTO(facultyDetails, faculty);
        Faculty updated = facultyRepository.save(faculty);
        return facultyMapper.toDTO(updated);
    }

    @Override
    public void deleteFaculty(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Faculty not found with id " + id);
        }
        facultyRepository.deleteById(id);
        log.warn("Deleted faculty with id {}", id);
    }
}