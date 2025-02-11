package com.telus.ait.steps.sample.db;

import com.telus.ait.integration.kb.dao.KnowbilityDao;
import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertTrue;

@ContextConfiguration({"classpath:kb-db-context.xml"})
public class SampleKbDbSteps {
    @Rule
    public SpringIntegration springIntegration = new SpringIntegration();

    @Autowired
    private KnowbilityDao kbDao;

    @Step
    public void checkDataSource() {
        boolean value = kbDao.checkDataSource();
        assertTrue("Value should be true", value);
    }

}