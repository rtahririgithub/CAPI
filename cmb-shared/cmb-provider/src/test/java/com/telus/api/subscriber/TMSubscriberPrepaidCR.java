package com.telus.api.subscriber;


import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.NumberGroup;


public class TMSubscriberPrepaidCR extends BaseTest{

	public TMSubscriberPrepaidCR(String name) throws Throwable {
		super(name);
		// TODO Auto-generated constructor stub
	}
	static{
		//setupD3();
		setupEASECA_QA();
	}
	//Need to test more scenarios 
	//Tested wheather the data is populated from LDAP Succesfully.
	
	public void testAvailableNumberGroups() throws UnknownBANException, TelusAPIException{

		Subscriber[] subscriber = provider.getAccountManager().findSubscribersByBAN(4036958, 4);
		NumberGroup[] numberGroup = subscriber[0].getAvailableNumberGroups();
		assertEquals(47,numberGroup.length);
		Subscriber[] subscriber1 = provider.getAccountManager().findSubscribersByPhoneNumber("6472104749",1,false);
		NumberGroup[] numberGroup1 = subscriber1[0].getAvailableNumberGroups();
		System.out.println("numberGroup1"+numberGroup1.length);
		assertEquals(47,numberGroup1.length);
	}
	

}
