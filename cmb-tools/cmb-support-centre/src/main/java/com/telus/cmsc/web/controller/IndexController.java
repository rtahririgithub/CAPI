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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Pavel Simonovsky
 *
 */

@Controller
public class IndexController {

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String getLandingPage() {
		return "core/landing-page";
	}
}
