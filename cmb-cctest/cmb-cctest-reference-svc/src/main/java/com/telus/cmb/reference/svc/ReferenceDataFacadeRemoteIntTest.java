package com.telus.cmb.reference.svc;

import java.util.Arrays;
import java.util.Hashtable;

import javax.naming.Context;

import org.junit.BeforeClass;
import org.junit.Test;

import com.telus.api.reference.BillCycle;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePolicyInfo;


public class ReferenceDataFacadeRemoteIntTest {

	static ReferenceDataFacade facade;
	static ReferenceDataHelper service ;
	static ServiceOrderReferenceFacade serviceOrder;
	
	@BeforeClass
	public static void prepare() throws Throwable {
		//String url = "t3://wldv103umgenutilsvc:20152";
//		String url = "t3://sn25257:30152";
		String url = "t3://localhost:7001";
//		url = "t3://um-generalutilities-pt148.tmi.telus.com:30152";
//		String url = "t3://um-generalutilities-dv103.tmi.telus.com:20152";
//		service = (ReferenceDataSvc) RemoteBeanProxyFactory.createProxy(
//				ReferenceDataSvc.class, 
//				"ReferenceDataSvc#com.telus.cmb.reference.svc.impl.ReferenceDataSvcHome", 
//				"t3://wldv103umgenutilsvc:20152");

		System.out.println( url );
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext(url));
		facade = (ReferenceDataFacade) context.lookup(EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE);
		service = (ReferenceDataHelper) context.lookup(EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_HELPER);
		serviceOrder = (ServiceOrderReferenceFacade)context.lookup("ServiceOrderReferenceFacade#com.telus.cmb.reference.svc.ServiceOrderReferenceFacade");
		context.close();
	}
	
	private static Hashtable<Object,Object> setEnvContext(String url){

		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}
	

	@Test
	public void getAccountTypes() throws Exception {
		System.out.println(facade.getAccountTypes());
	}
	
	@Test
	public void getLanguages() throws Exception {
		System.out.println(facade.getLanguages());
	}
	
	@Test
	public void getServiceCodesByGroup() throws Exception {
		System.out.println("Start getServiceCodesByGroup()");
		String serviceGroupCode="FPUEWQ";
		String[] result= facade.getServiceCodesByGroup(serviceGroupCode);
		System.out.println("Result length :"+result.length);
		System.out.println("End getServiceCodesByGroup()");
	}
	
	@Test
	public void getDealerSalesRepByCode() throws Exception {
		
		System.out.println("Start getDealerSalesRepByCode()");
		 facade.getDealerSalesRepByCode("B00AB00003", "0001", true);
		System.out.println("End getDealerSalesRepByCode()");
	}
	
	@Test
	public void removeBillCyclesByProvince() throws Exception {
		
		System.out.println("Start removeBillCyclesByProvince()");
		BillCycle billCycleInfo =  service.retrieveBillCycle("54");
		BillCycle [] billCycleInfos = new BillCycle[2];
		System.out.println("billCycleInfo"+billCycleInfo.toString());
		billCycleInfos[0]=billCycleInfo;
		facade.removeBillCyclesByProvince(billCycleInfos, "ON");
		System.out.println("End removeBillCyclesByProvince()");
	}
	
	@Test
	public void getServiceTerm() throws Exception  {
		String serviceCode="SAIR2T1";	
		ServiceTermDto result=facade.getServiceTerm(serviceCode);
		System.out.println("RESULT : "+result.toString());
	}

	@Test
	public void getPDSPaymentMethod() throws Exception {
		String code = "R";	
		String result = facade.getPaymentMethodTypeByKBPaymentMethodType(code);
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void getPDSSubscriptionType() throws Exception {
		String code = "R";
		String result = facade.getSubscriptionTypeByKBServiceType(code);
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void getPDSServiceInstanceStatus() throws Exception {
		String code = "R";	
		String result = facade.getServiceInstanceStatusByKBSubscriberStatus(code);
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void getPDSBillingAccountStatus() throws Exception {
		String code = "N";	
		String result = facade.getBillingAccountStatusByKBAccountStatus(code);
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void getPDSCreditCardType() throws Exception {
		String code = "M";	
		String result = facade.getCreditCardTypeByKBCreditCardType(code);
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void getPDSBillCycleCode() throws Exception {
		String code = "1";
		String result = facade.getBillCycleCodeByKBBillCycleCode(code);
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void getNameSuffixByKBNameSuffix() throws Exception {	
		String result = facade.getNameSuffixByKBNameSuffix("S");
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void getSaluationCodeByKBSaluationCode() throws Exception {	
		String result = facade.getSaluationCodeByKBSaluationCode("TEST");
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void getEquipmentGroupTypeBySEMSEquipmentGroupType() throws Exception {	
		String result = facade.getEquipmentGroupTypeBySEMSEquipmentGroupType("PCS");
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void getProvinceCodeByKBProvinceCode() throws Exception {	
		String result = facade.getProvinceCodeByKBProvinceCode("ON");
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void getCountryCodeByKBCountryCode() throws Exception {	
		String result = facade.getCountryCodeByKBCountryCode("CA");
		System.out.println("RESULT : " + result);
	}
	
	@Test
	public void testGetDataSharingGroups() throws TelusException {
		
		System.out.println( facade.getDataSharingGroup("CAD_TXT") );
		System.out.println( Arrays.asList(facade.getDataSharingGroups()));
		System.out.println( facade.getDataSharingGroup("CAD_TXT") );
		System.out.println( facade.getDataSharingGroup("US_TXT") );
		
	}
	@Test
	public void testGetRegularServices()  {
		long start = System.currentTimeMillis();

		try {
			
			System.out.println("Starting getRegularServices()...");
			ServiceInfo [] result = facade.getRegularServices();
			System.out.println("Retrieved [" + result.length + "] services in [" + (System.currentTimeMillis() - start) + "] msec.");
			
		} catch (Exception e) {
			System.out.println("Exception [" + e.getMessage()+ "] after [" + (System.currentTimeMillis() - start) + "] msec.");
			e.printStackTrace();
		}
	}
	
	@Test
	public void isNotificationEligible() throws Throwable {
		String transactionType = "PYMT_MAKE";
		String originatingeApp = "SMARTDESKTOP";
		int brandId = 1;
		String accountType = "IR";
		String banSegment = "TCSO";
		String productType = null;
		
		System.out.println(facade.isNotificationEligible(transactionType, originatingeApp, brandId, accountType, banSegment, productType));
	}
	
	@Test
	public void testCheckPricePlanPrivilege() throws Throwable{
		
		System.out.println("start of testCheckServicePriviledge");
		//String serviceCode[] = {"ANY503","77SBLDTPD","SVTPROVN","SQC911"};
		String pricePlanCodes[] = {"SABUN05","SLDC20RM2","AT150A1","BRO1"};
				
		ServicePolicyInfo servicePolicyInfo[] = serviceOrder.checkPricePlanPrivilege(pricePlanCodes, "CLIENT", "RTNONCHNG");
		
		for(ServicePolicyInfo serviceInfo:servicePolicyInfo){
			System.out.println("Code :"+serviceInfo.getCode());		
		}
		System.out.println("End of testCheckServicePriviledge length:"+servicePolicyInfo.length);
	}
}
