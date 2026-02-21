package com.example.issuetracker.dto;

import com.example.issuetracker.entity.Issue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    private boolean archived;
    private Instant archivedAt;
    private List<IssueResponseDTO> issues;

}
