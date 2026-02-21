package com.example.issuetracker.mappers;

import com.example.issuetracker.dto.ProjectCreateDTO;
import com.example.issuetracker.dto.ProjectDTO;
import com.example.issuetracker.dto.ProjectUpdateDTO;
import com.example.issuetracker.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "issues", ignore = true)
    Project toEntity(ProjectCreateDTO projectCreateDTO);


    ProjectDTO toDTO(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "issues", ignore = true)
    void updateProjectFromDTO(ProjectUpdateDTO projectUpdateDTO, @MappingTarget Project project);

}
