package com.telus.ait.steps.sample.db;

import com.telus.ait.integration.ncr.dao.NcrDao;
import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;

@ContextConfiguration({"classpath:ncr-db-context.xml"})
public class SampleNcrDbSteps {
    @Rule
    public SpringIntegration springIntegration = new SpringIntegration();

    @Autowired
    private NcrDao ncrDao;

    @Step
    public void checkDataSource() {
        long value = ncrDao.checkDataSource();
        assertEquals("Value should be 2", 2, value);
    }

    @Step
    public long getOfferId(Long offerItemId) {
        return ncrDao.getOfferId(offerItemId);
    }

}