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
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("archived"), archived);

    }

    // Name filter
    public static Specification<Project> hasName(String name){
        if(name == null || name.isEmpty()){
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        return (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");

    }

    public static Specification<Project> createdAfter(java.time.Instant createdAfter){
        if(createdAfter == null){
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        return (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAfter);

    }

    public static Specification<Project> createdBefore(java.time.Instant createdBefore){
        if(createdBefore == null){
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdBefore);

    }



}
