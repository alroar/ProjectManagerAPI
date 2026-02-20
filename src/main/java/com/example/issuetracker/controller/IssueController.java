package com.example.issuetracker.controller;

import com.example.issuetracker.dto.*;
import com.example.issuetracker.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService){
        this.issueService = issueService;
    }

    // Create issue
    @PostMapping
    public ResponseEntity<IssueResponseDTO> createIssue(@RequestBody @Valid IssueCreateDTO issueCreateDTO) throws Exception {

        IssueResponseDTO newDto = issueService.createIssue(issueCreateDTO);

        return ResponseEntity.status(201).body(newDto);
    }

    // Get all issues
    @GetMapping
    public ResponseEntity<List<IssueResponseDTO>> getIssues(){
        List<IssueResponseDTO> issuesList = issueService.getAllIssues();
        return ResponseEntity.ok(issuesList);
    }

    // Get issue by id
    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> getIssueById(@PathVariable Long id) throws Exception {
        IssueResponseDTO issueResponseDTO = issueService.getIssueById(id);

        return ResponseEntity.ok(issueResponseDTO);
    }

    // Update issue
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> updateIssue(@PathVariable Long id, @RequestBody @Valid IssueUpdateDTO issueUpdateDTO) throws Exception {

        IssueResponseDTO issueUpdated = issueService.updateIssue(id, issueUpdateDTO);

        return ResponseEntity.ok(issueUpdated);

    }

    // Update Status of an issue
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<IssueResponseDTO> updateIssueStatus(@PathVariable Long id, @RequestBody @Valid IssueStatusUpdateDTO issueStatusUpdateDTO) throws Exception {
       IssueResponseDTO statusUpdated = issueService.changeIssueStatus(id, issueStatusUpdateDTO);

       return ResponseEntity.ok(statusUpdated);
    }

    // Assign User to an issue
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/assign/{userId}")
    public ResponseEntity<IssueResponseDTO> assignUserToIssue(@PathVariable Long id, @PathVariable Long userId) throws Exception {
        IssueResponseDTO issueUpdated = issueService.assignUserToIssue(id, userId);

        return ResponseEntity.ok(issueUpdated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/archive")
    public ResponseEntity<IssueResponseDTO> archiveIssue(@PathVariable Long id, @RequestBody @Valid IssueArchiveDTO issueArchiveDTO){
        IssueResponseDTO updatedIssue = issueService.archiveIssue(id, issueArchiveDTO.isArchived());

        return ResponseEntity.ok(updatedIssue);
    }



}
