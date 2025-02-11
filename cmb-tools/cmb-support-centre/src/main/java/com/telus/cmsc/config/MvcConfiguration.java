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

import java.util.Properties;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.telus.cmsc.web.model.Action;
import com.telus.cmsc.web.model.ActionManager;
import com.telus.cmsc.web.support.AttributeExposingInterceptor;

/**
 * @author Pavel Simonovsky
 *
 */

@EnableWebMvc
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addViewControllers(org.springframework.web.servlet.config.annotation.ViewControllerRegistry)
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/auth/login").setViewName("core/login-page");
		registry.addViewController("/auth/logout").setViewName("core/logout-page");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry)
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureDefaultServletHandling(org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer)
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Bean
	public ViewResolver jspViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasename("/WEB-INF/messages");
		source.setCacheSeconds(1);	
		source.setDefaultEncoding("UTF-8");
		return source;
	}
	
	@Bean
	public HandlerInterceptor attributeExposingInterceptor() {
		AttributeExposingInterceptor interceptor = new AttributeExposingInterceptor();
		interceptor.setActionManager(actionManager());
		return interceptor;
	}

	@Bean
	public HandlerInterceptor webContentInterceptor() { 
		
		Properties cacheMappings = new Properties();
		cacheMappings.put("/resources/**", "2592000");
		
		WebContentInterceptor interceptor = new WebContentInterceptor();
		interceptor.setCacheSeconds(0);
		interceptor.setUseExpiresHeader(true);
		interceptor.setUseCacheControlHeader(true);
		interceptor.setUseCacheControlNoStore(true);
		interceptor.setCacheMappings(cacheMappings);
		
		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(attributeExposingInterceptor());
		registry.addInterceptor(webContentInterceptor());
	}
	
	@Bean
	public ActionManager actionManager() {
		ActionManager manager = new ActionManager();
		
		Action dashboard = new Action("dashboard", "Dashboard", "icon-speedometer", "dashboard/summary");
		
		Action admin = new Action("administration", "Administration", "icon-settings", "javascript:;");
		admin.addAction( new Action("environmentManagement", "Environment Management", "icon-social-dropbox", "admin/environments/manage"));
		admin.addAction( new Action("artifactManagement", "Artifact Management", "icon-social-dropbox", "admin/artifacts/manage"));

		Action development = new Action("development", "Development", "icon-puzzle", "javascript:;");
		development.addAction( new Action("configExplorer", "Configuration Explorer"));
		development.addAction( new Action("identityManagement", "Identity Management", "icon-social-dropbox", "identity/manage"));

		Action support = new Action("support", "Support", "icon-earphones-alt", "javascript:;");
//		support.addAction( new Action("environmentRuntimes", "Environment Runtimes Summary", "icon-social-dropbox", "runtime/environments"));
		support.addAction( new Action("referenceVersionManagement", "Reference Versions", "icon-social-dropbox", "versions/manage"));
		
		manager.addAction(dashboard);
		manager.addAction(admin);
		manager.addAction(development);
		manager.addAction(support);
		
		return manager;
	}
	
	
}
