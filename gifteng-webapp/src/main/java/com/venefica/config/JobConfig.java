/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config;

import com.venefica.common.AmazonUpload;
import com.venefica.common.EmailSender;
import com.venefica.dao.AdDao;
import com.venefica.dao.AdDataDao;
import com.venefica.dao.ForgotPasswordDao;
import com.venefica.dao.ImageDao;
import com.venefica.dao.InvitationDao;
import com.venefica.dao.IssueDao;
import com.venefica.dao.RequestDao;
import com.venefica.dao.UserDao;
import com.venefica.dao.UserPointDao;
import com.venefica.dao.UserTransactionDao;
import com.venefica.dao.UserVerificationDao;
import com.venefica.job.AdExpirationJob;
import com.venefica.job.AdExportJob;
import com.venefica.job.AdOnlineJob;
import com.venefica.job.ForgotPasswordExpirationJob;
import com.venefica.job.InvitationExpirationJob;
import com.venefica.job.InvitationReminderJob;
import com.venefica.job.IssueUnsentJob;
import com.venefica.job.UserVerificationReminderJob;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import org.quartz.Job;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author gyuszi
 */
@Configuration
@PropertySource("/" + Constants.APPLICATION_PROPERTIES)
public class JobConfig {
    
    private static final String AD_EXPIRATION_JOB_KEY = "adExpirationJob";
    private static final String AD_EXPIRATION_TRIGGER_KEY = "adExpirationTrigger";
    
    private static final String AD_ONLINE_JOB_KEY = "adOnlineJob";
    private static final String AD_ONLINE_TRIGGER_KEY = "adOnlineTrigger";
    
    private static final String INVITATION_JOB_KEY = "invitationExpirationJob";
    private static final String INVITATION_TRIGGER_KEY = "invitationExpirationTrigger";
    
    private static final String INVITATION_REMINDER_JOB_KEY = "invitationReminderJob";
    private static final String INVITATION_REMINDER_TRIGGER_KEY = "invitationReminderTrigger";
    
    private static final String FORGOT_PASSWORD_JOB_KEY = "forgotPasswordExpirationJob";
    private static final String FORGOT_PASSWORD_TRIGGER_KEY = "forgotPasswordExpirationTrigger";
    
    private static final String USER_VERIFICATION_JOB_KEY = "userVerificationReminderJob";
    private static final String USER_VERIFICATION_TRIGGER_KEY = "userVerificationReminderTrigger";
    
    private static final String ISSUE_UNSENT_JOB_KEY = "issueUnsentJob";
    private static final String ISSUE_UNSENT_TRIGGER_KEY = "issueUnsentTrigger";
    
    private static final String AD_EXPORT_JOB_KEY = "adExportJob";
    private static final String AD_EXPORT_TRIGGER_KEY = "adExportTrigger";
    
    private static final String JOB_GROUP = "common";
    
    @Inject
    private Environment environment;
    @Inject
    private PlatformTransactionManager transactionManager;
    
    @Inject
    private AppConfig appConfig;
    @Inject
    private EmailConfig emailConfig;
    @Inject
    private InvitationConfig invitationConfig;
    @Inject
    private AdDao adDao;
    @Inject
    private AdDataDao adDataDao;
    @Inject
    private RequestDao requestDao;
    @Inject
    private UserTransactionDao userTransactionDao;
    @Inject
    private UserDao userDao;
    @Inject
    private ImageDao imageDao;
    @Inject
    private UserPointDao userPointDao;
    @Inject
    private InvitationDao invitationDao;
    @Inject
    private ForgotPasswordDao forgotPasswordDao;
    @Inject
    private UserVerificationDao userVerificationDao;
    @Inject
    private IssueDao issueDao;
    @Inject
    private EmailSender emailSender;
    @Inject
    private AmazonUpload amazonUpload;
    
    private int adExpirationIntervalCheckSecond;
    private int adOnlineStartingCheckHour;
    private int adExportIntervalCheckSecond;
    private int invitationExpirationIntervalCheckSecond;
    private int invitationExpirationReminderCheckSecond;
    private int forgotPasswordExpirationIntervalCheckSecond;
    private int userVerificationReminderIntervalCheckSecond;
    private int issueUnsentIntervalCheckSecond;
    
