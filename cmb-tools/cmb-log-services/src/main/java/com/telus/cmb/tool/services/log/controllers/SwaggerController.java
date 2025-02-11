package com.telus.cmb.tool.services.log.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.telus.cmb.tool.services.log.domain.swagger.SwaggerFileEnum;

@Controller
public class SwaggerController {

	Logger logger = Logger.getLogger(SwaggerController.class);
			
	@RequestMapping("/swagger")
	public ModelAndView swagger() {		
		ModelAndView model = new ModelAndView("swagger_list");
		model.getModelMap().addAttribute("services", SwaggerFileEnum.values());
		return model;
	}
	
	@RequestMapping("/swagger/read/{serviceName}")
	public ModelAndView swaggerRead(@PathVariable("serviceName") String serviceName) {		
		ModelAndView model = new ModelAndView("swagger_read");
		model.getModelMap().addAttribute("swaggerFile", SwaggerFileEnum.valueOf(serviceName));
		return model;
	}
}
