package com.example.issuetracker.service;

import com.example.issuetracker.dto.*;
import com.example.issuetracker.entity.Issue;
import com.example.issuetracker.entity.IssueStatus;
import com.example.issuetracker.entity.Project;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.*;
import com.example.issuetracker.mappers.IssueMapper;
import com.example.issuetracker.repository.IssueRepository;
import com.example.issuetracker.repository.ProjectRepository;
import com.example.issuetracker.repository.UserRepository;

import com.example.issuetracker.specification.IssueSpecification;
import com.example.issuetracker.util.IssueTransitions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public IssueService(IssueRepository issueRepository,
                        IssueMapper issueMapper,
                        ProjectRepository projectRepository,
                        UserRepository userRepository){
        this.issueRepository = issueRepository;
        this.issueMapper = issueMapper;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public IssueResponseDTO createIssue(IssueCreateDTO issueCreateDTO, @AuthenticationPrincipal String username){

        Project project = projectRepository.findById(issueCreateDTO.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        if(project.isArchived()){
            throw new ProjectArchivedException("Project is archived");
        }

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(project.getUsers().stream().noneMatch((u -> u.getUserName().equals(username)))){
            throw new AccessDeniedException("User is not working on this project");
        }

        Issue issue = issueMapper.toEntity(issueCreateDTO);
        issue.setProject(project);
        issue.setIssueStatus(IssueStatus.OPEN);
        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toDTO(savedIssue);
    }


    public IssueResponseDTO getIssueById(Long id){
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));

        return issueMapper.toDTO(issue);
    }

    public List<IssueResponseDTO> getAllIssues(){

        List<Issue> issues = issueRepository.findAll();

        return issues.stream()
                .map(issue -> issueMapper.toDTO(issue))
                .collect(Collectors.toList());
    }

    public IssueResponseDTO updateIssue(Long id, IssueUpdateDTO issueUpdateDTO){
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));

        if(issue.isArchived()){
            throw new IssueArchivedException("Cannot update an archived issue");
        }

        issueMapper.updateEntityFromDTO(issueUpdateDTO, issue);

        Issue updatedIssue = issueRepository.save(issue);

        return issueMapper.toDTO(updatedIssue);
    }


    public IssueResponseDTO assignUserToIssue(Long issueId, Long userId) throws Exception{
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));

        if(issue.getIssueStatus() != IssueStatus.CLOSED){
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User couldn't be found"));
            issue.setUser(user);
            issueRepository.save(issue);
        }else{
            throw new BusinessException("Cannot assign user to a CLOSED issue");
        }

        return issueMapper.toDTO(issue);
    }

    public IssueResponseDTO changeIssueStatus(Long id, IssueStatusUpdateDTO issueStatusUpdateDTO){
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));

        if(!IssueTransitions.isValidTransition(issue.getIssueStatus(), issueStatusUpdateDTO.getIssueStatus())){
            throw new BusinessException("Invalid transition. Cannot move from "
            +issue.getIssueStatus() + " to " + issueStatusUpdateDTO.getIssueStatus() + ". Allowed: " + IssueTransitions.VALID_TRANSITIONS.get(issue.getIssueStatus()));
        }

        issueMapper.updateStatusFromDTO(issueStatusUpdateDTO, issue);
        issueRepository.save(issue);
        return issueMapper.toDTO(issue);
    }

    public IssueResponseDTO archiveIssue(Long id, boolean archived){

        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));

        if(archived && issue.getIssueStatus() != IssueStatus.CLOSED){
            throw new BusinessException("Only CLOSED issues can be archived");
        }

        IssueArchiveDTO archiveDTO = new IssueArchiveDTO();
        archiveDTO.setArchived(archived);
        archiveDTO.setArchivedAt(archived ? Instant.now() : null);
        issueMapper.updateArchiveFromDTO(archiveDTO, issue);
        issueRepository.save(issue);

        return issueMapper.toDTO(issue);

    }

    // Get Filtered Issues
    public Page<IssueResponseDTO> getFilteredIssues(IssueStatus status, Long userId, Boolean archived, Pageable pageable){

        Specification<Issue> statusSpec = (status != null) ? IssueSpecification.hasStatus(status) : null;
        Specification<Issue> userSpec = (userId != null) ? IssueSpecification.hasUser(userId) : null;
        Specification<Issue> archivedSpec = (archived != null) ? IssueSpecification.isArchived(archived) : null;

        Specification<Issue> combinedSpec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        if(statusSpec != null) combinedSpec = combinedSpec.and(statusSpec);
        if (userSpec != null) combinedSpec = combinedSpec.and(userSpec);
        if (archivedSpec != null) combinedSpec = combinedSpec.and(archivedSpec);

        Page<Issue> page = issueRepository.findAll(combinedSpec, pageable);
        List<IssueResponseDTO> dtos = page.getContent().stream().map(issue -> issueMapper.toDTO(issue)).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }
}
