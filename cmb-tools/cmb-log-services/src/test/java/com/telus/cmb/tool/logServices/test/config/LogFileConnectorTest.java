package com.telus.cmb.tool.logServices.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.telus.cmb.tool.services.log.service.LogFileService;

@Test
@ContextConfiguration("classpath:application-context-test.xml")
public class LogFileConnectorTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private LogFileService logFileService;

	@Test
	public void verify_jsch_invalid_credentials() {
		Assert.assertEquals(logFileService.isCredentialValid("ln98312", "x162017", "password"), false);
	}
	
}
