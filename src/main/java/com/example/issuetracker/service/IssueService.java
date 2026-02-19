package com.example.issuetracker.service;

import com.example.issuetracker.dto.IssueDTO;
import com.example.issuetracker.entity.Issue;
import com.example.issuetracker.entity.Project;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.IssueNotFoundException;
import com.example.issuetracker.exceptions.ProjectNotFoundException;
import com.example.issuetracker.exceptions.UserNotFoundException;
import com.example.issuetracker.mappers.IssueMapper;
import com.example.issuetracker.repository.IssueRepository;
import com.example.issuetracker.repository.ProjectRepository;
import com.example.issuetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

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

    public IssueDTO createIssue(IssueDTO issueDTO) throws Exception{

        projectRepository.findById(issueDTO.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        if(issueDTO.getUserId() != null){
            userRepository.findById(issueDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User couldn't be found"));
        }

        Issue issue = issueMapper.toEntity(issueDTO);
        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toDTO(savedIssue);
    }

    public IssueDTO getIssueById(Long id) throws Exception{
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));

        return issueMapper.toDTO(issue);
    }

    public List<IssueDTO> getAllIssues(){
        List<Issue> issues = issueRepository.findAll();

        return issues.stream()
                .map(issue -> issueMapper.toDTO(issue))
                .collect(Collectors.toList());
    }

    public IssueDTO updateIssue(Long id, IssueDTO issueDTO) throws Exception{
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));
        if(issueDTO.getUserId() != null){
            User user = userRepository.findById(issueDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User couldn't be found"));
            issue.setUser(user);
        }
        Project project = projectRepository.findById(issueDTO.getProjectId())
                        .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        issue.setTitle(issueDTO.getTitle());
        issue.setDescription(issueDTO.getDescription());
        issue.setIssueStatus(issueDTO.getIssueStatus());

        issue.setProject(project);
        issueRepository.save(issue);

        return issueMapper.toDTO(issue);
    }
}
