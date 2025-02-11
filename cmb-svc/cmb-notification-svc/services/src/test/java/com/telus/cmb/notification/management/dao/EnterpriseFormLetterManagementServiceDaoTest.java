package com.telus.cmb.notification.management.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations="classpath:application-context-test.xml")
@ActiveProfiles({"standalone"})
public class EnterpriseFormLetterManagementServiceDaoTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private EnterpriseFormLetterManagementServiceDao dao;
	
	@Test
	public void submitFormLetter() throws Exception {
		
		System.out.println(dao);
	}
}
