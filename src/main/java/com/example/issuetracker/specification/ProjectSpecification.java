package com.example.issuetracker.specification;


import com.example.issuetracker.entity.Project;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {

    public ProjectSpecification(){
        throw new IllegalStateException("Utility class");
    }

    public static Specification<Project> isArchived(Boolean archived){
        if(archived == null){
            return null;
        }else{
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("archived"), archived);
        }
    }



}
