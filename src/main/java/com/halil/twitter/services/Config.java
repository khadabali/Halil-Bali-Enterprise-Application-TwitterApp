package com.halil.twitter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class Config {

		public static final String[] ENDPOINTS_WHITELIST = {
	        "/login/**",
	        "/register",
	        "/logout/**",
	        "/token/**",
	        "/test"
	    };	
		
		@Autowired
		AuthenticationSuccessHandler successHandler;
		

		@Bean
		SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http.cors() // Enable Cross-Origin Resource Sharing (CORS)
				.and().csrf().disable() // Disable CSRF protection
				.authorizeHttpRequests(auth ->
					auth.requestMatchers(ENDPOINTS_WHITELIST).permitAll()
						.anyRequest().authenticated())				
				.oauth2Login().successHandler(successHandler);
			
			DefaultSecurityFilterChain build = http.build();
			return build;
		}
				
	
}
