package com.vidyasagar.attendance.repository;

import com.vidyasagar.attendance.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
