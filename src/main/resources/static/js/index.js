class IssueTracker {
    constructor() {
        this.init();
    }

    init() {
        this.bindEvents();
        this.loadIssues();
    }

    bindEvents() {
        const form = document.getElementById('issueForm');
        form.addEventListener('submit', (e) => this.createIssue(e));
    }

    async loadIssues() {
        try {
            const response = await fetch('/get/all');
            const issues = await response.json();
            this.renderIssues(issues);
            this.updateIssueCount(issues.length);
        } catch (error) {
            console.error('Error loading issues:', error);
            this.showMessage('Failed to load issues', 'error');
        }
    }

    renderIssues(issues) {
        const issueList = document.getElementById('issueList');

        if (issues.length === 0) {
            issueList.innerHTML = this.getEmptyState();
            return;
        }

        issueList.innerHTML = issues.map(issue => this.createIssueHTML(issue)).join('');

        // Bind delete events
        issues.forEach(issue => {
            const deleteBtn = document.querySelector(`[data-issue-id="${issue.id}"]`);
            if (deleteBtn) {
                deleteBtn.addEventListener('click', () => this.deleteIssue(issue.id));
            }
        });
    }

    createIssueHTML(issue) {
        const formattedDeadline = issue.deadline ?
            new Date(issue.deadline).toLocaleDateString('en-US', {
                year: 'numeric',
                month: 'short',
                day: 'numeric'
            }) : 'No deadline';

        return `
            <div class="issue-item">
                <div class="issue-header">
                    <div>
                        <div class="issue-title">${this.escapeHtml(issue.title)}</div>
                        <div class="issue-id">#${issue.id}</div>
                    </div>
                </div>
                
                <div class="issue-meta">
                    <span class="issue-type ${issue.type.toLowerCase()}">${issue.type}</span>
                    <span class="issue-status ${issue.status.toLowerCase()}">${this.formatStatus(issue.status)}</span>
                </div>
                
                ${issue.description ? `<div class="issue-description">${this.escapeHtml(issue.description)}</div>` : ''}
                
                <div class="issue-deadline">📅 ${formattedDeadline}</div>
                
                <button class="delete-btn" data-issue-id="${issue.id}">
                    🗑️ Delete
                </button>
            </div>
        `;
    }

    getEmptyState() {
        return `
            <div class="empty-state">
                <h3>No issues yet</h3>
                <p>Create your first issue to get started!</p>
            </div>
        `;
    }

    formatStatus(status) {
        const statusMap = {
            'TODO': 'To-Do',
            'InProgress': 'In Progress',
            'Finished': 'Finished'
        };
        return statusMap[status] || status;
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    updateIssueCount(count) {
        const issueCount = document.getElementById('issueCount');
        issueCount.textContent = `${count} ${count === 1 ? 'issue' : 'issues'}`;
    }

    async createIssue(e) {
        e.preventDefault();

        const formData = this.getFormData();

        if (!this.validateForm(formData)) {
            return;
        }

        try {
            const response = await fetch('/submit', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                this.resetForm();
                this.showMessage('Issue created successfully!', 'success');
                await this.loadIssues();
            } else {
                throw new Error('Failed to create issue');
            }
        } catch (error) {
            console.error('Error creating issue:', error);
            this.showMessage('Failed to create issue', 'error');
        }
    }

    getFormData() {
        return {
            title: document.getElementById('issueTitle').value.trim(),
            type: document.getElementById('issueType').value,
            description: document.getElementById('issueDescription').value.trim(),
            status: document.getElementById('status').value,
            deadline: document.getElementById('deadline').value
        };
    }

    validateForm(formData) {
        if (!formData.title) {
            this.showMessage('Please enter a title', 'error');
            return false;
        }

        if (formData.type === 'Schedule' && !formData.deadline) {
            this.showMessage('Please select a deadline for Schedule type issues', 'error');
            return false;
        }

        return true;
    }

    resetForm() {
        document.getElementById('issueForm').reset();
    }

    async deleteIssue(issueId) {
        if (!confirm('Are you sure you want to delete this issue?')) {
            return;
        }

        try {
            const response = await fetch(`/delete/${issueId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                this.showMessage('Issue deleted successfully!', 'success');
                await this.loadIssues();
            } else {
                throw new Error('Failed to delete issue');
            }
        } catch (error) {
            console.error('Error deleting issue:', error);
            this.showMessage('Failed to delete issue', 'error');
        }
    }

    showMessage(message, type) {
        const messageEl = document.getElementById('formMessage');
        messageEl.textContent = message;
        messageEl.className = `form-message ${type}`;

        setTimeout(() => {
            messageEl.style.display = 'none';
        }, 5000);
    }
}

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new IssueTracker();
});