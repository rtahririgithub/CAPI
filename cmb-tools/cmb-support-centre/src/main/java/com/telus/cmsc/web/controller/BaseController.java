/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.telus.cmsc.domain.user.UserProfile;
import com.telus.cmsc.web.model.ResponseStatus;

/**
 * @author Pavel Simonovsky	
 *
 */
public class BaseController {

	protected ResponseStatus createSuccessResponse(RedirectAttributes redirectAttributes, String message, Object ... args) {
		ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.addMessage(String.format(message, args));
		
		redirectAttributes.addFlashAttribute("status", responseStatus);
		
		return createResponse(responseStatus, redirectAttributes);
	}
	
	protected ResponseStatus createFailureResponse(RedirectAttributes redirectAttributes, String message, Object ... args) {
		ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.addError(String.format(message, args));
		
		return createResponse(responseStatus, redirectAttributes);
	}
	
	protected ResponseStatus createResponse(ResponseStatus responseStatus, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("status", responseStatus);
		return responseStatus;
	}	

	protected ResponseStatus createSuccessResponse(Model model, String message) {
		return createSuccessResponse(model, message, new Object[0]);
	}
	protected ResponseStatus createSuccessResponse(Model model, String message, Object ... args) {
		ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.addMessage(String.format(message, args));
		
		return createResponse(responseStatus, model);
	}
	
	protected ResponseStatus createFailureResponse(Model model, String message, Object ... args) {
		ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.addError(String.format(message, args));
		
		return createResponse(responseStatus, model);
	}
	
	protected ResponseStatus createResponse(ResponseStatus responseStatus, Model model) {
		model.addAttribute("status", responseStatus);
		return responseStatus;
	}	
	
	protected UserProfile getUserProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			return (UserProfile) authentication.getPrincipal();
		}
		return null;
	}
	
}
