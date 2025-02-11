package com.telus.cmb.subscriber.lifecyclefacade.svc.impl;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import javax.jms.Message;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.ServicesValidation;
import com.telus.api.account.Subscriber;
import com.telus.api.portability.PRMReferenceData;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequestSummary;
import com.telus.api.reference.Brand;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.common.dao.provisioning.WirelessProvisioningServiceRequestFactory;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacadeTestPoint;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.ServicesValidationInfo;
import com.telus.eas.activitylog.domain.ChangeAccountAddressActivity;
import com.telus.eas.activitylog.domain.ChangeAccountPinActivity;
import com.telus.eas.activitylog.domain.ChangeAccountTypeActivity;
import com.telus.eas.activitylog.domain.ChangeEquipmentActivity;
import com.telus.eas.activitylog.domain.ChangePaymentMethodActivity;
import com.telus.eas.activitylog.domain.ChangePhoneNumberActivity;
import com.telus.eas.activitylog.domain.ChangeSubscriberStatusActivity;
import com.telus.eas.activitylog.domain.MoveSubscriberActivity;
import com.telus.eas.config.info.AccountStatusChangeInfo;
import com.telus.eas.config.info.AddressChangeInfo;
import com.telus.eas.config.info.BillPaymentInfo;
import com.telus.eas.config.info.EquipmentChangeInfo;
import com.telus.eas.config.info.PaymentMethodChangeInfo;
import com.telus.eas.config.info.PhoneNumberChangeInfo;
import com.telus.eas.config.info.PrepaidTopupInfo;
import com.telus.eas.config.info.PricePlanChangeInfo;
import com.telus.eas.config.info.RoleChangeInfo;
import com.telus.eas.config.info.ServiceChangeInfo;
import com.telus.eas.config.info.SubscriberChangeInfo;
import com.telus.eas.config.info.SubscriberChargeInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ProductEnterpriseDataInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.ProvisioningTransactionDetailInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberLifecycleInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.MigrationTypeInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.NumberRangeInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ProvisioningRequestInfo;
import com.telus.eas.utility.info.ServiceInfo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-datasources-lifecyclehelper-testing-d3.xml"})

public class SubscriberLifecycleFacadeImplIntTest{

//	String url="t3://localhost:7168";
//	String url="t3://cmosr-custinfomgmt2-pt148.tmi.telus.com:30022";
	String url = "t3://localhost:7002";
	//String url="t3://ln99231:30022"; //Pt148
	//String url="t3://ln98556:31022"; //PT168
	
	SubscriberLifecycleFacade facadeImpl = null;
	SubscriberLifecycleHelper subscriberLifecycleHelper=null;
	SubscriberLifecycleManager subscriberLifecycleManager = null;
	String sessionId = null;
	ReferenceDataFacade referenceDataFacade = null;
	ReferenceDataHelper referenceDataHelper =  null;
	SubscriberLifecycleFacadeTestPoint subscriberLifecycleFacadeTestPoint = null;
	AccountInformationHelper impl = null;
	@Before
	public void setup() throws Exception {
System.setProperty("com.telusmobility.config.java.naming.provider.url"
		,"ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
//	System.setProperty("com.telusmobility.config.java.naming.provider.url"
	//		,"ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
//	System.setProperty("cmb.webservices.Provisioning.WirelessProvisioningService.url"
//			,"https://soa-mp-laird-pt148.tsl.telus.com/v1/smo/activation/serviceactivationmgmt/wirelessprovisioningservice-v1-0_vs0");
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext());
		getSubscriberLifecycleFacadeRemote(context);
		sessionId = facadeImpl.openSession("18654", "apollo", "SMARTDESKTOP");
		context.close();
	}

	private Hashtable<Object,Object> setEnvContext(){
		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}

	private void getSubscriberLifecycleFacadeRemote(Context context) throws Exception{
		facadeImpl = (SubscriberLifecycleFacade)context.lookup(EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_FACADE);
		subscriberLifecycleFacadeTestPoint = (SubscriberLifecycleFacadeTestPoint) context.lookup("SubscriberLifecycleFacade#com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacadeTestPoint");
		
		subscriberLifecycleHelper = (SubscriberLifecycleHelper) context.lookup(EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER);
		subscriberLifecycleManager = (SubscriberLifecycleManager) context.lookup(EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_MANAGER);
		
		//impl = (AccountInformationHelper) context.lookup(EJBUtil.TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER);
		
		/*referenceDataFacade = EJBUtil.getHelperProxy(ReferenceDataFacade.class, "ReferenceDataFacade#com.telus.cmb.reference.svc.impl.ReferenceDataFacadeHome");
		referenceDataHelper = EJBUtil.getHelperProxy(ReferenceDataHelper.class, "ReferenceDataHelper#com.telus.cmb.reference.svc.impl.ReferenceDataHelperHome");*/
		
		
	}
	
	@Test
	public void testRetrieveProvisioningTransactionDetails() throws Exception {
		String subscriberId = "4037109998";
		String transactionNo = "34667";
		ProvisioningTransactionDetailInfo[] s=facadeImpl.retrieveProvisioningTransactionDetails(subscriberId, transactionNo);
		System.out.println("ProvisioningTransactionDetailInfo array length: "+s.length);
	}
	
	
	
	/*** Recursively exhaust the JNDI tree*/
	private static final void listContext(Context ctx, String indent) {
		try {   
			NamingEnumeration list = ctx.listBindings("");   
			while (list.hasMore()) {       
				Binding item = (Binding) list.next();       
				String className = item.getClassName();       
				String name = item.getName();       
				System.out.println(indent + className + " " + name);       
				Object o = item.getObject();       
				if (o instanceof javax.naming.Context) {    
					listContext((Context) o, indent + " ");       
					}   }
			} catch (NamingException ex) {   
				System.out.println("JNDI failure: "+ ex);
				}
	}
	
	@Test
	   public void testAssignTNResources() throws Throwable {
		   String phoneNumber="9058019871";
		   String networkType="C";
		   String  localIMSI="";
		   String remoteIMSI="";
		    
		   System.out.println("Entering Test method : testAssignTNResources ");
		   facadeImpl.assignTNResources(phoneNumber, networkType, localIMSI, remoteIMSI);
		   System.out.println("Exiting Test method : testAssignTNResources ");
	   }
		
		@Test
		public void testChangeTN() throws Throwable {
			
			String oldPhoneNumber="9058019871";
			String newPhoneNumber="9059995924";
			String networkType="C";
			System.out.println("Entering Test method : testChangeTN ");
			facadeImpl.changeTN(oldPhoneNumber, newPhoneNumber, networkType);
			System.out.println("Exiting Test method : testChangeTN ");
		}
		
		
		@Test
		public void testRetrieveTNProvisionAttributes() throws Throwable {
		
			System.out.println("Entering Test method : testRetrieveTNProvisionAttributes ");
			String min = "";
			Object result=facadeImpl.retrieveTNProvisionAttributes("9059995924", "C") ;
			min=(String) result;
			System.out.println("MIN : "+ min);
			System.out.println("Exiting Test method : testRetrieveTNProvisionAttributes ");
		}
		
		@Test
		public void testReleaseTNResources() throws Throwable {
			System.out.println("Entering Test method : testReleaseTNResources ");
			facadeImpl.releaseTNResources("9059995924", "C") ;
			System.out.println("Exiting Test method : testReleaseTNResources ");
		}
	
	
//##################################### HSPA Testcases #####################################
		/*	RCM SERVICE HSPA TEST DATA FOR D3 ENVIRONMENT 
	
	    PHONE NUMBER - Use TN from '7780551010' to '7780551025'	
		LOCAL_IMSI	     REMOTE_IMSI	   USIM_ID
		302220100040033	214031000040033	9900000000000400347
		302220100040034	214031000040034	9900000000000400356
		302220100040035	214031000040035	9900000000000400365
		302220100040036	214031000040036	9900000000000400374
		302220100040037	214031000040037	9900000000000400383
		302220100040038	214031000040038	9900000000000400392
		302220100040039	214031000040039	9900000000000400401
		302220100040040	214031000040040	9900000000000400410
		302220100040041	214031000040041	9900000000000400429 
		
		
		
		*/
  
		
	
	@Test
	public void testAssignTNResources_HSPA() throws Throwable {
		   String phoneNumber="7781752310";
		   String networkType="H";
		   String  localIMSI="302220999756791";
		   String remoteIMSI="214030000756791";
		    
		   System.out.println("Entering Test method : testAssignTNResources ");
		   facadeImpl.assignTNResources(phoneNumber, networkType, localIMSI, remoteIMSI);
		   System.out.println("Exiting Test method : testAssignTNResources "); 
	}
	
