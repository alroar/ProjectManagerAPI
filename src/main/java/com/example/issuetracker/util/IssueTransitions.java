package com.example.issuetracker.util;

import com.example.issuetracker.entity.IssueStatus;

import java.util.List;
import java.util.Map;

public class IssueTransitions {
    public static final Map<IssueStatus, List<IssueStatus>> VALID_TRANSITIONS =
            Map.of(
                    IssueStatus.OPEN, List.of(IssueStatus.IN_PROGRESS, IssueStatus.RESOLVED),
                    IssueStatus.IN_PROGRESS, List.of(IssueStatus.RESOLVED),
                    IssueStatus.RESOLVED, List.of(IssueStatus.OPEN, IssueStatus.CLOSED),
                    IssueStatus.CLOSED, List.of()
            );

    public static boolean isValidTransition(IssueStatus from, IssueStatus to){
        return VALID_TRANSITIONS.get(from).contains(to);
    }
}
