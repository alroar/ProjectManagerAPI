package com.example.issuetracker.repository;

import com.example.issuetracker.entity.Issue;
import com.example.issuetracker.entity.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {
    List<Issue> findByIssueStatus(IssueStatus status);
    List<Issue> findByUserId(Long userId);
}
