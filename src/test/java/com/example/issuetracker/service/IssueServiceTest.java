package com.example.issuetracker.service;

import com.example.issuetracker.dto.IssueCreateDTO;
import com.example.issuetracker.dto.IssueResponseDTO;
import com.example.issuetracker.entity.Issue;
import com.example.issuetracker.entity.IssueStatus;
import com.example.issuetracker.entity.Project;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.AccessDeniedException;
import com.example.issuetracker.exceptions.ProjectArchivedException;
import com.example.issuetracker.exceptions.ProjectNotFoundException;
import com.example.issuetracker.mappers.IssueMapper;
import com.example.issuetracker.repository.IssueRepository;
import com.example.issuetracker.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @Mock
    IssueRepository issueRepository;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    IssueMapper issueMapper;

    @InjectMocks
    IssueService issueService;

    // Create Issue Successfully
    @Test
    void shouldCreateIssueSuccessfullyWhenProjectExistsAndIsNotArchived(){
        // Objects test
        // Create users
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");

        //Create Project
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("This is a test");
        project.setArchived(false);
        project.setUsers(Set.of(user1, user2));

        // Create Issue
        IssueCreateDTO issueCreateDTO = new IssueCreateDTO();
        issueCreateDTO.setProjectId(1L);
        issueCreateDTO.setTitle("Test Issue");
        issueCreateDTO.setDescription("Test Issue Description");

        // Create Initial Issue
        Issue initialIssue = new Issue();
        initialIssue.setTitle(issueCreateDTO.getTitle());
        initialIssue.setDescription(issueCreateDTO.getDescription());
        initialIssue.setProject(project);
        initialIssue.setIssueStatus(IssueStatus.OPEN);

        // Create Saved Issue
        Issue savedIssue = new Issue();
        savedIssue.setId(100L);
        savedIssue.setTitle(issueCreateDTO.getTitle());
        savedIssue.setDescription(issueCreateDTO.getDescription());
        savedIssue.setProject(project);
        savedIssue.setIssueStatus(IssueStatus.OPEN);

        // Create Issue Response
        IssueResponseDTO issueResponseDTO = new IssueResponseDTO();
        issueResponseDTO.setId(savedIssue.getId());
        issueResponseDTO.setProjectId(project.getId());
        issueResponseDTO.setIssueStatus(savedIssue.getIssueStatus());

        // Mock configuration
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(issueMapper.toEntity(issueCreateDTO)).thenReturn(initialIssue);
        when(issueRepository.save(initialIssue)).thenReturn(savedIssue);
        when(issueMapper.toDTO(savedIssue)).thenReturn(issueResponseDTO);

        IssueResponseDTO result = issueService.createIssue(issueCreateDTO, user1);

        // Validations
        assertNotNull(result, "Result should not be null");
        assertEquals(100L, result.getId(), "Issue ID should be 100");
        assertEquals(1L, result.getProjectId(), "Project ID should be 1");
        assertEquals(IssueStatus.OPEN, result.getIssueStatus(), "Issue status should be OPEN");
        assertEquals(2, savedIssue.getProject().getUsers().size(),"Project should have users");
    }

    // Error creating issue from inexistent project
    @Test
    void shouldThrowExceptionWhenProjectDoesNotExist(){

        // Create users
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");

        // Create Issue
        IssueCreateDTO issueCreateDTO = new IssueCreateDTO();
        issueCreateDTO.setProjectId(99L);
        issueCreateDTO.setTitle("Issue without project");
        issueCreateDTO.setDescription("This issue has not project");

        // Mock: proyect does not exist
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        ProjectNotFoundException exception = assertThrows(ProjectNotFoundException.class, () -> {
            issueService.createIssue(issueCreateDTO, user1);});

        //  Validation
        assertEquals("Project couldn't be found", exception.getMessage());

    }

    // Error creating issue when project is archived
    @Test
    void shouldThrowExceptionWhenProjectIsArchived(){

        // Create users
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");

        // Archived project
        Project archivedProject = new Project();
        archivedProject.setId(1L);
        archivedProject.setName("Archived Project");
        archivedProject.setDescription("This project is archived");
        archivedProject.setArchived(true);
        archivedProject.setArchivedAt(Instant.now());
        archivedProject.setUsers(Set.of(user1, user2));

        // Create issue
        IssueCreateDTO issueCreateDTO = new IssueCreateDTO();
        issueCreateDTO.setProjectId(1L);
        issueCreateDTO.setTitle("Test Issue");
        issueCreateDTO.setDescription("This is a test issue");

        //Mock return archived project
        when(projectRepository.findById(1L)).thenReturn(Optional.of(archivedProject));
        ProjectArchivedException exception = assertThrows(ProjectArchivedException.class,
                () -> issueService.createIssue(issueCreateDTO, user1));

        assertEquals("Project is archived", exception.getMessage());
    }

    // Filtered Issues
    @Test
    void shouldGetFilteredIssues(){
        // Create user
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");

        // Create project
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setArchived(false);
        project.setUsers(Set.of(user1));

        // Create Issues
        Issue issue1 = new Issue();
        issue1.setId(1L);
        issue1.setTitle("Open issue");
        issue1.setDescription("Open issue");
        issue1.setIssueStatus(IssueStatus.OPEN);
        issue1.setProject(project);

        Issue issue2 = new Issue();
        issue2.setId(2L);
        issue2.setTitle("Closed issue");
        issue2.setDescription("Closed issue");
        issue2.setIssueStatus(IssueStatus.CLOSED);
        issue2.setProject(project);

        // Create DTOs
        IssueResponseDTO dto1 = new IssueResponseDTO();
        dto1.setId(issue1.getId());
        dto1.setProjectId(project.getId());
        dto1.setIssueStatus(issue1.getIssueStatus());

        IssueResponseDTO dto2 = new IssueResponseDTO();
        dto2.setId(issue2.getId());
        dto2.setProjectId(project.getId());
        dto2.setIssueStatus(issue2.getIssueStatus());

        // Pageable
        Pageable pageable = Pageable.ofSize(10);

        //Mock
        when(issueRepository.findAll((Specification<Issue>) any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(issue1, issue2), pageable, 2));
        when(issueMapper.toDTO(issue1)).thenReturn(dto1);
        when(issueMapper.toDTO(issue2)).thenReturn(dto2);

        // Filter by status OPEN
        Page<IssueResponseDTO> result = issueService.getFilteredIssues(IssueStatus.OPEN, null, false, pageable);

        // Asserts
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(2L, result.getContent().get(1).getId());
        assertEquals(IssueStatus.OPEN, result.getContent().get(0).getIssueStatus());
        assertEquals(IssueStatus.CLOSED, result.getContent().get(1).getIssueStatus());
    }

    // Filtered Issues By User
    @Test
    void shouldGetFilteredIssuesByUser(){

        // Create users
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");

        // Create project
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setArchived(false);
        project.setUsers(Set.of(user1, user2));

        // Create issues
        Issue issue1 = new Issue();
        issue1.setId(1L);
        issue1.setTitle("Issue assigned to Alice");
        issue1.setDescription("This issues is managed by Alice");
        issue1.setIssueStatus(IssueStatus.OPEN);
        issue1.setProject(project);

        Issue issue2 = new Issue();
        issue2.setId(2L);
        issue2.setTitle("Issue assigned to Bob");
        issue2.setDescription("This issue is managed by Bob");
        issue2.setIssueStatus(IssueStatus.OPEN);
        issue2.setProject(project);

        // Create DTOs
        IssueResponseDTO dto1 = new IssueResponseDTO();
        dto1.setId(issue1.getId());
        dto1.setProjectId(project.getId());
        dto1.setIssueStatus(issue1.getIssueStatus());

        IssueResponseDTO dto2 = new IssueResponseDTO();
        dto2.setId(issue2.getId());
        dto2.setProjectId(project.getId());
        dto2.setIssueStatus(issue2.getIssueStatus());

        // Pageable
        Pageable pageable = Pageable.ofSize(10);
        Page<Issue> mockedPage = new PageImpl<>(List.of(issue1), pageable, 1);

        // Mock
        when(issueRepository.findAll((Specification<Issue>) any(Specification.class), eq(pageable)))
                .thenReturn(mockedPage);

        when(issueMapper.toDTO(issue1)).thenReturn(dto1);

        Page<IssueResponseDTO> result = issueService.getFilteredIssues(null, 1L, false, pageable);

        // Asserts
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(IssueStatus.OPEN, result.getContent().get(0).getIssueStatus());

    }

    // Filtered Issue by combined filters
    @Test
    void shouldGetFilteredIssuesCombinedFilters() {
        // Create users
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setArchived(false);
        project.setUsers(Set.of(user1, user2));

        // Crear Issues
        Issue issue1 = new Issue();
        issue1.setId(1L);
        issue1.setTitle("Open Issue assigned to Alice");
        issue1.setIssueStatus(IssueStatus.OPEN);
        issue1.setProject(project);

        Issue issue2 = new Issue();
        issue2.setId(2L);
        issue2.setTitle("Closed Issue assigned to Bob");
        issue2.setIssueStatus(IssueStatus.CLOSED);
        issue2.setProject(project);

        Issue issue3 = new Issue();
        issue3.setId(3L);
        issue3.setTitle("Open Issue assigned to Bob");
        issue3.setIssueStatus(IssueStatus.OPEN);
        issue3.setProject(project);

        // DTOs
        IssueResponseDTO dto1 = new IssueResponseDTO();
        dto1.setId(issue1.getId());
        dto1.setProjectId(project.getId());
        dto1.setIssueStatus(issue1.getIssueStatus());

        IssueResponseDTO dto3 = new IssueResponseDTO();
        dto3.setId(issue3.getId());
        dto3.setProjectId(project.getId());
        dto3.setIssueStatus(issue3.getIssueStatus());

        // Pageable
        Pageable pageable = Pageable.ofSize(10);

        // Mock
        Page<Issue> mockedPage = new PageImpl<>(List.of(issue1, issue3), pageable, 2); // Solo issues que cumplen filtros

        when(issueRepository.findAll((Specification<Issue>) any(Specification.class), eq(pageable)))
                .thenReturn(mockedPage);

        when(issueMapper.toDTO(issue1)).thenReturn(dto1);
        when(issueMapper.toDTO(issue3)).thenReturn(dto3);

        // Act: filtrar por status OPEN, userId = 1L (Alice), archived = false
        Page<IssueResponseDTO> result = issueService.getFilteredIssues(IssueStatus.OPEN, 1L, false, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size(), "Debe devolver los issues que cumplen todos los filtros");
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(3L, result.getContent().get(1).getId());
    }

    // Empty page when no matching filters
    @Test
    void shouldReturnEmptyPageWhenNoIssuesMatchFilters() {

        // Pageable
        Pageable pageable = Pageable.ofSize(10);

        // Mock
        Page<Issue> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(issueRepository.findAll((Specification<Issue>) any(Specification.class), eq(pageable)))
                .thenReturn(emptyPage);

        Page<IssueResponseDTO> result =
                issueService.getFilteredIssues(IssueStatus.OPEN, 99L, false, pageable);

        // Assert
        assertNotNull(result, "El resultado no debe ser null");
        assertTrue(result.getContent().isEmpty(), "La lista de issues debe estar vacía");
        assertEquals(0, result.getTotalElements(), "El total de elementos debe ser 0");
    }

    // User is not working on project
    @Test
    void shouldThrowAccessDeniedWhenUserNotInProject() {

        // User working on project
        User member = new User();
        member.setId(1L);
        member.setName("Alice");

        // External user
        User outsider = new User();
        outsider.setId(99L);
        outsider.setName("Eve");

        // Project for Alice
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setArchived(false);
        project.setUsers(Set.of(member));

        // Issue DTO
        IssueCreateDTO dto = new IssueCreateDTO();
        dto.setProjectId(1L);
        dto.setTitle("Security Test");
        dto.setDescription("User not allowed");

        // Mock
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // Act
        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> issueService.createIssue(dto, outsider)
        );

        // Assert
        assertEquals("User is not working on this project", exception.getMessage());
    }

    @Test
    void shouldCreateIssueWhenUserIsMemberOfProject() {

        // Usuario miembro
        User member = new User();
        member.setId(1L);
        member.setName("Alice");

        // Proyecto con Alice
        Project project = new Project();
        project.setId(1L);
        project.setArchived(false);
        project.setUsers(Set.of(member));

        // Issue DTO
        IssueCreateDTO dto = new IssueCreateDTO();
        dto.setProjectId(1L);
        dto.setTitle("Authorized Test");
        dto.setDescription("User allowed");

        // Issue entity y saved
        Issue issue = new Issue();
        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setProject(project);
        issue.setIssueStatus(IssueStatus.OPEN);

        Issue savedIssue = new Issue();
        savedIssue.setId(10L);
        savedIssue.setTitle(dto.getTitle());
        savedIssue.setProject(project);
        savedIssue.setIssueStatus(IssueStatus.OPEN);

        IssueResponseDTO response = new IssueResponseDTO();
        response.setId(10L);
        response.setProjectId(1L);
        response.setIssueStatus(IssueStatus.OPEN);

        // Mocks
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(issueMapper.toEntity(dto)).thenReturn(issue);
        when(issueRepository.save(issue)).thenReturn(savedIssue);
        when(issueMapper.toDTO(savedIssue)).thenReturn(response);

        // Act
        IssueResponseDTO result = issueService.createIssue(dto, member);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(IssueStatus.OPEN, result.getIssueStatus());
    }

}
