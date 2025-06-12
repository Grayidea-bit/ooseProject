class IssueTracker {
    constructor() {
        this.issues = [];
        this.init();
    }

    init() {
        this.loadIssuesFromDOM();
        this.bindEvents();
        window.issueTracker = this;
    }

    loadIssuesFromDOM() {
        // 從 DOM 中獲取所有 issue 元素
        const issueElements = document.querySelectorAll('.issue-item');
        this.issues = Array.from(issueElements).map(element => ({
            id: parseInt(element.dataset.issueId),
            title: element.querySelector('.issue-title').textContent,
            type: element.querySelector('.issue-type').textContent,
            status: element.querySelector('.issue-status').textContent,
            description: element.querySelector('.issue-description')?.textContent || '',
            deadline: element.querySelector('.issue-deadline span')?.textContent || null
        }));
    }

    bindEvents() {
        const form = document.getElementById('issueForm');
        form.addEventListener('submit', (e) => this.createIssue(e));

        // Add event delegation for edit/save/cancel buttons
        this.handleIssueActions();
    }

    // Event delegation for issue actions
    handleIssueActions() {
        document.addEventListener('click', (e) => {
            const issueId = e.target.dataset.issueId;

            if (e.target.classList.contains('edit-btn')) {
                this.enterEditMode(issueId);
            } else if (e.target.classList.contains('delete-btn')) {
                this.deleteIssue(issueId);
            }
        });
    }

    showEditModal(issue) {
            const deadlineValue = issue.deadline ?
                new Date(issue.deadline).toISOString().split('T')[0] : '';

            const modalHTML = `
                <div class="modal-overlay" id="editModal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3>Edit Issue #${issue.id}</h3>
                            <button class="modal-close" onclick="issueTracker.closeEditModal()">&times;</button>
                        </div>

                        <div class="modal-body">
                            <div class="form-group">
                                <label for="editTitle">Title *</label>
                                <input type="text" id="editTitle" value="${this.escapeHtml(issue.title)}" placeholder="Issue title">
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="editType">Type</label>
                                    <select id="editType">
                                        <option value="Daily" ${issue.type === 'Daily' ? 'selected' : ''}>Daily</option>
                                        <option value="Schedule" ${issue.type === 'Schedule' ? 'selected' : ''}>Schedule</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="editStatus">Status</label>
                                    <select id="editStatus">
                                        <option value="TODO" ${issue.status === 'TODO' ? 'selected' : ''}>To-Do</option>
                                        <option value="InProgress" ${issue.status === 'InProgress' ? 'selected' : ''}>In Progress</option>
                                        <option value="Finished" ${issue.status === 'Finished' ? 'selected' : ''}>Finished</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="editDescription">Description</label>
                                <textarea id="editDescription" placeholder="Issue description" rows="4">${this.escapeHtml(issue.description || '')}</textarea>
                            </div>

                            <div class="form-group">
                                <label for="editDeadline">Deadline</label>
                                <input type="date" id="editDeadline" value="${deadlineValue}">
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button class="btn btn-primary" onclick="issueTracker.saveIssueFromModal(${issue.id})">Save Changes</button>
                        </div>
                    </div>
                </div>
            `;

            // Add modal to body
            document.body.insertAdjacentHTML('beforeend', modalHTML);

            // Add event listener for ESC key
            document.addEventListener('keydown', this.handleModalEscape.bind(this));

            // Focus on title input
            setTimeout(() => {
                document.getElementById('editTitle').focus();
            }, 100);
    }

    enterEditMode(issueId) {
        const issue = this.findIssueById(issueId);
        console.log(issue, issueId);
            if (issue) {
                this.showEditModal(issue);
            }
        }

    // Close edit modal
    closeEditModal() {
        const modal = document.getElementById('editModal');
        if (modal) {
            modal.remove();
        }
        document.removeEventListener('keydown', this.handleModalEscape.bind(this));
    }

    // Handle ESC key to close modal
    handleModalEscape(e) {
        if (e.key === 'Escape') {
            this.closeEditModal();
        }
    }

    // Save issue from modal
    async saveIssueFromModal(issueId) {
        const updatedData = {
            title: document.getElementById('editTitle').value.trim(),
            type: document.getElementById('editType').value,
            status: document.getElementById('editStatus').value,
            description: document.getElementById('editDescription').value.trim(),
            deadline: document.getElementById('editDeadline').value || null
        };

        // Validate required fields
        if (!updatedData.title) {
            this.showMessage('Please enter a title', 'error');
            return;
        }

        if (updatedData.type === 'Schedule' && !updatedData.deadline) {
            this.showMessage('Please select a deadline for Schedule type issues', 'error');
            return;
        }

        try {
            const issueId2Int = Number(issueId);
            const response = await fetch(`/edit/${issueId2Int}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedData)
            });

            if (response.ok) {
                this.closeEditModal();
                this.showMessage('Issue updated successfully!', 'success');
                window.location.reload();
            } else {
                throw new Error('Failed to update issue');
            }
        } catch (error) {
            console.error('Error updating issue:', error);
            this.showMessage('Failed to update issue', 'error');
        }
    }

    // Find issue by ID
    findIssueById(id) {
        console.log(this.issues, id);
        return this.issues.find(issue => issue.id === parseInt(id));
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
                window.location.reload();
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
                window.location.reload();
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