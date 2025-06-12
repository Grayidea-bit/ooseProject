package org.fcu.ooseproject.entity;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.time.LocalDate;

import org.fcu.ooseproject.entity.type.IssueType;
import org.fcu.ooseproject.entity.type.StatusType;

@Entity
@Table(name = "issues")
@Schema(description = "Issue Entity - Represents a task or schedule item")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the issue", example = "1")
    private Long id;

    @Schema(description = "Title of the issue", example = "Complete project documentation")
    private String title;

    @Schema(description = "Detailed description of the issue", example = "Write comprehensive documentation for the project including API endpoints and database schema")
    private String description;

    @Schema(description = "Type of the issue (Daily or Schedule)", example = "Daily")
    @Enumerated(EnumType.STRING)
    private IssueType type;

    @Schema(description = "Current status of the issue (TODO, InProgress, or Finished)", example = "TODO")
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @Schema(description = "Deadline for the issue", example = "2024-12-31")
    private Date deadline;

    private Date createdAt = new Date();

    public Issue() {
    }

    public Issue(String title, String description, IssueType type, StatusType status) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.status = status;
        this.deadline = null;
    }

    public Issue(String title, String description, IssueType type, StatusType status, Date deadline) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
    }

    public Issue(Long id, String title, String description, IssueType type, StatusType status, Date deadline,
            Date createdAt) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}