	@Test
	public void testReleaseTNResources_HSPA() throws Throwable {
	    System.out.println("Entering Test method : testReleaseTNResources_HSPA ");
	    facadeImpl.releaseTNResources("7781752310", "H") ;
		System.out.println("Exiting Test method : testReleaseTNResources_HSPA ");
	}
	
	@Test
	public void testChangeTN_HSPA() throws Throwable {
	try
	{
		 System.out.println("Entering Test method : testChangeTN_HSPA ");
		 facadeImpl.changeTN("4164019129", "9059995924", "H");
		 System.out.println("Exiting Test method : testChangeTN_HSPA "); 
	}
	catch(ApplicationException ae){
	String errormessage = "PolicyException : WS0021 : Inconsistent state for the phone number in database";
	assertEquals(errormessage, ae.getMessage());
	}
}
	
	@Test
	public void testChangeNetwork_HSPA() throws Throwable {
	
		 System.out.println("Entering Test method : testChangeNetwork_HSPA ");
		 facadeImpl.changeNetwork("7781752310", "C", "H", "302220999756791", "214030000756791", "8912230000002017671");
		 System.out.println("Exiting Test method : testChangeNetwork_HSPA "); 
	}
	
	
	@Test
	public void testsetIMSIStatus_HSPA() throws Throwable {
		 System.out.println("Entering Test method : testsetIMSIStatus_HSPA ");
		 facadeImpl.setIMSIStatus("H", "0000000000000000000000", "000000000000000", "AS");
		 System.out.println("Exiting Test method : testsetIMSIStatus_HSPA ");
	}
	
	@Test
	public void testSetTNStatus_HSPA() throws Throwable {
		 System.out.println("Entering Test method : testSetTNStatus_HSPA ");
		 facadeImpl.setTNStatus("7780551010", "H", "AS");
		 System.out.println("Exiting Test method : testSetTNStatus_HSPA ");
	}
	
	@Test
	public void testChangeIMSIs_HSPA() throws Throwable {
		 System.out.println("Entering Test method : testChangeIMSIs_HSPA ");
		 facadeImpl.changeIMSIs("7780551010", "H", "302220100040039", "214031000040039");
		 System.out.println("Exiting Test method : testChangeIMSIs_HSPA "); 
	
	}
	
	@Test
	public void testGetServiceAgreement() throws Throwable {
		String subscriberId = "7781752310";
		int billingAccountNumber = 70567346;
		facadeImpl.getServiceAgreement(subscriberId, billingAccountNumber);
	}

	@Test
	public void testAsync() throws Throwable {
		facadeImpl.asyncInsertProductInstance(70567346, "7781752310", ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_UPDATE, sessionId);
	}
	
	@Test
	public void testServiceAddToBusinessAnywhereAccount() throws Throwable {
		
		try
		{
		//test 1- BA Account with Familytype='Y', test 2- familytype!='Y'
		char accountType=AccountSummary.ACCOUNT_TYPE_BUSINESS;
//		char accountSubType=AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR;
		
		//test 3- non-BA Account with Familytype='Y' , test 4-familytype!='Y' 
		char accountSubType=AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL;
		
//		ServiceInfo serviceInfo=new ServiceInfo();
		ServiceInfo serviceInfo=new PricePlanInfo();
		serviceInfo.addFamilyType(ServiceSummary.FAMILY_TYPE_CODE_DATA_DOLLAR_POOLING);
		serviceInfo.addFamilyType(ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE);
		serviceInfo.setCode("Code");
		
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setAccountType(accountType);
		accountInfo.setAccountSubType(accountSubType);
		
		facadeImpl.testServiceAddToBusinessAnywhereAccount(accountInfo, serviceInfo);
		}
		catch(Exception ex)
		{
			assertEquals("PricePlan to be added to subscriber is not compatible with the account: AccountType[B] AccountSubType[P] PricePlan[Code] familyType[Y]",ex.getMessage());

		}		
	}	
	@Test
	public void testInsertProductInstance() throws Throwable {
	
		String processType = ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_ACTIVATION;
		facadeImpl.insertProductInstance(70567346, "7781752310", processType, sessionId);
	}

	@Test
	public void testAsyncPerformPostSubscriberCommitTasks() throws Throwable {

		facadeImpl.asyncPerformPostSubscriberCommitTasks("testing", sessionId,70567346, "7781752310",null,null);
				
	}

	@Test
	public void testPerformPostSubscriberCommitTasks() throws Throwable {		
		 System.out.println("Entering Test method : performPostSubscriberCommitTasks ");		
		  facadeImpl.performPostSubscriberCommitTasks("testing", 70567346, "7781752310", "18654", "apollo", "SMARTDESKTOP");
		 System.out.println("Exiting Test method : performPostSubscriberCommitTasks "); 
				
	}
	@Test
	public void testBrowseAllMessages() throws Throwable {
	String queueBeanId = "slowQueue";
	 System.out.println("Entering Test method : BrowseAllMessages ");	
	 List <Message> messages = facadeImpl.browseAllMessages(queueBeanId);
	   for (Message message : messages) {
         System.out.println(" Messages"+message.toString());
	}
	 System.out.println("Exiting Test method : BrowseAllMessages "); 
	}
	
	@Test
	public void testBrowseMessageByCmbJMSType() throws Throwable {	
	 System.out.println("Entering Test method : BrowseMessageByCmbJMSType ");
	 String cmbJmsType = "Test";
	 String queueBeanId = "slowQueue";
	   List <Message> messages = facadeImpl.browseMessageByCmbJMSType(queueBeanId, cmbJmsType);
	   for (Message message : messages) {
         System.out.println(" Messages"+message.toString());
	}
	 System.out.println("Exiting Test method : BrowseMessageByCmbJMSType "); 
	}
	
	@Test
	public void testBrowseMessageByCmbJMSTypeandSubType() throws Throwable {
		System.out.println("Entering Test method : BrowseMessageByCmbJMSTypeandSubType ");
		String cmbJmsType = "Test";
		String subType = "test";
	    String queueBeanId = "slowQueue";
		List<Message> messages = facadeImpl.browseMessageByCmbJMSType(queueBeanId, cmbJmsType, subType);			
		for (Message message : messages) {
			System.out.println(" Messages" + message.toString());
		}
		System.out.println("Exiting Test method : BrowseMessageByCmbJMSTypeandSubType ");			
	}
	
	@Test
	public void testAsyncInsertProductInstance() throws Throwable {
	System.out.println("Entering Test method : AsyncInsertProductInstance ");
	String processType = ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_ACTIVATION;
	 facadeImpl.asyncInsertProductInstance(70567346, "7781752310", processType, sessionId);			
	System.out.println("Exiting Test method : AsyncInsertProductInstance ");			
   }
@Test
	public void testAsyncUpdateProductInstance() throws Throwable {
	System.out.println("Entering Test method : AsyncUpdateProductInstance( ");
	String processType = ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_ACTIVATION;
	 facadeImpl.asyncUpdateProductInstance(70567346, "7781752310", "7781752310", processType, sessionId);	
	System.out.println("Exiting Test method : AsyncUpdateProductInstance( ");			
  }

	@Test
	public void testAsyncUpdateProductInstance1() throws Throwable {
	System.out.println("Entering Test method : AsyncUpdateProductInstance ");
	String processType = ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_ACTIVATION;
	SubscriberInfo subscriberInfo = null;
	EquipmentInfo equipmentInfo = null;
	SubscriberContractInfo subscriberContractInfo = null;
	 facadeImpl.asyncUpdateProductInstance(70567346, subscriberInfo, equipmentInfo, subscriberContractInfo, processType, sessionId);
	System.out.println("Exiting Test method : AsyncUpdateProductInstance ");			
  }

	@Test
	public void testUpdateProductInstance() throws Throwable {
	System.out.println("Entering Test method : UpdateProductInstance ");
	String processType = ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_ACTIVATION;
	 facadeImpl.updateProductInstance(70567346, "7781752310", "7781752310", processType, sessionId);	
	System.out.println("Exiting Test method : UpdateProductInstance ");			
  }
	
	@Test
	public void testUpdateProductInstance1() throws Throwable {
	System.out.println("Entering Test method : UpdateProductInstance ");
	String processType = ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_ACTIVATION;
	SubscriberInfo subscriberInfo = null;
	EquipmentInfo equipmentInfo = null;
	SubscriberContractInfo subscriberContractInfo = null;
	 facadeImpl.updateProductInstance(70567346, subscriberInfo, equipmentInfo, subscriberContractInfo, processType, sessionId);	
	System.out.println("Exiting Test method : UpdateProductInstance ");			
  }
	
