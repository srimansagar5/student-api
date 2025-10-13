package com.vidyasagar.attendance.api.v1.repository;

import com.vidyasagar.attendance.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
