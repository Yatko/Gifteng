/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Issue;
import java.util.List;

/**
 *
 * @author gyuszi
 */
public interface IssueDao {
    
    /**
     * Saves the issue in the database
     *
     * @param issue issue to save
     * @return id of the saved issue
     */
    Long save(Issue issue);
    
    /**
     * Updates the issue.
     * 
     * @param issue 
     */
    void update(Issue issue);
    
    /**
     * 
     * @return list of issues having their email sent flag on false
     */
    List<Issue> getUnsentIssues();
}
