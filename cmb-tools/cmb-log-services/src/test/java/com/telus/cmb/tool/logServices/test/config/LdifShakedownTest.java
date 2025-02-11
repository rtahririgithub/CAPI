package com.telus.cmb.tool.logServices.test.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.domain.LdifSearchResult;
import com.telus.cmb.tool.services.log.service.LdifShakedownService;

@Test
@ContextConfiguration("classpath:application-context-test.xml")
public class LdifShakedownTest extends AbstractTestNGSpringContextTests {

	@Autowired
	LdifShakedownService ldifShakedownService;

	FilePathConfig filePathConfig = FilePathConfig.getInstance();
	
	@Test(enabled = false)
	public void test_ldif_shakedown() throws Exception {
		Environment environment = filePathConfig.getEnvironment("st101a");
		List<LdifSearchResult> results = ldifShakedownService.shakedown(environment, 50473);
		// To test for integrity of the shakedown, run in the other ldif tool with the same environment and release id
		// Search for the # of "ERRORs" and update the value here
		Assert.assertEquals(results.size(), 14);
	}
	
}
