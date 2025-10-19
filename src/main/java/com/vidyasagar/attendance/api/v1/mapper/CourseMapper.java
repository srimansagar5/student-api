package com.vidyasagar.attendance.api.v1.mapper;

import com.vidyasagar.attendance.api.v1.dto.response.CourseDTO;
import com.vidyasagar.attendance.api.v1.dto.response.StudentDTO;
import com.vidyasagar.attendance.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mappings({
            @Mapping(source = "student.id", target = "studentId"),
            @Mapping(source = "student.name", target = "studentName")
    })
    CourseDTO toDTO(Course course);

    @Mappings({
            @Mapping(source = "studentId", target = "student.id")
    })
    Course toEntity(CourseDTO courseDTO);

    List<CourseDTO> toDTOList(List<Course> courses);
}
