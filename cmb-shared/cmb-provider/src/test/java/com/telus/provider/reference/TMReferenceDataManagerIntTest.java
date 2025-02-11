package com.telus.provider.reference;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentManager;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.DataSharingGroup;
import com.telus.api.reference.DiscountPlan;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.eas.utility.info.DiscountPlanInfo;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.equipment.TMEquipmentManager;

public class TMReferenceDataManagerIntTest extends BaseTest {

	private ReferenceDataManager referenceDataManager;
	private EquipmentManager equipmentManager;

	static {
		//setupD3();
		//setupCHNLECA_PT168();
		setupEASECA_QA();
		//		System.setProperty("cmb.services.ReferenceDataFacade.url", "t3://localhost:7001");
		//		System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:7001");
	}

	public TMReferenceDataManagerIntTest(String name) throws Throwable {
		super(name);
	}

	public void setUp() throws Exception{
		super.setUp();
		referenceDataManager = super.provider.getReferenceDataManager();
		equipmentManager =  super.provider.getEquipmentManager0();	
	}

	public void testGetDataSharingGroup() throws TelusAPIException{

		DataSharingGroup dataSharingGroup = referenceDataManager.getDataSharingGroup("US_DATA");
		System.out.println(dataSharingGroup.toString());	

	}

	public void testGetDataSharingGroups() throws TelusAPIException{
		DataSharingGroup[] dataSharingGroups = referenceDataManager.getDataSharingGroups();
		System.out.println("dataSharingGroups length"+dataSharingGroups.length);
		for(int i=0;i<dataSharingGroups.length;i++){
			System.out.println(dataSharingGroups[i].toString());
		}
	}

	/* AT Test cases for PT148 */

	public void testGetDataSharingGroup1() throws TelusAPIException {

		DataSharingGroup dataSharingGroup = referenceDataManager
				.getDataSharingGroup("CAD_TXT");
		System.out.println(dataSharingGroup.toString());
		assertEquals(dataSharingGroup.getCode(), "CAD_TXT");


	}


	public void testGetDataSharingGroup4() throws TelusAPIException {
		try {
			DataSharingGroup dataSharingGroup = referenceDataManager
					.getDataSharingGroup("");
			System.out
			.println("dataSharingGroup" + dataSharingGroup.toString());
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());

		}
	}


	public void testGetTitles() throws TelusAPIException{

		System.out.println(referenceDataManager.getTitles());

	}

	public void testIsServiceAssociatedToPricePlan ()throws TelusAPIException 
	{
		String pricePlanCode ="PCONSPC25"; //"PPAPT";
		String serviceCode="XSBUSD250";//"SPAPT";
		String printStr="price plan "+pricePlanCode+" and service "+serviceCode+" is ";

		try { 			 
			if (!referenceDataManager.isServiceAssociatedToPricePlan(pricePlanCode, serviceCode))
				printStr += " NOT ";
			printStr +=" associated!";		
			System.out.println(printStr);

		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage()); 
		}
	}
	//koodo smartpay-sim only test cases begin

	public void testGetDiscounts() throws TelusAPIException {
		boolean current = true;
		
		System.out.println("start get Discounts methods");
		
		// 1. retrieving all current active discountPlans
		DiscountPlan[] discountPlans = referenceDataManager.getDiscountPlans(current);
		for (int i = 0; i < discountPlans.length; i++) {
			System.out.println("\n"+discountPlans[i].toString());
		}

		// 2. retrieving all  discountPlan by discount code
		DiscountPlan discountPlan = referenceDataManager.getDiscountPlan("D10P");
		System.out.println("\n"+discountPlan.toString());

		// 3. retrieving all  current discountPlans by pricePlan code and province

		DiscountPlan[] discountPlans1 = referenceDataManager.getDiscountPlans(current, "P45TB4", "ON", 0);
		for (int i = 0; i < discountPlans1.length; i++) {
			System.out.println("\n"+discountPlans[i].toString());
		}

		// 4 retrieving all  current discountPlans by pricePlan code and province and equipment

		Equipment equipment = (TMEquipment) equipmentManager.getEquipment("06800084396");

		DiscountPlan[] discountPlans2 = referenceDataManager.getDiscountPlans(current, "P35TB4", "ON", equipment, 0);

		for (int i = 0; i < discountPlans2.length; i++) {
			System.out.println("\n" + discountPlans2[i].toString());
		}
		System.out.println("end get Discounts methods");
////System.out.println(referenceDataManager.getAccountType("BF", 1));
	}
	
	public void getAccountTypeTest() throws TelusAPIException{
		Account account = api.getAccountManager().findAccountByBAN(70701880);
		AccountType accountType = referenceDataManager.getAccountType(account);
		System.out.println("Deafault dealer code"+accountType.getDefaultDealer());
		System.out.println("Deafault Sales code"+accountType.getDefaultSalesCode());
		
	}
	
}


