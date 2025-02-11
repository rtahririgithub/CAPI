package com.telus.ait.cmb.steps.account;

import static net.serenitybdd.core.Serenity.setSessionVariable;

import javax.annotation.PostConstruct;

import net.thucydides.core.annotations.Step;
import net.thucydides.junit.spring.SpringIntegration;

import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.telus.ait.fwk.util.SslUtil;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetAccountByAccountNumber;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetAccountByAccountNumberResponse;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account;
import com.telus.wsdl.cmo.informationmgmt.accountinformationservice_3.AccountInformationServicePortType;

/**
 * Created by wcheong on 10/12/2015.
 */
@ContextConfiguration({"classpath:clientapi-steps-context.xml"})
public class AccountInformationSteps {
	@Rule
	public SpringIntegration springIntegration = new SpringIntegration();

	@Autowired
	@Qualifier("accountInformationServicePort")
	private AccountInformationServicePortType accountInformationServicePortType;

    @PostConstruct
    private void initSSL() {
        SslUtil.initSsl(new Object[]{accountInformationServicePortType});
    }

	@Step
	public void getAccountByBan(String accountNumber) throws Exception {
		GetAccountByAccountNumber parameters = new GetAccountByAccountNumber();
		parameters.setBillingAccountNumber(accountNumber);
		GetAccountByAccountNumberResponse response = accountInformationServicePortType.getAccountByAccountNumber(parameters);
		Account account = response.getAccount();
		setSessionVariable("account").to(account);
	}

}