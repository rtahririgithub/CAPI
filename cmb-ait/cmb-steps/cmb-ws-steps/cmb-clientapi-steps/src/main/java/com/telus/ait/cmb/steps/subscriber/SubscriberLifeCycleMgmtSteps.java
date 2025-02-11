package com.telus.ait.cmb.steps.subscriber;

import javax.annotation.PostConstruct;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.fwk.util.SslUtil;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReleaseSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReleaseSubscriberResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriberResponse;
import com.telus.wsdl.cmo.ordermgmt.subscriberlifecyclemanagementservice_1.SubscriberLifeCycleManagementServicePortType;

/**
 * Created by wcheong on 10/12/2015.
 */
@ContextConfiguration({"classpath:clientapi-steps-context.xml"})
public class SubscriberLifeCycleMgmtSteps {
	@Rule
	public SpringIntegration springIntegration = new SpringIntegration();

	@Autowired
	@Qualifier("subscriberLifeCycleMgmtServicePort")
	private SubscriberLifeCycleManagementServicePortType subscriberLifeCycleManagementServicePortType;

    @PostConstruct
    private void initSSL() {
        SslUtil.initSsl(new Object[]{subscriberLifeCycleManagementServicePortType});
    }
    
	@Step
	public ReservePhoneNumberForSubscriberResponse reservePhoneNumberForSubscriber(ReservePhoneNumberForSubscriber parameters) throws Exception {
		return subscriberLifeCycleManagementServicePortType.reservePhoneNumberForSubscriber(parameters);
	}

	@Step
	public ReleaseSubscriberResponse releasePhoneNumberForSubscriber(ReleaseSubscriber parameters) throws Exception {
		return subscriberLifeCycleManagementServicePortType.releaseSubscriber(parameters);		
	}

}