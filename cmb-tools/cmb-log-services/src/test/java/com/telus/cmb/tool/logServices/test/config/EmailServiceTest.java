package com.telus.cmb.tool.logServices.test.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.tool.services.log.service.EmailService;

@Test
@ContextConfiguration("classpath:application-context-test.xml")
public class EmailServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	EmailService emailService;
	
	@Test(enabled = false)
	public void test_anonymous_email() throws Exception {
		List<String> emailList = Arrays.asList(new String[] {"your_email@here.com"}); 
		String subject = "CAPI Logtool unit test";
		String body = "Unit test for CAPI Logtool";
		emailService.sendEmail(emailList, subject, body);
	}

	@Test(enabled = false)
	public void test_personal_email() throws Exception {	
		List<String> emailList = Arrays.asList(new String[] {"your_email@here.com"}); 
		String subject = "CAPI Logtool unit test2";
		String body = "Unit test for CAPI Logtool";
		String fromEmail = "capi.mailer2@telus.com"; 
		String fromName = "CAPI Mailer2";
		emailService.sendEmail(emailList, subject, body, fromEmail, fromName, false);
	}
	
}
