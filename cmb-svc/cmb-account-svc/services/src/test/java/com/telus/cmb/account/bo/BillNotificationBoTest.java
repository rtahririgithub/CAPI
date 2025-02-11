package com.telus.cmb.account.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.portalprofile.Persona;
import com.telus.cmb.common.dao.portalprofile.PortalProfile;
import com.telus.cmb.common.dao.portalprofile.PortalProfileFilterCriteria;

@ContextConfiguration(locations = { "classpath:application-context-test.xml" })
@ActiveProfiles("standalone")
public class BillNotificationBoTest extends AbstractTestNGSpringContextTests {
	private static final String USER_PERSONA_ROLE_OWNER = "Owner";
	private static final String USER_PERSONA_ROLE_MANAGER = "Manager";
	private static final String USER_PERSONA_ROLE_MEMBER = "Member";
	private static final String USER_PERSONA_STATUS_ACTIVE = "Active";
	
	static {
		System.setProperty("weblogic.Name", "standalone");
		String ldapUrl = "ldaps://ldapread-pt168.tmi.telus.com:636/cn=pt168_81,o=telusconfiguration";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", ldapUrl);
		System.setProperty("useLdapUrl", "true");
		System.setProperty("com.telusmobility.config.java.naming.security.ssl", "yes");
		System.setProperty("com.telusmobility.config.java.naming.security.authentication", "simple");
		System.setProperty("com.telusmobility.config.java.naming.security.principal", "uid=telusAdmin,ou=administrators,o=telusconfiguration");
		System.setProperty("com.telusmobility.config.java.naming.security.credentials", "ptEM168");
	}
	
	@Test(dataProvider = "get_portalprofile_email_happy_path")
	public void get_portal_profile_email_address_happy_path(int ban, List<PortalProfile> portalProfiles) {
		String expectedPortalProfileEmailAddress = "second.tester@telus.com";
		List<String> portalProfileEmailAddressList = getPortalProfileEmailAddressList(ban, portalProfiles);
		Assert.assertTrue(portalProfileEmailAddressList.size() == 1, "Found one portal profile email address");
		Assert.assertTrue(portalProfileEmailAddressList.get(0).equalsIgnoreCase(expectedPortalProfileEmailAddress), "Portal profile email address " + expectedPortalProfileEmailAddress);
	}
	
	@Test(dataProvider = "get_portalprofile_email_exception_case")
	public void get_portal_profile_email_address_exception_case(int ban, List<PortalProfile> portalProfiles) {
		List<String> portalProfileEmailAddressList = getPortalProfileEmailAddressList(ban, portalProfiles);
		Assert.assertTrue(portalProfileEmailAddressList.size() == 0, "Portal profile email address of active owner or manager is not found");
	}
	
	private List<String> getPortalProfileEmailAddressList(int ban, List<PortalProfile> portalProfiles) {

		List<String> registerBillNotificationEmailAddressList = new ArrayList<String>();

		String portalProfileEmail = getPortalProfileEmail(ban, portalProfiles);

		if (StringUtils.isNotEmpty(portalProfileEmail) && StringUtils.isNotBlank(portalProfileEmail)) {
			registerBillNotificationEmailAddressList.add(portalProfileEmail);
		}

		return registerBillNotificationEmailAddressList;
	}

