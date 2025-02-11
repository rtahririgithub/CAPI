package com.telus.ait.steps.sample.db;

import com.telus.ait.integration.emcm.dao.EMCMDao;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

import static junit.framework.Assert.assertTrue;

@ContextConfiguration({"classpath:emcm-db-context.xml"})
public class SampleEMCMDbSteps {
	@Rule
	public SpringIntegration springIntegration = new SpringIntegration();

	@Autowired
	private EMCMDao emcmDao;

	@Step
    public void pauseForDataToPopulateInDBForNumberOfSeconds(int seconds) throws Exception {
		Thread.sleep(seconds * 1000);
	}

	@Step
    public void verifyThat_SMSRequestWasGeneratedCorrectlyInDB(String subscriberNumber, Date creationDate) throws Exception {
        long contactEventId = emcmDao.getContactEventIdFromNotificationEntry(subscriberNumber, creationDate);
        assertTrue("SMS notification entry was not found in db", contactEventId > 0);
    }

}