/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.erm.referenceods.domain.ReferenceDecode;
import com.telus.erm.referenceods.domain.RuleOutput;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;

/**
 * @author R. Fong
 *
 */

@Test
@ContextConfiguration(locations="classpath:application-context-test.xml")
//@ActiveProfiles({"remote","lab"})
@ActiveProfiles("standalone")
//@ActiveProfiles({"remote","pt168"})
public class ReferenceDataRefPDSTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Test
	public void getXRefDecodes() throws Exception {
		
		String xrefName = "CONTACT_ROLE";
		String code = "ACCTOWNR";
		
		Collection<String> coll = ReferencePdsAccess.getAllReferenceDecodes(xrefName, ReferencePdsAccess.LANG_EN);
		System.out.print(ToStringBuilder.reflectionToString(coll));
		
		coll = ReferencePdsAccess.getAllXrefDecodes(xrefName, code, ReferencePdsAccess.LANG_EN);
		System.out.print(ToStringBuilder.reflectionToString(coll));
		
		coll = ReferencePdsAccess.getReferenceDecodes(xrefName, ReferencePdsAccess.LANG_EN);
		System.out.print(ToStringBuilder.reflectionToString(coll));

		coll = ReferencePdsAccess.getView(xrefName, ReferencePdsAccess.LANG_EN);
		System.out.print(ToStringBuilder.reflectionToString(coll));
		
		String text = ReferencePdsAccess.getDecodeText(xrefName, ReferencePdsAccess.LANG_EN, code);
		System.out.print(text);
	}
	
	@Test
	public void getDecodes() throws Exception {
		
		String xrefName = "PROVINCE_STATE_1";
		String currCode = "NL";
		Map<String, String> map = ReferencePdsAccess.getDecodes(xrefName, ReferencePdsAccess.LANG_EN, currCode);
		System.out.print(ToStringBuilder.reflectionToString(map));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getAllReferenceDecodes() throws Exception {

		Collection<ReferenceDecode> decodeList = ReferencePdsAccess.getAllReferenceDecodes("CDA_BILLING_ACCOUNT_TYP_SUBTYP", ReferencePdsAccess.LANG_EN);
		for (ReferenceDecode decode : decodeList) {
			System.out.print(ToStringBuilder.reflectionToString(decode));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void evaluateRule() throws Exception {

		Collection<ReferenceDecode> decodeList = ReferencePdsAccess.getAllReferenceDecodes("BUSINESS_CONNECT_EDITION", ReferencePdsAccess.LANG_EN);
		for (ReferenceDecode decode : decodeList) {
			HashMap<String, String> criteria = new HashMap<String, String>();
			criteria.put("SERVICE_EDITION_CD", decode.getCode());
			RuleOutput ruleOutput = ReferencePdsAccess.evaluateRule("BUSINESS_CONNECT_EDITION_RULE_V2", criteria);
			System.out.print(ToStringBuilder.reflectionToString(ruleOutput));
		}
	}

}