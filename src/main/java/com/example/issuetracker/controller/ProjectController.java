package com.example.issuetracker.controller;

import com.example.issuetracker.dto.ProjectArchiveDTO;
import com.example.issuetracker.dto.ProjectCreateDTO;
import com.example.issuetracker.dto.ProjectResponseDTO;
import com.example.issuetracker.dto.ProjectUpdateDTO;
import com.example.issuetracker.exceptions.UserNotFoundException;
import com.example.issuetracker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getProjects(){
        List<ProjectResponseDTO> projectList = projectService.getAllProjects();
        return ResponseEntity.ok(projectList);
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody @Valid ProjectCreateDTO projectCreateDTO){
        ProjectResponseDTO createdProject = projectService.createProject(projectCreateDTO);
        return ResponseEntity.status(201).body(createdProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) throws Exception {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectUpdateDTO projectUpdateDTO) throws Exception {
        ProjectResponseDTO project = projectService.updateProject(id, projectUpdateDTO);
        return ResponseEntity.ok(project);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/archive")
    public ResponseEntity<ProjectResponseDTO> archiveProject(@PathVariable Long id, @RequestBody @Valid ProjectArchiveDTO projectArchiveDTO) throws Exception {
        ProjectResponseDTO archivedProject = projectService.archiveProject(id, projectArchiveDTO.isArchived());

        return ResponseEntity.ok(archivedProject);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{projectId}/assignUser/{userId}")
    public ResponseEntity<ProjectResponseDTO> assignUserToProject(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long userId
    ) throws Exception {
        ProjectResponseDTO projectUpdated = projectService.assignUserToProject(projectId, userId);
        return ResponseEntity.ok(projectUpdated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{projectId}/removeUser/{userId}")
    public ResponseEntity<ProjectResponseDTO> removeUserFromProject(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long userId
    ) throws Exception{
        ProjectResponseDTO projectUpdated = projectService.removeUserFromProject(projectId, userId);
        return ResponseEntity.ok(projectUpdated);
    }



}
