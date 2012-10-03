package com.venefica.config;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;


import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.auth.TokenEncryptor;
import com.venefica.dao.AdDao;
import com.venefica.job.ExpirationJob;

/**
 * Main configuration class for the application
 * 
 * @author Sviatoslav Grebenchukov
 */
@Configuration
@ComponentScan(basePackages = "com.venefica", excludeFilters = { @Filter(Configuration.class) })
@PropertySource("/application.properties")
public class MainConfig {

	public static final int EXPIRATION_INTERVAL_CHECK_SECS = 60;

	@Inject
	private Environment environment;

	@Bean(name = "tokenEncryptor")
	public TokenEncryptor tokenEncryptor() throws NoSuchAlgorithmException, NoSuchPaddingException {
		return new TokenEncryptor(environment.getProperty("authentication.secretkey"));
	}

	@Bean(name = "securityContextHolder")
	public ThreadSecurityContextHolder threadSecurityContextHolder() {
		return new ThreadSecurityContextHolder();
	}

	@Bean(name = "scheduler", destroyMethod = "shutdown")
	public Scheduler scheduler() throws SchedulerException {
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.scheduleJob(expirationJobDetail(), expirationTrigger());
		scheduler.start();
		return scheduler;
	}

	@Inject
	private AdDao adDao;

	@Bean(name = "expirationJobDetail")
	public JobDetail expirationJobDetail() {
		// @formatter:off
		JobDetail job = newJob(ExpirationJob.class)
				.withIdentity("expirationJob", "common")				
				.build();
		job.getJobDataMap().put(ExpirationJob.ADDAO, adDao);
		return job;
		// @formatter:on 
	}

	@Bean(name = "expirationTrigger")
	public Trigger expirationTrigger() {
		// @formatter:off
		return newTrigger()
				.withIdentity("expirationTrigger", "common")
				.withSchedule(simpleSchedule()
							.withIntervalInSeconds(EXPIRATION_INTERVAL_CHECK_SECS)
							.repeatForever())
				.build();
		// @formatter:on
	}
}
