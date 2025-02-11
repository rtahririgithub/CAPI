package com.telus.cmb.reference.dao;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.equipment.Equipment;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PricePlanSummary;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao.xml", "classpath:application-context-datasources-dv103.xml"})

public class ReferenceDataKnowbilityDaoTest {

	@Autowired
	ReferenceDataKnowbilityDao dao;
		
	@Test
	public void testRetrieveNumberGroupsByProvinceCode(){
		System.out.println("testRetrieveNumberGroupsByProvinceCode  Start");
		
		char accountType = 'I';
		char accountSubTypeType = 'Q';
		String productType = "C";
		String equipmentType = "D";
		String provinceCode = "ON";

		Collection<NumberGroupInfo> ngInfoList= dao.retrieveNumberGroupListByProvince(accountType, accountSubTypeType, productType, equipmentType, provinceCode);
		//Collection<NumberGroupInfo> ngInfoList= (referenceDataKnowbilityDao.retrieveNumberGroupList(accountType, accountSubTypeType, productType, equipmentType, provinceCode));
		System.out.println("SIZE: "+ngInfoList.size());
		for(Iterator<NumberGroupInfo> i=ngInfoList.iterator();i.hasNext();){
			NumberGroupInfo	ngInfo=(NumberGroupInfo) i.next();
			System.out.println(ngInfo);
		}
		System.out.println("testRetrieveNumberGroupsByProvinceCode  End");
		
	}
	
	@Test
	public void testRetrieveNumberGroupsByMarketArea(){
		System.out.println("testRetrieveNumberGroupsByMarketArea  Start");
		
		char accountType = 'I';
		char accountSubTypeType = 'Q';
		String productType = "C";
		String equipmentType = "D";
		String marketArea="TME";
	
		Collection<NumberGroupInfo> ngInfoList= dao.retrieveNumberGroupList(accountType, accountSubTypeType, productType, equipmentType, marketArea);
		System.out.println("SIZE: "+ngInfoList.size());
		for(Iterator<NumberGroupInfo> i=ngInfoList.iterator();i.hasNext();){
			NumberGroupInfo	ngInfo=(NumberGroupInfo) i.next();
			System.out.println(ngInfo);
		}
		System.out.println("testRetrieveNumberGroupsByMarketArea  End");
	}
	
	@Test
	public void testRetrieveNumberGroups(){
		System.out.println("testRetrieveNumberGroups  Start");
		
		char accountType = 'I';
		char accountSubTypeType = 'Q';
		String productType = "C";
		String equipmentType = "D";
	
		Collection<NumberGroupInfo> ngInfoList= dao.retrieveNumberGroupList(accountType, accountSubTypeType, productType, equipmentType);
		System.out.println("SIZE: "+ngInfoList.size());
		for(Iterator<NumberGroupInfo> i=ngInfoList.iterator();i.hasNext();){
			NumberGroupInfo	ngInfo=(NumberGroupInfo) i.next();
			System.out.println(ngInfo);
		}
		System.out.println("testRetrieveNumberGroups  End");
	}
	
	@Test
	public void testRetrieveBillCycleListLeastUsed(){
		System.out.println("testRetrieveBillCycleListLeastUsed  Start");
		
		int[] billCycles= dao.retrieveBillCycleListLeastUsed();
		
		assertEquals(billCycles[billCycles.length-1],31);
		
		
		System.out.println("testRetrieveBillCycleListLeastUsed  End");
	}
	

	
	
	
	
	
	
}
