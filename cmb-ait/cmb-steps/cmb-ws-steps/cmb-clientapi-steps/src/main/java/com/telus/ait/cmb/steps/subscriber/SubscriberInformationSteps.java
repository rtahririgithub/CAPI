package com.telus.ait.cmb.steps.subscriber;

import javax.annotation.PostConstruct;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.fwk.util.SslUtil;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.subscriberinformationservicerequestresponse_v3.GetSubscriberByPhoneNumber;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.subscriberinformationservicerequestresponse_v3.GetSubscriberByPhoneNumberResponse;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.subscriberinformationservicerequestresponse_v3.PhoneNumberSearchOption;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.Subscriber;
import com.telus.wsdl.cmo.informationmgmt.subscriberinformationservice_3.PolicyException;
import com.telus.wsdl.cmo.informationmgmt.subscriberinformationservice_3.ServiceException;
import com.telus.wsdl.cmo.informationmgmt.subscriberinformationservice_3.SubscriberInformationServicePortType;

/**
 * Created by wcheong on 10/12/2015.
 */
@ContextConfiguration({"classpath:clientapi-steps-context.xml"})
public class SubscriberInformationSteps {
	@Rule
	public SpringIntegration springIntegration = new SpringIntegration();

	@Autowired
	@Qualifier("subscriberInformationServicePort")
	private SubscriberInformationServicePortType subscriberInformationServicePortType;

    @PostConstruct
    private void initSSL() {
        SslUtil.initSsl(new Object[]{subscriberInformationServicePortType});
    }

    @Step
	public Subscriber getSubscriberByPhoneNumber(String phoneNumber, PhoneNumberSearchOption phoneNumberSearchOption) throws PolicyException, ServiceException {
    	GetSubscriberByPhoneNumber parameters = new GetSubscriberByPhoneNumber();
    	parameters.setPhoneNumber(phoneNumber);
    	parameters.setPhoneNumberSearchOption(phoneNumberSearchOption);
    	GetSubscriberByPhoneNumberResponse response = subscriberInformationServicePortType.getSubscriberByPhoneNumber(parameters);
		return response.getSubscriber();
	}

}