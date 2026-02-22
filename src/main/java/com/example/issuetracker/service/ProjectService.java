package com.example.issuetracker.service;

import com.example.issuetracker.dto.ProjectArchiveDTO;
import com.example.issuetracker.dto.ProjectCreateDTO;
import com.example.issuetracker.dto.ProjectResponseDTO;
import com.example.issuetracker.dto.ProjectUpdateDTO;
import com.example.issuetracker.entity.Project;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.BussinessException;
import com.example.issuetracker.exceptions.ProjectArchivedException;
import com.example.issuetracker.exceptions.ProjectNotFoundException;
import com.example.issuetracker.exceptions.UserNotFoundException;
import com.example.issuetracker.mappers.ProjectMapper;
import com.example.issuetracker.repository.ProjectRepository;
import com.example.issuetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectMapper projectMapper,
                          UserRepository userRepository){
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userRepository = userRepository;
    }

    // Create new Project
    public ProjectResponseDTO createProject(ProjectCreateDTO projectDTO){
        Project project = projectMapper.toEntity(projectDTO);
        Project savedProject = projectRepository.save(project);
        return projectMapper.toDTO(savedProject);
    }

    // Get Project By Id
    public ProjectResponseDTO getProjectById(Long id){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

       return projectMapper.toDTO(project);
    }

    // Get All Projects
    public List<ProjectResponseDTO> getAllProjects(){
        List<Project> projects = projectRepository.findAll();

        return projects.stream().map(project -> projectMapper.toDTO(project))
                .collect(Collectors.toList());
    }

    // Update a Project
    public ProjectResponseDTO updateProject(Long id, ProjectUpdateDTO projectUpdateDTO){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        projectMapper.updateProjectFromDTO(projectUpdateDTO, project);
        Project savedProject = projectRepository.save(project);

        return projectMapper.toDTO(savedProject);
    }

    // Archive a Project
    public ProjectResponseDTO archiveProject(Long id, boolean archived){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        if(archived && project.isArchived()){
            throw new BussinessException("Project is already archived");
        }
        ProjectArchiveDTO archiveDTO = new ProjectArchiveDTO();
        archiveDTO.setArchived(archived);
        archiveDTO.setArchivedAt(archived ? Instant.now() : null);
        projectMapper.updateArchiveFromDTO(archiveDTO, project);

        projectRepository.save(project);

        return projectMapper.toDTO(project);
    }

    // Assign User To Project
    public ProjectResponseDTO assignUserToProject(Long projectId, Long userId) throws UserNotFoundException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        if (project.isArchived()){
            throw new ProjectArchivedException("Can't assign users to a closed project");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found"));

        project.getUsers().add(user);
        Project updatedProject = projectRepository.save(project);

        return projectMapper.toDTO(updatedProject);

    }

    // Remove User From Project
    public ProjectResponseDTO removeUserFromProject(Long projectId, Long userId) throws Exception{
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        if(project.isArchived()){
            throw new ProjectArchivedException("Can't remove users from a closed project");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found"));

        project.getUsers().remove(user);
        Project updatedProject = projectRepository.save(project);

        return projectMapper.toDTO(updatedProject);
    }





}
