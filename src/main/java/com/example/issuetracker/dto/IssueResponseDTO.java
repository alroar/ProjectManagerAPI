package com.example.issuetracker.dto;

import com.example.issuetracker.entity.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponseDTO {

    private Long id;
    private String title;
    private String description;
    private IssueStatus issueStatus;
    private Long projectId;
    private Long userId;
    private Instant createdAt;
    private Instant updatedAt;
}
