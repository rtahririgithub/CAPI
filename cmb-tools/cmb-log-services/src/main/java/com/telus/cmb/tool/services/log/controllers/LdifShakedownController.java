package com.telus.cmb.tool.services.log.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.domain.RAPiDRelease;
import com.telus.cmb.tool.services.log.service.LdifShakedownService;

@Controller
public class LdifShakedownController {

	private FilePathConfig filePathConfig = FilePathConfig.getInstance();
	
	@Autowired
	private LdifShakedownService ldifShakedownService;

	@RequestMapping("/ldif")
	public ModelAndView selectEnvironment() {
		List<Environment> environments = new ArrayList<>();
		for (Environment environment : filePathConfig.getEnvironments()) {
			if (environment.getLdap() != null) {
				environments.add(environment);
			}
		}
		return new ModelAndView("ldif_choose_env", "environments", environments);
	}

	@RequestMapping("/ldif/{env}")
	public ModelAndView selectRelease(@PathVariable("env") String envShortName, HttpSession session) {

		Environment environment = filePathConfig.getEnvironment(envShortName);
		if (environment == null) {
			return new ModelAndView("redirect:/ldif");
		}
		ModelAndView model = new ModelAndView("ldif_choose_release");
		model.getModelMap().addAttribute("environment", environment);
		model.getModelMap().addAttribute("releases", ldifShakedownService.getReleases());

		return model;
	}

	@RequestMapping("/ldif/{env}/shakedown/release/{release}")
	public ModelAndView shakedownForRelease(@PathVariable("env") String envShortName, @PathVariable("release") String releaseId, HttpSession session) {

		Environment environment = filePathConfig.getEnvironment(envShortName);
		if (environment == null || releaseId == null || !StringUtils.isNumeric(releaseId)) {
			return new ModelAndView("redirect:/ldif");
		}
		
		ModelAndView model = new ModelAndView("ldif_results");
		model.getModelMap().addAttribute("environment", environment);
		model.getModelMap().addAttribute("shakedown", getRelease(Integer.valueOf(releaseId)).getName());
		model.getModelMap().addAttribute("results", ldifShakedownService.shakedown(environment, Integer.valueOf(releaseId)));
		
		return model;
	}

	@RequestMapping("/ldif/{env}/shakedown/list")
	public ModelAndView shakedownByLdifList(@PathVariable("env") String envShortName, @RequestParam String ldifList, HttpSession session) {

		Environment environment = filePathConfig.getEnvironment(envShortName);
		
		if (environment == null || StringUtils.isBlank(ldifList)) {
			return new ModelAndView("redirect:/ldif");
		}
		
		ModelAndView model = new ModelAndView("ldif_results");
		model.getModelMap().addAttribute("environment", environment);
		model.getModelMap().addAttribute("shakedown", "provided LDIF list");
		model.getModelMap().addAttribute("results", ldifShakedownService.shakedown(environment, Arrays.asList(ldifList.split("\\r?\\n"))));
		
		return model;
	}

	private RAPiDRelease getRelease(int releaseId) {				
		for (RAPiDRelease release : ldifShakedownService.getReleases()) {
			 if (release.getId() == releaseId) {
				 return release;
			 }
		}
		return new RAPiDRelease();
	}

}