	@Test
	public void testCancelPortedInSubscriber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testCancelPortedInSubscriber Start");			
			int ban = 816166;
			String subscriberId = "2047980182";
			facadeImpl.cancelPortedInSubscriber(ban, "2047980182", "ATPR", new Date(), "portOutInd", false, subscriberId, sessionId);

		}catch(ApplicationException ex){	
			String errorMsg="Activity Reason ATPR is either invalid or does not exist.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testCancelPortedInSubscriber End");
	}
	
	@Test
	public void testCancelSubscriber() throws ApplicationException, RemoteException{
		
		try{
			
		System.out.println("testCancelSubscriber Start");
		
		String subscriberNo="4182212600";
		SubscriberInfo pSubscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberNo);
		Date pActivityDate=new Date();
		String pActivityReasonCode="AIE";
		String pDepositReturnMethod="R";
		String pWaiveReason="FEW";
		String pUserMemoText="memo";
		
		facadeImpl.cancelSubscriber(pSubscriberInfo, pActivityDate, pActivityReasonCode,	pDepositReturnMethod, pWaiveReason, pUserMemoText, true ,null,sessionId); 	
		}catch(ApplicationException ex){
			
			assertEquals("1111750",ex.getErrorCode());
			
		}
		System.out.println("testCancelSubscriber End");
	}
	
	@Test
	public void testSuspendSubscriber() throws RemoteException, TelusException,
			ApplicationException {

		try {
			System.out.println("testSuspendSubscriber Start");
			int ban=197806;
			String subscriberId = "4037109656";
			
			SubscriberInfo subscriberInfo = subscriberLifecycleHelper
					.retrieveSubscriberByPhoneNumber(subscriberId);
			subscriberInfo.setProductType("I");
			facadeImpl.suspendSubscriber(subscriberInfo, new Date(), "CR",
					"suspend subscriber", sessionId);

			System.out.println("testSuspendSubscriber End");
		} catch (ApplicationException ex) {

			System.out.println("Error occured");
			String errorCode = ex.getErrorCode();
			assertEquals("1115020", errorCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("testSuspendSubscriber End");
	}
	
	@Test
	public void testRestoreSuspendedSubscriber() throws ApplicationException, RemoteException{
		try
		{
		System.out.println("testRestoreSuspendedSubscriber Start");
		String subscriberNo="4036507804";
		SubscriberInfo pSubscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberNo);
		Date pActivityDate=new Date();
		String pActivityReasonCode="CNV";
		String pUserMemoText="memo";
		boolean portIn=true;
		
		facadeImpl.restoreSuspendedSubscriber(pSubscriberInfo, pActivityDate, pActivityReasonCode, pUserMemoText, portIn, sessionId);
		}
		catch(ApplicationException ae)
		{
			String errorMessage="Subscriber is not ported-out. Restore suspended subscriber can not be performed.";
			assertEquals(errorMessage, ae.getErrorMessage());
		}
		System.out.println("testRestoreSuspendedSubscriber End");
	}
	
	@Test
	public void testResumeCancelledSubscriber() throws ApplicationException, RemoteException{
		System.out.println("testResumeCancelledSubscriber Start");
		try{
		String subscriberNo="4033505995";
		SubscriberInfo pSubscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(3720, subscriberNo);
		String pActivityReasonCode="INPO";
		String pUserMemoText="memo";
		boolean portIn=false;
		String portProcessType=null;
		int oldBanId=0;
		String oldSubscriberId=null;
		
		facadeImpl.resumeCancelledSubscriber(pSubscriberInfo, pActivityReasonCode, pUserMemoText, 
				portIn, portProcessType, oldBanId, oldSubscriberId, sessionId);
		}catch(ApplicationException ae){
			String errorMessage="Cannot resume the SUBSCRIBER as the last activity performed on this subscriber was Move CTN.";
			assertEquals(errorMessage, ae.getErrorMessage());
		}
		System.out.println("testResumeCancelledSubscriber End");
	}
	
	@Test
	public void testChangeEquipment() throws ApplicationException, RemoteException{
		try{
			String phoneNumber = "2502660468";
			SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phoneNumber);
			EquipmentInfo oldPrimaryEquipmentInfo = new EquipmentInfo();
			oldPrimaryEquipmentInfo.setSerialNumber("06800067821");
			EquipmentInfo newPrimaryEquipmentInfo = new EquipmentInfo();
		newPrimaryEquipmentInfo.setSerialNumber("06800072487");
			EquipmentInfo[] newSecondaryEquipmentInfo = new EquipmentInfo[0];
			String dealerCode ="A001000001";
			String salesRepCode ="0000";
			String requestorId = "tester";
			String swapType = "REPLACE";
			SubscriberContractInfo subscriberContractInfo = subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber("2502660468");
	
			PricePlanValidationInfo pricePlanValidation=new PricePlanValidationInfo();
			pricePlanValidation.setCurrentValidation(false);
			PricePlanInfo pricePlan=new PricePlanInfo();
			pricePlan.setActive(true);
			subscriberContractInfo.setPricePlanInfo(pricePlan);
			//facadeImpl.changeEquipment(subscriberInfo, oldPrimaryEquipmentInfo, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo, dealerCode, salesRepCode, requestorId, swapType, subscriberContractInfo, pricePlanValidation, sessionId);
		}catch(ApplicationException aex){
			aex.printStackTrace();
			assertEquals("1116280",aex.getErrorCode());
		
		}
	}
	
	@Test
	public void testChangeIP() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testChangeIP Start");		
			int ban = 17605;		
			String subscriberId = "4034850238";
			facadeImpl.changeIP(ban, subscriberId, "10.3.45.23", "abc", "def", sessionId);

		}catch(ApplicationException ex){	
			ex.printStackTrace();
			String errorMsg="Unable to locate a Subscriber number";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testChangeIP End");
	}
	
	@Test
	public void testChangeFaxNumber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testChangeFaxNumber Start");
       		int ban = 660416;		
			String subscriberId = "4164552264";
	
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);	
			facadeImpl.changeFaxNumber(subscriberInfo, sessionId);			

		}catch(ApplicationException ex){	
			String errorMsg="phoneNumberReservation.getNumberGroup() should not be null";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testChangeFaxNumber End");
	}
	
	@Test
	public void testChangeMemberIdentity() throws ApplicationException, RemoteException{
		
		try{
			System.out.println("testChangeMemberIdentity Start");
		
			int ban = 194587;
			String subscriberId = "4037109998";
			int urbanId=131077;
			int fleetId=905;
			String memberId="19069";
	
			IDENSubscriberInfo iDENSubscriberInfo=new IDENSubscriberInfo();
			iDENSubscriberInfo.setPhoneNumber(subscriberId);
			iDENSubscriberInfo.setBanId(ban);
			
			facadeImpl.changeMemberIdentity(iDENSubscriberInfo, urbanId, fleetId, memberId, sessionId);
		
		}catch(ApplicationException ex){		
			
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("1115060", errorCode );
		}	
		System.out.println("testChangeMemberIdentity End");
	}
	
	@Test
	public void testCreateSubscriber() throws RemoteException, ApplicationException {
		try
		{
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setProductType(Subscriber.PRODUCT_TYPE_PCS);
		subscriberInfo.setBanId(17859);
		subscriberInfo.setSubscriberId("4038502143");	
		SubscriberContractInfo subscriberContractInfo = null;
		boolean activate = false;
		boolean overridePatternSearchFee = false;
		String activationFeeChargeCode = null;
		boolean dealerHasDeposit = false;
		boolean portedIn = false;
		ServicesValidation srvValidation = null;
		String portProcessType = null;
		int oldBanId = 0;
		String oldSubscriberId = null;
		facadeImpl.createSubscriber(subscriberInfo, subscriberContractInfo
				, activate, overridePatternSearchFee, activationFeeChargeCode
				, dealerHasDeposit, portedIn, srvValidation
				, portProcessType, oldBanId, oldSubscriberId, sessionId);
		}
		catch(ApplicationException ae)
		{
			assertEquals("Can not Activate/Save Subscriber that is not reserved.", ae.getMessage());
		}
	}
	
	@Test
	public void testMigrateSubscriber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testMigrateSubscriber Start");
			
			int srcBan = 70595817;
			String srcSubscriberId = "5141792544";
			
			int newBan = 660416;
			String newSubscriberId = "4164552264";
			
			MigrationTypeInfo migrationTypeInfo=new MigrationTypeInfo();
			migrationTypeInfo.setCode(MigrationType.IDEN_TO_PCSPOST);
			migrationTypeInfo.setDescription(MigrationType.IDEN_TO_PCSPOST);

			SubscriberInfo srcSubscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(srcSubscriberId);			
			SubscriberInfo newSubscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(newSubscriberId);	

			MigrationRequestInfo migrationRequestInfo=new MigrationRequestInfo(migrationTypeInfo);
			migrationRequestInfo.setDealerCode(srcSubscriberInfo.getDealerCode());
			migrationRequestInfo.setSalesRepCode(srcSubscriberInfo.getSalesRepId());
			migrationRequestInfo.setMigrationReasonCode("PRPO");
			
			SubscriberContractInfo  subscriberContractInfo= new SubscriberContractInfo();
			 EquipmentInfo newPriEquipmentInfo=new EquipmentInfo();

			EquipmentInfo[] newSecEquipmentInfoArr=new EquipmentInfo[2];

			facadeImpl.migrateSubscriber(srcSubscriberInfo, newSubscriberInfo, new Date(), subscriberContractInfo, newPriEquipmentInfo, 
					newSecEquipmentInfoArr, migrationRequestInfo,sessionId);

		}catch(ApplicationException ex){	
			//ex.printStackTrace();
			String errorMsg="Activity Reason PRPO is either invalid or does not exist.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			//ex.printStackTrace();	
		}	
		System.out.println("testMigrateSubscriber End");
	}
	
	@Test
	public void testChangePricePlan() throws ApplicationException,RemoteException{
		System.out.println("testChangePricePlan Start");
		
		String phNumber="4037109656";
		try{
		SubscriberInfo subscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phNumber);
		SubscriberContractInfo subscriberContractInfo = subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber("4037109656");

		PricePlanValidationInfo pricePlanValidation=new PricePlanValidationInfo();
		pricePlanValidation.setCurrentValidation(false);
		PricePlanInfo pricePlan=new PricePlanInfo();
		pricePlan.setActive(true);
		subscriberContractInfo.setPricePlanInfo(pricePlan);

		
		//facadeImpl.changePricePlan(subscriberInfo, subscriberContractInfo, subscriberInfo.getDealerCode(), 
			//	subscriberInfo.getSalesRepId(), pricePlanValidation, true, null, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1117020",aex.getErrorCode());
		}
		System.out.println("testChangePricePlan End");
	}
	
	/*Integration test will be done when provider changes are completed for ChangeServiceAgreement*/
	@Test
	public void testChangeServiceAgreement() throws ApplicationException,RemoteException{
		
		System.out.println("testChangeServiceAgreement Start");
		String phNumber="4037109656";
		try{
		SubscriberInfo subscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phNumber);
		SubscriberContractInfo subscriberContractInfo = subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber("4037109656");

		PricePlanValidationInfo pricePlanValidation=new PricePlanValidationInfo();
		pricePlanValidation.setCurrentValidation(false);
		PricePlanInfo pricePlan=new PricePlanInfo();
		pricePlan.setActive(true);
		subscriberContractInfo.setPricePlanInfo(pricePlan);
		facadeImpl.changeServiceAgreement(subscriberInfo, subscriberContractInfo,  
				subscriberInfo.getDealerCode(), subscriberInfo.getSalesRepId(), pricePlanValidation, true, null, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1117020",aex.getErrorCode());
		}
		System.out.println("testChangeServiceAgreement End");
	}	
	
	@Test
	public void testReleaseSubscriber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testReleaseSubscriber Start");	
			int ban = 70666180;
			String subscriberId = "5871227712";	
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);			
			facadeImpl.releaseSubscriber(subscriberInfo,null, null, sessionId);
			
		}catch(ApplicationException ex){	
			String errorMsg="Can not Activate/Save Subscriber that is not reserved.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(Exception ex){		
			ex.printStackTrace();
		
		}	
		System.out.println("testReleaseSubscriber End");
	}
	@Test
	public void testReleasePortedInSubscriber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testReleasePortedInSubscriber Start");
			int ban = 70666180;
			String subscriberId = "5871227712";
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);	
			facadeImpl.releasePortedInSubscriber(subscriberInfo,null, null, sessionId);


		}catch(ApplicationException ex){	
			String errorMsg="Can not Activate/Save Subscriber that is not reserved.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testReleasePortedInSubscriber End");
	}
	
	/*Integration test will be done when provider changes are completed for ActivateReservedSubscriber*/
	@Test
	public void testActivateReservedSubscriber() throws ApplicationException, RemoteException{
		System.out.println("testActivateReservedSubscriber Start");
		String phNumber="4037109656";
		try{
			SubscriberInfo subscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phNumber);
			SubscriberContractInfo subscriberContractInfo = subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber("4037109656");

			PricePlanValidationInfo pricePlanValidation=new PricePlanValidationInfo();
			pricePlanValidation.setCurrentValidation(false);
			PricePlanInfo pricePlan=new PricePlanInfo();
			pricePlan.setActive(true);
			subscriberContractInfo.setPricePlanInfo(pricePlan);

		Date startServiceDate = new Date();
		String activityReasonCode="test";
		ServicesValidation srvValidation = new ServicesValidationInfo();
		String portProcessType ="test";
		int oldBanId=254977;
		String oldSubscriberId="4033317837";
		facadeImpl.activateReservedSubscriber(subscriberInfo, subscriberContractInfo, startServiceDate ,
				activityReasonCode, srvValidation , portProcessType , oldBanId, oldSubscriberId, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1115110",aex.getErrorCode());
			assertEquals("Can not Activate/Save Subscriber that is not reserved.",aex.getErrorMessage());
		}
		System.out.println("testActivateReservedSubscriber End");
	}

	@Test
	public void testcheckPortInEligibility() throws ApplicationException,RemoteException {
		try {
			System.out.println("checkPortInEligibility Start");
			String portVisibility = "INT_2H";
			int incomingBrand = 1;
			PortInEligibility portInEligibility = facadeImpl.checkPortInEligibility("6471251863", portVisibility,	incomingBrand);
			System.out.println("portInEligibility"+ portInEligibility.toString());
			System.out.println("checkPortInEligibility End");
		} catch (ApplicationException ae) {
			ae.printStackTrace();
		}
	}
	
	@Test
	public void testgetCurrentPortRequestsByBan()throws ApplicationException, RemoteException {	
	
		System.out.println("CurrentPortRequestsByBan Start");
		PortRequestInfo [] portRequestInfo = facadeImpl.getCurrentPortRequestsByBan(70615536);
		for (int i= 0;i<portRequestInfo.length;i++)
		System.out.println("portRequestInfo: "+portRequestInfo[i].toString());
		System.out.println("CurrentPortRequestsByBan End");

	}
	@Test
	public void testgetCurrentPortRequestsByPhoneNumber()throws ApplicationException, RemoteException {
	
	System.out.println("CurrentPortRequestsByPhoneNumber Start");
	PortRequestInfo [] portRequestInfo = facadeImpl.getCurrentPortRequestsByPhoneNumber("6471220008",1);
	for (int i= 0;i<portRequestInfo.length;i++)
		System.out.println("portRequestInfo: "+portRequestInfo[i].toString());
	System.out.println("CurrentPortRequestsByPhoneNumber End");
	}
	
	@Test
	public void testCheckPortRequestStatus()throws ApplicationException, RemoteException {
	
	System.out.println("CheckPortRequestStatus Start");
	PortRequestSummary portRequestSummary = facadeImpl.checkPortRequestStatus("6471220008", 1);
	System.out.println("portRequestSummary: "+portRequestSummary.toString());
	System.out.println("CheckPortRequestStatus End");
	}
	
	@Test
	public void testValidatePortInRequest()throws ApplicationException, RemoteException {
	 
	System.out.println("validatePortInRequest Start");
	PortRequestInfo[] portRequest = facadeImpl.getCurrentPortRequestsByBan(70615536);
	facadeImpl.validatePortInRequest(portRequest[0], "SMARTDESKTOP", "test");
    System.out.println("validatePortInRequest END");   
	}
	
	@Test
	public void testActivatePortInRequest()throws ApplicationException, RemoteException {
	 
	System.out.println("activatePortInRequest Start");
	PortRequestInfo[] portRequest = facadeImpl.getCurrentPortRequestsByBan(70615536);
    facadeImpl.activatePortInRequest(portRequest[0], "SMARTDESKTOP");
    System.out.println("activatePortInRequest End");   
	}
	
	@Test
	public void testcancelPortInRequest()throws ApplicationException, RemoteException {
	 
	System.out.println("cancelPortInRequest Start");
    facadeImpl.cancelPortInRequest("12345", "CPI", "SMARTDESKTOP");
    System.out.println("cancelPortInRequest End");   
	}
	
	@Test
	public void testCreatePortInRequest()throws ApplicationException, RemoteException {
	 
	System.out.println("createPortInRequest Start");
	SubscriberInfo subscriber = subscriberLifecycleHelper.retrieveSubscriber(70669646, "4161626237");
	PortRequestInfo [] portRequests=  facadeImpl.getCurrentPortRequestsByBan(70669646);
	PortRequestInfo  portRequest= portRequests[0];
	  portRequest.setPortRequestId("");     
    portRequest.setOSPAccountNumber("123456");
    portRequest.setOSPSerialNumber("1234567890");
    portRequest.setOSPPin("1234");
    portRequest.setAlternateContactNumber("4164165577");
    portRequest.setAgencyAuthorizationDate(java.sql.Date.valueOf("2010-10-25"));
    portRequest.setAgencyAuthorizationName("ASDF");
    portRequest.setAgencyAuthorizationIndicator("Y");
    portRequest.setAutoActivate(false);
    //portRequest.setDesiredDateTime(java.sql.Date.valueOf("2010-10-04"));
    portRequest.setPhoneNumber("4161626237");
    portRequest.setRemarks("");
    portRequest.setBanId(70669646);
    portRequest.setCanBeActivate(true);
    portRequest.setCanBeCancel(true);
    portRequest.setCanBeModify(true);
    portRequest.setCanBeSubmit(true);
    portRequest.setCreationDate(java.sql.Date.valueOf("2010-10-25"));
    portRequest.setIncomingBrandId(1);
    portRequest.setOutgoingBrandId(1);

    String portId = facadeImpl.createPortInRequest(subscriber, "test", 1, 2, "H", "H", "SMARTDESKTOP", "test", portRequest);
    System.out.println("createPortInRequest End" +portId);   
	}
	
	@Test
	public void testSubmitPortInRequest()throws ApplicationException, RemoteException {
	 
	System.out.println("submitPortInRequest Start");
     facadeImpl.submitPortInRequest("12345", "SMARTDESKTOP");
    System.out.println("submitPortInRequest End");   
	}
	
	@Test
	public void testRetrieveReferenceData()throws ApplicationException, RemoteException {
	
	System.out.println("RetrieveReferenceData Start");
	PRMReferenceData [] PRMReferenceDataa =  facadeImpl.retrieveReferenceData("CARRIER_INFO");
	for (int i= 0;i<PRMReferenceDataa.length;i++)
		System.out.println("portRequestInfo: "+PRMReferenceDataa[i].toString());
     System.out.println("RetrieveReferenceData End");
}

	@Test
	public void testGetCallingCircleInformation() throws ApplicationException,RemoteException {
	try
	{
	System.out.println("getCallingCircleInformation Start");
	facadeImpl.getCallingCircleInformation(197806, "4037109656", "C2UHT2   " , "FBC", "C", sessionId);
     System.out.println("getCallingCircleInformation Start");
     }catch (ApplicationException ae) {
    	 String message = "Service Code [C2UHT2   ] is not available on Subscriber Service Agreement";
	    assertEquals(message, ae.getMessage());
	}
} 
	
	@Test
	public void testAsyncReportChangeSubscriberStatus() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncReportChangeSubscriberStatus start...");
		
		ChangeSubscriberStatusActivity activity = new ChangeSubscriberStatusActivity(getServiceRequestHeader());
		activity.setActors("A001000001", "0000", "tester");
		activity.setBanId(292007);
		activity.setSubscriberId("7807198318");
		activity.setNewSubscriberStatus('C');
		activity.setOldSubscriberStatus('A');
		activity.setPhoneNumber("7807198318");
		activity.setReason("CA");
		activity.setSubscriberActivationDate(new Date());
		
		facadeImpl.asyncReportChangeSubscriberStatus(activity);
		
		
		System.out.println("testAsyncReportChangeSubscriberStatus end...");
	}
	@Test
	public void testAsyncReportChangeEquipment() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncReportChangeEquipment start...");
		
		EquipmentInfo oldEquipment = new EquipmentInfo();
		oldEquipment.setSerialNumber("06800067821");
		EquipmentInfo newEquipment = new EquipmentInfo();
		newEquipment.setSerialNumber("06800072487");
		
		ChangeEquipmentActivity activity = new ChangeEquipmentActivity(getServiceRequestHeader());
		activity.setActors("A001000001", "0000", "tester");
		activity.setBanId(292007);
		activity.setSubscriberId("7807198318");
		activity.setOldEquipment(oldEquipment);
		activity.setNewEquipment(newEquipment);
		activity.setOldAssociatedMuleEquipment(oldEquipment);
		activity.setNewAssociatedMuleEquipment(newEquipment);
		activity.setRepairId("repairId");
		activity.setSwapType("REPLACE");
		
		facadeImpl.asyncReportChangeEquipment(activity);
		
		System.out.println("testAsyncReportChangeEquipment end...");
	}
	
	
	
	@Test
	public void testAsyncReportChangePhoneNumber() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncReportChangePhoneNumber start...");
		ChangePhoneNumberActivity activity = new ChangePhoneNumberActivity(getServiceRequestHeader());
		activity.setActors("A001000001", "0000", "tester");
		activity.setBanId(292007);
		activity.setSubscriberId("7807198318");
		activity.setNewSubscriberId("7807198319");
		activity.setOldPhoneNumber("7807198318");
		activity.setNewPhoneNumber("7807198319");
		
		facadeImpl.asyncReportChangePhoneNumber(activity);
		
		System.out.println("testAsyncReportChangePhoneNumber end...");
	}
	@Test
	public void testAsyncReportMoveSubscriber() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncReportMoveSubscriber start...");
		
		MoveSubscriberActivity activity =new MoveSubscriberActivity(getServiceRequestHeader());
		activity.setActors("A001000001", "0000", "tester");
		activity.setBanId(750714);
		activity.setSubscriberId("5196351113");
		activity.setNewBanId(805938);
		activity.setPhoneNumber("5196351113");
		activity.setReason("MVE");
		activity.setSubscriberActivationDate(new Date());
		
		facadeImpl.asyncReportMoveSubscriber(activity);
		
		System.out.println("testAsyncReportMoveSubscriber end...");
	}
	
	@Test
	public void testAsyncReportChangeAccountType() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncReportChangeAccountType start...");
		
		ChangeAccountTypeActivity activity =new ChangeAccountTypeActivity(getServiceRequestHeader());
		activity.setActors("A001000001", "0000", "tester");
		activity.setBanId(750714);
		activity.setOldAccountType(AccountSummary.ACCOUNT_TYPE_CONSUMER);
		activity.setNewAccountType(AccountSummary.ACCOUNT_TYPE_CONSUMER);
		activity.setOldAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
		activity.setNewAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR);
		
		facadeImpl.asyncReportChangeAccountType(activity);
		
		System.out.println("testAsyncReportChangeAccountType end...");
	}
	
	@Test
	public void testAsyncReportChangeAccountAddress() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncReportChangeAccountAddress start...");
		AddressInfo address= new AddressInfo();
		address.setStreetName("300 Consilium Street");
		address.setPoBox("41");
		address.setCountry("CA");
		ChangeAccountAddressActivity activity =new ChangeAccountAddressActivity(getServiceRequestHeader());
		activity.setActors("A001000001", "0000", "tester");
		activity.setBanId(750714);
		activity.setAddress(address);
		
		facadeImpl.asyncReportChangeAccountAddress(activity);
		
		System.out.println("testAsyncReportChangeAccountAddress end...");
	}
	
	@Test
	public void testAsyncReportChangeAccountPin() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncReportChangeAccountPin start...");
		
		ChangeAccountPinActivity activity =new ChangeAccountPinActivity(getServiceRequestHeader());
		activity.setActors("A001000001", "0000", "tester");
		activity.setBanId(750714);
		
		facadeImpl.asyncReportChangeAccountPin(activity);
		
		System.out.println("testAsyncReportChangeAccountPin end...");
	}
	@Test
	public void testAsyncReportChangePaymentMethod() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncReportChangePaymentMethod start...");
		
		ChangePaymentMethodActivity activity =new ChangePaymentMethodActivity(getServiceRequestHeader());
		activity.setActors("A001000001", "0000", "tester");
		activity.setBanId(750714);
		activity.setPaymentMethodCode("C");
		
		facadeImpl.asyncReportChangePaymentMethod(activity);
		
		System.out.println("testAsyncReportChangePaymentMethod end...");
	}
	
	
	
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	
	
	@Test
	public void testAsyncLogChangeRole() throws ApplicationException,RemoteException {
	// Table Name: TMI_ROLE_CHANGE
		System.out.println("testAsyncLogChangeRole start...");
		RoleChangeInfo roleChangeInfo= new RoleChangeInfo();
		roleChangeInfo.setTransactionId(69020);
		roleChangeInfo.setTransactionDate(getDateInput(2010, 9, 21));
		roleChangeInfo.setOldRole("AA");
		roleChangeInfo.setNewRole("SR");
		facadeImpl.asyncLogChangeRole(roleChangeInfo);
		
		System.out.println("testAsyncLogChangeRole end...");
	}
	
	@Test
	public void testAsyncLogMakePayment() throws ApplicationException,RemoteException {
	// Table Name: TMI_PAYMENT_TRANSACTION
		System.out.println("testAsyncLogMakePayment start...");
		BillPaymentInfo billPaymentInfo= new BillPaymentInfo();
		billPaymentInfo.setTransactionId(69020);
		billPaymentInfo.setTransactionDate(getDateInput(2010, 9, 21));
		billPaymentInfo.setPaymentAmount(100);
		billPaymentInfo.setPaymentMethod('C');
		facadeImpl.asyncLogMakePayment(billPaymentInfo);
		
		System.out.println("testAsyncLogMakePayment end...");
	}
	
	@Test
	public void testAsyncLogChangePricePlan() throws ApplicationException,RemoteException {
	// Table Name: TMI_PAYMENT_TRANSACTION
		System.out.println("testAsyncLogChangePricePlan start...");
		PricePlanChangeInfo pricePlanChangeInfo= new PricePlanChangeInfo();
		pricePlanChangeInfo.setTransactionId(69020);
		pricePlanChangeInfo.setTransactionDate(getDateInput(2010, 9, 21));
		pricePlanChangeInfo.setOldPricePlanCode("PTLK75CF");
		pricePlanChangeInfo.setNewPricePlanCode("PCS25MPTC");
		facadeImpl.asyncLogChangePricePlan(pricePlanChangeInfo);
		
		System.out.println("testAsyncLogChangePricePlan end...");
	}
	
	@Test
	public void testAsyncLogChangeAddress() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncLogChangeAddress start...");
		
		AddressInfo oldAddr = new AddressInfo();
		AddressInfo newAddr = new AddressInfo();

		oldAddr.setStreetNumber("90");
		oldAddr.setStreetName("GERRARD ST W");
		oldAddr.setCity("TORONTO");
		oldAddr.setProvince("ON");
		oldAddr.setPostalCode("M1M1M1");
		oldAddr.setCountry("CAN");
		
		newAddr.setStreetNumber("99");
		newAddr.setStreetName("GERRARD ST W");
		newAddr.setCity("TORONTO");
		newAddr.setProvince("ON");
		newAddr.setPostalCode("M1M1M1");
		newAddr.setCountry("CAN");
		
		AddressChangeInfo addressChangeInfo= new AddressChangeInfo();
		addressChangeInfo.setTransactionId(69020);
		addressChangeInfo.setTransactionDate(getDateInput(2010, 9, 21));
		addressChangeInfo.setOldAddress(oldAddr);
		addressChangeInfo.setNewAddress(newAddr);
		facadeImpl.asyncLogChangeAddress(addressChangeInfo);
		
		System.out.println("testAsyncLogChangeAddress end...");
	}
	
	@Test
	public void testAsyncLogSubscriberNewCharge() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncLogSubscriberNewCharge start...");
		SubscriberChargeInfo subscriberChargeInfo= new SubscriberChargeInfo();
		subscriberChargeInfo.setTransactionId(69020);
		subscriberChargeInfo.setTransactionDate(getDateInput(2010, 9, 21));
		subscriberChargeInfo.setChargeCode("ACTMMK");
		subscriberChargeInfo.setWaiverCode(null);
		facadeImpl.asyncLogSubscriberNewCharge(subscriberChargeInfo);
		
		System.out.println("testAsyncLogSubscriberNewCharge end...");
	}
	
	@Test
	public void testAsyncLogChangePaymentMethod() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncLogChangePaymentMethod start...");
		PaymentMethodChangeInfo paymentMethodChangeInfo= new PaymentMethodChangeInfo();
		paymentMethodChangeInfo.setTransactionId(69020);
		paymentMethodChangeInfo.setTransactionDate(getDateInput(2010, 9, 21));
		paymentMethodChangeInfo.setOldPaymentMethod('C');
		paymentMethodChangeInfo.setNewPaymentMethod('C');
		facadeImpl.asyncLogChangePaymentMethod(paymentMethodChangeInfo);
		
		System.out.println("testAsyncLogChangePaymentMethod end...");
	}
	
	@Test
	public void testAsyncLogAccountStatusChange() throws ApplicationException,RemoteException {
	
		System.out.println("testAsyncLogAccountStatusChange start...");

		AccountStatusChangeInfo accountStatusChangeInfo= new AccountStatusChangeInfo();
		accountStatusChangeInfo.setTransactionId(69020);
		accountStatusChangeInfo.setTransactionDate(getDateInput(2010, 9, 21));
		accountStatusChangeInfo.setOldStatus('C');
		accountStatusChangeInfo.setNewStatus('A');
		accountStatusChangeInfo.setOldHotlinedInd('N');
		accountStatusChangeInfo.setNewHotlinedInd('N');
		accountStatusChangeInfo.setStatusFlag('A');
		facadeImpl.asyncLogAccountStatusChange(accountStatusChangeInfo);
		
		System.out.println("testAsyncLogAccountStatusChange end...");
	}
	
	@Test
	public void testAsyncLogPrepaidAccountTopUp() throws ApplicationException, RemoteException{
	
		System.out.println("testAsyncLogPrepaidAccountTopUp start...");

		PrepaidTopupInfo prepaidTopupInfo= new PrepaidTopupInfo();
		prepaidTopupInfo.setTransactionId(69020);
		prepaidTopupInfo.setTransactionDate(getDateInput(2010, 9, 21));
		prepaidTopupInfo.setAmount(109);
		prepaidTopupInfo.setCardType('C');
		prepaidTopupInfo.setTopUpType('O');
	
		facadeImpl.asyncLogPrepaidAccountTopUp(prepaidTopupInfo);
		
		System.out.println("testAsyncLogPrepaidAccountTopUp end...");
	}
	
	@Test
	public void testAsyncLogChangePhoneNumber() throws ApplicationException, RemoteException{
	
		System.out.println("testAsyncLogPrepaidAccountTopUp start...");

		PhoneNumberChangeInfo phoneNumberChangeInfo= new PhoneNumberChangeInfo();
		phoneNumberChangeInfo.setTransactionId(69020);
		phoneNumberChangeInfo.setTransactionDate(getDateInput(2010, 9, 21));
		phoneNumberChangeInfo.setOldPhoneNumber("4166874578");
		phoneNumberChangeInfo.setNewPhoneNumber("4169876976");
		
	
		facadeImpl.asyncLogChangePhoneNumber(phoneNumberChangeInfo);
		
		System.out.println("testAsyncLogPrepaidAccountTopUp end...");
	}
	
	@Test
	public void testAsyncLogChangeService() throws ApplicationException, RemoteException{
	
		System.out.println("testAsyncLogChangeService start...");
		
		ServiceAgreementInfo[] services = new ServiceAgreementInfo[0];
		
		ServiceChangeInfo serviceChangeInfo= new ServiceChangeInfo();
		serviceChangeInfo.setTransactionId(69020);
		serviceChangeInfo.setTransactionDate(getDateInput(2010, 9, 21));
		serviceChangeInfo.setServices(services);
		
		facadeImpl.asyncLogChangeService(serviceChangeInfo);
		
		System.out.println("testAsyncLogChangeService end...");
	}
	
	@Test
	public void testAsyncLogChangeSubscriber() throws ApplicationException, RemoteException{
	
		System.out.println("testAsyncLogChangeSubscriber start...");
		
		SubscriberChangeInfo subscriberChangeInfo= new SubscriberChangeInfo();
		subscriberChangeInfo.setTransactionId(69020);
		subscriberChangeInfo.setTransactionDate(getDateInput(2010, 9, 21));
		subscriberChangeInfo.setOldSubscriber(new SubscriberInfo());
		subscriberChangeInfo.setNewSubscriber(new SubscriberInfo());	
				
		facadeImpl.asyncLogChangeSubscriber(subscriberChangeInfo);
		
		System.out.println("testAsyncLogChangeSubscriber end...");
	}
	
	@Test
	public void testAsyncLogSubscriberChangeEquipment() throws ApplicationException, RemoteException{
	
		System.out.println("testAsyncLogSubscriberChangeEquipment start...");
		
		EquipmentChangeInfo equipmentChangeInfo= new EquipmentChangeInfo();
		equipmentChangeInfo.setTransactionId(69020);
		equipmentChangeInfo.setTransactionDate(getDateInput(2010, 9, 21));
		equipmentChangeInfo.setDealerCode("A000001");
		equipmentChangeInfo.setApplicationName("ClientAPI");
		equipmentChangeInfo.setSubscriberInfo(new SubscriberInfo());
		equipmentChangeInfo.setOldEquipmentInfo(new EquipmentInfo());
		equipmentChangeInfo.setNewEquipmentInfo(new EquipmentInfo());
		equipmentChangeInfo.setSalesRepCode("0001");
		
		
		facadeImpl.asyncLogSubscriberChangeEquipment(equipmentChangeInfo);
		
		System.out.println("testAsyncLogSubscriberChangeEquipment end...");
	}
	
	private ServiceRequestHeader getServiceRequestHeader(){

		ServiceRequestHeaderInfo header = new ServiceRequestHeaderInfo();
		header.setApplicationId(27);
		header.setLanguageCode("EN");
		return header;
	}
	
	
	@Test
	public void testReportChangeSubscriberStatus() throws ApplicationException,RemoteException {
	
		System.out.println("testReportChangeSubscriberStatus start...");
		
		String dealerCode ="A001000001";
		String salesRepCode ="0000";
		String userId="tester";
		
		ServiceRequestHeaderInfo header = new ServiceRequestHeaderInfo();
		header.setApplicationId(27);
		header.setLanguageCode("EN");
		
//		SubscriberInfo subInfo= subscriberLifecycleHelper.retrieveSubscriber(197806, "4037109656");
		SubscriberInfo subInfo= new SubscriberInfo();
		subInfo.setSubscriberId("4037109656");
		subInfo.setPhoneNumber("4037109656");
		subInfo.setStartServiceDate(new Date());
		
		
		facadeImpl.reportChangeSubscriberStatus(197806, subInfo, dealerCode, salesRepCode, userId,
				'A', 'C', "CA", new Date(), header);
		
		
		System.out.println("testReportChangeSubscriberStatus end...");
	}
	
	@Test
	public void testDeactivateMVNESubscriber() throws ApplicationException,RemoteException {
	
		System.out.println("testDeactivateMVNESubscriber start...");
		facadeImpl.deactivateMVNESubcriber("4037109656");
		System.out.println("testDeactivateMVNESubscriber end...");
	}
	
	
	@Test
	public void testCloseHPAAccount() throws ApplicationException,RemoteException {
		System.out.println("testcloseHPAAccount start...");
		int billingAccountNumber = 1234;
		String phoneNumber = "4037109656";
		boolean isAsync =  true;
		facadeImpl.closeHPAAccount(billingAccountNumber, phoneNumber, isAsync);
		System.out.println("testcloseHPAAccount end...");
	}
	@Test
	public void testRestoreHPAAccount() throws ApplicationException,RemoteException {
	
		System.out.println("testRestoreHPAAccount start...");
		int billingAccountNumber = 1234;
		String phoneNumber = "4037109656";
		boolean isAsync =  true;
		facadeImpl.restoreHPAAccount(billingAccountNumber, phoneNumber, isAsync);
		System.out.println("testRestoreHPAAccount end...");
	}
	
	@Test
	public void testReservePhoneNumber() throws ApplicationException,RemoteException, TelusException {	
		System.out.println("testReservePhoneNumber start...");
		
		//1.testReservePhoneNumber likematch scenario by passing wildcards as PhoneNumberPattern.
		String phoneNumber = "4160604665";
		String productType = "C";
		String serialNumber = "8912239900000603821";
		NumberGroupInfo numberGroups = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(phoneNumber, productType);
	System.out.println("numbergroupInfo"+numberGroups);
	
	//NumberRangeInfo[] numberRanges1=new NumberRangeInfo[2];
	//NumberRangeInfo numberRangeInfo = new NumberRangeInfo();
	//numberRanges1[0]= numberRangeInfo.newInstanmce("416060");
	//numberGroups.setNumberRanges(numberRanges1);
	
	String[] npanxx= new String[1] ;
	npanxx[0]="416060";
	numberGroups.setNpaNXX(npanxx);
	
		SubscriberInfo subscriberInfo =  new SubscriberInfo();
		subscriberInfo.setBanId(70687604);
		subscriberInfo.setDealerCode("A001000001");
		subscriberInfo.setSalesRepId("0000");
		subscriberInfo.setProductType(productType);
		subscriberInfo.setSerialNumber(serialNumber);
	      PhoneNumberReservationInfo pnr = new PhoneNumberReservationInfo();
	      pnr.setNumberGroup(numberGroups);
	      pnr.setPhoneNumberPattern("**********");
	      //SubscriberInfo reservedSubscriberInfo = 
	   facadeImpl.reservePhoneNumber(pnr, subscriberInfo, sessionId);
	     // System.out.println("reserved phonenumber is "+reservedSubscriberInfo.getPhoneNumber());
	
		System.out.println("testReservePhoneNumber end...");
			
		
	}

	@Test
	public void testReservePhoneNumberforExactMatchCase() throws ApplicationException,RemoteException, TelusException {	
		System.out.println("testReservePhoneNumberforExactMatchCase start...");
		
		//2.testReservePhoneNumber exact match scenario by passing 10 digit number as PhoneNumberPattern.
		
		String phoneNumber = "6472148184";
		String productType = "C";
		String serialNumber = "8912239900000606675";
		NumberGroupInfo numberGroups = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(phoneNumber, productType);
		NumberRangeInfo[] numberRanges1=new NumberRangeInfo[2];
		NumberRangeInfo numberRangeInfo = new NumberRangeInfo();
		numberRanges1[0]= numberRangeInfo.newInstanmce("647214");
		numberGroups.setNumberRanges(numberRanges1);
		System.out.println("numberGroups"+numberGroups.toString());
	
		SubscriberInfo subscriberInfo =  new SubscriberInfo();
		subscriberInfo.setBanId(70687550);
		subscriberInfo.setDealerCode("A001000001");
		subscriberInfo.setSalesRepId("0000");
		subscriberInfo.setProductType(productType);
		subscriberInfo.setSerialNumber(serialNumber);
	      PhoneNumberReservationInfo pnr = new PhoneNumberReservationInfo();
	      pnr.setNumberGroup(numberGroups);
	      pnr.setLikeMatch(false);
	      pnr.setPhoneNumberPattern("647214000%");
	      
	    SubscriberInfo subscriberInfo1 =facadeImpl.reservePhoneNumber(pnr, subscriberInfo, sessionId);
	    System.out.println("reserved phonenumber is "+subscriberInfo1.getPhoneNumber());
	
		System.out.println("testReservePhoneNumberforExactMatchCase end...");
			  
	}
	
	
	@Test
	public void testReservePortInPhoneNumber() throws ApplicationException,RemoteException, TelusException {	
		
		System.out.println("testReservePortInPhoneNumber start...");
		
		
		String phoneNumber = "6471273701"; // koodo prepaid num,mvne test case
		String productType = "C";
		String serialNumber = "8912239900000604746";
		NumberGroup numberGroups = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("6471260578", productType);
		PortInEligibility portInEligibility = facadeImpl.checkPortInEligibility(phoneNumber, PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2H, Brand.BRAND_ID_TELUS);
		
		System.out.println("portInEligibility PlatformId"+portInEligibility.getPlatformId());
		
		PortInEligibilityInfo portInEligibilityInfo = new PortInEligibilityInfo();
		portInEligibilityInfo.setPlatformId(2);
		portInEligibilityInfo.setIncomingBrandId(Brand.BRAND_ID_TELUS);
		portInEligibilityInfo.setOutgoingBrandId(Brand.BRAND_ID_NOT_APPLICABLE);
	
		SubscriberInfo subscriberInfo =  new SubscriberInfo();
		subscriberInfo.setBanId(70582009);
		subscriberInfo.setDealerCode("A001000001");
		subscriberInfo.setSalesRepId("0000");
		subscriberInfo.setProductType(productType);
		subscriberInfo.setSerialNumber(serialNumber);
	      PhoneNumberReservationInfo pnr = new PhoneNumberReservationInfo();
	      pnr.setNumberGroup(numberGroups);
	      pnr.setPhoneNumberPattern(phoneNumber);  
	      
	    facadeImpl.reservePortedInPhoneNumber(pnr, subscriberInfo, portInEligibilityInfo, sessionId);
	
		System.out.println("testReservePortInPhoneNumber end...");
			
		  
	}
	
	
	@Test
	public void testUnreservePhoneNumber() throws RemoteException, ApplicationException{
		System.out.println("testUnreservePhoneNumber start...");
		//1.telus own brand phoen number un-reserve
		boolean cancelPortIn = false;
		int ban= 70582009;
		String subscriberId ="6471273701";
		String productType = "C";
		PortInEligibilityInfo portInEligibilityInfo = null;
		
		
		SubscriberInfo subscriberInfo =  new SubscriberInfo();
		subscriberInfo.setBanId(ban);
		subscriberInfo.setSubscriberId(subscriberId);
		subscriberInfo.setProductType(productType);
		
		facadeImpl.unreservePhoneNumber(subscriberInfo, cancelPortIn, portInEligibilityInfo, sessionId);
	
		System.out.println("testUnreservePhoneNumber end...");
		
		
	}
	
	

	@Test
	public void testUnreservePortInPhoneNumber() throws RemoteException, ApplicationException{
		System.out.println("testUnreservePortInPhoneNumber start...");
		
		//2.port-In phone number un-reserve.we have to test  with MVNE and intra-carreir number.
		
		boolean cancelPortIn = true;
		int ban= 70582009;
		String subscriberId ="6471273701";
		String productType = "C";
		
		
		PortInEligibilityInfo portInEligibilityInfo = new PortInEligibilityInfo();
		portInEligibilityInfo.setPlatformId(2);
		portInEligibilityInfo.setIncomingBrandId(Brand.BRAND_ID_TELUS);
		portInEligibilityInfo.setOutgoingBrandId(Brand.BRAND_ID_NOT_APPLICABLE);
		
		SubscriberInfo subscriberInfo =  new SubscriberInfo();
		subscriberInfo.setBanId(ban);
		subscriberInfo.setSubscriberId(subscriberId);
		subscriberInfo.setProductType(productType);
		
		facadeImpl.unreservePhoneNumber(subscriberInfo, cancelPortIn, portInEligibilityInfo, sessionId);
	
		System.out.println("testUnreservePortInPhoneNumber end...");	
		
	}
	
	@Test
	public void testPhoneNumberReserveTest() throws RemoteException, TelusAPIException, ApplicationException
	{
		System.out.println("testPhoneNumberReserveTest start");
		String serialNumber = "8912239900000606675";
		AccountInfo account = impl.retrieveAccountByBan(70687713);
		
		PhoneNumberReservationInfo reservation = new PhoneNumberReservationInfo();
		
		NumberGroup[] numberGroups = referenceDataHelper
				.retrieveNumberGroupList(
						account.getAccountType(),
						account.getAccountSubType(),
						"C",
						"D");
		
	

		NumberGroup numberGroup = null;
		for (int i = 0; i < numberGroups.length; i++) {
			System.out.println(numberGroups[i].getProvinceCode());
			if ("ON".equals(numberGroups[i].getProvinceCode())  && "TOR".equals(numberGroups[i].getCode())) {
				numberGroup = numberGroups[i];
			}
		}
		System.out.println("NumberGroupInfo"+numberGroup.toString());
		reservation.setNumberGroup(numberGroup);
		reservation.setAsian(false);
		reservation.setLikeMatch(false);
		reservation.setWaiveSearchFee(true);
		reservation.setPhoneNumberPattern("**********");
		
		SubscriberInfo subscriberInfo =  new SubscriberInfo();
		subscriberInfo.setBanId(70687713);
		subscriberInfo.setDealerCode("A001000001");
		subscriberInfo.setSalesRepId("0000");
		subscriberInfo.setProductType("C");
		subscriberInfo.setSerialNumber(serialNumber);
	      PhoneNumberReservationInfo pnr = new PhoneNumberReservationInfo();
	      pnr.setNumberGroup(numberGroup);
	      pnr.setLikeMatch(false);
	      pnr.setPhoneNumberPattern("**********");
	      SubscriberInfo subscriberInfo1 =facadeImpl.reservePhoneNumber(pnr, subscriberInfo, sessionId);
		    System.out.println("reserved phonenumber is "+subscriberInfo1.getPhoneNumber());
		
		System.out.println("testPhoneNumberReserveTest end");
		
	}

	@Test
	public void testAsyncNotifyServiceCancellation() throws Throwable {
		String subscriberId = "4031610439"; //PT148
//		subscriberId = "9057160946";
		
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		
		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(subscriberId);
		subscriberInfo.setPortType(Subscriber.PORT_TYPE_PORT_OUT);
		
		facadeImpl.asyncNotifyServiceCancellation(subscriberInfo, new Date(), auditInfo, false, sessionId);
	}
	
	@Test
	public void testCancelPortOutSub() throws Throwable {
		String ban = "70691567";
		String phoneNumber = "5871724444";
		Date effectiveDate = new Date();
		boolean forceCancelImmediateIndicator = true;
		SubscriberLifecycleInfo subLifecycleInfo = new SubscriberLifecycleInfo();
		subLifecycleInfo.setMemoText("Port-out test");
		subLifecycleInfo.setReasonCode("PRTO");
		ServiceRequestHeader header = new ServiceRequestHeaderInfo();
		boolean notificationSuppressionInd = false;
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		
		facadeImpl.cancelPortOutSubscriber(ban, phoneNumber, effectiveDate, forceCancelImmediateIndicator, subLifecycleInfo, header, notificationSuppressionInd, auditInfo, sessionId);
	}
	

	@Test
	public void testsubmitProvisioningOrders() throws Throwable {
		
		//1. Account Cancel Test
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createAccountCancelRequest(1, 1, new Date(), "TEST");
		facadeImpl.submitProvisioningOrder(provisioningRequestInfo);	
	}
	
	@Test
	public void testWirelessProvisioningService() throws Throwable {
		TestPointResultInfo i = subscriberLifecycleFacadeTestPoint.testWirelessProvisioningService();
		System.out.println(i.toString());
	}
	
	@Test
	public void testLogicalResourceService() throws Throwable {
	 TestPointResultInfo tpr = subscriberLifecycleFacadeTestPoint.testLogicalResourceService();
			String excepectedErrorMessage = "[401:Unauthorized] when accessing the URI [http://resourcemanagementesb-";
			if(tpr.getExceptionDetail().contains(excepectedErrorMessage)){
				System.out.println("testLogicalResourceService is successful");
			}
			else{
				System.out.println("testLogicalResourceService is not successful");
			}

	}
	
	@Test
	public void testMinMdnService() throws Throwable {
		try {
			String result = facadeImpl.retrieveTNProvisionAttributes("1234567890","C");
			System.out.println(result);
		} catch (Throwable t) {
			System.out.println(t.getMessage().contains("W200012 : No MIN has been associated with this MDN"));
		}
	}
	
	@Test	
	public void testSubscriberProfile() throws Throwable{
		
		System.out.println("start of testSubscriberProfile");
		String billingAccountNumber = "1916609";
		String subscriberId = "6479990402";
		
		Boolean prepaidInd = true;
		String emailAddress = "test@test.com";
		String language = null;
		String invoiceCallSortOrderCd = "N";
		String subscriptionRoleCd = "AA";
		
		ConsumerNameInfo consumerNameInfo = new ConsumerNameInfo();
		consumerNameInfo.setFirstName("xyz");
		consumerNameInfo.setLastName("pqr");
		consumerNameInfo.setTitle("unit testing");

		AddressInfo addressInfo = new AddressInfo();
		
		addressInfo.setStreetNumber("200");

		addressInfo.setStreetName("canberra");

		addressInfo.setCity("Scarborough");

		addressInfo.setProvince("ON");

		addressInfo.setPostalCode("M128P6");

		addressInfo.setCountry("CAN");
		
		SubscriberInfo subscriberInfo = facadeImpl.updateSubscriberProfile(billingAccountNumber, subscriberId, prepaidInd, consumerNameInfo, 
				addressInfo, emailAddress, language, invoiceCallSortOrderCd, subscriptionRoleCd,sessionId);
		
		AddressInfo address1 = subscriberLifecycleHelper.retrieveSubscriberAddress(Integer.valueOf(billingAccountNumber),subscriberId);

		SubscriptionRoleInfo subscriberRoleInfo = subscriberLifecycleHelper.retrieveSubscriptionRole(subscriberId);
		System.out.println("Code :"+subscriberRoleInfo.getCode());
		
		System.out.println("End of testSubscriberProfile");
	}

	
}
