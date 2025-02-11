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
import org.springframework.web.bind.annotation.RequestMapping;

import com.telus.cmsc.service.ArtifactRuntimeService;
import com.telus.cmsc.service.EnvironmentService;

/**
 * @author Pavel Simonovsky
 *
 */

@Controller
@RequestMapping("dashboard")
public class DashboardController extends BaseController {

	@Autowired
	private EnvironmentService environmentService;
	
	@Autowired
	private ArtifactRuntimeService artifactRuntimeService;
	
	
	@RequestMapping("summary")
	public String getDashboardPage(Model model) {
		
		model.addAttribute("runtimes", artifactRuntimeService.getEnvironmentRuntimes(true));

//		model.addAttribute("environments", environmentService.getEnvironments());
		
		return "dashboard/dashboard-summary-page";
	}
	
}
