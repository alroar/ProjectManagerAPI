package com.example.issuetracker.mappers;

import com.example.issuetracker.dto.ProjectDTO;
import com.example.issuetracker.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDTO toDTO(Project project);
    Project toEntity(ProjectDTO projectDTO);

}
