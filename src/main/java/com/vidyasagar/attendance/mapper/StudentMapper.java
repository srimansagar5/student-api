package com.vidyasagar.attendance.mapper;

import com.vidyasagar.attendance.entity.Student;
import com.vidyasagar.attendance.entity.StudentDTO;
import org.mapstruct.*;

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
