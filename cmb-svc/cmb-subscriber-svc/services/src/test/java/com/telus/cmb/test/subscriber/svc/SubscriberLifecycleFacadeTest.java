/*

 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.test.subscriber.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Account;
import com.telus.api.account.Subscriber;
import com.telus.api.portability.PRMReferenceData;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequestException;
import com.telus.api.portability.PortRequestManager;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.common.dao.provisioning.RequestParamNameConstants;
import com.telus.cmb.common.dao.provisioning.WirelessProvisioningServiceRequestFactory;
import com.telus.cmb.framework.config.ConfigurationManagerFactory;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.subscriber.domain.CommunicationSuiteRepairData;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.ProductEnterpriseDataInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.message.info.ApplicationMessageInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.DataSharingSocTransferInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberLifecycleInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ProvisioningRequestInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.framework.crypto.EncryptionUtil;

/**
 * @author Pavel Simonovsky
 *
 */
@Test
@ContextConfiguration(locations="classpath:application-context-test.xml")
//@ActiveProfiles({"remote", "pra"})
//@ActiveProfiles({"remote", "st101b"})
//@ActiveProfiles({"remote", "dv103"})
@ActiveProfiles({"remote", "local"})
//@ActiveProfiles({"standalone"})
public class SubscriberLifecycleFacadeTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		System.setProperty("UseSunHttpHandler", "true");
		System.setProperty("ssl.SocketFactory.provider", "sun.security.ssl.SSLSocketFactoryImpl");
		System.setProperty("ssl.ServerSocketFactory.provider", "sun.security.ssl.SSLSocketFactoryImpl");		
	}
	
	@Autowired
	private SubscriberLifecycleFacade facade;
	
	@Autowired
	private SubscriberLifecycleHelper helper;
	
	@Autowired
	private AccountInformationHelper accountInformationHelper;
	
	//@Autowired
	private ProductEquipmentHelper productEquipmentHelper;	
	
	
	
	
	@Test (enabled = false)
	public void testCryptoFrameworks() throws Exception {
		String value = EncryptionUtil.encrypt("test string");
		System.out.println("Encrypted value: " + value);
	}
	
	@Test // (enabled = false)
	public void testConfigurationMapping() {
		System.out.println(ConfigurationManagerFactory.getInstance().getStringValue("MinMdnServiceUrl"));
	}
	
	
	@Test // (enabled = false)
	public void retrieveReferenceData() throws Exception {
		
		PRMReferenceData [] entries = facade.retrieveReferenceData("CARRIER_INFO");
		for (PRMReferenceData entry : entries) {
			System.out.println(entry);
		}
	}

	@Test // (enabled = false)
	public void getCurrentPortRequestsByPhoneNumber() throws Exception { 
		PortRequestInfo [] portRequestInfo = facade.getCurrentPortRequestsByPhoneNumber("6471220008", 1);
		for (int i= 0;i<portRequestInfo.length;i++) {
			System.out.println("portRequestInfo: "+portRequestInfo[i].toString());
		}
	}
	
	@Test // (enabled = false)
	public void checkPortRequestStatus() throws Exception {
		String sessionId =  facade.openSession("18654", "apollo", "SMARTDESKTOP");
		System.out.println(sessionId);
		//PortRequestSummary summary = facade.checkPortRequestStatus("6471220000", 1);
		//System.out.println(summary);
	}
	
	@Test // (enabled = false)
	public void validatePortInRequest() throws Exception {
		PortRequestInfo[] portRequest = facade.getCurrentPortRequestsByBan(70615536);
		if (portRequest.length > 0) {

			PortRequestInfo request = portRequest[0];
			
			AccountInfo account = new AccountInfo(Account.ACCOUNT_TYPE_CONSUMER, Account.ACCOUNT_SUBTYPE_PCS_PERSONAL);
			request.setAccount(account);

			request.setSubscriber( new SubscriberInfo());
			request.getSubscriber().setProductType(Subscriber.PRODUCT_TYPE_PCS);
			EquipmentInfo equipmentInfo = new EquipmentInfo();
			equipmentInfo.setNetworkType("H");
			request.setEquipment(equipmentInfo);
			
			facade.validatePortInRequest(request, "SMARTDESKTOP", "test");
		}
	}
	
	@Test // (enabled = false)
	void validatePortInExceptions() throws Exception {
			
			PortRequestInfo request = new PortRequestInfo();
			
			AccountInfo account = new AccountInfo(Account.ACCOUNT_TYPE_CONSUMER, Account.ACCOUNT_SUBTYPE_PCS_PERSONAL);
			request.setAccount(account);

			EquipmentInfo equipmentInfo = new EquipmentInfo();
			equipmentInfo.setNetworkType("H");
			request.setEquipment(equipmentInfo);
			
			request.setPortRequestId("");     
			request.setOSPAccountNumber("123456");
			request.setOSPSerialNumber("33");
			request.setOSPPin("1234");
			request.setAlternateContactNumber("4164165577");
			request.setAgencyAuthorizationDate(java.sql.Date.valueOf("2010-10-25"));
			request.setAgencyAuthorizationName("ASDF");
			request.setAgencyAuthorizationIndicator("Y");
			request.setAutoActivate(false);
			//portRequest.setDesiredDateTime(java.sql.Date.valueOf("2010-10-04"));
			request.setPhoneNumber("4161626237");
			request.setRemarks("");
			request.setBanId(70669646);
			request.setCanBeActivate(true);
			request.setCanBeCancel(true);
			request.setCanBeModify(true);
			request.setCanBeSubmit(true);
			request.setCreationDate(java.sql.Date.valueOf("2010-10-25"));
			request.setIncomingBrandId(1);
			request.setOutgoingBrandId(1);
			request.setExpedite("foo");
			
			SubscriberInfo subscriber = new SubscriberInfo();
			subscriber.setProductType(Subscriber.PRODUCT_TYPE_IDEN);

			request.setSubscriber(subscriber);
			
		try{
			
			System.out.println(facade.retrieveReferenceData("xxxxx"));
			
//			facade.validatePortInRequest(request, "foo", "user");
			
		}catch(ApplicationException ae){
			if(!ae.getErrorCode().equals("")){
				if( ae.getErrorCode().equals("PRM_FALSE")){
					throw new PortRequestException(null,  new ApplicationMessageInfo(1, 10, 100, 1000, 10000));
				}	
				throw new PortRequestException(ae, new ApplicationMessageInfo(1, 10, 100, 1000, 10000));
		}
			throw new TelusAPIException(ae);
		}		
	}
	
	@Test // (enabled = false)
	public void createPortInRequest() throws Exception {
	 
		SubscriberInfo subscriber = new SubscriberInfo();
		subscriber.setProductType(Subscriber.PRODUCT_TYPE_PCS);
		
		//PortRequestInfo [] portRequests=  facade.getCurrentPortRequestsByBan(70868681 );

		PortRequestInfo  portRequest = new PortRequestInfo();//portRequests[0];
		AccountInfo account = accountInformationHelper.retrieveAccountByBan(70893399);
		portRequest = (PortRequestInfo)PortRequestManager.Helper.copyName(account,portRequest);
		portRequest = (PortRequestInfo)PortRequestManager.Helper.copyAddress(account,portRequest);
		
		//AccountInfo account = new AccountInfo(Account.ACCOUNT_TYPE_CONSUMER, Account.ACCOUNT_SUBTYPE_PCS_PERSONAL);
		portRequest.setAccount(account);

		EquipmentInfo equipmentInfo = new EquipmentInfo();
		equipmentInfo.setNetworkType("H");
		portRequest.setEquipment(equipmentInfo);
		
		portRequest.setPortRequestId("");     
		portRequest.setOSPAccountNumber("123456");
		portRequest.setOSPSerialNumber("33");
		portRequest.setOSPPin("1234");
		portRequest.setAlternateContactNumber("4164165577");
		portRequest.setAgencyAuthorizationDate(java.sql.Date.valueOf("2010-10-25"));
		portRequest.setAgencyAuthorizationName("ASDF");
		portRequest.setAgencyAuthorizationIndicator("Y");
		portRequest.setAutoActivate(false);
		//portRequest.setDesiredDateTime(java.sql.Date.valueOf("2010-10-04"));
		portRequest.setPhoneNumber("6471152819");
		portRequest.setRemarks("");
		portRequest.setBanId(70893399);
		portRequest.setCanBeActivate(true);
		portRequest.setCanBeCancel(true);
		portRequest.setCanBeModify(true);
		portRequest.setCanBeSubmit(true);
		portRequest.setCreationDate(java.sql.Date.valueOf("2010-10-25"));
		portRequest.setIncomingBrandId(1);
		portRequest.setOutgoingBrandId(255);
				
		//facade.activatePortInRequest(portRequest, "SMARTDESKTOP");

		String portId = facade.createPortInRequest(subscriber, "test", 1, 2, "H", "H", "SMARTDESKTOP", "test", portRequest);

		System.out.println("createPortInRequest result: " + portId);   
	}
	
	
	@Test // (enabled = false)
	public void submitPortInRequest() throws Exception {
		facade.submitPortInRequest("7771111", "SMARTDESKTOP");
	}
	
	@Test // (enabled = false)
	public void cancelPortInRequest() throws Exception {
		facade.cancelPortInRequest("5555","XR","SMARTDESKTOP");
	}
	
	
	
	@Test // (enabled = false)
	public void retrieveTNProvisionAttributes() throws Exception {
		String result = facade.retrieveTNProvisionAttributes("4164165577", "");
		System.out.println(result);
	}
	
	@Test // (enabled = false)
	public void submitProvisioningOrder() throws Exception {
		Date logicalDate = new Date();
		AccountInfo accountInfo = accountInformationHelper.retrieveAccountByBan(70751276, Account.ACCOUNT_LOAD_ALL);
		String serviceEdition = "BCPSQC";	
		
		ProvisioningRequestInfo request = WirelessProvisioningServiceRequestFactory.createAccountChangeRequest(accountInfo, logicalDate, serviceEdition);
		System.out.println(RequestParamNameConstants.PRICE_PLAN_CODE);		
		facade.asyncSubmitProvisioningOrder(request);	
		System.out.println("Successful");
	}
	
	// 	this method to be used to test servcie changes only ( without priceplan)
	@Test
	public void changeServiceAgreement() throws Exception {

		int ban = 	70897195;
		String subscriberId = "4034920310";

		SubscriberInfo subscriberInfo = helper.retrieveSubscriber(ban,subscriberId);

		ContractChangeInfo contractChangeInfo = facade.getServiceAgreementForUpdate(subscriberId);
		SubscriberContractInfo subscriberContractInfo = contractChangeInfo.getCurrentContractInfo();

	      // Add service
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setCode("XSBCFI50R");
        subscriberContractInfo.addService(serviceInfo,new Date());
        
       // Remove service
        
		// priceplan validation
		PricePlanValidationInfo pricePlanValidation = new PricePlanValidationInfo();
		pricePlanValidation.setProvinceServiceMatch(true);
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOutletId("6421");
		auditInfo.setSalesRepId("ftsale");
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");


		facade.changeServiceAgreement(subscriberInfo, subscriberContractInfo,"A001000001", "0000", pricePlanValidation, false, auditInfo,sessionId);
		System.out.println("Tested successfully");
	}

	
	
    @Test
    public void changePricePlan() throws Exception {
    	/**  Kafka Service Agreement changes - changePriceplan + addService + removeService*/
    	
    	System.out.println("begin changePricePlanNew ");
        String curPricePlanCode = "PVUNW40";
        String newPricePlanCode = "PVCUL35";//

        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setProductType(Subscriber.PRODUCT_TYPE_PCS);
        subscriberInfo.setBanId(70897195);
        subscriberInfo.setPhoneNumber("4034920310");
        subscriberInfo.setSubscriberId("4034920310");


        // New SubscriberContractInfo
        SubscriberContractInfo subscriberContractInfo = new SubscriberContractInfo();
        PricePlanInfo pricePlanInfo = new PricePlanInfo();
        pricePlanInfo.setCode(newPricePlanCode);
        subscriberContractInfo.setPricePlanInfo(pricePlanInfo);
        subscriberContractInfo.setPricePlanChange(true);
        
        // Add service
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setCode("XSFI105R ");
        serviceInfo.setServiceType("R");
       // subscriberContractInfo.addService(serviceInfo,new Date());
        
        // Add include service
        ServiceInfo serviceInfo1 = new ServiceInfo();
        serviceInfo1.setCode("SSMS400 ");
        serviceInfo1.setServiceType("O");
        subscriberContractInfo.addService(serviceInfo1,new Date());
        
        
        // RemoveService
       // subscriberContractInfo.removeService("S60IN10");
        // Current SubscriberContractInfo
        SubscriberContractInfo curSubscriberContractInfo = new SubscriberContractInfo();
        //curSubscriberContractInfo.setPricePlan(curPricePlanCode);
        pricePlanInfo = new PricePlanInfo();
        pricePlanInfo.setCode(curPricePlanCode);
        curSubscriberContractInfo.setPricePlanInfo(pricePlanInfo);
      
        PricePlanValidationInfo pricePlanValidation = new PricePlanValidationInfo();
        pricePlanValidation.setEquipmentServiceMatch(false);
        
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setOutletId("6421");
        auditInfo.setSalesRepId("ftsale");
        
    	System.out.println("begin openSession ");

        String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
        
        System.out.println("craeting seesion Id.."+sessionId);
        facade.changePricePlan(subscriberInfo, subscriberContractInfo, "A001000001", "0000", pricePlanValidation, false, auditInfo,
            curSubscriberContractInfo, sessionId);
        System.out.println("Tested successfully");
    }
    
    
	@Test
	public void testChangeServiceAgreement() throws ApplicationException, UnknownObjectException, TelusAPIException {

		System.out.println("testChangeServiceAgreement Start");
		String phNumber = "4034920310";
		try {
			SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phNumber);
			SubscriberContractInfo subscriberContractInfo = helper.retrieveServiceAgreementByPhoneNumber(phNumber);

			ServiceAgreementInfo[] optionalServices  = (ServiceAgreementInfo[] )subscriberContractInfo.getServices();
			for (ServiceAgreementInfo serviceAgreementInfo : optionalServices) {
				if (serviceAgreementInfo.getCode().equalsIgnoreCase("SRLH7")) {
					//subscriberContractInfo.removeService(serviceAgreementInfo.getService0().getCode());
				}
			}

			// price plan changes..
			PricePlanValidationInfo pricePlanValidation = new PricePlanValidationInfo();
			pricePlanValidation.setCurrentValidation(false);
			PricePlanInfo pricePlan = new PricePlanInfo();
			pricePlan.setActive(true);
			pricePlan.setCode("PVC50NAT");
			subscriberContractInfo.setPricePlanInfo(pricePlan);
//
//			// roaming soc changes..
//			ServiceInfo serviceInfo = new ServiceInfo();
//			serviceInfo.setCode("0SERI15  "); // SRLH7 ,0SERI12
//			serviceInfo.setServiceType("G");
//			// subscriberContractInfo.addService(serviceInfo, null);
//
			ServiceInfo serviceInfo1 = new ServiceInfo();
			serviceInfo1.setCode("S10GBSD65");
			serviceInfo1.setServiceType("R");
			subscriberContractInfo.addService(serviceInfo1, null);
			
			
			//subscriberContractInfo.removeService("SVTTUL5");
			
			String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");

			facade.changeServiceAgreement(subscriberInfo,subscriberContractInfo, subscriberInfo.getDealerCode(),subscriberInfo.getSalesRepId(), pricePlanValidation, false,null, sessionId);
		} catch (ApplicationException aex) {
			aex.printStackTrace();
		}
		System.out.println("testChangeServiceAgreement End");
	}

	// Volte test cases
	
	@Test
	public void testAddVOLTEServiceForNewEquipmentAndSave() throws ApplicationException, UnknownObjectException, TelusAPIException {
		System.out.println("testAddVOLTEServiceForNewEquipmentAndSave Start");
		String phNumber = "4161535741";
		try {
			SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phNumber);
			SubscriberContractInfo subscriberContractInfo = helper.retrieveServiceAgreementByPhoneNumber(phNumber);			
			EquipmentInfo equipmentInfo = productEquipmentHelper.getEquipmentInfobySerialNumber("900299993283012");
			String sessionId = "SQ1ZJ3DGERi8Vgw3gfXzKoAkumKZszUf+6/iq4wRwZs=";
			boolean added = facade.addVOLTEServiceForNewEquipmentAndSave(subscriberInfo, subscriberContractInfo, equipmentInfo, subscriberInfo.getDealerCode(),subscriberInfo.getSalesRepId(), sessionId, false);
			System.out.println("volte was added ? " + added);
			Assert.assertEquals(true, added );
		} catch (ApplicationException aex) {
			aex.printStackTrace();
		}
		System.out.println("testAddVOLTEServiceForNewEquipmentAndSave End");			
	}	
	
	@Test
	public void testGetVolteSocIfEligible() throws ApplicationException, UnknownObjectException, TelusAPIException {
		System.out.println("testGetVolteSocIfEligible Start");
		String phNumber = "4161310221";
		try {
			SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phNumber);
			SubscriberContractInfo subscriberContractInfo = helper.retrieveServiceAgreementByPhoneNumber(phNumber);			
			EquipmentInfo equipmentInfo = productEquipmentHelper.getEquipmentInfobySerialNumber("323536854759431");
			ServiceInfo volte = facade.getVolteSocIfEligible(subscriberInfo, subscriberContractInfo, equipmentInfo, false);
			System.out.println(volte.getService().getCode());
			Assert.assertEquals( "SVOLTE", volte.getService().getCode().trim() );
		} catch (ApplicationException aex) {
			aex.printStackTrace();
		}
		System.out.println("testGetVolteSocIfEligible End");			
	}
	
	@Test
	public void testGetVolteSocIfEligibleNegative() throws ApplicationException, UnknownObjectException, TelusAPIException {
		System.out.println("testGetVolteSocIfEligible Start");
		String phNumber = "5871712467";
		try {
			SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phNumber);
			SubscriberContractInfo subscriberContractInfo = helper.retrieveServiceAgreementByPhoneNumber(phNumber);			
			EquipmentInfo equipmentInfo = productEquipmentHelper.getEquipmentInfobySerialNumber("21101095648");
			ServiceInfo volte = facade.getVolteSocIfEligible(subscriberInfo, subscriberContractInfo, equipmentInfo, false);
			Assert.assertNull(volte);
		} catch (ApplicationException aex) {
			aex.printStackTrace();
		}
		System.out.println("testChangeServiceAgreement End");			
	}	

	@Test
	public void testGetVolteSocIfEligibleKoodo() throws ApplicationException, UnknownObjectException, TelusAPIException {
		System.out.println("testGetVolteSocIfEligible Start");
		String phNumber = "4161520313";
		try {
			SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phNumber);
			SubscriberContractInfo subscriberContractInfo = helper.retrieveServiceAgreementByPhoneNumber(phNumber);			
			EquipmentInfo equipmentInfo = productEquipmentHelper.getEquipmentInfobySerialNumber("900931423130504");
			ServiceInfo volte = facade.getVolteSocIfEligible(subscriberInfo, subscriberContractInfo, equipmentInfo, false);
			Assert.assertEquals( "3SVOLTE", volte.getService().getCode().trim() );
		} catch (ApplicationException aex) {
			aex.printStackTrace();
		}
		System.out.println("testGetVolteSocIfEligible End");			
	}	
	
	// Husky test cases  
	
	@Test
	public void removeFromCommunicationSuite() throws Exception {
		System.out.println("start removeFromCommunicationSuite %%%");
		int ban = 70792792;
		String subscriberNumber = "4161535361";
		String primaryPhoneNumber = "4161535354";
		CommunicationSuiteInfo info = new CommunicationSuiteInfo();
		info.setPrimaryPhoneNumber(primaryPhoneNumber);
		info.setBan(ban);
		facade.removeFromCommunicationSuite(ban, subscriberNumber, info, true);
		System.out.println("end removeFromCommunicationSuite");
		
	}
	
	@Test
	public void cancelSubscriber() throws Exception {
		System.out.println("start cancelSubscriber");
		String subscriberNo="5871684471";
		SubscriberInfo pSubscriberInfo=helper.retrieveSubscriberByPhoneNumber(subscriberNo);
		String pActivityReasonCode="AIE";
		String pDepositReturnMethod="R";
		String pWaiveReason="FEW";
		String pUserMemoText="memo";
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");

		facade.cancelSubscriber(pSubscriberInfo, getTodaysDate(), pActivityReasonCode, pDepositReturnMethod, pWaiveReason, pUserMemoText, false, null, null, null, sessionId);
		System.out.println("end cancelSubscriber");
		
	}
	
	
	private Date getTodaysDate() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Now use today date.
		c.add(Calendar.MONTH, 6); // Adding 5 days
		String output = sdf.format(c.getTime());
		System.out.println(output);
		return c.getTime();
	}

	@Test
	public void cancelCompanionSubscriber() throws Exception {
		System.out.println("start cancelCompanionSubscriber");
		int ban = 70792965;
		String subscriberNo="4161532926";

		SubscriberInfo pSubscriberInfo=helper.retrieveSubscriberByPhoneNumber(subscriberNo);
		CommunicationSuiteInfo info = helper.retrieveCommunicationSuite(ban, subscriberNo, CommunicationSuiteInfo.CHECK_LEVEL_ALL);
		
		String pActivityReasonCode="AIE";
		String pDepositReturnMethod="R";
		String pWaiveReason="FEW";
		String pUserMemoText="memo";
	
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");

		facade.cancelSubscriber(pSubscriberInfo, new Date(), pActivityReasonCode, pDepositReturnMethod, pWaiveReason, pUserMemoText, false, null, info, null, sessionId);
		System.out.println("end cancelCompanionSubscriber");
		
	}
	
	
	@Test
	public void cancelHuskyPrimarySubscriber() throws Exception {
		System.out.println("start cancelHuskyPrimarySubscriber ...*");
		
		int ban = 70872300;
		String subscriberNo="7781726592";

		SubscriberInfo pSubscriberInfo=helper.retrieveSubscriberByPhoneNumber(subscriberNo);
		CommunicationSuiteInfo commInfo = helper.retrieveCommunicationSuite(ban, subscriberNo, CommunicationSuiteInfo.CHECK_LEVEL_ALL);
		System.out.println(commInfo);
		
		String pActivityReasonCode="AIE";
		String pDepositReturnMethod="R";
		String pWaiveReason="FEW";
		String pUserMemoText="memo";
		
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");

		facade.cancelSubscriber(pSubscriberInfo, getTodaysDate(), pActivityReasonCode, pDepositReturnMethod, pWaiveReason, pUserMemoText, false, null, commInfo, null, sessionId);
		System.out.println("end cancelHuskyPrimarySubscriber");	
	}
	
	@Test
	public void cancelCommunicationSuiteCompanionSubs() throws Exception {
		System.out.println("start cancelCommunicationSuiteCompanionSubs");
		int ban = 70872300;
		String primarySubscriberNum="7781726592";
		CommunicationSuiteInfo commSuiteInfo = helper.retrieveCommunicationSuite(ban, primarySubscriberNum, CommunicationSuiteInfo.CHECK_LEVEL_ALL);
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		facade.cancelCommunicationSuiteCompanionSubs(ban, primarySubscriberNum, commSuiteInfo, getTodaysDate(), "AIE", 'O', "FEW", "sample txt", false, null, sessionId);
		System.out.println("end cancelCommunicationSuiteCompanionSubs");
	}
	
	@Test
	public void cancelCompanionSubscriberPortOut() throws Exception {
		System.out.println("start cancelCompanionSubscriberPortOut");
		int ban = 70876133;
		String subscriberNo="4373485414";
		
		String pActivityReasonCode="PRTO";
		String pUserMemoText="memo";
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		
		SubscriberLifecycleInfo lifeCycleInfo =  new SubscriberLifecycleInfo();
		lifeCycleInfo.setMemoText(pUserMemoText);
		lifeCycleInfo.setReasonCode(pActivityReasonCode);

		facade.cancelPortOutSubscriber(String.valueOf(ban), subscriberNo, getTodaysDate(), true, lifeCycleInfo, null, false, null, sessionId, true);

		System.out.println("end cancelCompanionSubscriberPortOut");
		
	}
	
	@Test
	public void cancelPortOutSubscriber() throws Exception {
		System.out.println("start cancelPortOutSubscriber");
		String ban = "70776488";
		String subscriberNo = "5141818687";
		String pActivityReasonCode = "PRTO";
		boolean forceCancelImmediateIndicator = true;
		SubscriberLifecycleInfo lifeCycleInfo = new SubscriberLifecycleInfo();
		lifeCycleInfo.setMemoText("sample cancel port out subscriber..");
		lifeCycleInfo.setReasonCode(pActivityReasonCode);
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");

		facade.cancelPortOutSubscriber(ban, subscriberNo, getTodaysDate(),forceCancelImmediateIndicator, lifeCycleInfo, null, false,null, sessionId, true);
		System.out.println("end cancelPortOutSubscriber");
	}
	
	//Business Connect service editions test cases

	@Test
	public void getVOIPAccountInfo() throws Exception {
		System.out.println("start getVOIPAccountInfo");
		int ban = 70792965;
		facade.getVOIPAccountInfo(ban);
		System.out.println("start getVOIPAccountInfo");
	}
	
	@Test
	public void addLicenses() throws Exception {
		System.out.println("start getVOIPAccountInfo");
		int ban = 70792965;
		String subscriptionId = "768769876";
		List<String> switchCodes = new ArrayList<String>();
		switchCodes.add("BCLRP ");
		facade.addLicenses(ban, subscriptionId, switchCodes);
		System.out.println("start getVOIPAccountInfo");

	}
	
	
	@Test
	public void insertProductInstance() throws Exception {
		System.out.println("start insertProductInstance");
		
		String subscriberNo="4161463780";

		SubscriberInfo pSubscriberInfo=helper.retrieveSubscriberByPhoneNumber(subscriberNo);
		SubscriberContractInfo subscriberContractInfo = helper.retrieveServiceAgreementByPhoneNumber(subscriberNo);
		AccountInfo accountInfo  = accountInformationHelper.retrieveLwAccountByBan(pSubscriberInfo.getBanId());
			
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");

		facade.insertProductInstance(accountInfo, pSubscriberInfo, pSubscriberInfo.getEquipment0(), subscriberContractInfo, ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_ACTIVATION, sessionId);
		System.out.println("end insertProductInstance");
		
	}
	
	@Test
	public void repairCommunicationSuiteDueToPhoneNumberChange() throws Exception {
		int ban = 70877225;
		String subscriberId = "4161942314";
		String previousCtn = "4161490399";
		
		String sessionId = "9C/mZDrSmOGydSbg/xlH2rOhxkXslzpPnY4cxZzRiuU=";//facade.openSession("18654", "apollo", "SMARTDESKTOP");
		//System.out.println(testPoint.testCommunicationSuiteMgmtRestService());
		CommunicationSuiteRepairData repairData = new CommunicationSuiteRepairData();
		repairData.setSubscriberId(subscriberId);
		repairData.setOldSubscriberId(previousCtn);
		repairData.setBan(ban);
		repairData.setActionCode(CommunicationSuiteRepairData.COMM_SUITE_RESYNC_ACTION_CODE);
		repairData.setReasonCode(CommunicationSuiteRepairData.COMM_SUITE_CTN_CHANGE_REPAIR_REASON_CODE);
		facade.repairCommunicationSuite(repairData, sessionId);
	}
	
	@Test
	public void repairCommunicationSuiteDueToSubResumed() throws Exception {
		int ban = 70877225;
		String subscriberId = "4161942314";
		String previousCtn = "4161490399";
		
		String sessionId = "9C/mZDrSmOGydSbg/xlH2rOhxkXslzpPnY4cxZzRiuU=";//facade.openSession("18654", "apollo", "SMARTDESKTOP");
		//System.out.println(testPoint.testCommunicationSuiteMgmtRestService());
		CommunicationSuiteRepairData repairData = new CommunicationSuiteRepairData();
		repairData.setSubscriberId(subscriberId);
		repairData.setBan(ban);
		repairData.setActionCode(CommunicationSuiteRepairData.COMM_SUITE_RESET_ACTION_CODE);
		repairData.setReasonCode(CommunicationSuiteRepairData.COMM_SUITE_SUB_RESUME_REPAIR_REASON_CODE);
		facade.repairCommunicationSuite(repairData, sessionId);
	}
	
	
	@Test
	public void testCreateSubscriber() {
		System.out.println("testCreateSubscriber Start");
		String phNumber = "4161301581";
		try {
			SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phNumber);
			SubscriberContractInfo subscriberContractInfo = helper.retrieveServiceAgreementByPhoneNumber(phNumber);
			String sessionId = "SQ1ZJ3DGERi8Vgw3gfXzKoAkumKZszUf+6/iq4wRwZs=";
			String portProcessType = PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT;

			facade.createSubscriber(subscriberInfo, subscriberContractInfo,true, false, null, true, false, null, portProcessType, 0,null, sessionId);

		} catch (ApplicationException aex) {
			aex.printStackTrace();
		}
		System.out.println("testAddVOLTEServiceForNewEquipmentAndSave End");
	}
	

	@Test
	public void validateDataSharingBeforeCancelSubscriber() throws Exception {
		
		String subscriberId = "6471154805";
		DataSharingSocTransferInfo info = facade.validateDataSharingBeforeCancelSubscriber(subscriberId);
		System.out.println(info);
		
	}
	
	@Test
	public void deactivateMVNESubcriber() throws Exception {
		
		String subscriberId = "9999999999";
		facade.deactivateMVNESubcriber(subscriberId);
		
	}
	
	
	@Test
	public void releasePortedInSubscriber() throws Exception {
		int ban = 70931464;
		String subscriberId = "5871803758";
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setProductType("C");
		subscriberInfo.setBanId(ban);
		subscriberInfo.setSubscriberId(subscriberId);
		subscriberInfo.setBrandId(3);
		subscriberInfo.setPhoneNumber(subscriberId);
		PortInEligibilityInfo portInEligibilityinfo = new PortInEligibilityInfo();
		portInEligibilityinfo.setIncomingBrandId(1);
		portInEligibilityinfo.setOutgoingBrandId(3);
		portInEligibilityinfo.setPlatformId(1);

		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		//System.out.println(testPoint.testCommunicationSuiteMgmtRestService());
		facade.unreservePhoneNumber(subscriberInfo, true, portInEligibilityinfo, sessionId);
	}
	
}