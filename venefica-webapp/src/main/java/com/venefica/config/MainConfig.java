package com.venefica.config;

import com.venefica.auth.MessageEncryptor;
import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.auth.TokenEncryptor;
import com.venefica.dao.AdDao;
import com.venefica.dao.InvitationDao;
import com.venefica.job.AdExpirationJob;
import com.venefica.job.InvitationExpirationJob;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Main configuration class for the application
 *
 * @author Sviatoslav Grebenchukov
 */
@Configuration
@ComponentScan(basePackages = "com.venefica", excludeFilters = {@Filter(Configuration.class)})
@PropertySource("/application.properties")
public class MainConfig {

    private static final int AD_EXPIRATION_INTERVAL_CHECK_SECS = 1 * 60; // default: 1 minut 
    private static final int INVITATION_EXPIRATION_INTERVAL_CHECK_SECS = 1 * 60 * 60; // default: 1 hour
    
    private static final String AD_JOB_KEY = "adExpirationJob";
    private static final String AD_TRIGGER_KEY = "adExpirationTrigger";
    
    private static final String INVITATION_JOB_KEY = "invitationExpirationJob";
    private static final String INVITATION_TRIGGER_KEY = "invitationExpirationTrigger";
    
    private static final String JOB_GROUP = "common";
    
    @Inject
    private Environment environment;
    
    @Inject
    private AdDao adDao;
    @Inject
    private InvitationDao invitationDao;

    @Bean(name = "tokenEncryptor")
    public TokenEncryptor tokenEncryptor() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return new TokenEncryptor(environment.getProperty("authentication.secretkey"));
    }
    
    @Bean(name = "messageEncryptor")
    public MessageEncryptor messageEncryptor() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return new MessageEncryptor();
    }

    @Bean(name = "securityContextHolder")
    public ThreadSecurityContextHolder threadSecurityContextHolder() {
        return new ThreadSecurityContextHolder();
    }

    @Bean(name = "scheduler", destroyMethod = "shutdown")
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.scheduleJob(adExpirationJobDetail(), adExpirationTrigger());
        scheduler.scheduleJob(invitationExpirationJobDetail(), invitationExpirationTrigger());
        scheduler.start();
        return scheduler;
    }
    
    // Ad related job config
    
    @Bean(name = "adExpirationJobDetail")
    public JobDetail adExpirationJobDetail() {
        // @formatter:off
        JobDetail job = newJob(AdExpirationJob.class)
                .withIdentity(AD_JOB_KEY, JOB_GROUP)
                .build();
        job.getJobDataMap().put(Constants.AD_DAO, adDao);
        return job;
        // @formatter:on 
    }

    @Bean(name = "adExpirationTrigger")
    public Trigger adExpirationTrigger() {
        // @formatter:off
        return newTrigger()
                .withIdentity(AD_TRIGGER_KEY, JOB_GROUP)
                .withSchedule(simpleSchedule()
                .withIntervalInSeconds(AD_EXPIRATION_INTERVAL_CHECK_SECS)
                .repeatForever())
                .build();
        // @formatter:on
    }
    
    // Invitation related job config
    
    @Bean(name = "invitationExpirationJobDetail")
    public JobDetail invitationExpirationJobDetail() {
        // @formatter:off
        JobDetail job = newJob(InvitationExpirationJob.class)
                .withIdentity(INVITATION_JOB_KEY, JOB_GROUP)
                .build();
        job.getJobDataMap().put(Constants.INVITATION_DAO, invitationDao);
        return job;
        // @formatter:on 
    }
    
    @Bean(name = "invitationExpirationTrigger")
    public Trigger invitationExpirationTrigger() {
        // @formatter:off
        return newTrigger()
                .withIdentity(INVITATION_TRIGGER_KEY, JOB_GROUP)
                .withSchedule(simpleSchedule()
                .withIntervalInSeconds(INVITATION_EXPIRATION_INTERVAL_CHECK_SECS)
                .repeatForever())
                .build();
        // @formatter:on
    }
}
