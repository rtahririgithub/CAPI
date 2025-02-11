package com.telus.cmb.subscriber.lifecyclemanager;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-lifecyclemanager.xml",
"classpath:application-context-datasources-lifecyclehelper-testing-d3.xml"})
public abstract class BaseLifecycleManagerIntTest {
	
/*	public BaseLifecycleManagerIntTest() {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:application-context-lifecyclemanager-test.xml");
		Properties props = (Properties)classPathXmlApplicationContext.getBean("environmentProperties");
		System.setProperties(props);
	}*/

}
