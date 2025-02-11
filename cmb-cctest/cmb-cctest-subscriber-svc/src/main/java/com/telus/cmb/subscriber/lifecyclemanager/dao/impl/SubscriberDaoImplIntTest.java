package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.subscriber.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;

@RunWith(SpringJUnit4ClassRunner.class)
public class SubscriberDaoImplIntTest extends BaseLifecycleManagerIntTest {

	@Autowired
	NewPcsSubscriberDaoImpl dao;
	
	
	@Before
	public void before() {

	}
	
	@Test
	public void testSaveSubscriptionPreference() throws ApplicationException{
		
		SubscriptionPreferenceInfo preferenceInfo = new SubscriptionPreferenceInfo();
		preferenceInfo.setPreferenceTopicId(1);
		preferenceInfo.setSubscriptionId(12255794);
		preferenceInfo.setSubscriberPreferenceId(22);
		preferenceInfo.setSubscrPrefChoiceSeqNum(0);
		preferenceInfo.setPreferenceValueTxt("N");
		String user = "ABC";

		dao.saveSubscriptionPreference(preferenceInfo, user);
	}	
	
}
