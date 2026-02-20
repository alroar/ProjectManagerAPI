package com.example.issuetracker.mappers;

import com.example.issuetracker.dto.IssueCreateDTO;
import com.example.issuetracker.dto.IssueResponseDTO;
import com.example.issuetracker.dto.IssueStatusUpdateDTO;
import com.example.issuetracker.dto.IssueUpdateDTO;
import com.example.issuetracker.entity.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IssueMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "user.id", target = "userId")
    IssueResponseDTO toDTO(Issue issue);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "issueStatus", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "project", ignore = true)
    Issue toEntity(IssueCreateDTO issueCreateDTO);

    @Mapping(target = "issueStatus", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(IssueUpdateDTO updateDTO, @MappingTarget Issue issue);

    @Mapping(target = "title", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateStatusFromDTO(IssueStatusUpdateDTO statusDTO, @MappingTarget Issue issue);



}
