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

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.telus.cmsc.domain.artifact.ArtifactRuntime;
import com.telus.cmsc.domain.artifact.ArtifactRuntimeInstance;
import com.telus.cmsc.domain.artifact.EnvironmentRuntime;
import com.telus.cmsc.service.ArtifactRuntimeService;
import com.telus.cmsc.service.LoggingService;

/**
 * @author Pavel Simonovsky	
 *
 */

@Controller
@RequestMapping("/logs")
public class LoggingController extends BaseController {

	@Autowired
	private ArtifactRuntimeService runtimeService;
	
	@Autowired
	private LoggingService loggingService;
	
	
	@RequestMapping("artifact")
	public String getArtifactLoggingPage(@RequestParam("environmentId") int environmentId, @RequestParam("artifactCode") String artifactCode, @RequestParam("instanceId") int instanceId, Model model) {
		
		
		EnvironmentRuntime environmentRuntime = runtimeService.getEnvironmentRuntime(environmentId, true);
		ArtifactRuntime artifactRuntime = environmentRuntime.getArtifactRuntime(artifactCode);
		ArtifactRuntimeInstance artifactInstance = artifactRuntime.getInstance(instanceId);
		
		model.addAttribute("environment", environmentRuntime.getEnvironment());
		model.addAttribute("artifactInstance", artifactInstance);
		model.addAttribute("artifact", artifactRuntime.getArtifact());
		model.addAttribute("logFiles", loggingService.getLogFiles(artifactInstance, environmentRuntime.getEnvironment()));
		model.addAttribute("logFilePath", artifactInstance.getLogFilePath(environmentRuntime.getEnvironment()));
		
		return "logging/artifact-logging-page";
	}
	
	@ResponseBody
	@RequestMapping(value = "file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> getLogFile(@RequestParam("environmentId") int environmentId, @RequestParam("artifactCode") String artifactCode, 
			@RequestParam("instanceId") int instanceId, @RequestParam("fileName") String fileName, Model model) {

		EnvironmentRuntime environmentRuntime = runtimeService.getEnvironmentRuntime(environmentId, true);
		ArtifactRuntime artifactRuntime = environmentRuntime.getArtifactRuntime(artifactCode);
		ArtifactRuntimeInstance artifactInstance = artifactRuntime.getInstance(instanceId);
		
		for (File file : loggingService.getLogFiles(artifactInstance, environmentRuntime.getEnvironment())) {
			if (StringUtils.equals(fileName, file.getName())) {
				

			    HttpHeaders respHeaders = new HttpHeaders();
			    respHeaders.setContentType(MediaType.TEXT_PLAIN);
			    respHeaders.setContentLength(file.length());
			    respHeaders.setContentDispositionFormData("attachment", fileName);

			    try {

			    	InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
			    	return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
			    	
			    } catch (Exception e) {
			    	e.printStackTrace();
			    }
			}
		}
		return null;
	}
}
