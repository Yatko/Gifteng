package com.venefica.job;

import com.venefica.common.EmailSender;
import com.venefica.config.AppConfig;
import com.venefica.config.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.venefica.dao.AdDao;
import com.venefica.dao.UserDao;
import com.venefica.dao.UserPointDao;
import com.venefica.model.Ad;
import com.venefica.model.NotificationType;
import com.venefica.model.User;
import com.venefica.model.UserPoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.quartz.ExecuteInJTATransaction;

@DisallowConcurrentExecution
//@ExecuteInJTATransaction
public class AdOnlineJob implements Job {

    private static final Log log = LogFactory.getLog(AdOnlineJob.class);
    
    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        AppConfig appConfig = (AppConfig) ctx.getJobDetail().getJobDataMap().get(Constants.APP_CONFIG);
        AdDao adDao = (AdDao) ctx.getJobDetail().getJobDataMap().get(Constants.AD_DAO);
        UserDao userDao = (UserDao) ctx.getJobDetail().getJobDataMap().get(Constants.USER_DAO);
        UserPointDao userPointDao = (UserPointDao) ctx.getJobDetail().getJobDataMap().get(Constants.USER_POINT_DAO);
        EmailSender emailSender = (EmailSender) ctx.getJobDetail().getJobDataMap().get(Constants.EMAIL_SENDER);

        try {
            log.info("Processing online advertisements");
            
            List<Ad> ads = adDao.getOfflineAds();
            for ( Ad ad : ads ) {
                try {
                    adDao.onlineAd(ad);
                    
                    User creator = userDao.getEager(ad.getCreator().getId());
                    UserPoint userPoint = creator.getUserPoint();
                    userPoint.setRequestLimit(userPoint.getRequestLimit() + appConfig.getRequestIncrementLimit());
                    userPointDao.update(userPoint);
                    
                    notifyFollowers(emailSender, userDao, ad, creator);
                    log.info("Ad (id: " + ad.getId() + ") become online");
                } catch ( Exception ex ) {
                    log.error("Exception thrown when making ad (id: " + ad.getId() + ") online", ex);
                }
            }
        } catch (Exception e) {
            log.error("Unable to process ads", e);
        }
    }
    
    private void notifyFollowers(EmailSender emailSender, UserDao userDao, Ad ad, User creator) {
        //all followers should be notified (if configured) about the "new" ad
        List<User> followers = userDao.getFollowers(creator.getId());
        if ( followers != null && !followers.isEmpty() ) {
            for ( User follower : followers ) {
                Map<String, Object> vars = new HashMap<String, Object>(0);
                vars.put("ad", ad);
                vars.put("creator", creator);
                vars.put("follower", follower);

                emailSender.sendNotification(NotificationType.FOLLOWER_AD_CREATED, follower, vars);
            }
        }
    }
}
