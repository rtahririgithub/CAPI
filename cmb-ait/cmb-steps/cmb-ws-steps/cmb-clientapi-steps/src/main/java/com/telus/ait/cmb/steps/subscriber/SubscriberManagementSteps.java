package com.telus.ait.cmb.steps.subscriber;

import javax.annotation.PostConstruct;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.fwk.util.SslUtil;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.ActivateSubscriber;
import com.telus.wsdl.cmo.ordermgmt.subscribermanagementservice_4.SubscriberManagementServicePortType;

/**
 * Created by wcheong on 10/12/2015.
 */
@ContextConfiguration({"classpath:clientapi-steps-context.xml"})
public class SubscriberManagementSteps {
	@Rule
	public SpringIntegration springIntegration = new SpringIntegration();

	@Autowired
	@Qualifier("subscriberManagementServicePort")
	private SubscriberManagementServicePortType subscriberManagementServicePortType;

    @PostConstruct
    private void initSSL() {
        SslUtil.initSsl(new Object[]{subscriberManagementServicePortType});
    }

	@Step
	public void activateSubscriber(ActivateSubscriber parameters) throws Exception {
		subscriberManagementServicePortType.activateSubscriber(parameters);
	}

}