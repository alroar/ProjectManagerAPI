package com.example.issuetracker.mappers;


import com.example.issuetracker.dto.IssueDTO;
import com.example.issuetracker.entity.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IssueMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "user.id", target = "userId")
    IssueDTO toDTO(Issue issue);

    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "userId", target = "user.id")
    Issue toEntity(IssueDTO issueDTO);
}
