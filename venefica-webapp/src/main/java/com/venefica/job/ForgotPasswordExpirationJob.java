/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.job;

import com.venefica.config.Constants;
import com.venefica.dao.ForgotPasswordDao;
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
public class ForgotPasswordExpirationJob implements Job {
    
    private static final Log log = LogFactory.getLog(ForgotPasswordExpirationJob.class);
    
    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        ForgotPasswordDao forgotPasswordDao = (ForgotPasswordDao) ctx.getJobDetail().getJobDataMap().get(Constants.FORGOT_PASSWORD_DAO);

        try {
            log.info("Processing expired forgot password requests");
            forgotPasswordDao.markExpiredRequests();
        } catch (Exception e) {
            log.error("Unable to process forgot password requests", e);
        }
    }
}
