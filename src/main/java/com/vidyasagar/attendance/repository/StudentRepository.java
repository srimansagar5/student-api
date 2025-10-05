package com.vidyasagar.attendance.repository;

import com.vidyasagar.attendance.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
// you get CRUD methods for free: save, findById, findAll, deleteById

}
