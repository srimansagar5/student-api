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
@RequestMapping("/api")
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

    @PostMapping("/faculty")
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

    @GetMapping("/faculty")
    public List<Faculty> getAllFaculty() {
        return facultyService.getAllFaculty();
    }

    @PutMapping("/faculty/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty facultyDetails) {
        Faculty updatedFaculty = facultyService.updateFaculty(id, facultyDetails);

        return ResponseEntity
                .status(HttpStatus.OK) // HTTP 200
                .body(updatedFaculty);
    }


    @DeleteMapping("/faculty/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
