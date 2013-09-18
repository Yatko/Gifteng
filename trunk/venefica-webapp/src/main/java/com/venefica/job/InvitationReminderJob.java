/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.job;

import com.venefica.common.EmailSender;
import com.venefica.common.MailException;
import com.venefica.config.Constants;
import com.venefica.dao.InvitationDao;
import com.venefica.model.Invitation;
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
 * When there is a small amount of time till the invitation will expire (invitation code reminder).
 * 
 * @author gyuszi
 */
@DisallowConcurrentExecution
public class InvitationReminderJob implements Job {

    private static final Log log = LogFactory.getLog(InvitationReminderJob.class);
    
    private static final String INVITATION_REMINDER_TEMPLATE = "invitation-reminder/";
    private static final String INVITATION_REMINDER_SUBJECT_TEMPLATE = INVITATION_REMINDER_TEMPLATE + "subject.vm";
    private static final String INVITATION_REMINDER_HTML_MESSAGE_TEMPLATE = INVITATION_REMINDER_TEMPLATE + "message.html.vm";
    private static final String INVITATION_REMINDER_PLAIN_MESSAGE_TEMPLATE = INVITATION_REMINDER_TEMPLATE + "message.txt.vm";
    
    private final boolean useEmailSender = false;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        InvitationDao invitationDao = (InvitationDao) context.getJobDetail().getJobDataMap().get(Constants.INVITATION_DAO);
        EmailSender emailSender = (EmailSender) context.getJobDetail().getJobDataMap().get(Constants.EMAIL_SENDER);
        
        List<Invitation> invitations = invitationDao.getByRemainingDay(Constants.INVITATION_EXPIRATION_REMINDER_DAYS);
        if ( invitations != null && !invitations.isEmpty() ) {
            for ( Invitation invitation : invitations ) {
                String code = invitation.getCode();
                String email = invitation.getEmail();
                
                if ( useEmailSender ) {
                    try {
                        Map<String, Object> vars = new HashMap<String, Object>(0);
                        vars.put("invitationCode", code);
                        vars.put("invitation", invitation);

                        emailSender.sendHtmlEmailByTemplates(
                                INVITATION_REMINDER_SUBJECT_TEMPLATE,
                                INVITATION_REMINDER_HTML_MESSAGE_TEMPLATE,
                                INVITATION_REMINDER_PLAIN_MESSAGE_TEMPLATE,
                                email,
                                vars);
                    } catch ( MailException ex ) {
                        log.error("Email exception when sending reminder (email: " + email + ", code: " + code + ")", ex);
                    }
                }
            }
        } else {
            log.info("There are no invitation expiring soon");
        }
    }
    
}
