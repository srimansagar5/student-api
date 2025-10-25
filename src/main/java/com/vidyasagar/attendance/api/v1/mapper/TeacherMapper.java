package com.vidyasagar.attendance.api.v1.mapper;

import com.vidyasagar.attendance.api.v1.dto.request.TeacherRequestDTO;
import com.vidyasagar.attendance.api.v1.dto.response.TeacherResponseDTO;
import com.vidyasagar.attendance.entity.Course;
import com.vidyasagar.attendance.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface TeacherMapper {

    Teacher toEntity(TeacherRequestDTO dto);

    @Mapping(target = "courses", expression = "java(mapCourseTitles(teacher.getCourses()))")
    TeacherResponseDTO toDTO(Teacher teacher);

    // ✅ Helper method to handle list of Course → list of titles
    default List<String> mapCourseTitles(List<Course> courses) {
        if (courses == null) return null;
        return courses.stream()
                .map(Course::getTitle)
                .collect(Collectors.toList());
    }
}