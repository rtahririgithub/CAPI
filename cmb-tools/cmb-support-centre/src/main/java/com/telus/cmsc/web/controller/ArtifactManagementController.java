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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.telus.cmsc.domain.artifact.Artifact;
import com.telus.cmsc.domain.artifact.ArtifactGroup;
import com.telus.cmsc.service.ArtifactService;
import com.telus.cmsc.service.EnvironmentService;

/**
 * @author Pavel Simonovsky	
 *
 */
@Controller
@RequestMapping("/admin/artifacts")
public class ArtifactManagementController extends BaseController {

	@Autowired
	private ArtifactService artifactService;
	
	@Autowired
	private EnvironmentService environmentService;
	
	@RequestMapping("manage")
	public String getEnvironmentManagementPage(Model model) {
		
		model.addAttribute("artifacts", artifactService.getArtifacts());
		model.addAttribute("groups", artifactService.getGroups());
		
		return "admin/artifact-management-page";
	}

	@RequestMapping("register")
	public String registerArtifact(@RequestParam("artifactCode") String artifactCode, Model model) {
		Artifact artifact = new Artifact();
		artifact.setCode(artifactCode);
		return getArtifactPage(artifact, model);
	}
	
	@RequestMapping("create")
	public String createArtifact(Model model) {
		return getArtifactPage( new Artifact(), model);
	}

	@RequestMapping("group/create")
	public String createGroup(Model model) {
		return getGroupPage( new ArtifactGroup(), model);
	}
	
	@RequestMapping("modify")
	public String modifyArtifact(@RequestParam("artifactId") int artifactId, Model model) {
		return getArtifactPage( artifactService.getArtifact(artifactId), model);
	}

	@RequestMapping("group/modify")
	public String modifyGroup(@RequestParam("groupId") int groupId, Model model) {
		return getGroupPage(artifactService.getGroup(groupId), model);
	}
	
	@RequestMapping("delete")
	public String deleteArtifact(@RequestParam("artifactId") int artifactId, Model model) {
		artifactService.deleteArtifact(artifactId);
		return redirectToManagementPage();
	}

	@RequestMapping("group/delete")
	public String deleteGroup(@RequestParam("groupId") int groupId, Model model) {
		artifactService.deleteGroup(groupId);
		return redirectToManagementPage();
	}
	
	private String getArtifactPage(Artifact artifact, Model model) {
		
		model.addAttribute("artifact", artifact);
		if (artifact.getArtifactId() != null) {
			model.addAttribute("artifactGroups", artifactService.getArtifactGroups(artifact.getArtifactId()));
		}
		model.addAttribute("groups", artifactService.getGroups());
		
		return "admin/artifact-modify-page";
	}

	private String getGroupPage(ArtifactGroup group, Model model) {
		model.addAttribute("group", group);
		if (group.getGroupId() != null) {
			model.addAttribute("groupArtifacts", artifactService.getGroupArtifacts(group.getGroupId()));
		}
		model.addAttribute("artifacts", artifactService.getArtifacts());
		return "admin/group-modify-page";
	}
	
	@RequestMapping("save")
	public String saveArtifact(@ModelAttribute("artifact") Artifact artifact, @RequestParam(value="groupId", defaultValue="") List<Integer> groupIds, BindingResult errors, RedirectAttributes redirectAttributes, Model model) {
		
		ValidationUtils.rejectIfEmpty(errors, "name", "error.field.empty");
		ValidationUtils.rejectIfEmpty(errors, "code", "error.field.empty");
		ValidationUtils.rejectIfEmpty(errors, "logPathPattern", "error.field.empty");
		
		if (errors.hasErrors()) {
			return getArtifactPage(artifact, model);
		}
		
		artifactService.saveArtifact(artifact, groupIds);
		
		createSuccessResponse(redirectAttributes, "Artifact '%s' was updated successfully", artifact.getName());
		
		return redirectToManagementPage();
	}

	@RequestMapping("group/save")
	public String saveGroup(@ModelAttribute("group") ArtifactGroup group, @RequestParam(value="artifactId", defaultValue="") List<Integer> artifactIds, BindingResult errors, RedirectAttributes redirectAttributes, Model model) {
		
		ValidationUtils.rejectIfEmpty(errors, "name", "error.field.empty");
		
		if (errors.hasErrors()) {
			return getGroupPage(group, model);
		}
		
		artifactService.saveGroup(group, artifactIds);
		
		createSuccessResponse(redirectAttributes, "Group '%s' was updated successfully", group.getName());
		
		return redirectToManagementPage();
	}
	
	private String redirectToManagementPage() {
		return "redirect:/admin/artifacts/manage";
	}		
}
