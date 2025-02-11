package com.telus.ait.cmb.steps.db;

import static junit.framework.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.integration.dist.dao.DistDao;
import com.telus.ait.integration.kb.dao.KnowbilityDao;

@ContextConfiguration({"classpath:db-context.xml"})
public class ClientApiDistDbSteps {
    @Rule
    public SpringIntegration springIntegration = new SpringIntegration();

    @Autowired
    private DistDao distDao;

    @Autowired
    private KnowbilityDao kbDao;
    
    @Step
    public String retrieveFirstEligibleImeiBySku(String sku) throws Exception {
        Iterator<String> eligibleImeiListBySku = distDao.getEligibleImeiListBySku(sku);
        String firstEligibleImeiBySku = eligibleImeiListBySku.next();
        return firstEligibleImeiBySku;
    }

    @SuppressWarnings("deprecation")
	@Step
    public void checkDataSource() {
        long value = distDao.checkDataSource();
        assertEquals("Value should be 1", 1, value);
    }

    @Step
    public String getAvailableSimNumber() throws Exception {
        List<String> simList = distDao.getAvailableSimNumbers();
        if (simList != null && !simList.isEmpty()) {
        	return simList.get(0);
        }
        return null;
    }

    @Step
    public String getAvailableUSim(int brandId) throws Exception {
    	List<Map<String, String>> latestUSimAndImsis = distDao.getLatestUSimAndImsis(String.valueOf(brandId));
    	for (Map<String, String> uSimAndImsi : latestUSimAndImsis) {
    		for (String uSim : uSimAndImsi.keySet()) {
    			if (!kbDao.isUSimUsed(uSimAndImsi.get(uSim))) {
    				return uSim;
    			}
    		}
    	}
    	return null;
    }
}