package com.telus.cmb.account.lifecyclemanager;

import java.util.Properties;

import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-lifecyclemanager.xml"
		, "classpath:application-context-datasources-informationhelper-testing-d3.xml"
		, "classpath:application-context-lifecyclemanager-test.xml"})
public abstract class BaseLifecycleManagerIntTest {
	
	public BaseLifecycleManagerIntTest() {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:application-context-lifecyclemanager-test.xml");
		Properties props = (Properties)classPathXmlApplicationContext.getBean("environmentProperties");
		System.setProperties(props);
	}

}
