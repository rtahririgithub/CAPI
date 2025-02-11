package com.telus.cmb.subscriber.lifecyclemanager.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.api.account.Subscriber;
import com.telus.cmb.subscriber.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.NewCdpdSubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.NewIdenSubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.NewPagerSubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.NewPcsSubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.NewTangoSubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.SubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.UpdateCdpdSubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.UpdateIdenSubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.UpdatePagerSubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.UpdatePcsSubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.UpdateTangoSubscriberDaoImpl;

@RunWith(SpringJUnit4ClassRunner.class)

public class SubscriberDaoFactoryTest extends BaseLifecycleManagerIntTest {
	
	@Autowired
	SubscriberDaoFactory daoFactory;

	@Test
	public void testGetNewIdenSubscriberDao() throws ApplicationException {
		NewSubscriberDao newSubscriberDao = daoFactory.getNewSubscriberDao(Subscriber.PRODUCT_TYPE_IDEN);
		assertEquals(NewIdenSubscriberDaoImpl.class, newSubscriberDao.getClass());
		
	}
	
	@Test
	public void testGetNewCdpdSubscriberDao() throws ApplicationException {
		assertEquals(NewCdpdSubscriberDaoImpl.class, daoFactory.getNewSubscriberDao(Subscriber.PRODUCT_TYPE_CDPD).getClass());
	}
	
	@Test
	public void testGetNewPcsSubscriberDao() throws ApplicationException {
		assertEquals(NewPcsSubscriberDaoImpl.class, daoFactory.getNewSubscriberDao(Subscriber.PRODUCT_TYPE_PCS).getClass());
	}
	
	@Test
	public void testGetNewPagerSubscriberDao() throws ApplicationException {
		assertEquals(NewPagerSubscriberDaoImpl.class, daoFactory.getNewSubscriberDao(Subscriber.PRODUCT_TYPE_PAGER).getClass());
	}
	
	@Test
	public void testGetNewTangoSubscriberDao() throws ApplicationException {
		assertEquals(NewTangoSubscriberDaoImpl.class, daoFactory.getNewSubscriberDao(Subscriber.PRODUCT_TYPE_TANGO).getClass());
	}
	
	@Test
	public void testGetNewSubscriberDaoExceptionExpected() throws ApplicationException {
		try {
			daoFactory.getNewSubscriberDao("321321");
			fail("Exception expected.");
		} catch (ApplicationException e) {
			
		}		
	}

	@Test
	public void testGetUpdateIdenSubscriberDao() throws ApplicationException {
		assertEquals(UpdateIdenSubscriberDaoImpl.class, daoFactory.getUpdateSubscriberDao(Subscriber.PRODUCT_TYPE_IDEN).getClass());
	}
	
	@Test
	public void testGetUpdateCdpdSubscriberDao() throws ApplicationException {
		assertEquals(UpdateCdpdSubscriberDaoImpl.class, daoFactory.getUpdateSubscriberDao(Subscriber.PRODUCT_TYPE_CDPD).getClass());
	}
	
	@Test
	public void testGetUpdatePcsSubscriberDao() throws ApplicationException {
		UpdateSubscriberDao updateSubscriberDao = daoFactory.getUpdateSubscriberDao(Subscriber.PRODUCT_TYPE_PCS);
		assertEquals(UpdatePcsSubscriberDaoImpl.class, updateSubscriberDao.getClass());
	}
	
	@Test
	public void testGetUpdatePagerSubscriberDao() throws ApplicationException {
		assertEquals(UpdatePagerSubscriberDaoImpl.class, daoFactory.getUpdateSubscriberDao(Subscriber.PRODUCT_TYPE_PAGER).getClass());
	}
	
	@Test
	public void testGetUpdateTangoSubscriberDao() throws ApplicationException {
		assertEquals(UpdateTangoSubscriberDaoImpl.class, daoFactory.getUpdateSubscriberDao(Subscriber.PRODUCT_TYPE_TANGO).getClass());
	}
	

	@Test
	public void testgetUpdateSubscriberDaoExceptionExpected() throws ApplicationException {
		try {
			daoFactory.getUpdateSubscriberDao("321321");
			fail("Exception expected.");
		} catch (ApplicationException e) {
			
		}		
	}
	
	@Test
	public void testGetUpdateIdenIdenPcsSubscriberDao() throws ApplicationException {
		assertEquals(UpdateIdenSubscriberDaoImpl.class, daoFactory.getUpdateIdenPcsSubscriberDao(Subscriber.PRODUCT_TYPE_IDEN).getClass());
	}
	
	@Test
	public void testGetUpdatePcsIdenPcsSubscriberDao() throws ApplicationException {
		assertEquals(UpdatePcsSubscriberDaoImpl.class, daoFactory.getUpdateIdenPcsSubscriberDao(Subscriber.PRODUCT_TYPE_PCS).getClass());
	}
	
	@Test
	public void testgetUpdateIdenPcsSubscriberDaoExceptionExpected() throws ApplicationException {
		try {
			daoFactory.getUpdateIdenPcsSubscriberDao("321321");
			fail("Exception expected.");
		} catch (ApplicationException e) {
			
		}		
	}
	
	@Test
	public void testGetNewIdenIdenPcsSubscriberDao() throws ApplicationException {
		assertEquals(NewIdenSubscriberDaoImpl.class, daoFactory.getNewIdenPcsSubscriberDao(Subscriber.PRODUCT_TYPE_IDEN).getClass());
	}
	
	@Test
	public void testGetNewPcsIdenPcsSubscriberDao() throws ApplicationException {
		assertEquals(NewPcsSubscriberDaoImpl.class, daoFactory.getNewIdenPcsSubscriberDao(Subscriber.PRODUCT_TYPE_PCS).getClass());
	}
	
	@Test
	public void testGetNewIdenPcsSubscriberDaoExceptionExpected() throws ApplicationException {
		try {
			daoFactory.getNewIdenPcsSubscriberDao("321321");
			fail("Exception expected.");
		} catch (ApplicationException e) {
			
		}		
	}
	
	@Test
	public void testGetNewPcsSubscriberDaoNonSwitch() {
		assertEquals(NewPcsSubscriberDaoImpl.class, daoFactory.getNewPcsSubscriberDao().getClass());		
	}
	
	@Test
	public void testGetNewIdenSubscriberDaoNonSwitch() {
		assertEquals(NewIdenSubscriberDaoImpl.class, daoFactory.getNewIdenSubscriberDao().getClass());		
	}
	
	@Test
	public void testGetSubscriberDao() {
		assertEquals(SubscriberDaoImpl.class, daoFactory.getSubscriberDao().getClass());
	}

}
