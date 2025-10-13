package com.vidyasagar.attendance.api.v1.repository;

import com.vidyasagar.attendance.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
