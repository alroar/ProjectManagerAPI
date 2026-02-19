package com.example.issuetracker.repository;

import com.example.issuetracker.entity.Role;
import com.example.issuetracker.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleName roleType);
}
