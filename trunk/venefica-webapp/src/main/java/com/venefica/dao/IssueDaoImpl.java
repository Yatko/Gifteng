/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Issue;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class IssueDaoImpl extends DaoBase<Issue> implements IssueDao {

    @Override
    public Long save(Issue issue) {
        issue.setCreatedAt(new Date());
        return saveEntity(issue);
    }
    
    @Override
    public void update(Issue issue) {
        updateEntity(issue);
    }

    @Override
    public List<Issue> getUnsentIssues() {
        List<Issue> issues = createQuery(""
                + "from " + getDomainClassName() + " i "
                + "where i.emailSent = false"
                + "")
                .list();
        return issues;
    }
    
}
