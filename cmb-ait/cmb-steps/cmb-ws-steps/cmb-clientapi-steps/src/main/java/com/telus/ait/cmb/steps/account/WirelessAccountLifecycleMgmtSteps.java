package com.telus.ait.cmb.steps.account;

import javax.annotation.PostConstruct;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.fwk.util.SslUtil;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePostpaidAccount;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePostpaidAccountResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePrepaidAccount;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePrepaidAccountResponse;
import com.telus.wsdl.cmo.ordermgmt.wirelessaccountlifecyclemgmtservice_1.WirelessAccountLifecycleMgmtServicePortType;

/**
 * Created by wcheong on 10/12/2015.
 */
@ContextConfiguration({"classpath:clientapi-steps-context.xml"})
public class WirelessAccountLifecycleMgmtSteps {
	@Rule
	public SpringIntegration springIntegration = new SpringIntegration();

	@Autowired
	@Qualifier("wirelessAccountLifecycleMgmtServicePort")
	private WirelessAccountLifecycleMgmtServicePortType wirelessAccountLifecycleMgmtServicePortType;

    @PostConstruct
    private void initSSL() {
        SslUtil.initSsl(new Object[]{wirelessAccountLifecycleMgmtServicePortType});
    }

	@Step
	public String createPostpaidAccount(CreatePostpaidAccount parameters, OriginatingUserType header) throws Exception {
		CreatePostpaidAccountResponse response = wirelessAccountLifecycleMgmtServicePortType.createPostpaidAccount(parameters, header);		
		return response.getAccountCreationResult().getBillingAccountNumber();
	}
	
	@Step
	public String createPrepaidAccount(CreatePrepaidAccount parameters, OriginatingUserType header) throws Exception {
		CreatePrepaidAccountResponse response = wirelessAccountLifecycleMgmtServicePortType.createPrepaidAccount(parameters, header);
		return response.getBillingAccountNumber();
	}
	
}