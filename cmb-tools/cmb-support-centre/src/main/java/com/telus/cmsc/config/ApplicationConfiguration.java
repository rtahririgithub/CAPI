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

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.ConfigurationFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.telus.cmsc.service.ApplicationIdentityManagementService;
import com.telus.cmsc.service.ApplicationIdentityManagementServiceImpl;
import com.telus.cmsc.service.EnvironmentRuntimeBuilder;

/**
 * @author Pavel Simonovsky
 *
 */

@Configuration
@EnableCaching
@ComponentScan(basePackages = "com.telus.cmsc")
public class ApplicationConfiguration {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean
	public ApplicationIdentityManagementService applicationIdentityManagementService() {
		ApplicationIdentityManagementServiceImpl service = new ApplicationIdentityManagementServiceImpl();
		
		service.setKeystore(applicationContext.getResource("classpath:app-identity-mgmt.keystore"));
		service.setKeyStorePassword("weblogic");
		
		return service;
	}
	
	@Bean 
	public org.springframework.cache.CacheManager cacheManager() {
		InputStream is = getClass().getResourceAsStream("/ehcache.xml");
		net.sf.ehcache.config.Configuration config = ConfigurationFactory.parseConfiguration(is);
		CacheManager ehcacheManager = CacheManager.create(config);
		EhCacheCacheManager cacheManager = new EhCacheCacheManager(ehcacheManager);
		return cacheManager;
	}
	
	@Bean
	public net.sf.ehcache.CacheManager ehCacheManager() {
		return ((EhCacheCacheManager) cacheManager()).getCacheManager();
	}	
	
	@Bean
	public ExecutorService executorService() {
		return Executors.newFixedThreadPool(5);
	}
	
	@Bean
	public EnvironmentRuntimeBuilder environmentRuntimeBuilder() {
		return new EnvironmentRuntimeBuilder();
	}
	
}
