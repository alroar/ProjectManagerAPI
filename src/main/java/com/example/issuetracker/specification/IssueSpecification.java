package com.example.issuetracker.specification;

import com.example.issuetracker.entity.Issue;
import com.example.issuetracker.entity.IssueStatus;
import org.springframework.data.jpa.domain.Specification;
public class IssueSpecification {

    private IssueSpecification(){
        throw new IllegalStateException("Utility class");
    }

    public static Specification<Issue> hasStatus(IssueStatus status){
        if (status == null){
            return null;
        }else{
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("issueStatus"), status);
        }
    }

    public static Specification<Issue> hasUser(Long userId){
        if(userId == null){
            return null;
        }else{
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("user").get("id"), userId);
        }
    }

    public static Specification<Issue> isArchived(Boolean archived){
        if(archived == null){
            return null;
        }else{
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("archived"), archived);
        }
    }
}
