package com.example.issuetracker.mappers;

import com.example.issuetracker.dto.ProjectArchiveDTO;
import com.example.issuetracker.dto.ProjectCreateDTO;
import com.example.issuetracker.dto.ProjectResponseDTO;
import com.example.issuetracker.dto.ProjectUpdateDTO;
import com.example.issuetracker.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {IssueMapper.class})
public interface ProjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "issues", ignore = true)
    @Mapping(target = "archived", ignore = true)
    Project toEntity(ProjectCreateDTO projectCreateDTO);


    @Mapping(target = "issues", source = "issues")
    ProjectResponseDTO toDTO(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "issues", ignore = true)
    @Mapping(target = "archived", ignore = true)
    void updateProjectFromDTO(ProjectUpdateDTO projectUpdateDTO, @MappingTarget Project project);

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "issues", ignore = true)
    void updateArchiveFromDTO(ProjectArchiveDTO archiveDTO, @MappingTarget Project project);


}
