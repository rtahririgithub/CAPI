package com.telus.ait.cmb.steps.reference;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.fwk.util.SslUtil;
import com.telus.tmi.xmlschema.srv.smo.ordermgmt.serviceorderreferenceservicerequestresponse_v5.GetLogicalDate;
import com.telus.tmi.xmlschema.srv.smo.ordermgmt.serviceorderreferenceservicerequestresponse_v5.GetLogicalDateResponse;
import com.telus.tmi.xmlschema.srv.smo.ordermgmt.serviceorderreferenceservicerequestresponse_v5.GetPricePlanList;
import com.telus.tmi.xmlschema.srv.smo.ordermgmt.serviceorderreferenceservicerequestresponse_v5.GetPricePlanListResponse;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.PricePlanSummary;
import com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_5.ServiceOrderReferenceServicePortType;

/**
 * Created by wcheong on 10/12/2015.
 */
@ContextConfiguration({"classpath:clientapi-steps-context.xml"})
public class ServiceOrderReferenceSteps {
	@Rule
	public SpringIntegration springIntegration = new SpringIntegration();

	@Autowired
	@Qualifier("serviceOrderReferenceServicePort")
	private ServiceOrderReferenceServicePortType serviceOrderReferenceServicePortType;

    @PostConstruct
    private void initSSL() {
        SslUtil.initSsl(new Object[]{serviceOrderReferenceServicePortType});
    }

    @Step 
    public Date getLogicalDate() throws Exception {
    	GetLogicalDateResponse response = serviceOrderReferenceServicePortType.getLogicalDate(new GetLogicalDate());
    	return response.getGetLogicalDateResponseType().getLogicalDate();
    }
    
	@Step
	public List<PricePlanSummary> getPricePlanList(GetPricePlanList parameters) throws Exception {
		GetPricePlanListResponse response = serviceOrderReferenceServicePortType.getPricePlanList(parameters);
		return response.getGetPricePlanListResponseType().getPricePlanSummaryList();
	}
	
}