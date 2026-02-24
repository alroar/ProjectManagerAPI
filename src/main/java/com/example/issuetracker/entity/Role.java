package com.example.issuetracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName roleType;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> users;

    public Role(Long id, RoleName roleType){
        this.id = id;
        this.roleType = roleType;
    }
}
