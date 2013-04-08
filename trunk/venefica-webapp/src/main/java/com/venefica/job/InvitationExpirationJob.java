/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.job;

import com.venefica.config.Constants;
import com.venefica.dao.InvitationDao;
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
public class InvitationExpirationJob implements Job {
    
    private static final Log log = LogFactory.getLog(InvitationExpirationJob.class);
    
    @Override
    //@Transactional
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        InvitationDao invitationDao = (InvitationDao) ctx.getJobDetail().getJobDataMap().get(Constants.INVITATION_DAO);
        
        try {
            log.info("Processing expired invitations");
            invitationDao.markExpiredInvitations();
        } catch (Exception e) {
            log.error("Unable to process invitations", e);
        }
    }
}
