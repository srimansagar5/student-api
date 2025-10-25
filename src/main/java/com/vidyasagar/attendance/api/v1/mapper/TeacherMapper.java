package com.vidyasagar.attendance.api.v1.mapper;

import com.vidyasagar.attendance.api.v1.dto.request.TeacherRequestDTO;
import com.vidyasagar.attendance.api.v1.dto.response.TeacherResponseDTO;
import com.vidyasagar.attendance.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    Teacher toEntity(TeacherRequestDTO dto);

    @Mapping(target = "courses",
            expression = "java(teacher.getCourses() == null ? null : teacher.getCourses().stream().map(c -> c.getTitle()).toList())")
    TeacherResponseDTO toDTO(Teacher teacher);
}
