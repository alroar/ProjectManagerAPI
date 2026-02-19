package com.example.issuetracker.controller;

import com.example.issuetracker.dto.ProjectDTO;
import com.example.issuetracker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/create")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody @Valid ProjectDTO projectDTO){
        ProjectDTO createdProject = projectService.createProject(projectDTO);
        return ResponseEntity.status(201).body(createdProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) throws Exception {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) throws Exception {
        ProjectDTO project = projectService.updateProject(id, projectDTO);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) throws Exception {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
