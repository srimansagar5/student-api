package com.vidyasagar.attendance.api.v1.mapper;

import com.vidyasagar.attendance.api.v1.dto.response.FacultyDTO;
import com.vidyasagar.attendance.entity.Faculty;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
    FacultyDTO toDTO(Faculty faculty);

    Faculty toEntity(FacultyDTO dto);

    List<FacultyDTO> toDTOList(List<Faculty> faculties);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateFacultyFromDTO(FacultyDTO dto, @MappingTarget Faculty entity);
}
