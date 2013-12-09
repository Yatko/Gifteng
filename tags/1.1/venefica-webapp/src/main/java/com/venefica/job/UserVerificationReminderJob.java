/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.job;

import com.venefica.common.EmailSender;
import com.venefica.common.MailException;
import com.venefica.config.Constants;
import com.venefica.dao.UserDao;
import com.venefica.dao.UserVerificationDao;
import com.venefica.model.User;
import com.venefica.model.UserVerification;
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
 *
 * @author gyuszi
 */
@DisallowConcurrentExecution
//@ExecuteInJTATransaction
public class UserVerificationReminderJob implements Job {
    
    private static final Log log = LogFactory.getLog(UserVerificationReminderJob.class);
    
    private final boolean useEmailSender = false;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        UserVerificationDao userVerificationDao = (UserVerificationDao) context.getJobDetail().getJobDataMap().get(Constants.USER_VERIFICATION_DAO);
        UserDao userDao = (UserDao) context.getJobDetail().getJobDataMap().get(Constants.USER_DAO);
        EmailSender emailSender = (EmailSender) context.getJobDetail().getJobDataMap().get(Constants.EMAIL_SENDER);
        
        List<UserVerification> verifications = userVerificationDao.getAllUnverified();
        if ( verifications != null && !verifications.isEmpty() ) {
            for ( UserVerification userVerification : verifications ) {
                User user = userDao.get(userVerification.getUser().getId());
                String code = userVerification.getCode();
                String email = user.getEmail();
                
                if ( useEmailSender ) {
                    try {
                        Map<String, Object> vars = new HashMap<String, Object>(0);
                        vars.put("code", code);
                        vars.put("user", user);

                        emailSender.sendHtmlEmailByTemplates(
                                Constants.USER_VERIFICATION_REMINDER_SUBJECT_TEMPLATE,
                                Constants.USER_VERIFICATION_REMINDER_HTML_MESSAGE_TEMPLATE,
                                Constants.USER_VERIFICATION_REMINDER_PLAIN_MESSAGE_TEMPLATE,
                                email,
                                vars);
                    } catch ( MailException ex ) {
                        log.error("Email exception when sending user verification reminder (email: " + email + ", code: " + code + ")", ex);
                    }
                }
            }
        } else {
            log.info("There are no user verification reminder to send");
        }
    }
}
