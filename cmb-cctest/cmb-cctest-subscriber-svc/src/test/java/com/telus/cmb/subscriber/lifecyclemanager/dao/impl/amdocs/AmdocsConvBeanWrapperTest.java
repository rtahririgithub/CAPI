package com.telus.cmb.subscriber.lifecyclemanager.dao.impl.amdocs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.NewCdpdConv;
import amdocs.APILink.sessions.interfaces.NewCellularConv;
import amdocs.APILink.sessions.interfaces.NewIdenConv;
import amdocs.APILink.sessions.interfaces.NewPagerConv;
import amdocs.APILink.sessions.interfaces.NewTangoConv;
import amdocs.APILink.sessions.interfaces.UpdateCdpdConv;
import amdocs.APILink.sessions.interfaces.UpdateCellularConv;
import amdocs.APILink.sessions.interfaces.UpdatePagerConv;
import amdocs.APILink.sessions.interfaces.UpdateTangoConv;

import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.utility.info.NumberGroupInfo;

public class AmdocsConvBeanWrapperTest {

	
	@Before
	public void setup() {
		 MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetAvailablePhoneNumbersNewCellularConvBean() throws RemoteException, ValidateException {
		
		NewCellularConv newCellularConvMocked = Mockito.mock(NewCellularConv.class);
		
		PhoneNumberReservation phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setAsian(false);
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		numberGroup.setCode("321");
		phoneNumberReservation.setNumberGroup(numberGroup);
		
		AmdocsConvBeanWrapper.getAvailablePhoneNumbers(newCellularConvMocked, phoneNumberReservation, "abc", "123");
		
		Mockito.verify(newCellularConvMocked, Mockito.times(1)).getAvailableSubscriberList("321", "123", "abc", false);		
	}
	
	@Test
	public void testGetAvailablePhoneNumbersUpdateCellularConvBean() throws RemoteException, ValidateException {
		UpdateCellularConv updateCellularConvMocked = Mockito.mock(UpdateCellularConv.class);
		
		PhoneNumberReservation phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setAsian(false);
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		numberGroup.setCode("321");
		phoneNumberReservation.setNumberGroup(numberGroup);
		
		AmdocsConvBeanWrapper.getAvailablePhoneNumbers(updateCellularConvMocked, phoneNumberReservation, "abc", "123");
		
		Mockito.verify(updateCellularConvMocked, Mockito.times(1)).getAvailableSubscriberList("321", "123", "abc", false);		
	}
	
	@Test
	public void testGetAvailablePhoneNumbersNewCdpdConvBean() throws RemoteException, ValidateException {		
		NewCdpdConv newConvMocked = Mockito.mock(NewCdpdConv.class);
		
		PhoneNumberReservation phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setAsian(false);
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		numberGroup.setCode("321");
		phoneNumberReservation.setNumberGroup(numberGroup);
		
		AmdocsConvBeanWrapper.getAvailablePhoneNumbers(newConvMocked, phoneNumberReservation, "abc", "123");
		
		Mockito.verify(newConvMocked, Mockito.times(1)).getAvailableSubscriberList("321", "123", "abc", false);		
	}
	
	@Test
	public void testGetAvailablePhoneNumbersUpdateCdpdConvBean() throws RemoteException, ValidateException {
		UpdateCdpdConv updateConvMocked = Mockito.mock(UpdateCdpdConv.class);
		
		PhoneNumberReservation phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setAsian(false);
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		numberGroup.setCode("321");
		phoneNumberReservation.setNumberGroup(numberGroup);
		
		AmdocsConvBeanWrapper.getAvailablePhoneNumbers(updateConvMocked, phoneNumberReservation, "abc", "123");
		
		Mockito.verify(updateConvMocked, Mockito.times(1)).getAvailableSubscriberList("321", "123", "abc", false);		
	}
	
	@Test
	public void testGetAvailablePhoneNumbersNewTangoConvBean() throws RemoteException, ValidateException {		
		NewTangoConv newConvMocked = Mockito.mock(NewTangoConv.class);
		
		PhoneNumberReservation phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setAsian(false);
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		numberGroup.setCode("321");
		phoneNumberReservation.setNumberGroup(numberGroup);
		
		AmdocsConvBeanWrapper.getAvailablePhoneNumbers(newConvMocked, phoneNumberReservation, "abc", "123");
		
		Mockito.verify(newConvMocked, Mockito.times(1)).getAvailableSubscriberList("321", "123", "abc", false);		
	}
	
	@Test
	public void testGetAvailablePhoneNumbersUpdateTangoConvBean() throws RemoteException, ValidateException {
		UpdateTangoConv updateConvMocked = Mockito.mock(UpdateTangoConv.class);
		
		PhoneNumberReservation phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setAsian(false);
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		numberGroup.setCode("321");
		phoneNumberReservation.setNumberGroup(numberGroup);
		
		AmdocsConvBeanWrapper.getAvailablePhoneNumbers(updateConvMocked, phoneNumberReservation, "abc", "123");
		
		Mockito.verify(updateConvMocked, Mockito.times(1)).getAvailableSubscriberList("321", "123", "abc", false);		
	}	
	
	@Test
	public void testGetAvailablePhoneNumbersNewPagerConvBean() throws RemoteException, ValidateException {		
		NewPagerConv newConvMocked = Mockito.mock(NewPagerConv.class);
		
		PhoneNumberReservation phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setAsian(false);
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		numberGroup.setCode("321");
		phoneNumberReservation.setNumberGroup(numberGroup);
		
		AmdocsConvBeanWrapper.getAvailablePhoneNumbers(newConvMocked, phoneNumberReservation, "abc", "123");
		
		Mockito.verify(newConvMocked, Mockito.times(1)).getAvailableSubscriberList("321", "123", "abc", false);		
	}
	
	@Test
	public void testGetAvailablePhoneNumbersUpdatePagerConvBean() throws RemoteException, ValidateException {
		UpdatePagerConv updateConvMocked = Mockito.mock(UpdatePagerConv.class);
		
		PhoneNumberReservation phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setAsian(false);
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		numberGroup.setCode("321");
		phoneNumberReservation.setNumberGroup(numberGroup);
		
		AmdocsConvBeanWrapper.getAvailablePhoneNumbers(updateConvMocked, phoneNumberReservation, "abc", "123");
		
		Mockito.verify(updateConvMocked, Mockito.times(1)).getAvailableSubscriberList("321", "123", "abc", false);		
	}	
	
	@Test
	public void testInvalidObject() throws RemoteException, ValidateException {
		NewIdenConv newConvMocked = Mockito.mock(NewIdenConv.class);
		
		PhoneNumberReservation phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setAsian(false);
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		numberGroup.setCode("321");
		phoneNumberReservation.setNumberGroup(numberGroup);
		
		try {
			AmdocsConvBeanWrapper.getAvailablePhoneNumbers(newConvMocked, phoneNumberReservation, null, null);
			fail("SystemException expected.");
		} catch (SystemException e){
			assertEquals(SystemCodes.CMB_SLM_DAO, e.getSystemCode());
		}
	}

}
