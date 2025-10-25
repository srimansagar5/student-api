package com.vidyasagar.attendance.api.v1.controller.Teacher;

import com.vidyasagar.attendance.api.v1.dto.request.TeacherRequestDTO;
import com.vidyasagar.attendance.api.v1.dto.response.TeacherResponseDTO;
import com.vidyasagar.attendance.api.v1.service.TeacherService;
import com.vidyasagar.attendance.entity.Teacher;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        List<TeacherResponseDTO> teacherResponseDTOS =  teacherService.getAllTeachers();
        return ResponseEntity.ok(teacherResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@PathVariable Long id) {
        TeacherResponseDTO teacherResponseDTO = teacherService.getTeacherById(id);
        return ResponseEntity.ok(teacherResponseDTO);
    }

    @PostMapping
    @Operation(summary = "Create new Teacher")
    public ResponseEntity<TeacherResponseDTO> createTeacher(@Valid @RequestBody TeacherRequestDTO teacherRequestDTO) {
        TeacherResponseDTO teacherResponseDTO = teacherService.createTeacher(teacherRequestDTO);
        URI locations = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(teacherResponseDTO.getId())
                .toUri();
        return ResponseEntity.created(locations).body(teacherResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(@PathVariable Long id, @RequestBody TeacherRequestDTO teacherRequestDTO) {
        TeacherResponseDTO teacherResponseDTO = teacherService.updateTeacher(id, teacherRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(teacherResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
