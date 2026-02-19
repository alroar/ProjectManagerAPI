package com.example.issuetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    @OneToMany(mappedBy = "project")
    private List<Issue> issues;

    @PrePersist
    public void prePersist(){
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void preUpdate(){
        this.updatedAt = Instant.now();
    }
}
