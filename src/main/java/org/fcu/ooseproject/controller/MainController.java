package org.fcu.ooseproject.controller;

import org.fcu.ooseproject.entity.Issue;
import org.fcu.ooseproject.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Sort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;     
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Issue Management", description = "APIs for managing issues and tasks")
public class MainController {

    @Autowired
    private IssueService issueService;

    @Operation(summary = "View home page", description = "Returns the main page with all issues")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the home page")
    @GetMapping("/")
    @ResponseBody
    public ModelAndView index() {
        List<Issue> issues = issueService.getAllIssues(Sort.by(Sort.Direction.ASC, "id"));
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("issues", issues);
        return modelAndView;
    }

    @Operation(summary = "Create new issue", description = "Creates a new issue with the provided details")
    @ApiResponse(responseCode = "200", description = "Issue created successfully")
    @PostMapping("/submit")
    @ResponseBody
    public void submit(@RequestBody Issue issue) {
        issueService.createIssue(issue);
    }

    @Operation(
        summary = "Get filtered and sorted issues", 
        description = "Retrieves issues with optional filtering and sorting parameters. Returns both JSON data and HTML view based on Accept header."
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Successfully retrieved filtered issues",
        content = {
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Issue.class, type = "array")
            ),
            @Content(
                mediaType = "text/html",
                schema = @Schema(implementation = String.class)
            )
        }
    )
    @GetMapping(
        value = "/api/getAll",
        produces = {"application/json", "text/html"}
    )
    @ResponseBody
    public ModelAndView getIssuesWithSortFilter(
        @Parameter(description = "Sort parameter (e.g., 'id,desc', 'type,asc')")
        Sort sort,
        
        @Parameter(description = "Filter by issue type (e.g., 'Daily', 'Schedule' or multiple: 'Daily,Schedule')")
        @RequestParam(value = "type", required = false) String type,
        
        @Parameter(description = "Filter by status (e.g., 'TODO', 'InProgress', 'Finished' or multiple)")
        @RequestParam(value = "status", required = false) String status,
        
        @Parameter(description = "Search by keyword in title or description")
        @RequestParam(value = "keyword", required = false) String keyword,
        
        @Parameter(description = "Filter by deadline before date (format: YYYY-MM-DD)")
        @RequestParam(value = "before", required = false) LocalDate before
    ) {
        List<Issue> issues = issueService.getAllIssuesWithSortFilter(sort, type, status, keyword, before);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("issues", issues);
        return modelAndView;
    }

    @Operation(summary = "Get issue by ID", description = "Retrieves a specific issue by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the issue")
    @GetMapping("/getById/{issueId}")
    @ResponseBody
    public Issue getIssue(
        @Parameter(description = "ID of the issue to retrieve")
        @PathVariable Long issueId
    ) {
        return issueService.getIssueById(issueId);
    }

    @Operation(summary = "Update issue", description = "Updates an existing issue with new details")
    @ApiResponse(responseCode = "200", description = "Issue updated successfully")
    @PutMapping("/edit/{issueId}")
    @ResponseBody
    public void editIssue(
        @Parameter(description = "ID of the issue to update")
        @PathVariable Long issueId,
        @RequestBody Issue issue
    ) {
        issueService.updateIssue(issueId, issue);
    }

    @Operation(summary = "Delete issue", description = "Deletes an issue by its ID")
    @ApiResponse(responseCode = "200", description = "Issue deleted successfully")
    @DeleteMapping("/delete/{issueId}")
    @ResponseBody
    public void deleteIssue(
        @Parameter(description = "ID of the issue to delete")
        @PathVariable Long issueId
    ) {
        issueService.deleteIssue(issueId);
    }
}
