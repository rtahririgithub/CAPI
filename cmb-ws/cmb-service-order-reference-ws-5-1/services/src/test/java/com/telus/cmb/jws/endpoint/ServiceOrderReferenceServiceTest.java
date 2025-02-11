package com.telus.cmb.jws.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.endpoint.ServiceOrderReferenceService;

@ContextConfiguration(locations = "classpath:application-context-test.xml")
public class ServiceOrderReferenceServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ServiceOrderReferenceService endpoint;
	
	@Test
	public void testGetCommitmentReasonList() throws Exception {
	}

	@Test
	public void testGetCommitmentReason() throws Exception {
	}
	
	@Test
	public void getSeatTypeList() throws Exception {
	}
	
	@Test
	public void getCalculatedEffectiveAirTimeAllocationList() throws Exception {
	}
	
}
