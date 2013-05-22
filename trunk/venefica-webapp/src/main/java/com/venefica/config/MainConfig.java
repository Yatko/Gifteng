package com.venefica.config;

import com.venefica.auth.MessageEncryptor;
import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.auth.TokenEncryptor;
import com.venefica.dao.AdDao;
import com.venefica.dao.ForgotPasswordDao;
import com.venefica.dao.InvitationDao;
import com.venefica.job.AdExpirationJob;
import com.venefica.job.ForgotPasswordExpirationJob;
import com.venefica.job.InvitationExpirationJob;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import org.quartz.Job;
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
import org.springframework.context.annotation.ImportResource;
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
@ImportResource("classpath:main.xml")
public class MainConfig {

    private static final String AD_JOB_KEY = "adExpirationJob";
    private static final String AD_TRIGGER_KEY = "adExpirationTrigger";
    
    private static final String INVITATION_JOB_KEY = "invitationExpirationJob";
    private static final String INVITATION_TRIGGER_KEY = "invitationExpirationTrigger";
    
    private static final String FORGOT_PASSWORD_JOB_KEY = "forgotPasswordExpirationJob";
    private static final String FORGOT_PASSWORD_TRIGGER_KEY = "forgotPasswordExpirationTrigger";
    
    private static final String JOB_GROUP = "common";
    
    @Inject
    private Environment environment;
    
    @Inject
    private AdDao adDao;
    @Inject
    private InvitationDao invitationDao;
    @Inject
    private ForgotPasswordDao forgotPasswordDao;

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
        scheduler.scheduleJob(forgotPasswordExpirationJobDetail(), forgotPasswordExpirationTrigger());
        scheduler.start();
        return scheduler;
    }
    
    // Ad related job config
    
    @Bean(name = "adExpirationJobDetail")
    public JobDetail adExpirationJobDetail() {
        JobDetail job = createJobDetail(AdExpirationJob.class, AD_JOB_KEY);
        job.getJobDataMap().put(Constants.AD_DAO, adDao);
        return job;
    }

    @Bean(name = "adExpirationTrigger")
    public Trigger adExpirationTrigger() {
        return createTrigger(AD_TRIGGER_KEY, Constants.AD_EXPIRATION_INTERVAL_CHECK_SECS);
    }
    
    // Invitation related job config
    
    @Bean(name = "invitationExpirationJobDetail")
    public JobDetail invitationExpirationJobDetail() {
        JobDetail job = createJobDetail(InvitationExpirationJob.class, INVITATION_JOB_KEY);
        job.getJobDataMap().put(Constants.INVITATION_DAO, invitationDao);
        return job;
    }
    
    @Bean(name = "invitationExpirationTrigger")
    public Trigger invitationExpirationTrigger() {
        return createTrigger(INVITATION_TRIGGER_KEY, Constants.INVITATION_EXPIRATION_INTERVAL_CHECK_SECS);
    }
    
    // ForgotPassword requests related job config
    
    @Bean(name = "forgotPasswordExpirationJobDetail")
    public JobDetail forgotPasswordExpirationJobDetail() {
        JobDetail job = createJobDetail(ForgotPasswordExpirationJob.class, FORGOT_PASSWORD_JOB_KEY);
        job.getJobDataMap().put(Constants.FORGOT_PASSWORD_DAO, forgotPasswordDao);
        return job;
    }
    
    @Bean(name = "forgotPasswordExpirationTrigger")
    public Trigger forgotPasswordExpirationTrigger() {
        return createTrigger(FORGOT_PASSWORD_TRIGGER_KEY, Constants.FORGOT_PASSWORD_EXPIRATION_INTERVAL_CHECK_SECS);
    }
    
    // internal
    
    private JobDetail createJobDetail(Class <? extends Job> jobClass, String identity) {
        // @formatter:off
        JobDetail job = newJob(jobClass)
                .withIdentity(identity, JOB_GROUP)
                .build();
        return job;
        // @formatter:on 
    }
    
    private Trigger createTrigger(String identity, int intervalInSeconds) {
        // @formatter:off
        return newTrigger()
                .withIdentity(identity, JOB_GROUP)
                .withSchedule(simpleSchedule()
                .withIntervalInSeconds(intervalInSeconds)
                .repeatForever())
                .build();
        // @formatter:on
    }
}
