/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.telus.cmsc.domain.user.UserProfile;
import com.telus.cmsc.web.model.ActionManager;

/**
 * @author Pavel Simonovsky	
 *
 */
public class AttributeExposingInterceptor extends HandlerInterceptorAdapter {

	private ActionManager actionManager;
	
	public void setActionManager(ActionManager actionManager) {
		this.actionManager = actionManager;
	}
	
	private String baseHref;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserProfile) {
			UserProfile profile = (UserProfile) authentication.getPrincipal();
			request.setAttribute("userProfile", profile);
			request.setAttribute("userPreferences", profile.getPreferences());
		}
		
		request.setAttribute("baseHref", getBaseHref(request));
		request.setAttribute("actionManager", actionManager);
		
		return super.preHandle(request, response, handler);
	}
	
	private String getBaseHref(HttpServletRequest request) {
		if (StringUtils.isEmpty(baseHref)) {
				
			StringBuffer buffer = new StringBuffer();
				
			buffer.append(request.getScheme()).append("://").append(request.getServerName())
				.append(":").append(request.getServerPort()).append(request.getContextPath()).append("/");
				
				baseHref = buffer.toString();
		}
		
		System.out.println(baseHref);
		
		return baseHref;
	}
	
}
