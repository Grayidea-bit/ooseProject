package org.fcu.ooseproject.service;

import org.fcu.ooseproject.entity.Issue;
import org.fcu.ooseproject.entity.type.IssueType;
import org.fcu.ooseproject.entity.type.StatusType;
import org.fcu.ooseproject.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class IssueService {
    
    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public List<Issue> findByType(IssueType type) {
        return issueRepository.findByType(type);
    }

    public List<Issue> findByStatus(StatusType status) {
        return issueRepository.findByStatus(status);
    }

    
    public List<Issue> getAllIssues(Sort sort) {
        return issueRepository.findAll(sort);
    }

    public List<Issue> getAllIssuesWithSortFilter(Sort sort, String type, String status, String keyword, LocalDate before) {
        // 獲取排序後的資料
        List<Issue> issues = issueRepository.findAll(sort != null ? sort : Sort.by(Sort.Direction.ASC, "id"));
        
        Predicate<Issue> filter = issue -> true;

        // 1. 篩選 type
        if (type != null && !type.equalsIgnoreCase("All Type")) {
            Set<IssueType> typeFilters = Arrays.stream(type.split(","))
                                            .map(String::trim)
                                            .map(IssueType::valueOf)
                                            .collect(Collectors.toSet());
            filter = filter.and(issue -> typeFilters.contains(issue.getType()));
        }

        // 2. 篩選 status
        if (status != null && !status.equalsIgnoreCase("All Status")) {
            Set<StatusType> statusFilters = Arrays.stream(status.split(","))
                                                .map(s -> s.trim().equalsIgnoreCase("TO-DO") ? "TODO" : s.trim())
                                                .map(StatusType::valueOf)
                                                .collect(Collectors.toSet());
            filter = filter.and(issue -> statusFilters.contains(issue.getStatus()));
        }

        // 3. 模糊關鍵字（title 或 description 含關鍵字）
        if (keyword != null && !keyword.isBlank()) {
            String lowerKeyword = keyword.toLowerCase();
            filter = filter.and(issue -> 
                (issue.getTitle() != null && issue.getTitle().toLowerCase().contains(lowerKeyword)) ||
                (issue.getDescription() != null && issue.getDescription().toLowerCase().contains(lowerKeyword))
            );
        }

        // 4. 截止日期 before（包含當天）
        if (before != null) {
            filter = filter.and(issue -> {
                Date deadline = issue.getDeadline();
                return deadline != null && !deadline.toInstant().isAfter(before.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            });
        }
        return issues.stream().filter(filter).collect(Collectors.toList());
    }

    public void createIssue(Issue issue) {
        // 處理 Schedule 類型的特殊邏輯
        if (issue.getType() != IssueType.Schedule) {
            issue.setDeadline(null);
        }
        issueRepository.save(issue);
    }

    public Issue getIssueById(Long issueId) {
        return issueRepository.findById(issueId).orElse(null);
    }

    public void updateIssue(Long issueId, Issue issue) {
        // 設置 ID 確保更新而不是創建新記錄
        issue.setId(issueId);
        
        // 處理 Schedule 類型的特殊邏輯
        if (issue.getType() != IssueType.Schedule) {
            issue.setDeadline(null);
        }
        
        issueRepository.save(issue);
    }

    public void deleteIssue(Long issueId) {
        issueRepository.deleteById(issueId);
    }
}
