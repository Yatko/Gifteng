/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.job;

import com.venefica.common.EmailSender;
import com.venefica.config.Constants;
import com.venefica.config.EmailConfig;
import com.venefica.dao.AdDao;
import com.venefica.dao.IssueDao;
import com.venefica.dao.RequestDao;
import com.venefica.dao.UserDao;
import com.venefica.model.Ad;
import com.venefica.model.Issue;
import com.venefica.model.NotificationType;
import com.venefica.model.Request;
import com.venefica.model.User;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author gyuszi
 */
@DisallowConcurrentExecution
public class IssueUnsentJob implements Job {
    
    private static final Log log = LogFactory.getLog(IssueUnsentJob.class);
    
    private final boolean useEmailSender = true;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        UserDao userDao = (UserDao) context.getJobDetail().getJobDataMap().get(Constants.USER_DAO);
        IssueDao issueDao = (IssueDao) context.getJobDetail().getJobDataMap().get(Constants.ISSUE_DAO);
        RequestDao requestDao = (RequestDao) context.getJobDetail().getJobDataMap().get(Constants.REQUEST_DAO);
        AdDao adDao = (AdDao) context.getJobDetail().getJobDataMap().get(Constants.AD_DAO);
        EmailSender emailSender = (EmailSender) context.getJobDetail().getJobDataMap().get(Constants.EMAIL_SENDER);
        EmailConfig emailConfig = (EmailConfig) context.getJobDetail().getJobDataMap().get(Constants.EMAIL_CONFIG);
        
        List<Issue> issues = issueDao.getUnsentIssues();
        if ( issues != null && !issues.isEmpty() ) {
            for ( Issue issue : issues ) {
                if ( useEmailSender ) {
                    User from = userDao.get(issue.getFrom().getId());
                    Request request = requestDao.get(issue.getRequest().getId());
                    Ad ad = adDao.get(request.getId());
                    
                    Map<String, Object> vars = new HashMap<String, Object>(0);
                    vars.put("issue", issue);
                    vars.put("from", from);
                    vars.put("request", request);
                    vars.put("ad", ad);
                    
                    if ( emailSender.sendNotification(NotificationType.ISSUE_NEW, emailConfig.getIssueEmailAddress(), vars) ) {
                        issue.setEmailSent(true);
                        issue.setEmailSentAt(new Date());
                        issueDao.update(issue);
                    }
                }
            }
        } else {
            log.info("There are no unsent issues to send");
        }
    }
}