	/**
	 * Get the portal profile email by ban
	 * @param ban
	 * @return the portal profile email if any
	 */
	private String getPortalProfileEmail(int ban, List<PortalProfile> portalProfiles) {
		String portalProfileEmail = null;
		PortalProfileFilterCriteria criteria = new PortalProfileFilterCriteria();
		criteria.setStatus(USER_PERSONA_STATUS_ACTIVE);
		
		try {
			if (ban < 0) {
				throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ErrorCodes.BAN_NOT_FOUND, "Invalid BAN: " + ban);
			}
			
			List<PortalProfile> portalProfileList = new ArrayList<PortalProfile>();
			
			if (CollectionUtils.isNotEmpty(portalProfiles)) {
				// This response sometimes gives us extra info, let's filter out all the profiles with non-matching personas
				for (PortalProfile portalProfile : portalProfiles) {
					if (portalProfile.hasMatchingPersonaToBan(String.valueOf(ban))) {
						portalProfileList.add(portalProfile);
					}
				}
			}
			
			for (PortalProfile portalProfile : portalProfileList) {
				for (Persona persona : portalProfile.getPersonaList()) {
					if (StringUtils.equalsIgnoreCase(persona.getRole(), USER_PERSONA_ROLE_OWNER) && !StringUtils.isBlank(portalProfile.getUuidEmail())
							&& StringUtils.equals(persona.getBan(), String.valueOf(ban))) {
						return portalProfile.getUuidEmail();
					} else if (StringUtils.equalsIgnoreCase(persona.getRole(), USER_PERSONA_ROLE_MANAGER) && StringUtils.equals(persona.getBan(), String.valueOf(ban))) {
						portalProfileEmail = portalProfile.getUuidEmail();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return portalProfileEmail;
	}

	
	// Use data providers to simulate the response data of ProfileManagementSvc

	@DataProvider(name = "get_portalprofile_email_happy_path")
	public Object[][] testdata_get_portalprofile_email() {
		List<PortalProfile> portalProfiles = new ArrayList<PortalProfile>();
		
		PortalProfile portalProfileA = new PortalProfile();
		List<Persona> personaListA = new ArrayList<Persona>();
		portalProfileA.setUuidEmail("first.tester@telus.com");
		portalProfileA.setUuidStatus("ACTIVE");
		portalProfileA.setPersonaList(personaListA);
		
		Persona persona01 = new Persona();
		persona01.setPersonaId(1029746L);
		persona01.setRole("Manager");
		persona01.setBan("70778845");
		persona01.setStatus("ACTIVE");
		
		Persona persona02 = new Persona();
		persona02.setPersonaId(1029746L);
		persona02.setRole("Owner");
		persona02.setBan("0");
		persona02.setStatus("ACTIVE");
		
		personaListA.add(persona01);
		personaListA.add(persona02);
		
		PortalProfile portalProfileB = new PortalProfile();
		List<Persona> personaListB = new ArrayList<Persona>();
		portalProfileB.setUuidEmail("second.tester@telus.com");
		portalProfileB.setUuidStatus("ACTIVE");
		portalProfileB.setPersonaList(personaListB);
		
		Persona persona03 = new Persona();
		persona03.setPersonaId(2088538L);
		persona03.setRole("Manager");
		persona03.setBan("70778845");
		persona03.setStatus("ACTIVE");
		
		Persona persona04 = new Persona();
		persona04.setPersonaId(2088538L);
		persona04.setRole("Owner");
		persona04.setBan("70778845");
		persona04.setStatus("ACTIVE");
		
		personaListB.add(persona03);
		personaListB.add(persona04);
		
		portalProfiles.add(portalProfileA);
		portalProfiles.add(portalProfileB);
		
		System.out.println("##############");
		System.out.println(portalProfiles);
		return new Object[][] { { 70778845, portalProfiles } };
	}
	
	@DataProvider(name = "get_portalprofile_email_exception_case")
	public Object[][] testdata_get_portalprofile_email_exception_case() {
		List<PortalProfile> portalProfiles = new ArrayList<PortalProfile>();
		
		PortalProfile portalProfileA = new PortalProfile();
		List<Persona> personaList = new ArrayList<Persona>();
		portalProfileA.setUuidEmail("tester@telus.com");
		portalProfileA.setUuidStatus("ACTIVE");
		portalProfileA.setPersonaList(personaList);
		
		Persona persona01 = new Persona();
		persona01.setPersonaId(1038512L);
		persona01.setRole("Manager");
		persona01.setBan("0");
		persona01.setStatus("ACTIVE");
		
		Persona persona02 = new Persona();
		persona02.setPersonaId(1038516L);
		persona02.setRole("Owner");
		persona02.setBan("0");
		persona02.setStatus("ACTIVE");
		
		Persona persona03 = new Persona();
		persona03.setPersonaId(1038518L);
		persona03.setRole("Member");
		persona03.setBan("70778845");
		persona03.setStatus("ACTIVE");
		
		personaList.add(persona01);
		personaList.add(persona02);
		personaList.add(persona03);
		portalProfiles.add(portalProfileA);
		System.out.println("##############");
		System.out.println(portalProfiles);
		return new Object[][] { { 70778845, portalProfiles } };
	}
}
