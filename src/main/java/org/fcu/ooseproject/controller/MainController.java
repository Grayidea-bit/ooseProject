package org.fcu.ooseproject.controller;

import org.fcu.ooseproject.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.util.List;


@RestController
public class MainController {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @PostMapping("/submit")
    public void submit(@RequestBody Issue issue) {
        String sql = "INSERT INTO issues (title, type, description, status, deadline) VALUES (:title, :type, :description, :status, :deadline)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", issue.getTitle());
        params.addValue("description", issue.getDescription());
        params.addValue("type", issue.getType().toString());
        params.addValue("status", issue.getStatus().toString());

        if (issue.getType().equals(IssueType.Schedule)) {
            params.addValue("deadline", issue.getDeadline());
        }
        else {
            params.addValue("deadline", null);
        }

        namedParameterJdbcTemplate.update(sql, params);
    }



    @GetMapping("/get/all")
    public List<Issue> getAllIssues() {
        String sql = "SELECT id, title, description, type, status, deadline, created_at FROM issues ORDER BY created_at DESC";

        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(), (rs, rowNum) -> {
            Issue issue = new Issue(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    IssueType.valueOf(rs.getString("type")),
                    StatusType.valueOf(rs.getString("status")),
                    rs.getDate("deadline"),
                    rs.getTimestamp("created_at")
            );
            return issue;
        });
    }


    @GetMapping("/getById/{issueId}")
    public Issue getIssue(@PathVariable Integer issueId) {
        String sql = "SELECT * FROM issues WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", issueId);

        return namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> new Issue(
                rs.getString("title"),
                rs.getString("description"),
                IssueType.valueOf(rs.getString("type")),
                StatusType.valueOf(rs.getString("status")),
                rs.getDate("deadline") != null ? rs.getDate("deadline") : null
        ));
    }

    @PutMapping("/edit/{issueId}")
    public void editIssue(@PathVariable Integer issueId, @RequestBody Issue issue) {

        String sql = "UPDATE issues SET title = :title, type = :type, description = :description, status = :status , deadline = :deadline WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", issueId);
        params.addValue("title", issue.getTitle());
        params.addValue("type", issue.getType().toString());
        params.addValue("description", issue.getDescription());
        params.addValue("status", issue.getStatus().toString());
        params.addValue("deadline", issue.getDeadline());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @DeleteMapping("/delete/{issueId}")
    public void deleteIssue(@PathVariable Integer issueId) {
        String sql = "DELETE FROM issues WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("id", issueId));
    }

}
