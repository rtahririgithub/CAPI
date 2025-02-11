package com.telus.cmb.productequipment.manager.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.telus.api.ApplicationException;
import com.telus.cmb.productequipment.EquipmentErrorMessages;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.productequipment.manager.dao.CardManagerDao;
import com.telus.cmb.productequipment.manager.dao.EquipmentManagerDao;
import com.telus.eas.equipment.info.EquipmentInfo;

public class ProductEquipmentManagerImplTest {

	

	ProductEquipmentManagerImpl mockedEquipmentManagerImpl;
	
	@Mock
	private ProductEquipmentHelper mockedEquipmentHelper;
	
	@Mock
	private CardManagerDao mockedCardManagerDao;	

	@Mock
	private EquipmentManagerDao mockedEquipmentManagerDao;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockedEquipmentManagerImpl = new ProductEquipmentManagerImpl();
		mockedEquipmentManagerImpl.setCardDao(mockedCardManagerDao);	
		mockedEquipmentManagerImpl.setEquipmentDao(mockedEquipmentManagerDao);
		mockedEquipmentManagerImpl.setProductEquipmentHelper(mockedEquipmentHelper);
	}	
	
	
	@Test
	public void testInsertAnalogEquipment_flow1() throws ApplicationException{
		//Positive flow
		String pSerialNo="12345678901";
		String pUserID="user";
		EquipmentInfo equipmentInfo=new EquipmentInfo();
		equipmentInfo.setSerialNumber(pSerialNo);
		
		when(mockedEquipmentHelper.isValidESNPrefix(pSerialNo)).thenReturn(true);
		when(mockedEquipmentHelper.isVirtualESN(pSerialNo)).thenReturn(false);
		when(mockedEquipmentHelper.getEquipmentInfobySerialNumber(pSerialNo)).thenReturn(equipmentInfo);
		when(mockedEquipmentHelper.getESNByPseudoESN(pSerialNo)).thenReturn(new String[0]);
		
		mockedEquipmentManagerImpl.insertAnalogEquipment(pSerialNo, pUserID);
		
		verify(mockedEquipmentManagerDao).insertAnalogEquipment(pSerialNo, pUserID);
		
		
	}
	
	@Test
	public void testInsertAnalogEquipment_flow2(){
		//Negative flow
		String pSerialNo="123";
		
		// test1 - Invalid Serial Number Manufacturer Prefix
		try{
		    when(mockedEquipmentHelper.isValidESNPrefix(pSerialNo)).thenReturn(false);
		}catch(ApplicationException e){
			assertEquals(EquipmentErrorMessages.UNKNOWN_SERIAL_NUMBER_MANUFACTURER, e.getMessage());
		}
		
		// test2- Invalid Serial Number
		try{
			when(mockedEquipmentHelper.isVirtualESN(pSerialNo)).thenReturn(true);
		}catch(ApplicationException e){
			assertEquals(EquipmentErrorMessages.INVALID_SERAIL_NUMBER_EN, e.getMessage());
		}
		
		// test3- Invalid Serial Number
		try{
			EquipmentInfo equipmentInfo=new EquipmentInfo();
			equipmentInfo.setSerialNumber("1234567890");
			when(mockedEquipmentHelper.getEquipmentInfobySerialNumber(pSerialNo)).thenReturn(equipmentInfo);
		}catch(ApplicationException e){
			assertEquals(EquipmentErrorMessages.INVALID_SERAIL_NUMBER_EN, e.getMessage());
		}
		
		// test4 - Invalid Serial Number
		try{
			when(mockedEquipmentHelper.getESNByPseudoESN(pSerialNo)).thenReturn(new String[0]);
		}catch(ApplicationException e){
			assertEquals(EquipmentErrorMessages.INVALID_SERAIL_NUMBER_EN, e.getMessage());
		}
	}
	
	
	@Test
	public void testSetCardStatus_flow1() throws ApplicationException{
		//Positive flow
		String pSerialNo="12345678901";
		String pUser="user";
		int pStatusId=101;
		int currentCardStatus=102;
		
		when(mockedCardManagerDao.getCardStatus(pSerialNo)).thenReturn(currentCardStatus);

		mockedEquipmentManagerImpl.setCardStatus(pSerialNo, pStatusId, pUser);
		
		verify(mockedCardManagerDao).getCardStatus(pSerialNo);
		verify(mockedCardManagerDao).setCardStatus(pSerialNo, pStatusId, pUser,  0, null, null, false);
		
	}
	
	@Test
	public void testSetCardStatus_flow2() {
		// Negative flow
		String pSerialNo="123";
		String pUser="user";
		int pStatusId=100;
		
		
		// test1 - status id invalid
		try{
			mockedEquipmentManagerImpl.setCardStatus(pSerialNo, pStatusId, pUser);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.INVALID_CARD_STATUS_EN, ae.getErrorMessage());
		}
		
		// test2 - User id null check
		try{
			 pStatusId=101;
			 pUser=null;
			mockedEquipmentManagerImpl.setCardStatus(pSerialNo, pStatusId, pUser);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.UNKNOWN_USER_ID_EN, ae.getErrorMessage());
		}
		
		// test3 - Serial number null check 
		try{
			 pUser="user";
			 pSerialNo=null;
			mockedEquipmentManagerImpl.setCardStatus(pSerialNo, pStatusId, pUser);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.SERIAL_NUMBER_NULL_EN, ae.getErrorMessage());
		}
		
		// test4- Serial number length check
		try{
			 pSerialNo="1";
			mockedEquipmentManagerImpl.setCardStatus(pSerialNo, pStatusId, pUser);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.INVALID_SERAIL_NUMBER_LENGTH_EN, ae.getErrorMessage());
		}
		
		// test5 - validation check failed
		try{
			 pSerialNo="12345678901";
			 pStatusId=102;
			mockedEquipmentManagerImpl.setCardStatus(pSerialNo, pStatusId, pUser);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.VALIDATAION_FAILED_EN, ae.getErrorMessage());
		}
		
	}
	
	@Test
	public void testCreditCard_flow1() throws ApplicationException{
		//Positive flow 
		String pSerialNo="10000011909";
		String pUser="clientapi";
		int ban=8285849;
		String phoneNumber="2507105183";
		String equipmentSerialNo="24700260123";
		boolean autoRenewInd=true;
		int pStatusId=109;
		int currentCardStatus=102;
		
		when(mockedCardManagerDao.getCardStatus(pSerialNo)).thenReturn(currentCardStatus);

		mockedEquipmentManagerImpl.creditCard(pSerialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, pUser);
		
		verify(mockedCardManagerDao).getCardStatus(pSerialNo);
		verify(mockedCardManagerDao).setCardStatus(pSerialNo, pStatusId, pUser, ban, phoneNumber, equipmentSerialNo, autoRenewInd);
		
		
	}
	
	@Test
	public void testCreditCard_flow2() throws ApplicationException{
		//Negative flow
		String serialNo=null;
		String user="clientapi";
		int ban=234;
		String phoneNumber="1234";
		String equipmentSerialNo="1234";
		boolean autoRenewInd=true;
		
		
		// test1 - serial number null check 
		try{
			mockedEquipmentManagerImpl.creditCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.SERIAL_NUMBER_NULL_EN, ae.getErrorMessage());
		}
		
		// test2 - serial number length check 
		try{
			serialNo="1";
			mockedEquipmentManagerImpl.creditCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.INVALID_SERAIL_NUMBER_LENGTH_EN, ae.getErrorMessage());
		}
		
		// test3 - user id null check 
		try{
			 serialNo="01234567890";
			 user=null;
			mockedEquipmentManagerImpl.creditCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.UNKNOWN_USER_ID_EN, ae.getErrorMessage());
		}
		
		// test4 - Phone Number null check
		try{
			 user="user";
			 phoneNumber=null;
			 mockedEquipmentManagerImpl.creditCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN, ae.getErrorMessage());
		}
		
		// test5- Equipment serial number null check
		try{
			equipmentSerialNo=null;
			phoneNumber="1234";
			mockedEquipmentManagerImpl.creditCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.EQUIPMENT_SERIAL_NUMBER_NULL_EN, ae.getErrorMessage());
		}
		// test6- Validation failed
		try{
			equipmentSerialNo="1234";
			phoneNumber="1234";
			mockedEquipmentManagerImpl.creditCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.VALIDATAION_FAILED_EN, ae.getErrorMessage());
		}
		
	}
	
	@Test
	public void testActivateCard_flow1() throws ApplicationException{
		
		//Positive flow 
		String serialNo="12345678901";
		String user="clientapi";
		int ban=8285849;
		String phoneNumber="1234";
		String equipmentSerialNo="1234";
		boolean autoRenewInd=true;
		int pStatusId=103;
		int currentCardStatus=102;
		
		when(mockedCardManagerDao.getCardStatus(serialNo)).thenReturn(currentCardStatus);

		mockedEquipmentManagerImpl.activateCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		
		verify(mockedCardManagerDao).getCardStatus(serialNo);
		verify(mockedCardManagerDao).setCardStatus(serialNo, pStatusId, user, ban, phoneNumber, equipmentSerialNo, autoRenewInd);
	}
	
	@Test
	public void testActivateCard_flow2() throws ApplicationException{
		//Negative flow
		String serialNo=null;
		String user="clientapi";
		int ban=234;
		String phoneNumber="1234";
		String equipmentSerialNo="1234";
		boolean autoRenewInd=true;
		
		
		// test1 - serial number null check 
		try{
			mockedEquipmentManagerImpl.activateCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.SERIAL_NUMBER_NULL_EN, ae.getErrorMessage());
		}
		
		// test2 - serial number length check 
		try{
			serialNo="1";
			mockedEquipmentManagerImpl.activateCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.INVALID_SERAIL_NUMBER_LENGTH_EN, ae.getErrorMessage());
		}
		
		// test3 - user id null check 
		try{
			 serialNo="01234567890";
			 user=null;
			mockedEquipmentManagerImpl.activateCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.UNKNOWN_USER_ID_EN, ae.getErrorMessage());
		}
		
		// test4 - Phone Number null check
		try{
			 user="user";
			 phoneNumber=null;
			 mockedEquipmentManagerImpl.activateCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN, ae.getErrorMessage());
		}
		
		// test5- Equipment serial number null check
		try{
			phoneNumber="1234323";
			equipmentSerialNo=null;
			mockedEquipmentManagerImpl.activateCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals( EquipmentErrorMessages.EQUIPMENT_SERIAL_NUMBER_NULL_EN, ae.getErrorMessage());
		}
		// test6- Validation failed
		try{
			equipmentSerialNo="1234";
			phoneNumber="1234";
			mockedEquipmentManagerImpl.activateCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
		}catch(ApplicationException ae){
			assertEquals(EquipmentErrorMessages.VALIDATAION_FAILED_EN, ae.getErrorMessage());
		}
		
	}
	
	
	@Test
	public void testGetMasterLockbySerialNo1() throws ApplicationException{
		//Positive flow 
		when(mockedEquipmentManagerDao.getMasterLockbySerialNo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(new String());
		when(mockedEquipmentManagerImpl.getMasterLockbySerialNo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(new String());
	}
	
	@Test
	public void testGetMasterLockbySerialNo2() throws ApplicationException{
		//Positive flow 
		when(mockedEquipmentManagerDao.getMasterLockbySerialNo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(new String());
		when(mockedEquipmentManagerImpl.getMasterLockbySerialNo(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(new String());
	}
	
	@Test
	public void testInsertPagerEquipment() throws ApplicationException{
		
		mockedEquipmentManagerImpl.insertPagerEquipment(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		verify(mockedEquipmentManagerDao).insertPagerEquipment(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void testUpdateStatus() throws ApplicationException{
		mockedEquipmentManagerImpl.updateStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());
		verify(mockedEquipmentManagerDao).updateStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());
	}
	
	@Test
	public void testSetSIMMule() throws ApplicationException{
		mockedEquipmentManagerImpl.setSIMMule(Mockito.anyString(), Mockito.anyString(), Mockito.any(Date.class), Mockito.anyString());
		verify(mockedEquipmentManagerDao).setSIMMule(Mockito.anyString(), Mockito.anyString(), Mockito.any(Date.class), Mockito.anyString());
	}
	@Test
	public void testStartSIMMuleRelation() throws ApplicationException{
		mockedEquipmentManagerImpl.startSIMMuleRelation("sim", new Date());
		verify(mockedEquipmentManagerDao).setSIMMule(Mockito.anyString(), Mockito.anyString(), Mockito.any(Date.class), Mockito.anyString());
	}
	
	@Test
	public void testActivateSIMMule() throws ApplicationException{
		mockedEquipmentManagerImpl.activateSIMMule("sim", "mule", new Date());
		verify(mockedEquipmentManagerDao).setSIMMule(Mockito.anyString(), Mockito.anyString(), Mockito.any(Date.class), Mockito.anyString());
	}
	
}
