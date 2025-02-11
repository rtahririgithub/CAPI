package com.telus.cmb.subscriber.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import amdocs.APILink.sessions.interfaces.NewCdpdConv;
import amdocs.APILink.sessions.interfaces.NewCellularConv;
import amdocs.APILink.sessions.interfaces.NewIdenConv;
import amdocs.APILink.sessions.interfaces.NewPagerConv;
import amdocs.APILink.sessions.interfaces.NewTangoConv;
import amdocs.APILink.sessions.interfaces.UpdateCdpdConv;
import amdocs.APILink.sessions.interfaces.UpdateCellularConv;
import amdocs.APILink.sessions.interfaces.UpdateIdenConv;
import amdocs.APILink.sessions.interfaces.UpdatePagerConv;
import amdocs.APILink.sessions.interfaces.UpdateTangoConv;

import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.Subscriber;

public class AmdocsConvBeanClassFactoryTest {

	@Test
	public void testGetNewProductConvBeanIden() {		
		assertEquals(NewIdenConv.class, AmdocsConvBeanClassFactory.getNewProductConvBean(Subscriber.PRODUCT_TYPE_IDEN));
	}
	
	@Test
	public void testGetNewProductConvBeanPager() {		
		assertEquals(NewPagerConv.class, AmdocsConvBeanClassFactory.getNewProductConvBean(Subscriber.PRODUCT_TYPE_PAGER));
	}
	
	@Test
	public void testGetNewProductConvBeanCdpd() {		
		assertEquals(NewCdpdConv.class, AmdocsConvBeanClassFactory.getNewProductConvBean(Subscriber.PRODUCT_TYPE_CDPD));
	}
	
	@Test
	public void testGetNewProductConvBeanPcs() {		
		assertEquals(NewCellularConv.class, AmdocsConvBeanClassFactory.getNewProductConvBean(Subscriber.PRODUCT_TYPE_PCS));
	}
	@Test
	public void testGetNewProductConvBeanTango() {		
		assertEquals(NewTangoConv.class, AmdocsConvBeanClassFactory.getNewProductConvBean(Subscriber.PRODUCT_TYPE_TANGO));
	}
	
	@Test
	public void testGetNewProductConvBeanInvalid() {
		try {
			AmdocsConvBeanClassFactory.getNewProductConvBean("INVALID_TYPE");
			fail("Exception expected.");
		} catch (SystemException se) {
			assertEquals(SystemCodes.CMB_EJB, se.getSystemCode());		
		}
	}

	@Test
	public void testGetUpdateProductConvBeanIden() {
		assertEquals(UpdateIdenConv.class, AmdocsConvBeanClassFactory.getUpdateProductConvBean(Subscriber.PRODUCT_TYPE_IDEN));
	}
	
	@Test
	public void testGetUpdateProductConvBeanPager() {		
		assertEquals(UpdatePagerConv.class, AmdocsConvBeanClassFactory.getUpdateProductConvBean(Subscriber.PRODUCT_TYPE_PAGER));
	}
	
	@Test
	public void testGetUpdateProductConvBeanCdpd() {		
		assertEquals(UpdateCdpdConv.class, AmdocsConvBeanClassFactory.getUpdateProductConvBean(Subscriber.PRODUCT_TYPE_CDPD));
	}
	
	@Test
	public void testGetUpdateProductConvBeanPcs() {		
		assertEquals(UpdateCellularConv.class, AmdocsConvBeanClassFactory.getUpdateProductConvBean(Subscriber.PRODUCT_TYPE_PCS));
	}
	@Test
	public void testGetUpdateProductConvBeanTango() {		
		assertEquals(UpdateTangoConv.class, AmdocsConvBeanClassFactory.getUpdateProductConvBean(Subscriber.PRODUCT_TYPE_TANGO));
	}
	
	@Test
	public void testGetUpdateProductConvBeanInvalid() {
		try {
			AmdocsConvBeanClassFactory.getUpdateProductConvBean("INVALID_TYPE");
			fail("Exception expected.");
		} catch (SystemException se) {
			assertEquals(SystemCodes.CMB_EJB, se.getSystemCode());		
		}
	}


}
