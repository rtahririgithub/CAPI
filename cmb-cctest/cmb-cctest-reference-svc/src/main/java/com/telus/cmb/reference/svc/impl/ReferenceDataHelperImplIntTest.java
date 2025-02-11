package com.telus.cmb.reference.svc.impl;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import javax.naming.NamingException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.eas.utility.info.BillCycleInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class ReferenceDataHelperImplIntTest {
	private static ReferenceDataHelper helper;
	private static String url;
	
	static {
		//url = "t3://lp97546:53153"; //prB
//		url = "t3://sn25257:30152"; //D3
		url = "t3://ln99246:43152";
	}
	
	@BeforeClass
	public static void beforeClass() throws NamingException{
		
		helper = (ReferenceDataHelper) RemoteBeanProxyFactory.createProxy(
				ReferenceDataHelper.class, 
				"ReferenceDataHelper#com.telus.cmb.reference.svc.impl.ReferenceDataHelperHome", 
				url);
	}
	
	@Test
	public void testRetrieveBillCycleListLeastUsed() {
		try {
			assertEquals(helper.retrieveBillCycleListLeastUsed().length,5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRetrieveIncludedPromotions() throws Throwable {
		String pPricePlanCD = "P40SKVM";
		String pEquipmentType = "P";
		String pNetworkType ="H";
		String pProvinceCD = "SK";
		int term = 36;
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		ServiceInfo[] promotionList = helper.retrieveIncludedPromotions(pPricePlanCD, pEquipmentType, pNetworkType, pProvinceCD, term);
		long endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println(promotionList.length);
		System.out.println("Time elapsed: "+(endTime-startTime));
		
	}
	
	@Test
	public void testRetrieveBillCycleListLeastUsed1() {
		try {
			BillCycleInfo[] i = helper.retrieveBillCycles();
			for (BillCycleInfo billCycleInfo : i) {
				System.out.println(billCycleInfo.getCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
