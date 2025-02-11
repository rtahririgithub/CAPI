/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.test.subscriber.dao;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.account.Account;
import com.telus.api.account.Subscriber;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.subscriber.lifecyclefacade.dao.PortRequestInformationDao;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.ProvinceInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@ContextConfiguration(locations="classpath:com/telus/cmb/test/subscriber/dao/test-context-dao.xml")
public class PortRequestInformationDaoTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@Autowired
	private PortRequestInformationDao dao;
	
	@Autowired
	private ReferenceDataFacade referenceFacade;
	
	
	
	@Test
	public void isValidatePortInRequest() throws Exception {
		try {
			
			ProvinceInfo[] provinces = referenceFacade.getProvinces();
			
			Collection<PortRequestInfo> requests = dao.getCurrentPortRequestsByBan(70615536, provinces);
			if (!requests.isEmpty()) {
				PortRequestInfo request = requests.iterator().next();
				
				AccountInfo account = new AccountInfo(Account.ACCOUNT_TYPE_CONSUMER, Account.ACCOUNT_SUBTYPE_PCS_PERSONAL);
				request.setAccount(account);

				request.setSubscriber( new SubscriberInfo());
				request.getSubscriber().setProductType(Subscriber.PRODUCT_TYPE_PCS);
				EquipmentInfo equipmentInfo = new EquipmentInfo();
				equipmentInfo.setNetworkType("H");
				request.setEquipment(equipmentInfo);
				
				dao.isValidPortInRequest(request, "SMARTDESKTOP", "test", provinces);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
