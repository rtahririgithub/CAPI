package com.telus.ait.cmb.steps.reference;

import java.util.List;

import javax.annotation.PostConstruct;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.fwk.util.SslUtil;
import com.telus.tmi.xmlschema.srv.rmo.ordermgmt.resourceorderreferenceservicerequestresponse_1_0.GetAvailableNumberGroups;
import com.telus.tmi.xmlschema.srv.rmo.ordermgmt.resourceorderreferenceservicerequestresponse_1_0.GetAvailableNumberGroupsResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.NumberGroup;
import com.telus.wsdl.rmo.ordermgmt.resourceorderreferenceservice_1.PolicyException;
import com.telus.wsdl.rmo.ordermgmt.resourceorderreferenceservice_1.ResourceOrderReferenceServicePortType;
import com.telus.wsdl.rmo.ordermgmt.resourceorderreferenceservice_1.ServiceException;

/**
 * Created by wcheong on 10/12/2015.
 */
@ContextConfiguration({"classpath:clientapi-steps-context.xml"})
public class ResourceOrderReferenceSteps {
	@Rule
	public SpringIntegration springIntegration = new SpringIntegration();

	@Autowired
	@Qualifier("resourceOrderReferenceServicePort")
	private ResourceOrderReferenceServicePortType resourceOrderReferenceServicePortType;

    @PostConstruct
    private void initSSL() {
        SslUtil.initSsl(new Object[]{resourceOrderReferenceServicePortType});
    }

    @Step
    public void test() throws PolicyException, ServiceException {
    	Ping parameters = new Ping();
    	resourceOrderReferenceServicePortType.ping(parameters);
    }
    
	@Step
	public List<NumberGroup> getAvailableNumberGroups(GetAvailableNumberGroups parameters) throws Exception {
		GetAvailableNumberGroupsResponse response = resourceOrderReferenceServicePortType.getAvailableNumberGroups(parameters);
		return response.getNumberGroup();
	}
	
}