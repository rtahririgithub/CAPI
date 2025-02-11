package com.telus.ait.cmb.steps.db;

import static junit.framework.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.integration.kb.dao.KnowbilityDao;

@ContextConfiguration({"classpath:kb-db-context.xml"})
public class ClientApiKbDbSteps {
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
    public String getNGPByNPA(String npa) {
    	return kbDao.getNGPByNPA(npa);
    }
    
    @Step
    public String findAvailableUSim(List<Map<String, String>> latestUSimAndImsis) {
    	for (Map<String, String> uSimAndImsi : latestUSimAndImsis) {
        	for (String uSim : uSimAndImsi.keySet()) {
        		if (kbDao.isUSimUsed(uSimAndImsi.get(uSim))) {
        			return uSim;
        		}
        	}
        }
    	return null;
    }
    
}