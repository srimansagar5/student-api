package com.vidyasagar.attendance.api.v1.mapper;

import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.api.v1.dto.response.StudentDTO;
import org.mapstruct.*;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    // Single entity to DTO
    StudentDTO toDTO(Student student);

    //DTO to Entity (optional, if needed for POST/PUT)
    Student toEntity(StudentDTO dto);

    //List<Entity> -> List<DTO>
    List<StudentDTO> toDTOList(List<Student> students);

    //Update existing entity from DTO (Used for PUT)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateStudentFromDTO(StudentDTO dto, @MappingTarget Student entity);
}
