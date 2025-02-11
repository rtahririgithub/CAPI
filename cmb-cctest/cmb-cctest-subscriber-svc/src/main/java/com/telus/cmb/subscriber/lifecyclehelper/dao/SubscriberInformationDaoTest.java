package com.telus.cmb.subscriber.lifecyclehelper.dao;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry;

@RunWith(SpringJUnit4ClassRunner.class)
public class SubscriberInformationDaoTest extends  BaseLifecycleHelperIntTest {

	@Autowired
	SubscriberInformationCodsDao dao;
	
	private static long TEST_SUBSCRIPTION_ID = 5203610; // D3 - 5203610 ; PT148 - 7019032
	
	@Test
	public void mainTest() {
		System.out.println("START TESTING");
		System.out.println("--------------------------------------------------");
		
		//testRetrievePhoneDirectory();
		testUpdatePhoneDirectory();
		//testDeletePhoneDirectory();
	}
	
	private void testRetrievePhoneDirectory() {
		System.out.println("Entering Test method : testRetrievePhoneDirectory ");
		System.out.println("--------------------------------------------------");

		PhoneDirectoryEntry[] pdList = dao.getPhoneDirectory(TEST_SUBSCRIPTION_ID); 
		
		for(int i=0; i<pdList.length; i++) {
			System.out.println("Ph. Num. : " + pdList[i].getPhoneNumber());
			System.out.println("Nickname : " + pdList[i].getNickName());
			System.out.println("Effective  : " + pdList[i].getEffectiveDate());
			System.out.println("--------------------------------------------------");
		}

		System.out.println("Exiting Test method : testRetrievePhoneDirectory ");
		System.out.println("--------------------------------------------------");
	}

	private void testUpdatePhoneDirectory() {
		System.out.println("Entering Test method : testUpdatePhoneDirectory ");
		System.out.println("--------------------------------------------------");

		PhoneDirectoryEntry pdEntry1 = new PhoneDirectoryEntry();
		pdEntry1.setPhoneNumber("5198702348");
		pdEntry1.setNickName("Test Dan Upd2");
		pdEntry1.setEffectiveDate(new Date());
		
		PhoneDirectoryEntry pdEntry2 = new PhoneDirectoryEntry();
		pdEntry2.setPhoneNumber("4165551234");
		pdEntry2.setNickName("Test Dan New2");
		pdEntry2.setEffectiveDate(new Date());
		
		PhoneDirectoryEntry[] entries = new PhoneDirectoryEntry[] {pdEntry1, pdEntry2};

		dao.updatePhoneDirectory(TEST_SUBSCRIPTION_ID, entries); 

		System.out.println("Exiting Test method : testUpdatePhoneDirectory ");
		System.out.println("--------------------------------------------------");
	}

	private void testDeletePhoneDirectory() {
		System.out.println("Entering Test method : testDeletePhoneDirectory ");
		System.out.println("--------------------------------------------------");

		PhoneDirectoryEntry pdEntry1 = new PhoneDirectoryEntry();
		pdEntry1.setPhoneNumber("4165551234");
		
		PhoneDirectoryEntry pdEntry2 = new PhoneDirectoryEntry();
		pdEntry2.setPhoneNumber("4165559999");
		
		PhoneDirectoryEntry[] entries = new PhoneDirectoryEntry[] {pdEntry1, pdEntry2};

		dao.deletePhoneDirectoryEntries(TEST_SUBSCRIPTION_ID, entries); 

		System.out.println("Exiting Test method : testDeletePhoneDirectory ");
		System.out.println("--------------------------------------------------");
	}

}
