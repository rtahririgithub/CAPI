package com.telus.cmb.tool.logServices.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.telus.cmb.tool.services.log.dao.SubscriberInfoDao;
import com.telus.cmb.tool.services.log.dao.SubscriberInfoSvcDaoImpl;
import com.telus.cmb.tool.services.log.domain.task.SubscriberInfo;

@Test
@ContextConfiguration("classpath:application-context-test.xml")
public class SubscriberInfoSvcTest extends AbstractTestNGSpringContextTests {

	@Autowired
	@Qualifier("database")
	SubscriberInfoDao subscriberInfoDao;
	
	@Test
	public void test_get_subscriber_by_phone_number() throws Exception {
		SubscriberInfo sub = subscriberInfoDao.getSubscriberBySubscriberId("6472708915");
		Assert.assertEquals(sub.getBan(), 13570200);		
		Assert.assertEquals(sub.getDatasource(), SubscriberInfoSvcDaoImpl.SOURCE);
	}
	
}