    @PostConstruct
    public void init() {
        adExpirationIntervalCheckSecond = environment.getProperty("job.ad.expirationIntervalCheckSecond", int.class);
        adOnlineStartingCheckHour = environment.getProperty("job.ad.onlineStartingCheckHour", int.class);
        adExportIntervalCheckSecond = environment.getProperty("job.ad.exportIntervalCheckSecond", int.class);
        invitationExpirationIntervalCheckSecond = environment.getProperty("job.invitation.expirationIntervalCheckSecond", int.class);
        invitationExpirationReminderCheckSecond = environment.getProperty("job.invitation.expirationReminderCheckSecond", int.class);
        forgotPasswordExpirationIntervalCheckSecond = environment.getProperty("job.forgotPassword.expirationIntervalCheckSecond", int.class);
        userVerificationReminderIntervalCheckSecond = environment.getProperty("job.user.verificationReminderIntervalCheckSecond", int.class);
        issueUnsentIntervalCheckSecond = environment.getProperty("job.issue.unsentIntervalCheckSecond", int.class);
    }
    
    @Bean(initMethod = "start", destroyMethod = "stop")
    public SchedulerFactoryBean scheduler() {
        JobDetail adExpirationJobDetail = adExpirationJobDetail();
        JobDetail adOnlineJobDetail = adOnlineJobDetail();
        JobDetail invitationExpirationJobDetail = invitationExpirationJobDetail();
        JobDetail invitationReminderJobDetail = invitationReminderJobDetail();
        JobDetail forgotPasswordExpirationJobDetail = forgotPasswordExpirationJobDetail();
        JobDetail userVerificationReminderJobDetail = userVerificationReminderJobDetail();
        JobDetail issueUnsentJobDetail = issueUnsentJobDetail();
        JobDetail adExportJobDetail = adExportJobDetail();
        
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setTransactionManager(transactionManager); //TODO: no effect unfortunatelly
        schedulerFactory.setJobDetails(new JobDetail[] {
            adExpirationJobDetail,
            adOnlineJobDetail,
            invitationExpirationJobDetail,
            invitationReminderJobDetail,
            forgotPasswordExpirationJobDetail,
            userVerificationReminderJobDetail,
            issueUnsentJobDetail,
            adExportJobDetail,
        });
        schedulerFactory.setTriggers(new Trigger[] {
            createRepeatTrigger(AD_EXPIRATION_TRIGGER_KEY, adExpirationIntervalCheckSecond, adExpirationJobDetail),
            createDailyTrigger(AD_ONLINE_TRIGGER_KEY, adOnlineStartingCheckHour, adOnlineJobDetail),
            createRepeatTrigger(INVITATION_TRIGGER_KEY, invitationExpirationIntervalCheckSecond, invitationExpirationJobDetail),
            createRepeatTrigger(INVITATION_REMINDER_TRIGGER_KEY, invitationExpirationReminderCheckSecond, invitationReminderJobDetail),
            createRepeatTrigger(FORGOT_PASSWORD_TRIGGER_KEY, forgotPasswordExpirationIntervalCheckSecond, forgotPasswordExpirationJobDetail),
            createRepeatTrigger(USER_VERIFICATION_TRIGGER_KEY, userVerificationReminderIntervalCheckSecond, userVerificationReminderJobDetail),
            createRepeatTrigger(ISSUE_UNSENT_TRIGGER_KEY, issueUnsentIntervalCheckSecond, issueUnsentJobDetail),
            createRepeatTrigger(AD_EXPORT_TRIGGER_KEY, adExportIntervalCheckSecond, adExportJobDetail),
        });
        return schedulerFactory;
    }
    
    // job details
    
    private JobDetail adExpirationJobDetail() {
        JobDetail job = createJobDetail(AdExpirationJob.class, AD_EXPIRATION_JOB_KEY);
        job.getJobDataMap().put(Constants.AD_DAO, adDao);
        job.getJobDataMap().put(Constants.REQUEST_DAO, requestDao);
        job.getJobDataMap().put(Constants.USER_TRANSACTION_DAO, userTransactionDao);
        return job;
    }

    private JobDetail adOnlineJobDetail() {
        JobDetail job = createJobDetail(AdOnlineJob.class, AD_ONLINE_JOB_KEY);
        job.getJobDataMap().put(Constants.APP_CONFIG, appConfig);
        job.getJobDataMap().put(Constants.AD_DAO, adDao);
        job.getJobDataMap().put(Constants.AD_DATA_DAO, adDataDao);
        job.getJobDataMap().put(Constants.USER_DAO, userDao);
        job.getJobDataMap().put(Constants.USER_POINT_DAO, userPointDao);
        job.getJobDataMap().put(Constants.EMAIL_SENDER, emailSender);
        return job;
    }

