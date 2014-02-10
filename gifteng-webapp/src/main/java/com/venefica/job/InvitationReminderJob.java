/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.job;

import com.venefica.common.EmailSender;
import com.venefica.config.Constants;
import com.venefica.config.InvitationConfig;
import com.venefica.dao.InvitationDao;
import com.venefica.model.Invitation;
import com.venefica.model.NotificationType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.ExecuteInJTATransaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * When there is a small amount of time till the invitation will expire (invitation code reminder).
 * 
 * @author gyuszi
 */
@DisallowConcurrentExecution
//@ExecuteInJTATransaction
public class InvitationReminderJob implements Job {

    private static final Log log = LogFactory.getLog(InvitationReminderJob.class);
    
    private final boolean useEmailSender = false;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        InvitationDao invitationDao = (InvitationDao) context.getJobDetail().getJobDataMap().get(Constants.INVITATION_DAO);
        EmailSender emailSender = (EmailSender) context.getJobDetail().getJobDataMap().get(Constants.EMAIL_SENDER);
        InvitationConfig invitationConfig = (InvitationConfig) context.getJobDetail().getJobDataMap().get(Constants.INVITATION_CONFIG);
        
        List<Invitation> invitations = invitationDao.getByRemainingDay(Constants.INVITATION_EXPIRATION_REMINDER_DAYS);
        if ( invitations != null && !invitations.isEmpty() ) {
            for ( Invitation invitation : invitations ) {
                String code = invitation.getCode();
                String email = invitation.getEmail();
                String zipcode = invitation.getZipCode();
                boolean containsZipcode = invitationConfig.contains(zipcode);
                
                if ( useEmailSender && containsZipcode ) {
                    Map<String, Object> vars = new HashMap<String, Object>(0);
                    vars.put("invitationCode", code);
                    vars.put("invitation", invitation);
                    
                    emailSender.sendNotification(NotificationType.INVITATION_REMINDER, email, vars);
                }
            }
        } else {
            log.info("There are no invitation expiring soon");
        }
    }
    
}
