package com.venefica.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.venefica.auth.SecurityContextCleaner;
import com.venefica.auth.TokenAuthorizationInterceptor;

/**
 * Configuration of Apache CXF web services.
 * 
 * @author Sviatoslav Grebenchukov
 */
@Configuration
@ImportResource("classpath:cxf-services.xml")
public class CxfConfig {

	@Bean(name = "securityContextCleaner")
	public SecurityContextCleaner securityContextCleaner() {
		return new SecurityContextCleaner();
	}

	@Bean(name = "tokenAuthorizationInterceptor")
	public TokenAuthorizationInterceptor tokenAuthenticationInterceptor() {
		return new TokenAuthorizationInterceptor();
	}
}
