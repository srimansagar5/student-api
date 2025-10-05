package com.vidyasagar.attendance.repository;

import com.vidyasagar.attendance.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
// you get CRUD methods for free: save, findById, findAll, deleteById
}
