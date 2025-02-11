package com.telus.cmb.utility.dealermanager.svc.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.cmb.utility.dealermanager.svc.DealerManager;
import com.telus.eas.equipment.info.CPMSDealerInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-datasources.xml", 
									"classpath:application-context-dao-utility-dealermanager.xml",
									"classpath:com/telus/cmb/utility/dealermanager/svc/impl/application-context-svc-local.xml"})
									
public class DealerManagerImplTest {

	@Autowired
	DealerManager dealerManager;
	
	static {
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.provider", "com.telus.provider.config.PropertiesOverridingConfigurationProvider");
		System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
	}
	
	@Test
	public void getChnlOrgAssociation() throws Throwable {
		long chnlOrgId = 106782655;
		String associateReasonCd = "SH";
		
		long[] chnlOrg = dealerManager.getChnlOrgAssociation(chnlOrgId, associateReasonCd);
		for (long c : chnlOrg) {
			System.out.println(c);
		}
	}
	
	@Test
	public void getChnlOrgAssociationSAPSoldToParty() throws Throwable {
		long chnlOrgId = 106782655;
		long[] chnlOrg = dealerManager.getChnlOrgAssociationSAPSoldToParty(chnlOrgId);
		for (long c : chnlOrg) {
			System.out.println(c);
		}
	}
	
	@Test
	public void getCPMSDealerByKBDealerCode() throws Throwable {
		String pKBDealerCode = "1100027386";
		String pKBSalesRepCode = "0000";
		CPMSDealerInfo dealerInfo = dealerManager.getCPMSDealerByKBDealerCode(pKBDealerCode, pKBSalesRepCode);
		if (dealerInfo != null) {
			System.out.println(dealerInfo);
		}else {
			System.out.println("dealerInfo is null.");
		}
	}
}
