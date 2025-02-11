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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.telus.cmsc.domain.artifact.Artifact;
import com.telus.cmsc.domain.artifact.ArtifactRuntime;
import com.telus.cmsc.domain.artifact.EnvironmentRuntime;
import com.telus.cmsc.domain.artifact.ReferenceVersion;
import com.telus.cmsc.service.ArtifactRuntimeService;
import com.telus.cmsc.service.ArtifactService;

/**
 * @author Pavel Simonovsky	
 *
 */

@Controller
@RequestMapping("/runtime")
public class ArtifactRuntimeController extends BaseController {

	@Autowired
	private ArtifactRuntimeService runtimeService;
	
	@Autowired
	private ArtifactService artifactService; 
	
	@RequestMapping("environments")
	public String getEnvironmentRuntimesPage(Model model) {
		
		model.addAttribute("environmentRuntimes", runtimeService.getEnvironmentRuntimes(isFilterStandaloneArtifacts()));
		
		return "runtimes/environment-runtimes-page";
	}

	@RequestMapping("artifact/update-reference-version")
	public String updateArtifactReferenceVersion(@RequestParam("environmentId") int environmentId, @RequestParam("version") String version,
			@RequestParam("notes") String notes, @RequestParam("artifactCode") String artifactCode, RedirectAttributes redirectAttributes, Model model) {
		
		
		EnvironmentRuntime environmentRuntime = runtimeService.getEnvironmentRuntime(environmentId, isFilterStandaloneArtifacts());
		ArtifactRuntime artifactRuntime = environmentRuntime.getArtifactRuntime(artifactCode);
		Artifact artifact = artifactRuntime.getArtifact();
		
		ReferenceVersion referenceVersion = artifactService.getReferenceVersion(artifact.getArtifactId(), environmentId);
		if (referenceVersion == null) {
			referenceVersion = new ReferenceVersion();
			referenceVersion.setArtifactId(artifact.getArtifactId());
			referenceVersion.setEnvironmentId(environmentId);
		}
		
		referenceVersion.setVersion(version);
		referenceVersion.setNotes(notes);
		
		artifactService.saveReferenceVersion(referenceVersion);
		
		model.addAttribute("environment", environmentRuntime.getEnvironment());
		model.addAttribute("runtime", artifactRuntime);
		
		createSuccessResponse(redirectAttributes, "Reference version for '%s' was updated successfully", artifact.getName());

		return "redirect:/runtime/artifact/details?environmentId=" + environmentId + "&artifactCode=" + artifactCode;
	}
	
	@RequestMapping("artifact/details")
	public String getArtifactRuntimeDetailsPage(@RequestParam("environmentId") int environmentId, @RequestParam("artifactCode") String artifactCode, Model model) {
		
		EnvironmentRuntime runtime = runtimeService.getEnvironmentRuntime(environmentId, isFilterStandaloneArtifacts());
		model.addAttribute("environment", runtime.getEnvironment());
		model.addAttribute("runtime", runtime.getArtifactRuntime(artifactCode));
		
		return "runtimes/artifact-runtime-details-page";
	}

	@RequestMapping("environment")
	public String getEnvironmentRuntimePage(@RequestParam("environmentId") int environmentId, @RequestParam(value="filterClusteredOnly", required = false) Boolean filterClusteredOnly, Model model) {
		
		if (filterClusteredOnly != null) {
			getUserProfile().getPreferences().setFilterStandaloneArtifacts(filterClusteredOnly);
		}
		
		EnvironmentRuntime runtime = runtimeService.getEnvironmentRuntime(environmentId, isFilterStandaloneArtifacts());
		model.addAttribute("environmentRuntime", runtime);
		model.addAttribute("runtimeGroups", runtime.getRuntimeGroups(artifactService));
		
		return getRuntimesPage(runtime, "runtimes/environment-runtime-page", model);
	}
	
	@RequestMapping("environment/artgroups")
	public String getRuntimeArtifactGroupsPage(@RequestParam("environmentId") int environmentId, Model model) {
		
		EnvironmentRuntime runtime = runtimeService.getEnvironmentRuntime(environmentId, isFilterStandaloneArtifacts());
		model.addAttribute("runtimeGroups", runtime.getRuntimeGroups(artifactService));
		
		return getRuntimesPage(runtime, "runtimes/runtime-artifact-groups-page", model);
	}

	@RequestMapping("environment/unregistered-artifacts")
	public String getRuntimeUnregisteredArtifactsPage(@RequestParam("environmentId") int environmentId, Model model) {
		
		EnvironmentRuntime runtime = runtimeService.getEnvironmentRuntime(environmentId, isFilterStandaloneArtifacts());
		model.addAttribute("unregisteredRuntimes", runtime.getUnregisteredArtifactRuntimes());
		
		return getRuntimesPage(runtime, "runtimes/unregistered-artifacts-page", model);
	}
	
	private String getRuntimesPage(EnvironmentRuntime runtime, String targetPage, Model model) {
		model.addAttribute("environmentRuntime", runtime);
		return targetPage;
	}
	
	private boolean isFilterStandaloneArtifacts() {
		return getUserProfile().getPreferences().isFilterStandaloneArtifacts();
	}
}
