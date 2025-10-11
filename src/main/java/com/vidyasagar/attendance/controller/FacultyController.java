package com.vidyasagar.attendance.controller;

import com.vidyasagar.attendance.entity.Faculty;
import com.vidyasagar.attendance.service.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    // Constructor Injection
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    //Then your endpoints become /api/faculty, /api/hello-faculty, etc.
    @GetMapping("hello-faculty")
    public String getFacultyMessage() {
        return  facultyService.getFacultyMessage();
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty savedFaculty = facultyService.saveFaculty(faculty);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedFaculty.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(savedFaculty);
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculty() {
        List<Faculty> faculties =  facultyService.getAllFaculty();
        return ResponseEntity.ok(faculties);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty facultyDetails) {
        Faculty updatedFaculty = facultyService.updateFaculty(id, facultyDetails);

        return ResponseEntity
                .status(HttpStatus.OK) // HTTP 200
                .body(updatedFaculty);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
