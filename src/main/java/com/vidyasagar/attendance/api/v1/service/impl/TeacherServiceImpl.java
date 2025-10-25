package com.vidyasagar.attendance.api.v1.service.impl;

import com.vidyasagar.attendance.api.v1.dto.request.TeacherRequestDTO;
import com.vidyasagar.attendance.api.v1.dto.response.TeacherResponseDTO;
import com.vidyasagar.attendance.api.v1.mapper.TeacherMapper;
import com.vidyasagar.attendance.api.v1.repository.TeacherRepository;
import com.vidyasagar.attendance.api.v1.service.TeacherService;
import com.vidyasagar.attendance.entity.Teacher;
import com.vidyasagar.attendance.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TeacherResponseDTO getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .map(teacherMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id " + id));
    }

    public TeacherResponseDTO createTeacher(TeacherRequestDTO teacherRequestDTO) {
        Teacher teacher = teacherMapper.toEntity(teacherRequestDTO);
        return teacherMapper.toDTO(teacherRepository.save(teacher));
    }

    public TeacherResponseDTO updateTeacher(Long id, TeacherRequestDTO teacherRequestDTO) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id " + id));
        existing.setName(teacherRequestDTO.getName());
        existing.setEmail(teacherRequestDTO.getEmail());
        return teacherMapper.toDTO(teacherRepository.save(existing));
    }

    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id " + id));
        teacherRepository.deleteById(id);
    }
}
