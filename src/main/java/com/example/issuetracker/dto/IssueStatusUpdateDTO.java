package com.example.issuetracker.dto;

import com.example.issuetracker.entity.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueStatusUpdateDTO {

    private IssueStatus issueStatus;
}
