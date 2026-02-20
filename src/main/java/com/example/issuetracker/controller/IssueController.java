package com.example.issuetracker.controller;

import com.example.issuetracker.dto.IssueResponseDTO;
import com.example.issuetracker.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issue")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService){
        this.issueService = issueService;
    }

    @GetMapping
    public ResponseEntity<List<IssueResponseDTO>> getIssues(@)

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> getIssueById(@PathVariable Long id) throws Exception {
        IssueResponseDTO issueResponseDTO = issueService.getIssueById(id);

        return ResponseEntity.ok(issueResponseDTO);
    }


    @PostMapping("/create")
    public ResponseEntity<IssueResponseDTO> createIssue(@RequestBody @Valid IssueResponseDTO issueResponseDTO) throws Exception {

        IssueResponseDTO newDto = issueService.createIssue(issueResponseDTO);

        return ResponseEntity.status(201).body(newDto);
    }


}