    private JobDetail invitationExpirationJobDetail() {
        JobDetail job = createJobDetail(InvitationExpirationJob.class, INVITATION_JOB_KEY);
        job.getJobDataMap().put(Constants.INVITATION_DAO, invitationDao);
        return job;
    }
    
    private JobDetail invitationReminderJobDetail() {
        JobDetail job = createJobDetail(InvitationReminderJob.class, INVITATION_REMINDER_JOB_KEY);
        job.getJobDataMap().put(Constants.INVITATION_DAO, invitationDao);
        job.getJobDataMap().put(Constants.EMAIL_SENDER, emailSender);
        job.getJobDataMap().put(Constants.INVITATION_CONFIG, invitationConfig);
        return job;
    }
    
    private JobDetail forgotPasswordExpirationJobDetail() {
        JobDetail job = createJobDetail(ForgotPasswordExpirationJob.class, FORGOT_PASSWORD_JOB_KEY);
        job.getJobDataMap().put(Constants.FORGOT_PASSWORD_DAO, forgotPasswordDao);
        return job;
    }
    
    private JobDetail userVerificationReminderJobDetail() {
        JobDetail job = createJobDetail(UserVerificationReminderJob.class, USER_VERIFICATION_JOB_KEY);
        job.getJobDataMap().put(Constants.USER_VERIFICATION_DAO, userVerificationDao);
        job.getJobDataMap().put(Constants.USER_DAO, userDao);
        job.getJobDataMap().put(Constants.EMAIL_SENDER, emailSender);
        return job;
    }
    
    private JobDetail issueUnsentJobDetail() {
        JobDetail job = createJobDetail(IssueUnsentJob.class, ISSUE_UNSENT_JOB_KEY);
        job.getJobDataMap().put(Constants.ISSUE_DAO, issueDao);
        job.getJobDataMap().put(Constants.USER_DAO, userDao);
        job.getJobDataMap().put(Constants.REQUEST_DAO, requestDao);
        job.getJobDataMap().put(Constants.AD_DAO, adDao);
        job.getJobDataMap().put(Constants.EMAIL_SENDER, emailSender);
        job.getJobDataMap().put(Constants.EMAIL_CONFIG, emailConfig);
        return job;
    }
    
    private JobDetail adExportJobDetail() {
        JobDetail job = createJobDetail(AdExportJob.class, AD_EXPORT_JOB_KEY);
        job.getJobDataMap().put(Constants.AD_DAO, adDao);
        job.getJobDataMap().put(Constants.AD_DATA_DAO, adDataDao);
        job.getJobDataMap().put(Constants.IMAGE_DAO, imageDao);
        job.getJobDataMap().put(Constants.AMAZON_UPLOAD, amazonUpload);
        return job;
    }
    
    // internal
    
    private JobDetail createJobDetail(Class <? extends Job> jobClass, String identity) {
        return newJob(jobClass)
                .withDescription("JobDetail (class: " + jobClass.getSimpleName() + ", identity: " + identity + ")")
                .withIdentity(identity, JOB_GROUP)
                .build();
    }
    
    private Trigger createRepeatTrigger(String identity, int intervalInSeconds, JobDetail jobDetail) {
        return newTrigger()
                .forJob(jobDetail)
                .withDescription("Trigger (identity: " + identity + ")")
                .withIdentity(identity, JOB_GROUP)
                .withSchedule(repeatSecondlyForever(intervalInSeconds))
                .build();
    }
    
    /**
     * See:
     * http://quartz-scheduler.org/documentation/quartz-2.1.x/cookbook/DailyTrigger
     * 
     * @param identity
     * @param startingHour
     * @return 
     */
    private Trigger createDailyTrigger(String identity, int startingHour, JobDetail jobDetail) {
        return newTrigger()
                .forJob(jobDetail)
                .withDescription("Trigger (identity: " + identity + ")")
                .withIdentity(identity, JOB_GROUP)
                .startNow()
                .withSchedule(dailyAtHourAndMinute(startingHour, 0))
                .build();
    }
}
