package com.telus.cmb.tool.services.log.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GeneralController {

	Logger logger = Logger.getLogger(GeneralController.class);
	
	@RequestMapping("/readme")
	public ModelAndView readme() {		
		return new ModelAndView("readme");
	}
	
}
