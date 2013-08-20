package com.venefica.config;

import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.connect.ConnectSupport;
import com.venefica.connect.DummySignUpAdapter;
import com.venefica.connect.UserSignInAdapter;
import com.venefica.connect.UserSignUpAdapter;
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
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
//import org.springframework.social.connect.appengine.AppEngineUsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.web.context.WebApplicationContext;
//import org.springframework.social.vkontakte.connect.VKontakteConnectionFactory;

/**
 * Spring social configuration.
 * 
 * Currently set as inactive. Direct website and social media (facebook, twitter, pinterest)
 * integration were done.
 *
 * @author Sviatoslav Grebenchukov
 */
//@Configuration
public class SocialConfig {

    @Inject
    private DataSource dataSource;
    
    @Inject
    private Environment environment;
    
    @Inject
    private ThreadSecurityContextHolder securityContextHolder;

    /**
     * When a new provider is added to the app register its
     * {@link ConnectionFactory} here.
     */
    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        String facebookClientId = environment.getProperty("facebook.clientId");
        String facebookClientSecret = environment.getProperty("facebook.clientSecret");
        
        String twitterConsumerKey = environment.getProperty("twitter.consumerKey");
        String twitterConsumerSecret = environment.getProperty("twitter.consumerSecret");
        
//        String vkontakteClientId = environment.getProperty("vkontakte.clientId");
//        String vkontakteClientSecret = environment.getProperty("vkontakte.clientSecret");
        
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(facebookClientId, facebookClientSecret));
        registry.addConnectionFactory(new TwitterConnectionFactory(twitterConsumerKey, twitterConsumerSecret));
//        registry.addConnectionFactory(new VKontakteConnectionFactory(vkontakteClientId, vkontakteClientSecret));
        
        return registry;
    }

    /**
     * Singleton data access object providing access to connections across
     * all users.
     */
    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), Encryptors.noOpText());
        repository.setConnectionSignUp(userSignUpAdapter());
        return repository;
    }
    
//    /**
//     * Instance that should be used when deploying into google app engine.
//     * @return 
//     */
//    @Bean
//    public UsersConnectionRepository usersConnectionRepository() {
//        AppEngineUsersConnectionRepository repository = new AppEngineUsersConnectionRepository(connectionFactoryLocator(), Encryptors.noOpText());
//        return repository;
//    }

    /**
     * A proxy to a request-scoped object.
     * 
     * @return 
     */
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        Long userId = securityContextHolder.getContext() != null ? securityContextHolder.getContext().getUserId() : null;
        if ( userId != null ) {
            return usersConnectionRepository().createConnectionRepository(userId.toString());
        }
        return null;
    }

    /**
     * Creates a session and URL containing the session id to redirect to after
     * a successful sign-in.
     */
    @Bean
    public UserSignInAdapter userSignInAdapter() {
        return new UserSignInAdapter();
    }

    /**
     * Creates partially completed user and stores him in the database.
     */
    @Bean
    public ConnectionSignUp userSignUpAdapter() {
        //return new UserSignUpAdapter();
        return new DummySignUpAdapter();
    }

    /**
     * Provides helper methods for sign-in and connect controllers.
     */
    @Bean
    public ConnectSupport connectSupport() {
        return new ConnectSupport();
    }
}
