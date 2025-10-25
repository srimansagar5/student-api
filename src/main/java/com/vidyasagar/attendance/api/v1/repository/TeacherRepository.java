package com.vidyasagar.attendance.api.v1.repository;

import com.vidyasagar.attendance.entity.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @EntityGraph(attributePaths = "courses")
    Optional<Teacher> findWithCoursesById(Long id);
}
