package com.example.issuetracker.controller;

import com.example.issuetracker.dto.ProjectCreateDTO;
import com.example.issuetracker.dto.ProjectDTO;
import com.example.issuetracker.dto.ProjectUpdateDTO;
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
    public ResponseEntity<List<ProjectDTO>> getProjects(){
        List<ProjectDTO> projectList = projectService.getAllProjects();

        return ResponseEntity.ok(projectList);
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody @Valid ProjectCreateDTO projectCreateDTO){
        ProjectDTO createdProject = projectService.createProject(projectCreateDTO);
        return ResponseEntity.status(201).body(createdProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) throws Exception {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectUpdateDTO projectUpdateDTO) throws Exception {
        ProjectDTO project = projectService.updateProject(id, projectUpdateDTO);
        return ResponseEntity.ok(project);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) throws Exception {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
