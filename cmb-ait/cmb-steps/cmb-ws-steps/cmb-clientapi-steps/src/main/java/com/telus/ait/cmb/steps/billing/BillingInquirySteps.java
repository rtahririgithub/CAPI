package com.telus.ait.cmb.steps.billing;

import java.util.List;

import javax.annotation.PostConstruct;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.fwk.util.SslUtil;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.billinginquiryservicerequestresponse_1.ApplyChargesAndAdjustmentsToAccountForSubscriberWithTax;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.billinginquiryservicerequestresponse_1.ApplyChargesAndAdjustmentsToAccountForSubscriberWithTaxResponse;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.billinginquiryservicerequestresponse_1.ChargesAndAdjustmentsId;
import com.telus.wsdl.cmo.billinginquirymgmt.billinginquiryservice_1.BillingInquiryServicePortType;

/**
 * Created by wcheong on 10/12/2015.
 */
@ContextConfiguration({"classpath:clientapi-steps-context.xml"})
public class BillingInquirySteps {
	@Rule
	public SpringIntegration springIntegration = new SpringIntegration();

	@Autowired
	@Qualifier("billingInquiryServicePort")
	private BillingInquiryServicePortType billingInquiryServicePortType;

    @PostConstruct
    private void initSSL() {
        SslUtil.initSsl(new Object[]{billingInquiryServicePortType});
    }

	@Step
	public List<ChargesAndAdjustmentsId> applyChargesAndAdjustmentsToAccountForSubscriberWithTax(ApplyChargesAndAdjustmentsToAccountForSubscriberWithTax parameters) throws Exception {
		ApplyChargesAndAdjustmentsToAccountForSubscriberWithTaxResponse response = billingInquiryServicePortType.applyChargesAndAdjustmentsToAccountForSubscriberWithTax(parameters);
		return response.getChargesAndAdjustmentsIdList();
	}

}