package com.example.issuetracker.service;

<<<<<<< HEAD
import com.example.issuetracker.dto.*;
import com.example.issuetracker.entity.Issue;
import com.example.issuetracker.entity.IssueStatus;
import com.example.issuetracker.entity.Project;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.BussinessException;
=======
import com.example.issuetracker.dto.IssueDTO;
import com.example.issuetracker.entity.Issue;
import com.example.issuetracker.entity.Project;
import com.example.issuetracker.entity.User;
>>>>>>> abccc12 (Creación de los servicios y repositorios de Issue y Project)
import com.example.issuetracker.exceptions.IssueNotFoundException;
import com.example.issuetracker.exceptions.ProjectNotFoundException;
import com.example.issuetracker.exceptions.UserNotFoundException;
import com.example.issuetracker.mappers.IssueMapper;
import com.example.issuetracker.repository.IssueRepository;
import com.example.issuetracker.repository.ProjectRepository;
import com.example.issuetracker.repository.UserRepository;
<<<<<<< HEAD
import com.example.issuetracker.util.IssueTransitions;
import org.springframework.stereotype.Service;

import java.time.Instant;
=======
import org.springframework.stereotype.Service;

>>>>>>> abccc12 (Creación de los servicios y repositorios de Issue y Project)
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

<<<<<<< HEAD
    public IssueResponseDTO createIssue(IssueCreateDTO issueCreateDTO) throws Exception{

        projectRepository.findById(issueCreateDTO.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        Issue issue = issueMapper.toEntity(issueCreateDTO);
        issue.setIssueStatus(IssueStatus.OPEN);
=======
    public IssueDTO createIssue(IssueDTO issueDTO) throws Exception{

        projectRepository.findById(issueDTO.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        if(issueDTO.getUserId() != null){
            userRepository.findById(issueDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User couldn't be found"));
        }

        Issue issue = issueMapper.toEntity(issueDTO);
>>>>>>> abccc12 (Creación de los servicios y repositorios de Issue y Project)
        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toDTO(savedIssue);
    }

<<<<<<< HEAD
    public IssueResponseDTO getIssueById(Long id) throws Exception{
=======
    public IssueDTO getIssueById(Long id) throws Exception{
>>>>>>> abccc12 (Creación de los servicios y repositorios de Issue y Project)
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));

        return issueMapper.toDTO(issue);
    }

<<<<<<< HEAD
    public List<IssueResponseDTO> getAllIssues(){
=======
    public List<IssueDTO> getAllIssues(){
>>>>>>> abccc12 (Creación de los servicios y repositorios de Issue y Project)
        List<Issue> issues = issueRepository.findAll();

        return issues.stream()
                .map(issue -> issueMapper.toDTO(issue))
                .collect(Collectors.toList());
    }

<<<<<<< HEAD
    public IssueResponseDTO updateIssue(Long id, IssueUpdateDTO issueUpdateDTO) throws Exception{
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue couldn't be found"));
        Project project = projectRepository.findById(issueUpdateDTO.getProjectId())
                        .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        issueMapper.updateEntityFromDTO(issueUpdateDTO, issue);
=======
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

>>>>>>> abccc12 (Creación de los servicios y repositorios de Issue y Project)
        issue.setProject(project);
        issueRepository.save(issue);

        return issueMapper.toDTO(issue);
    }
<<<<<<< HEAD

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
=======
>>>>>>> abccc12 (Creación de los servicios y repositorios de Issue y Project)
}
