package com.telus.ait.cmb.steps.db;

import static junit.framework.Assert.assertTrue;

import java.util.List;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.integration.kb.dao.KnowbilityDao;
import com.telus.ait.integration.kb.info.AccountInfo;
import com.telus.ait.integration.kb.info.ChargeInfo;

@ContextConfiguration({"classpath:db-context.xml"})
public class ClientApiKbDbSteps {
	
	public static final String CTN_STATUS_RELEASED = "AR";
	public static final String SUB_STATUS_RESERVED = "R";
	
    @Rule
    public SpringIntegration springIntegration = new SpringIntegration();

    @Autowired
    private KnowbilityDao kbDao;

    @SuppressWarnings("deprecation")
	@Step
    public void checkDataSource() {
        boolean value = kbDao.checkDataSource();
        assertTrue("Value should be true", value);
    }

    @Step
    public List<String> getNGPByNPA(String npa) {
    	return kbDao.getNGPByNPA(npa);
    }
      
    @Step 
    public boolean isPhoneNumberReserved(String phoneNumber) {
    	return SUB_STATUS_RESERVED.equals(kbDao.getSubscriberStatus(phoneNumber));
    }
    
    @Step 
    public boolean isPhoneNumberReleased(String phoneNumber) {
    	return CTN_STATUS_RELEASED.equals(kbDao.getCTNStatus(phoneNumber));
    }
    
	@Step 
	public String getSubscriberStatus(String phoneNumber) {
		return kbDao.getSubscriberStatus(phoneNumber);
	}
	  
	@Step 
	public String getCTNStatus(String phoneNumber) {
	  	return kbDao.getCTNStatus(phoneNumber);
	}
    
    @Step
    public ChargeInfo getCharge(String ban, String sequenceNumber) {
    	return kbDao.getChargeInfo(ban, sequenceNumber);
    }
    
    @Step
    public String getChargeSequenceNumber(String ban, String adjustmentId) {
    	return kbDao.getChargeSequenceNumber(ban, adjustmentId);
    }
    
    @Step
    public AccountInfo getAccountInfo(String ban) {
    	return kbDao.getAccountInfo(ban);
    }
    
    @Step
    public String findAnyBan(String banStatus, String accountType, String accountSubType) {
    	return kbDao.findAnyBan(banStatus, accountType, accountSubType);
    }
}