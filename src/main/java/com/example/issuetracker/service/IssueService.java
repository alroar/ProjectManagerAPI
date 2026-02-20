package com.example.issuetracker.service;

import com.example.issuetracker.dto.*;
import com.example.issuetracker.entity.Issue;
import com.example.issuetracker.entity.IssueStatus;
import com.example.issuetracker.entity.Project;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.BussinessException;
import com.example.issuetracker.exceptions.IssueNotFoundException;
import com.example.issuetracker.exceptions.ProjectNotFoundException;
import com.example.issuetracker.exceptions.UserNotFoundException;
import com.example.issuetracker.mappers.IssueMapper;
import com.example.issuetracker.repository.IssueRepository;
import com.example.issuetracker.repository.ProjectRepository;
import com.example.issuetracker.repository.UserRepository;
import com.example.issuetracker.util.IssueTransitions;
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

    public IssueResponseDTO createIssue(IssueCreateDTO issueCreateDTO) throws Exception{

        projectRepository.findById(issueCreateDTO.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        Issue issue = issueMapper.toEntity(issueCreateDTO);
        issue.setIssueStatus(IssueStatus.OPEN);
        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toDTO(savedIssue);
    }

    public IssueResponseDTO getIssueById(Long id) throws Exception{
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

    public IssueResponseDTO updateIssue(Long id, IssueUpdateDTO issueUpdateDTO) throws Exception{
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));
        Project project = projectRepository.findById(issueUpdateDTO.getProjectId())
                        .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        issueMapper.updateEntityFromDTO(issueUpdateDTO, issue);
        issue.setProject(project);
        issueRepository.save(issue);

        return issueMapper.toDTO(issue);
    }

    public void deleteIssue(Long id) throws Exception{
        issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));
        issueRepository.deleteById(id);
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
            throw new BussinessException("Cannot assign user to a CLOSED issue");
        }

        return issueMapper.toDTO(issue);
    }

    public IssueResponseDTO changeIssueStatus(Long id, IssueStatusUpdateDTO issueStatusUpdateDTO) throws Exception{
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));

        if(!IssueTransitions.isValidTransition(issue.getIssueStatus(), issueStatusUpdateDTO.getIssueStatus())){
            throw new BussinessException("Invalid transition. Cannot move from "
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
            throw new BussinessException("Only CLOSED issues can be archived");
        }

        issue.setArchived(archived);
        issue.setArchivedAt(archived ? Instant.now() : null);
        issueRepository.save(issue);

        return issueMapper.toDTO(issue);

    }
}
