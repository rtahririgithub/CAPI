package com.telus.cmb.tool.services.log.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.telus.cmb.tool.services.log.service.IdentityEncryptionService;

@Controller
public class IdentityEncryptionController {

	@Autowired
	private IdentityEncryptionService identityEncryptionService;

	@RequestMapping("/identity")
	public ModelAndView main() {
		return new ModelAndView("identity_encryption");
	}

	@RequestMapping("/identity/encrypt")
	public ModelAndView encrypt(@RequestParam String clearText, @RequestParam(required=false) String encryptedText, @RequestParam(required=false) String decryptedResult) {
		try {
			String encryptedResult = identityEncryptionService.encrypt(clearText);
			return getModelMap(clearText, encryptedText, encryptedResult, decryptedResult, null);
		} catch (Exception e) {
			return getModelMap(clearText, encryptedText, null, decryptedResult, e); 
		}
	} 

	@RequestMapping("identity/decrypt")
	public ModelAndView decrypt(@RequestParam String encryptedText, @RequestParam(required=false) String clearText, @RequestParam(required=false) String encryptedResult) {
		try {
			System.out.println(encryptedText);
			String decryptedResult = identityEncryptionService.decrypt(encryptedText);
			System.out.println(decryptedResult);
			System.out.println(identityEncryptionService.decrypt("51c06159a6e6d43097a6d9db2f39331a57238ccd0f6067b842478c55af3c79331ff6f2ed873f3d07b94d0fd0c5a993d20311b31b693bff1d7c9ec601b029d815d009d49c6be966a8d7c1e200f602aa561cf06ee832a3d78623d5c6e80fdbca6f9212bf8d07f96c9067bbbcfad8c6346b8c6265f951364c3b7a9da6287b9d7c56"));
			return getModelMap(clearText, encryptedText, encryptedResult, decryptedResult, null);
		} catch (Exception e) {
			return getModelMap(clearText, encryptedText, encryptedResult, null, e); 
		}
	} 
	
	private ModelAndView getModelMap(String clearText, String encryptedText, String encryptedResult, String decryptedResult, Exception exception) {
		
		ModelAndView model = new ModelAndView("identity_encryption");
		model.getModelMap().addAttribute("clearText", clearText);
		model.getModelMap().addAttribute("encryptedText", encryptedText);
		model.getModelMap().addAttribute("encryptedResult", encryptedResult);
		model.getModelMap().addAttribute("decryptedResult", decryptedResult);
		model.getModelMap().addAttribute("exception", exception);

		return model;
	}
}
