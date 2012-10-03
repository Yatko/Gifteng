package com.venefica.config;

import javax.inject.Inject;
import javax.sql.DataSource;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.social.vkontakte.connect.VKontakteConnectionFactory;

import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.connect.ConnectSupport;
import com.venefica.connect.UserSignInAdapter;
import com.venefica.connect.UserSignUpAdapter;

/**
 * Spring social configuration.
 * 
 * @author Sviatoslav Grebenchukov
 * 
 */
@Configuration
public class SocialConfig {

	@Inject
	private DataSource dataSource;

	@Inject
	private Environment environment;

	@Inject
	private ThreadSecurityContextHolder securityContextHolder;

	/**
	 * When a new provider is added to the app register its {@link ConnectionFactory} here.
	 */
	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new FacebookConnectionFactory(environment
				.getProperty("facebook.clientId"), environment.getProperty("facebook.clientSecret")));
		registry.addConnectionFactory(new TwitterConnectionFactory(environment
				.getProperty("twitter.consumerKey"), environment
				.getProperty("twitter.consumerSecret")));
		registry.addConnectionFactory(new VKontakteConnectionFactory(environment
				.getProperty("vkontakte.clientId"), environment
				.getProperty("vkontakte.clientSecret")));
		return registry;
	}

	/**
	 * Singleton data access object providing access to connections across all users.
	 */
	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator(), Encryptors.noOpText());
		repository.setConnectionSignUp(userSignUpAdapter());
		return repository;
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
		Long userId = securityContextHolder.getContext().getUserId();
		return userId != null ? usersConnectionRepository().createConnectionRepository(
				userId.toString()) : null;
	}

	/**
	 * Creates a session and URL containing the session id to redirect to after a successful
	 * sign-in.
	 */
	@Bean
	public UserSignInAdapter userSignInAdapter() {
		return new UserSignInAdapter();
	}

	/**
	 * Creates partially completed user and stores him in the database.
	 */
	@Bean
	public UserSignUpAdapter userSignUpAdapter() {
		return new UserSignUpAdapter();
	}

	/**
	 * Provides helper methods for sign-in and connect controllers.
	 */
	@Bean
	public ConnectSupport connectSupport() {
		return new ConnectSupport();
	}
}
