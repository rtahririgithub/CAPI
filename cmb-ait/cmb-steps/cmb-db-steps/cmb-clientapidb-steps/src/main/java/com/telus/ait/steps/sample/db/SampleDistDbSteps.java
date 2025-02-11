package com.telus.ait.steps.sample.db;

import com.telus.ait.integration.dist.dao.DistDao;
import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Iterator;

import static junit.framework.Assert.assertEquals;

@ContextConfiguration({"classpath:dist-db-context.xml"})
public class SampleDistDbSteps {
    @Rule
    public SpringIntegration springIntegration = new SpringIntegration();

    @Autowired
    private DistDao distDao;

    @Step
    public String retrieveFirstEligibleImeiBySku(String sku) throws Exception {
        Iterator<String> eligibleImeiListBySku = distDao.getEligibleImeiListBySku(sku);
        String firstEligibleImeiBySku = eligibleImeiListBySku.next();
        return firstEligibleImeiBySku;
    }

    @Step
    public void checkDataSource() {
        long value = distDao.checkDataSource();
        assertEquals("Value should be 1", 1, value);
    }
    
}