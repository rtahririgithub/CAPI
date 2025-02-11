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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.telus.cmsc.domain.artifact.Environment;
import com.telus.cmsc.service.EnvironmentService;

/**
 * @author Pavel Simonovsky	
 *
 */

@Controller
@RequestMapping("/admin/environments")
public class EnvironmentManagementController extends BaseController {

	@Autowired
	private EnvironmentService environmentService;
	
	@RequestMapping("manage")
	public String getEnvironmentManagementPage(Model model) {
		
		model.addAttribute("environments", environmentService.getEnvironments());
		
		return "admin/environment-management-page";
	}

	@RequestMapping("create")
	public String createEnvironment(Model model) {
		return getEnvironmentPage( new Environment(), model);
	}
	
	@RequestMapping("modify")
	public String modifyEnvironment(@RequestParam("environmentId") int environmentId, Model model) {
		Environment environment = environmentService.getEnvironment(environmentId);
		return getEnvironmentPage(environment, model);
	}

	@RequestMapping("delete")
	public String deleteEnvironment(@RequestParam("environmentId") int environmentId, Model model) {
		environmentService.deleteEnvironment(environmentId);

		return redirectToManagementPage();
	}
	
	private String getEnvironmentPage(Environment environment, Model model) {
		model.addAttribute("environment", environment);
		return "admin/environment-modify-page";
	}
	
	@RequestMapping("save")
	public String savePermission(@ModelAttribute("environment") Environment environment, BindingResult errors, RedirectAttributes redirectAttributes, Model model) {
		
		ValidationUtils.rejectIfEmpty(errors, "name", "error.field.empty");
		ValidationUtils.rejectIfEmpty(errors, "code", "error.field.empty");
		ValidationUtils.rejectIfEmpty(errors, "ldapUrl", "error.field.empty");
		
		if (errors.hasErrors()) {
			return getEnvironmentPage(environment, model);
		}
		
		environmentService.saveEnvironment(environment);
		
		createSuccessResponse(redirectAttributes, "Environment '%s' was updated successfully", environment.getName());
		
		return redirectToManagementPage();
	}

	private String redirectToManagementPage() {
		return "redirect:/admin/environments/manage";
	}	
}
