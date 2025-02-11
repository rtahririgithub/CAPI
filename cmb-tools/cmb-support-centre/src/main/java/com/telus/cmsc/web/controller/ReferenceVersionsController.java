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

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.telus.cmsc.domain.artifact.Artifact;
import com.telus.cmsc.domain.artifact.Environment;
import com.telus.cmsc.domain.artifact.ReferenceVersion;
import com.telus.cmsc.domain.artifact.ReferenceVersionEntry;
import com.telus.cmsc.domain.artifact.ReferenceVersionForm;
import com.telus.cmsc.service.ArtifactService;
import com.telus.cmsc.service.EnvironmentService;

/**
 * @author Pavel Simonovsky	
 *
 */
@Controller
@RequestMapping("/versions")
public class ReferenceVersionsController {

	@Autowired
	private ArtifactService artifactService;
	
	@Autowired
	private EnvironmentService environmentService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		
		binder.registerCustomEditor(Environment.class, "environment", new PropertyEditorSupport() {
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(environmentService.getEnvironment(Integer.parseInt(text)));
			};
		});
	}
	
	@RequestMapping("manage")
	public String getReferenceVersionsManagementPage(Model model) {
		
		
		return "runtimes/version-management-page";
	}

	@RequestMapping("save")
	public String saveReferenceVersions(@ModelAttribute("referenceVersionForm") ReferenceVersionForm form, Model model) {
		
		if (form.getArtifact() != null) {
			
			Artifact artifact = form.getArtifact();
			
			List<ReferenceVersion> versions = new ArrayList<ReferenceVersion>();
			
			for (ReferenceVersionEntry entry : form.getEntries()) {
				
				ReferenceVersion version = new ReferenceVersion();
				version.setArtifactId(artifact.getArtifactId());
				version.setEnvironmentId(entry.getEnvironment().getEnvironmentId());
				version.setVersion(entry.getVersion());
				version.setNotes(entry.getNotes());
				
				versions.add(version);
				
				artifactService.saveReferenceVersions(artifact.getArtifactId(), versions);
			}
		}
		
		return getReferenceVersionsManagementPage(model);
	}
	
	@ModelAttribute("referenceVersionForm")
	public ReferenceVersionForm prepareModel(@RequestParam(value = "artifactId", defaultValue = "0") Integer artifactId) {
		ReferenceVersionForm form = new ReferenceVersionForm();
		form.setArtifacts(artifactService.getArtifacts());
		if (artifactId != 0) {
			
			Artifact artifact = artifactService.getArtifact(artifactId);
			
			form.setArtifact(artifact);
			form.setArtifactId(artifact.getArtifactId());
			
			for (Environment environment : environmentService.getEnvironments()) {
				
				ReferenceVersionEntry entry = new ReferenceVersionEntry();
				entry.setEnvironment(environment);
				
				ReferenceVersion referenceVersion = artifactService.getReferenceVersion(artifactId, environment.getEnvironmentId());
				if (referenceVersion != null) {
					entry.setNotes(referenceVersion.getNotes());
					entry.setVersion(referenceVersion.getVersion());
				} 
				
				form.getEntries().add(entry);
			}
		}
		
		return form;
	}
}
