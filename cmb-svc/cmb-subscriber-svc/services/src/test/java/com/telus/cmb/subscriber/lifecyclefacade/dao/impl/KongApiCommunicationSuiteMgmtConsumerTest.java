package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.telus.cmb.common.security.KongClientSecurityConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={KongClientSecurityConfig.class, CommunicationSuiteMgmtSvcDaoImpl.class}, loader = AnnotationConfigContextLoader.class)
public class KongApiCommunicationSuiteMgmtConsumerTest {

    @Autowired
    private CommunicationSuiteMgmtSvcDaoImpl kongServiceConsumer;

    @Test
    public void access_api_with_token() throws Exception {
    	//https://apigw-st.tsl.telus.com/customer/communicationSuiteMgmt/v1/communicationSuite/70922766-4161966330/unlink
		int ban = 70922766;
		String primaryPhoneNumber = "4161966202";
    	String companionPhoneNumber = "4161966330";
		kongServiceConsumer.removeFromCommunicationSuite(ban, companionPhoneNumber, primaryPhoneNumber);
    	
    }
}
