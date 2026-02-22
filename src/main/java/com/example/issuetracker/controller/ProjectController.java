package com.example.issuetracker.controller;

import com.example.issuetracker.dto.ProjectArchiveDTO;
import com.example.issuetracker.dto.ProjectCreateDTO;
import com.example.issuetracker.dto.ProjectResponseDTO;
import com.example.issuetracker.dto.ProjectUpdateDTO;
import com.example.issuetracker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    // Get All Projects
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getProjects(){
        List<ProjectResponseDTO> projectList = projectService.getAllProjects();
        return ResponseEntity.ok(projectList);
    }

    // Create new Project
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody @Valid ProjectCreateDTO projectCreateDTO){
        ProjectResponseDTO createdProject = projectService.createProject(projectCreateDTO);
        return ResponseEntity.status(201).body(createdProject);
    }

    // Get Project by Id
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    // Update Project
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectUpdateDTO projectUpdateDTO) {
        ProjectResponseDTO project = projectService.updateProject(id, projectUpdateDTO);
        return ResponseEntity.ok(project);
    }

    // Archive Project
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/archive")
    public ResponseEntity<ProjectResponseDTO> archiveProject(@PathVariable Long id, @RequestBody @Valid ProjectArchiveDTO projectArchiveDTO) {
        ProjectResponseDTO archivedProject = projectService.archiveProject(id, projectArchiveDTO.isArchived());

        return ResponseEntity.ok(archivedProject);
    }

    // Assign User to Project
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{projectId}/assignUser/{userId}")
    public ResponseEntity<ProjectResponseDTO> assignUserToProject(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long userId
    ) throws Exception {
        ProjectResponseDTO projectUpdated = projectService.assignUserToProject(projectId, userId);
        return ResponseEntity.ok(projectUpdated);
    }

    // Remove User From Project
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{projectId}/removeUser/{userId}")
    public ResponseEntity<ProjectResponseDTO> removeUserFromProject(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long userId
    ) throws Exception{
        ProjectResponseDTO projectUpdated = projectService.removeUserFromProject(projectId, userId);
        return ResponseEntity.ok(projectUpdated);
    }


    // Filtered
    @GetMapping("/filtered")
    public ResponseEntity<Page<ProjectResponseDTO>> getFilteredProjects(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean archived,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdBefore,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
            ){
        Page<ProjectResponseDTO> page = projectService.getFilteredProjects(name, archived, createdAfter, createdBefore, pageable);

        return ResponseEntity.ok(page);
    }


}
