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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.telus.cmsc.domain.artifact.Environment;
import com.telus.cmsc.domain.identity.ApplicationIdentity;
import com.telus.cmsc.domain.identity.ApplicationIdentityEntry;
import com.telus.cmsc.service.ApplicationIdentityManagementService;
import com.telus.cmsc.service.EnvironmentService;
import com.telus.cmsc.web.model.IdentityDecryptionForm;

/**
 * @author Pavel Simonovsky
 *
 */

@Controller
@RequestMapping("identity")
public class IdentityManagementController extends BaseController {

	@Autowired
	private EnvironmentService environmentService;
	
	@Autowired
	private ApplicationIdentityManagementService identityManagementService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		
		binder.registerCustomEditor(Environment.class, "environment", new PropertyEditorSupport() {
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(environmentService.getEnvironment(Integer.parseInt(text)));
			};
		});
	}
	
	@RequestMapping("manage")
	public String getIdentityManagementPage(Model model) {
		return "identity/identity-mgmt-page";
	}
	
	@RequestMapping("encrypt")
	public String encryptIdentities(@ModelAttribute("applicationIdentity") ApplicationIdentity identity, Errors errors, 
			@ModelAttribute("decryptionForm") IdentityDecryptionForm decryptionForm, Model model) {
		
		ValidationUtils.rejectIfEmpty(errors, "applicationCode", "error.field.empty");
		ValidationUtils.rejectIfEmpty(errors, "applicationKey", "error.field.empty");
		
		if (!errors.hasErrors()) {
			try {
				String sourceContent = identityManagementService.encrypt(identity);
				decryptionForm.setSourceContent(sourceContent);
				createSuccessResponse(model, "Application identity has been encrypted successfully.");
			} catch (Exception e) {
				createFailureResponse(model, "Unable to encrypt application identity: %s", e.getMessage());
			}
		}
		
		return getIdentityManagementPage(model);
	}

	@RequestMapping("decrypt")
	public String decryptIdentities(@ModelAttribute("decryptionForm") IdentityDecryptionForm form, Errors errors, Model model) {

		try {

			ApplicationIdentity identity = identityManagementService.decrypt(form.getSourceContent());
			model.addAttribute("applicationIdentity", identity);
			
			createSuccessResponse(model, "Source content has been decrypted successfully");
			
		} catch (Exception e) {
			errors.rejectValue("sourceContent", "", "asdasdasdas");
			createFailureResponse(model, "Unable to decrypt source content: %s Please correct the errors and try again.", e.getMessage());
		}
		
		return getIdentityManagementPage(model);
	}
	
	@ModelAttribute("decryptionForm")
	public IdentityDecryptionForm prepareForm() {
		return new IdentityDecryptionForm();
	}
	
	@ModelAttribute("applicationIdentity")
	public ApplicationIdentity prepareModel(Model model) {
		
		ApplicationIdentity identity = new ApplicationIdentity();
		
		for (Environment environment : environmentService.getEnvironments()) {
			if (!environment.isFlipperMember()) {
				ApplicationIdentityEntry entry = new ApplicationIdentityEntry();
				entry.setEnvironment(environment);
				
				identity.getEntries().add(entry);
			}
		}
		
		return identity;
	}
}
