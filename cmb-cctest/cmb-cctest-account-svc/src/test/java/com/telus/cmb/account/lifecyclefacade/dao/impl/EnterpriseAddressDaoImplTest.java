package com.telus.cmb.account.lifecyclefacade.dao.impl;

import java.rmi.RemoteException;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.cmb.wsclient.EnterpriseAddressValidationServicePort;
import com.telus.cmb.wsclient.PolicyException_v1;
import com.telus.cmb.wsclient.ServiceException_v1;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.enterpriseaddressvalidationservicerequestresponse_v1.VerifyCanadianPostalAddress;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customercommon_v3.Address;

public class EnterpriseAddressDaoImplTest {

	@Autowired
	EnterpriseAddressDaoImpl enterpriseAddressDao;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateAddress(@Mocked(methods = {"verifyCanadianPostalAddress"}) final EnterpriseAddressValidationServicePort service) throws ApplicationException, RemoteException, PolicyException_v1, ServiceException_v1 {
			final String approvalCode = "approved";
			String termId = null;
			CreditCardTransactionInfo ccTxnInfo = new CreditCardTransactionInfo();
			AuditHeader auditHeader = null;
			
			new NonStrictExpectations() 
			{				
				@Mocked VerifyCanadianPostalAddress parameters;
				@Mocked Address addressToVerify;
				{
					service.verifyCanadianPostalAddress(parameters);
				}
			};
	}

}
