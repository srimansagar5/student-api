package com.vidyasagar.attendance.api.v1.mapper;

import com.vidyasagar.attendance.api.v1.dto.response.CourseDTO;
import com.vidyasagar.attendance.entity.Course;
import com.vidyasagar.attendance.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mappings({
            @Mapping(target = "studentIds", expression = "java(mapStudentIds(course.getStudents()))"),
            @Mapping(target = "studentNames", expression = "java(mapStudentNames(course.getStudents()))")
    })
    CourseDTO toDTO(Course course);

    @Mappings({
            @Mapping(target = "students", expression = "java(mapStudents(courseDTO.getStudentIds()))")
    })
    Course toEntity(CourseDTO courseDTO);

    List<CourseDTO> toDTOList(List<Course> courses);

    // Helper methods
    default List<Long> mapStudentIds(List<Student> students) {
        if (students == null) return null;
        return students.stream()
                .map(Student::getId)
                .collect(Collectors.toList());
    }

    default List<String> mapStudentNames(List<Student> students) {
        if (students == null) return null;
        return students.stream()
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    default List<Student> mapStudents(List<Long> studentIds) {
        if (studentIds == null) return null;
        return studentIds.stream()
                .map(id -> {
                    Student s = new Student();
                    s.setId(id);
                    return s;
                })
                .collect(Collectors.toList());
    }
}
