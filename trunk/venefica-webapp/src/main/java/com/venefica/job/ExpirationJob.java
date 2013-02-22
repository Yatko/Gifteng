package com.venefica.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.venefica.dao.AdDao;

@DisallowConcurrentExecution
public class ExpirationJob implements Job {

    public static final String ADDAO = "AdDao";
    
    private static final Log log = LogFactory.getLog(ExpirationJob.class);

    @Override
    // @Transactional
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        AdDao adDao = (AdDao) ctx.getJobDetail().getJobDataMap().get(ADDAO);

        try {
            log.info("Processing expired advertisiments");
            adDao.markExpiredAds();
        } catch (Exception e) {
            log.error("Unable to process ads", e);
        }
    }
}
