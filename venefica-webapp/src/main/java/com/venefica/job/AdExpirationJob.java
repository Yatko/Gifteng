package com.venefica.job;

import com.venefica.config.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.venefica.dao.AdDao;

@DisallowConcurrentExecution
public class AdExpirationJob implements Job {

    private static final Log log = LogFactory.getLog(AdExpirationJob.class);
    
    @Override
    //@Transactional
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        AdDao adDao = (AdDao) ctx.getJobDetail().getJobDataMap().get(Constants.AD_DAO);

        try {
            log.info("Processing expired advertisements");
            adDao.markExpiredAds();
        } catch (Exception e) {
            log.error("Unable to process ads", e);
        }
    }
}
