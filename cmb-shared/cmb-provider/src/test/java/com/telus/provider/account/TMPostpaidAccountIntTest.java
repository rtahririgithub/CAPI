package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;

public class TMPostpaidAccountIntTest extends BaseTest {

	static {
//		setupEASECA_PT168();
		//setupD3();
		setupEASECA_QA();
		
//		System.setProperty("cmb.services.SubscriberLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.SubscriberLifecycleHelper.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7001");
//		
//		System.setProperty("cmb.services.ReferenceDataFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:7001");
//		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMPostpaidAccountIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
	
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	
	}
	public void testSubscribersByDataSharingGroupResult() throws TelusAPIException{
		System.out.println("testSubscribersByDataSharingGroupResult start");
		String[] codes={"CAD_DATA","CAD_TXT","Int_Data","Int_Txt","US_DATA","N_America_TXT","N_America_Data","US_TXT"};
		Account account =  api.getAccountManager().findAccountByBAN(70615520);
		
		SubscribersByDataSharingGroupResultInfo[] subscribersByDataSharingGroupResult =
			(SubscribersByDataSharingGroupResultInfo[])((TMPostpaidAccount)account)
			.getSubscribersByDataSharingGroups(codes,getDateInput(2011,11,15));
		
		System.out.println("subscribersByDataSharingGroupResult.length : "+subscribersByDataSharingGroupResult.length);
		System.out.println("Print Output : ");
		for(int i=0; i<subscribersByDataSharingGroupResult.length; i++){
			System.out.println("getDataSharingGroup : "+subscribersByDataSharingGroupResult[i].toString());
		}
	   	System.out.println("testSubscribersByDataSharingGroupResult End");
	}
	
	public void testSubscribersByDataSharingGroupResultATTestingPT148() throws TelusAPIException{
		/*
		 * This query will give you a list of socs, the data sharing group code, and whether it is contributing(C) or access(A)
		 select distinct sg.soc, sgasg.ALLOW_SHARING_GROUP_CD , sgasg.ALLOW_SHARING_ACCESS_TYPE_CD 
               FROM  SOC_GROUP SG join  soc_grp_allow_sharing_grp sgasg on sgasg.gp_soc = sg.gp_soc                       

		 */
		System.out.println("Test subscriber with Contributing flag   Start  ");
		String[] codes = { "N_America_Data" };
		Account account = api.getAccountManager().findAccountByBAN(70615520);

		SubscribersByDataSharingGroupResultInfo[] subscribersByDataSharingGroupResult = (SubscribersByDataSharingGroupResultInfo[]) ((TMPostpaidAccount) account)
				.getSubscribersByDataSharingGroups(codes, null);
		System.out.println("subscribersByDataSharingGroupResult.length : "
				+ subscribersByDataSharingGroupResult.length);
		System.out.println("Print Output : ");
		for (int i = 0; i < subscribersByDataSharingGroupResult.length; i++) {
			System.out.println("isContributing flag value : "
					+ subscribersByDataSharingGroupResult[i].getDataSharingSubscribers()[i].isContributing());				
			System.out.println("Get SubscriberID : "+ subscribersByDataSharingGroupResult[i].getDataSharingSubscribers()[i].getSubscriberId());								
			assertEquals(
					subscribersByDataSharingGroupResult[i].getDataSharingSubscribers()[i].isContributing(), false);						
		}
	   	System.out.println("Test subscriber with Contributing flad false   End");
	}
	
	
	
}


