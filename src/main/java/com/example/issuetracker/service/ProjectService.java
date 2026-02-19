package com.example.issuetracker.service;

import com.example.issuetracker.dto.ProjectDTO;
import com.example.issuetracker.entity.Project;
import com.example.issuetracker.exceptions.ProjectNotFoundException;
import com.example.issuetracker.mappers.ProjectMapper;
import com.example.issuetracker.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectMapper projectMapper){
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

<<<<<<< HEAD
    public ProjectDTO createProject(ProjectDTO projectDTO){
=======
    public ProjectDTO createProyect(ProjectDTO projectDTO){
>>>>>>> abccc12 (Creación de los servicios y repositorios de Issue y Project)
        Project project = projectMapper.toEntity(projectDTO);
        Project savedProject = projectRepository.save(project);
        return projectMapper.toDTO(savedProject);
    }

    public ProjectDTO getProjectById(Long id) throws Exception{
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

       return projectMapper.toDTO(project);
    }

    public List<ProjectDTO> getAllProjects(){
        List<Project> projects = projectRepository.findAll();

        return projects.stream().map(project -> projectMapper.toDTO(project))
                .collect(Collectors.toList());
    }

    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) throws Exception{
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        Project savedProject = projectRepository.save(project);

        return projectMapper.toDTO(savedProject);
    }

    public void deleteById(Long id) throws Exception{
        projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project couldn't be found"));

        projectRepository.deleteById(id);
    }





}
