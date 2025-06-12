package org.fcu.ooseproject.repository;

import java.util.List;

import org.fcu.ooseproject.entity.Issue;
import org.fcu.ooseproject.entity.type.IssueType;
import org.fcu.ooseproject.entity.type.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByType(IssueType type);
    List<Issue> findByStatus(StatusType status);
}