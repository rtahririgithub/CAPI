package com.telus.api.reference;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.eas.utility.info.ServiceDataSharingGroupInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.provider.account.TMAccountManager;
import com.telus.provider.reference.TMReferenceDataManager;
import com.telus.provider.reference.TMServiceSummary;

public class TMServiceSummaryIntTest extends BaseTest {

	private TMAccountManager accountManager;
	private TMReferenceDataManager referenceDataManager;
	
	static {
		setupEASECA_PT168();
		//setupSMARTDESKTOP_D3();
//		setupEASECA_QA();
		//setupINTECA_CSI();
		
//		System.setProperty("cmb.services.ReferenceDataFacade.url", "t3://d070296:7001");
//		System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://d070296:7001");
//		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://d070296:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://d070296:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://d070296:7001");
//		System.setProperty("cmb.services.SubscriberLifecycleHelper.url","t3://d070296:7001");
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://d070296:7001");
//		System.setProperty("cmb.services.SubscriberLifecycleManager.url", "t3://d070296:7001");
//		System.setProperty("cmb.services.ProductEquipmentHelper.url", "t3://d070296:7001");
//		System.setProperty("cmb.services.ProductEquipmentLifecycleFacade.url", "t3://d070296:7001");
//		System.setProperty("cmb.services.ProductEquipmentManager.url", "t3://d070296:7001");
//		System.setProperty("cmb.services.ConfigurationManager.url", "t3://d070296:7001");
//		System.setProperty("cmb.services.MonitoringFacade.url", "t3://d070296:7001");
	}
	
	public TMServiceSummaryIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		referenceDataManager = provider.getReferenceDataManager0();
	}
	
	public void testApply() throws TelusAPIException{
		try{
		Account account = accountManager.findAccountByBAN(55426581);
		ServiceInfo si=new ServiceInfo();
		si.setActive(true);
		TMServiceSummary tss = new TMServiceSummary(referenceDataManager , si);
		FundSource[] fundSources = tss.getAllowedPurchaseFundSourceArray();
		System.out.println(fundSources);
		
		}catch(TelusAPIException t){
			System.out.println("Amdocs error - Invalid chargecode");
		}
		
	}
	
	public void testDataSharingGroups() throws TelusAPIException{
		ServiceDataSharingGroupInfo dataSharingGroup = new ServiceDataSharingGroupInfo();
		dataSharingGroup.setDataSharingGroupCode("Code");
		ServiceDataSharingGroup[] array= new ServiceDataSharingGroupInfo[1];
		array[0]=dataSharingGroup;
		System.out.println(dataSharingGroup.toString());
		ServiceInfo si=new ServiceInfo();
		si.setDataSharingGroups(array);
		
		TMServiceSummary tss = new TMServiceSummary(referenceDataManager , si);
		ServiceDataSharingGroup[] groups=tss.getDataSharingGroups();
		assertEquals(1, groups.length);
		
	}
	
	public void testAddDataSharingGroups() throws TelusAPIException{
		ServiceDataSharingGroupInfo dataSharingGroup = new ServiceDataSharingGroupInfo();
		dataSharingGroup.setDataSharingGroupCode("Code");
		
		ServiceInfo si=new ServiceInfo();
		si.addDataSharingGroup(dataSharingGroup);
		si.addDataSharingGroup(dataSharingGroup);
		si.addDataSharingGroup(dataSharingGroup);
		
		TMServiceSummary tss = new TMServiceSummary(referenceDataManager , si);
		ServiceDataSharingGroup[] groups=tss.getDataSharingGroups();
		assertEquals(3, groups.length);
		
	}
	
	public void testGetFamilyType()throws TelusAPIException {
		ServiceInfo si=provider.getReferenceDataFacade().getRegularService("XSCLDINT ");
		TMServiceSummary tss = new TMServiceSummary(referenceDataManager , si);
		String[] familyTypes=tss.getFamilyTypes();
		assertEquals(0,familyTypes.length);
		logFamilyTypes(familyTypes, si);
	}
	
	private void logFamilyTypes(String[] familyTypes, ServiceInfo si) {
		String familyTypesString = "";
		for(int i=0;i<familyTypes.length;i++){
			familyTypesString += familyTypes[i] +" | ";
		}
		System.out.println("FamilyTypes for " + si.getCode().trim() + ": " + familyTypesString);
	}
	
	public void testRegularServiceIsFlexPLanIsMandatoryAddOn()throws TelusAPIException {
		ServiceInfo si=provider.getReferenceDataFacade().getRegularService("SD500MB");
		TMServiceSummary tss = new TMServiceSummary(referenceDataManager , si);
		logFamilyTypes(tss.getFamilyTypes(), si);
		boolean isFlexPlan =tss.isFlexPlan();
		boolean isMandatoryAddOn =tss.isMandatoryAddOn();
		System.out.println("Service " + si.getCode() + " isFlexPLan:  " + isFlexPlan);
		System.out.println("Service " + si.getCode() + " isMandatoryAddOn:  " + isMandatoryAddOn);
		assertTrue("This is a Flex Plan Service.", !isFlexPlan);
		assertTrue("This is not a Mandatory AddOn Service.", isMandatoryAddOn);
	}
	
	public void testPricePlanIsFlexPLanIsMandatoryAddOn()throws TelusAPIException {
		ServiceInfo si=provider.getReferenceDataFacade().getPricePlan("FA12A");//PQC75SHPD, FA12A
		TMServiceSummary tss = new TMServiceSummary(referenceDataManager , si);
		logFamilyTypes(tss.getFamilyTypes(), si);
		boolean isFlexPlan =tss.isFlexPlan();
		boolean isMandatoryAddOn =tss.isMandatoryAddOn();
		System.out.println("Price Plan " + si.getCode() + " isFlexPLan:  " + isFlexPlan);
		System.out.println("Price Plan " + si.getCode() + " isMandatoryAddOn:  " + isMandatoryAddOn);
		assertTrue("This is not a Flex Plan Service.", isFlexPlan);
		assertTrue("This is a Mandatory AddOn Service.", !isMandatoryAddOn);
	}
	
/* AT Test cases for PT148 */
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}

	public void testGetDataSharingGroupATTest1() throws TelusAPIException {

		DataSharingGroup dataSharingGroup = referenceDataManager
				.getDataSharingGroup("US_DATA");
		System.out.println("dataSharingGroup"+dataSharingGroup.toString());
		
		assertEquals(dataSharingGroup.getCode(), "US_DATA");
		

	}
	
	public void testGetDataSharingGroupATTest2() throws TelusAPIException {

		DataSharingGroup dataSharingGroup = referenceDataManager
				.getDataSharingGroup("XPBAPDA2");
		if(dataSharingGroup == null)
			System.out.println("No DatasharingGroups found for given code XPBAPDA2");
		//assertEquals(dataSharingGroup.getCode(), "XPBAPDA2");
		

	}

	public void testGetSubscriber() throws Exception {
		try {

			Account account = accountManager.findAccountByBAN(70719128);
			Subscriber subscriber = account.getSubscriber("5871901282");
			System.out.println(subscriber);
			
			Equipment equipment = subscriber.getEquipment();
			System.out.println(equipment);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
 
}


