package com.venefica.config;

import com.venefica.auth.MessageEncryptor;
import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.auth.TokenEncryptor;
import com.venefica.auth.TokenUtil;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
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
@PropertySource("/" + Constants.APPLICATION_PROPERTIES)
@ImportResource("classpath:main.xml")
public class MainConfig {

    @Inject
    private Environment environment;
    
    @Bean
    public TokenEncryptor tokenEncryptor() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return new TokenEncryptor(environment.getProperty("authentication.secretkey"));
    }
    
    @Bean
    public MessageEncryptor messageEncryptor() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return new MessageEncryptor();
    }

    @Bean(name = "securityContextHolder")
    public ThreadSecurityContextHolder threadSecurityContextHolder() {
        return new ThreadSecurityContextHolder();
    }
    
    @Bean(name = "tokenUtil")
    public TokenUtil tokenUtil() {
        return new TokenUtil();
    }
}
