package com.example.issuetracker.specification;


import com.example.issuetracker.entity.Project;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {

    public ProjectSpecification(){
        throw new IllegalStateException("Utility class");
    }

    // Archived status
    public static Specification<Project> isArchived(Boolean archived){
        if(archived == null){
            return null;
        }else{
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("archived"), archived);
        }
    }

    // Name filter
    public static Specification<Project> hasName(String name){
        if(name == null || name.isEmpty()){
            return null;
        }else{
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + name + "%");
        }
    }

    public static Specification<Project> createdAfter(java.time.Instant createdAt){
        if(createdAt == null){
            return null;
        }else{
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAt);
        }
    }



}
