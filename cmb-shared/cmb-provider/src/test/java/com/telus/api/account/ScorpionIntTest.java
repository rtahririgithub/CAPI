package com.telus.api.account;


import com.telus.api.BaseTest;
import com.telus.api.BrandNotSupportedException;
import com.telus.api.TelusAPIException;
import com.telus.api.dealer.CPMSDealer;
import com.telus.api.dealer.DealerManager;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentManager;
import com.telus.api.portability.PortOutEligibility;
import com.telus.api.portability.PortRequestManager;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.Brand;
import com.telus.api.reference.ChargeType;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSelectionCriteria;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.Segmentation;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMAccount;
import com.telus.provider.account.TMAccountManager;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.equipment.TMEquipmentManager;
import com.telus.provider.util.transition.TMBrandTransitionMatrix;
import com.telus.provider.util.transition.ValidationResult;

public class ScorpionIntTest extends BaseTest{
	private TMAccountManager accountManager;
	private TMEquipmentManager equpmentManager;
	
	
	private int brandId = 4;
	
	public ScorpionIntTest(String name) throws Throwable {
		super(name);
	}
	static {
		//setupD3();
		setupEASECA_QA();
	}
	
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		equpmentManager = super.provider.getEquipmentManager0();
	}
	public void testTMProvider() throws TelusAPIException{
		TMProvider tmprovider = new TMProvider("18654", "apollo", ApplicationSummary.APP_SD,new int[]{1,4,5});
	}
	public void testInternationalServiceEligibilityCheckStrategy() throws UnknownBANException, TelusAPIException{
		TMAccount account = (TMAccount)accountManager.findAccountByBAN(12474);
		 
		 InternationalServiceEligibilityCheckResult resultInfo = account.checkInternationalServiceEligibility();
		 System.out.println("Deposit Amount ="+resultInfo.getDepositAmount());
	}
	public void testUpdateBrand() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(12474);
		String memoText="memo";
		
		account.updateBrand(brandId, memoText);
	}

	public void testIsBrandId() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(12474);
		assertTrue(account.isBrand(brandId));
	}
	
	public void testValidateSerialNumber() throws SerialNumberInUseException, UnknownSerialNumberException, InvalidSerialNumberException, TelusAPIException {
		 Equipment equipment = equpmentManager.validateSerialNumber("15603173774", brandId);
		 System.out.println("Equipment getBanID="+equipment.getBanID());
		 System.out.println("Equipment getEquipmentModel="+equipment.getEquipmentModel());
	}


	public void testGetSegmentation() throws TelusAPIException{
		ReferenceDataManager referenceDataManager = provider.getReferenceDataManager();
		Segmentation segmentation = referenceDataManager.getSegmentation(1, "B", "ON");
		System.out.println("Segmentation is=="+segmentation);
		System.out.println("Segmentation getCode=="+segmentation.getCode());
		System.out.println("Segmentation getDescription=="+segmentation.getDescription());
		System.out.println("Segmentation getSegment=="+segmentation.getSegment());

		AccountType accountType = referenceDataManager.getAccountType("EC", brandId);
		System.out.println("AccountType =="+accountType);
		System.out.println("AccountType getAccountSubType=="+accountType.getAccountSubType());
		System.out.println("AccountType getBillingNameFormat=="+accountType.getBillingNameFormat());
		System.out.println("AccountType getCode=="+accountType.getCode());
		
		Brand brand =referenceDataManager.getBrand(brandId);
		System.out.println("Brand ==="+brand);
		System.out.println("Brand getBrandId==="+brand.getBrandId());
		System.out.println("Brand getCode==="+brand.getCode());
		System.out.println("Brand getDescription==="+brand.getDescription());
		System.out.println("Brand getShortDescription==="+brand.getShortDescription());
		
		PricePlanSelectionCriteria criteria = new PricePlanSelectionCriteria();
		criteria.setProductType("C");
		criteria.setEquipmentType("D");
		criteria.setProvinceCode("BC");
		criteria.setAccountType(new Character('I'));
        criteria.setAccountSubType(new Character('R'));
		criteria.setBrandId(new Integer(1));
		criteria.setIncludeFeaturesAndServices(new Boolean(true));
		criteria.setCurrentPlansOnly(new Boolean(true));
		PricePlanSummary[] pricePlans = referenceDataManager.findPricePlans(criteria);
		
		assertEquals(41,pricePlans.length);
		for (int i = 0; i < pricePlans.length; i++) {
			PricePlan pricePlan = referenceDataManager.getPricePlan(pricePlans[i].getCode(), "D","ON", 'I', 'R', 1);
			System.out.println("PricePlan =="+pricePlan);
		}
		ChargeType chargeType=referenceDataManager.getPaperBillChargeType(3, "SK", 'I', 'Q', "TCSO", "1");
		System.out.println("chargeType  === "+chargeType);
	}
	

	public void testEquipmentMethods() throws TelusAPIException{
		EquipmentManager equipmentManager = provider.getEquipmentManager();
		Equipment equipment = equipmentManager.getEquipment("05300004600");
		System.out.println("isGreyMarket"+equipment.isGreyMarket(brandId));
		System.out.println("isValidForBrand"+equipment.isValidForBrand(brandId));
		//assertFalse(equipment.isGreyMarket(brandId));
		//assertTrue(equipment.isValidForBrand(brandId));
		
	}
	
	public void testValidTransitionMatrix() throws TelusAPIException{
		EquipmentManager equipmentManager = provider.getEquipmentManager();
		Equipment equipment = equipmentManager.getEquipment("05300004600");
		
		TMBrandTransitionMatrix transitionMatrix = provider.getBrandTransitionMatrix0();
		
		ValidationResult result=transitionMatrix.validTransition(brandId, equipment , "*", "SWAPTRACK", false);
		System.out.println("MessageID "+result.getMessageId());
		
	}
	public void testDealerMethods() throws TelusAPIException{
		try
		{
		DealerManager dealerManager = provider.getDealerManager();
		CPMSDealer dealer = dealerManager.getCPMSDealer("A001000001", "0000");
		System.out.println("phone = "+dealer.getPhone());
		System.out.println("user ="+dealer.getUserCode());
		for(int i=0 ;i<dealer.getBrandIds().length;i++){
			System.out.println("Brand="+dealer.getBrandIds()[i]);
		}
		
		CPMSDealer de=dealerManager.getCPMSDealerByLocationTelephoneNumber("5141752611");
		System.out.println(""+de.getChannelCode());
		System.out.println(""+de.getPhone());
		System.out.println(""+de.getUserCode());
		}
		catch (Exception e) {
			assertEquals("Unknown Dealer; name=[5141752611]: Location telephone number can not be mapped to CPMS Outlet", e.getMessage());
		}
	}
	public void testSubscriberMethods() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException{
		Subscriber[] subscriber = provider.getAccountManager().findSubscribersByBAN(70616122,2);
		((TMSubscriber)subscriber[0]).getPortRequestSummary();
	}
	public void testPortIneligibility() throws TelusAPIException{
		PortRequestManager manager= provider.getPortRequestManager();
		try{
		manager.testPortInEligibility("5141752611", "", 2);
		fail("Exception Expected");
		}catch(Exception ex){
			
		}
		PortOutEligibility eligibility =  manager.testPortOutEligibility("5141752611", "Y");
		System.out.println("Eligibility =="+eligibility.isEligible());
	}

}
