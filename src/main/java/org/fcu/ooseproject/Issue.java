package org.fcu.ooseproject;

import java.util.Date;

public class Issue {
    private Long id;
    private String title;
    private IssueType type;
    private String description;
    private StatusType status;
    private Date deadline = null;
    private Date createdAt;

    public Issue() {}

    public Issue(String title, String description,IssueType type, StatusType status) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.status = status;
        this.deadline = null;
    }

    public Issue(String title, String description,IssueType type, StatusType status, Date deadline) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
    }

    public Issue(Long id, String title, String description,IssueType type, StatusType status, Date deadline, Date createdAt) {
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
