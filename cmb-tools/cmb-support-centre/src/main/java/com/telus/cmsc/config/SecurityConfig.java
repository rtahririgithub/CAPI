/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;

import com.telus.cmsc.util.UserProfileContextMapper;

/**
 * @author Pavel Simonovsky	
 *
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		String successUrl = "/dashboard/summary";
		
		http.authorizeRequests()
				.antMatchers("/resources/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/auth/login")
				.defaultSuccessUrl(successUrl, true)
				.permitAll()
				.and()
			.logout()
				.permitAll()
				.and()
			.csrf().disable();
	}

	@Bean
	public BaseLdapPathContextSource userLdapSecurityContextSource() {
		
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource("ldap://ldap-pr.tsl.telus.com");
		contextSource.setUserDn("uid=ClientAPI,ou=people,ou=systems,ou=internal,o=telus");
		contextSource.setPassword("ClientAPI");
		
		return contextSource;
	}
	
	@Bean
	public AuthenticationProvider userAuthenticationProvider() {
		
		BindAuthenticator bindAuthenticator = new BindAuthenticator(userLdapSecurityContextSource());
		bindAuthenticator.setUserDnPatterns(new String[] {"uid={0},ou=people,ou=teammembers,ou=internal,o=telus"});
		
		LdapAuthenticationProvider authenticationProvider = new LdapAuthenticationProvider(bindAuthenticator);
		authenticationProvider.setUserDetailsContextMapper( new UserProfileContextMapper());
		
		return authenticationProvider;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(userAuthenticationProvider());
	}

	
}
