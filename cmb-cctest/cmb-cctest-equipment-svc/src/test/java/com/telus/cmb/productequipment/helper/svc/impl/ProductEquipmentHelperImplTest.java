package com.telus.cmb.productequipment.helper.svc.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import java.util.Date;
import java.util.HashMap;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.telus.api.ApplicationException;
import com.telus.cmb.productequipment.EquipmentErrorMessages;
import com.telus.cmb.productequipment.helper.dao.CardHelperDao;
import com.telus.cmb.productequipment.helper.dao.CreditAndPricingHelperDao;
import com.telus.cmb.productequipment.helper.dao.EquipmentHelperDao;
import com.telus.cmb.productequipment.helper.dao.impl.CreditAndPricingHelperDaoImpl;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.EquipmentModeInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.framework.info.ActivationCreditInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class ProductEquipmentHelperImplTest {

	ProductEquipmentHelperImpl  mockedEquipmentHelperImpl;
	
	@Mock
	private CreditAndPricingHelperDao  mockedCreditAndPricingDao;
		
	@Mock
	private EquipmentHelperDao  mockedEquipmentDao;
	
//	@Mock
//	private PrepaidHelperDao  mockedPrepaidDao;

	@Mock
	private CardHelperDao  mockedCardDao;
	
	@Mock 
	private ActivationCreditInfo activationCreditInfo;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockedEquipmentHelperImpl=new ProductEquipmentHelperImpl();
		
		mockedEquipmentHelperImpl.setCreditAndPricingDao(mockedCreditAndPricingDao);
		mockedEquipmentHelperImpl.setEquipmentDao(mockedEquipmentDao);
		//mockedEquipmentHelperImpl.setPrepaidHelperDao(mockedPrepaidDao);
		mockedEquipmentHelperImpl.setCardDao(mockedCardDao);
		
		
	}
	@Test
	public void testGetEquipmentInfobySerialNo() throws ApplicationException {
		
		 EquipmentInfo equipmentInfo=new EquipmentInfo();
		 equipmentInfo.setProductGroupTypeID(10);
		 equipmentInfo.setProductTypeID(11);
		 
		 when(mockedCreditAndPricingDao.retrieveContractTermCredits(equipmentInfo.getProductGroupTypeID(),equipmentInfo.getProductTypeID())).thenReturn(new double[4]);
		 when(mockedCreditAndPricingDao.getEquipmentInfobySerialNo(Mockito.anyString())).thenReturn(equipmentInfo);
		 when(mockedEquipmentDao.getSubscriberByEquipment(equipmentInfo)).thenReturn(equipmentInfo);
		
		 equipmentInfo=mockedEquipmentHelperImpl.getEquipmentInfobySerialNo(Mockito.anyString());
		 assertEquals(equipmentInfo.getContractTermCredits().length, 4);
		
	}
	
	
	@Test
	public void testGetEquipmentInfobySerialNumber() throws ApplicationException {
		
		when(mockedCreditAndPricingDao.getEquipmentInfobySerialNo(Mockito.anyString())).thenReturn(new EquipmentInfo());
		when(mockedEquipmentHelperImpl.getEquipmentInfobySerialNumber(Mockito.anyString())).thenReturn(new EquipmentInfo());
		
	}
	
	@Test
	public void testGetESNByPseudoESN() throws ApplicationException {
		
		when(mockedCreditAndPricingDao.getESNByPseudoESN(Mockito.anyString())).thenReturn(new String[0]);
		when(mockedEquipmentHelperImpl.getESNByPseudoESN(Mockito.anyString())).thenReturn(new String[0]);
		
	}
	
	@Test
	public void testIsValidESNPrefix() throws ApplicationException {
		
		when(mockedEquipmentDao.isValidESNPrefix(Mockito.anyString())).thenReturn(true);
		when(mockedEquipmentHelperImpl.isValidESNPrefix(Mockito.anyString())).thenReturn(true);
	}
	
	
	@Test
	public void testIsVirtualESN() throws ApplicationException {
		
		when(mockedEquipmentHelperImpl.isVirtualESN(Mockito.anyString())).thenReturn(true);
		when(mockedEquipmentDao.isVirtualESN(Mockito.anyString())).thenReturn(true);
	}
	
//	@Test
//	public void testGetAirCardByCardNo_flow1() throws ApplicationException{
//		//positive
//		when(mockedEquipmentHelperImpl.getAirCardByCardNo("012345678911111","phoneno", "serailno","user")).thenReturn(new CardInfo());
//		when(mockedPrepaidDao.getAirCardByCardNo(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(new CardInfo());
//		
//	}
	@Test
	public void testGetAirCardByCardNo_flow2() throws ApplicationException{
		//Negative flow
		try{
			when(mockedEquipmentHelperImpl.getAirCardByCardNo(null,"","","")).thenReturn(new CardInfo());
		}catch(ApplicationException e){
			assertEquals( EquipmentErrorMessages.CARD_NUMBER_NULL_EN, e.getErrorMessage());
		}
		
		try{
			when(mockedEquipmentHelperImpl.getAirCardByCardNo("","","","")).thenReturn(new CardInfo());
		}catch(ApplicationException e){
			assertEquals(EquipmentErrorMessages.INVALID_CARD_NUMBER_LENGTH_EN, e.getErrorMessage());
		}
		
		try{
			when(mockedEquipmentHelperImpl.getAirCardByCardNo("121212121212121","","",null)).thenReturn(new CardInfo());
		}catch(ApplicationException e){
			assertEquals(EquipmentErrorMessages.UNKNOWN_USER_ID_EN, e.getErrorMessage());
		}
		try{
			when(mockedEquipmentHelperImpl.getAirCardByCardNo("121212121212121",null,"","")).thenReturn(new CardInfo());
		}catch(ApplicationException e){
			assertEquals(EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN, e.getErrorMessage());
		}
	}
	
		@Test
		public void testGetAssociatedHandsetByUSIMID() throws ApplicationException{
			 EquipmentInfo equipmentInfo=new EquipmentInfo();
			 equipmentInfo.setProductGroupTypeID(10);
			 equipmentInfo.setProductTypeID(11);
			
			 when(mockedCreditAndPricingDao.retrieveContractTermCredits(equipmentInfo.getProductGroupTypeID(),equipmentInfo.getProductTypeID())).thenReturn(new double[4]);
			 when(mockedEquipmentDao.getAssociatedHandsetByUSIMID(Mockito.anyString())).thenReturn(equipmentInfo);
			
			 equipmentInfo=mockedEquipmentHelperImpl.getAssociatedHandsetByUSIMID(Mockito.anyString());
			 assertEquals(equipmentInfo.getContractTermCredits().length, 4);
			
		}
		
		@Test
		public void testGetBaseProductPrice() throws ApplicationException{
			
			when(mockedCreditAndPricingDao.getBaseProductPrice("1234","ON","416", null)).thenReturn(23.02);
			when(mockedEquipmentHelperImpl.getBaseProductPrice("1234","ON","416")).thenReturn(24.09);
			
		}
		@Test
		public void testGetBaseProductPriceByProductCode(@Mocked("getUSE_ACME") final ProductEquipmentHelperImpl mockedEquipmentHelperImpl) throws ApplicationException{
			
			String productCode="1234";
			String province="ON";
			String npa="416";
			
			mockedEquipmentHelperImpl.setCreditAndPricingDao(mockedCreditAndPricingDao);
			
			// Used to mock the private method getUSE_ACME()
			new Expectations() {
                {
                      invoke(mockedEquipmentHelperImpl, "getUSE_ACME"); result = "false";
                }
             };  
            when(mockedCreditAndPricingDao.getBaseProductPriceByProductCodeFromACME(productCode, province, npa, null)).thenReturn(23.0);
 			when(mockedCreditAndPricingDao.getBaseProductPriceByProductCodeFromP3MS(productCode, province, null)).thenReturn(23.0);
 			when(mockedEquipmentHelperImpl.getBaseProductPriceByProductCode(productCode, province, npa)).thenReturn(220.00);

		}
		
//		@Test
//		public void testGetCardBySerialNo_flow1() throws ApplicationException{
//			
//			when(mockedPrepaidDao.getCardBySerialNo(Mockito.anyString())).thenReturn(new CardInfo());
//			when(mockedEquipmentHelperImpl.getCardBySerialNo("12345678901")).thenReturn(new CardInfo());
//			
//		}
		
		@Test
		public void testGetCardBySerialNo_flow2() throws ApplicationException{
			
			// test1- Invalid Serial Number
			try{
				when(mockedEquipmentHelperImpl.getCardBySerialNo(null)).thenReturn(new CardInfo());
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.SERIAL_NUMBER_NULL_EN, e.getErrorMessage());
			}
			
			// test2- Invalid Serial Number length 
			try{
				when(mockedEquipmentHelperImpl.getCardBySerialNo("123")).thenReturn(new CardInfo());
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.INVALID_SERAIL_NUMBER_LENGTH_EN, e.getErrorMessage());
			}
			
			
		}
		
		@Test
		public void testGetCardServices_flow1() throws ApplicationException{
			
			when(mockedCardDao.getCardServices("12345678901", "tecttype","billtype")).thenReturn(new ServiceInfo[0]);
			when(mockedEquipmentHelperImpl.getCardServices("12345678901", "tecttype","billtype")).thenReturn(new ServiceInfo[0]);
		}
		
		@Test
		public void testGetCardServices_flow2() throws ApplicationException{
			
			// test1- Invalid Serial Number
			try{
				when(mockedEquipmentHelperImpl.getCardServices(null,"","")).thenReturn(new ServiceInfo[0]);
			}catch(ApplicationException e){
				assertEquals( EquipmentErrorMessages.SERIAL_NUMBER_NULL_EN, e.getErrorMessage());
			}
			
			// test2- Invalid Serial Number length
			try{
				when(mockedEquipmentHelperImpl.getCardServices("123","","")).thenReturn(new ServiceInfo[0]);
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.INVALID_SERAIL_NUMBER_LENGTH_EN, e.getErrorMessage());
			}
			
			// test3- Invalid techType
			try{
				when(mockedEquipmentHelperImpl.getCardServices("12345678901",null,"")).thenReturn(new ServiceInfo[0]);
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.TECHNOGOTY_TYPE_NULL_EN, e.getErrorMessage());
			}
			
			// test4- Invalid billing type
			try{
				when(mockedEquipmentHelperImpl.getCardServices("12345678901","",null)).thenReturn(new ServiceInfo[0]);
			}catch(ApplicationException e){
				assertEquals( EquipmentErrorMessages.BILLING_TYPE_NULL_EN, e.getErrorMessage());
			}
			
		}
		
		@Test
		public void testGetCards_flow1() throws ApplicationException{
			
			when(mockedCardDao.getCards(Mockito.anyString(),Mockito.anyString())).thenReturn(new CardInfo[0]);
			when(mockedEquipmentHelperImpl.getCards(Mockito.anyString(),Mockito.anyString())).thenReturn(new CardInfo[0]);
		}
		
		@Test
		public void testGetCards_flow2() throws ApplicationException{
			
			
			// test1- Invalid Phone number
			try{
				when(mockedEquipmentHelperImpl.getCards(null,"")).thenReturn(new CardInfo[0]);
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN, e.getErrorMessage());
			}
			// test2- Invalid Card Type
			try{
				when(mockedEquipmentHelperImpl.getCards("",null)).thenReturn(new CardInfo[0]);
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.CARD_TYPE_NULL_EN, e.getErrorMessage());
			}
		}
		
		@Test
		public void testGetCards_flow3() throws ApplicationException{
			
			when(mockedCardDao.getCards("4162341211",null)).thenReturn(new CardInfo[0]);
			when(mockedEquipmentHelperImpl.getCards("4162341211")).thenReturn(new CardInfo[0]);
		}
		
		@Test
		public void testGetCards_flow4() throws ApplicationException{
			
			// test1- Invalid Phone number
			try{
				when(mockedEquipmentHelperImpl.getCards(null)).thenReturn(new CardInfo[0]);
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN, e.getErrorMessage());
			}
		}
		
		
		@Test
		public void testGetEquipmentInfoByCapCode() throws ApplicationException{
			 EquipmentInfo equipmentInfo=new EquipmentInfo();
			 equipmentInfo.setProductGroupTypeID(10);
			 equipmentInfo.setProductTypeID(11);
			
			 when(mockedCreditAndPricingDao.retrieveContractTermCredits(equipmentInfo.getProductGroupTypeID(),equipmentInfo.getProductTypeID())).thenReturn(new double[4]);
			 when(mockedEquipmentDao.getEquipmentInfobyCapCode(Mockito.anyString(), Mockito.anyString())).thenReturn(equipmentInfo);
			 when(mockedEquipmentDao.getSubscriberByEquipment(equipmentInfo)).thenReturn(equipmentInfo);
			
			 equipmentInfo=mockedEquipmentHelperImpl.getEquipmentInfoByCapCode(Mockito.anyString(), Mockito.anyString());
			 assertEquals(equipmentInfo.getContractTermCredits().length, 4);
			
		}
		
		@Test
		public void testGetEquipmentInfobyPhoneNo() throws ApplicationException{
			
			when(mockedEquipmentDao.getEquipmentInfobyPhoneNo(Mockito.anyString())).thenReturn(new EquipmentInfo());
			when(mockedEquipmentHelperImpl.getEquipmentInfobyPhoneNo(Mockito.anyString())).thenReturn(new EquipmentInfo());
			
		}
		
		@Test
		public void testGetEquipmentInfobyProductCode() throws ApplicationException{
			 EquipmentInfo equipmentInfo=new EquipmentInfo();
			 equipmentInfo.setProductGroupTypeID(10);
			 equipmentInfo.setProductTypeID(11);
			
			 when(mockedCreditAndPricingDao.retrieveContractTermCredits(equipmentInfo.getProductGroupTypeID(),equipmentInfo.getProductTypeID())).thenReturn(new double[4]);
			 when(mockedEquipmentDao.getEquipmentInfobyProductCode( Mockito.anyString())).thenReturn(equipmentInfo);
			
			 equipmentInfo=mockedEquipmentHelperImpl.getEquipmentInfobyProductCode(Mockito.anyString());
			 assertEquals(equipmentInfo.getContractTermCredits().length, 4);
			
		}
		
		
		@Test
		public void testGetEquipmentInfobySerialNo_1() throws ApplicationException{
			when(mockedCreditAndPricingDao.getEquipmentInfobySerialNo(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(new EquipmentInfo[0]);
			when(mockedEquipmentDao.getSubscriberByEquipment(Mockito.any(EquipmentInfo.class))).thenReturn(new EquipmentInfo());
			when(mockedEquipmentHelperImpl.getEquipmentInfobySerialNo(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(new EquipmentInfo[0]);
			
		}
		
		@Test
		public void testGetEquipmentList() throws ApplicationException{
			
			String[] output=new String[]{"100","200"};
			
			when(mockedEquipmentDao.getEquipmentList(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(output);
			when(mockedEquipmentDao.getKBEquipmentList(Mockito.anyString(), Mockito.anyInt())).thenReturn(output);
			
			String[] out=mockedEquipmentHelperImpl.getEquipmentList("techtype",0,true,"123");
			 assertEquals(out.length, 2);
			 assertEquals(out[0], "100");
			 assertEquals(out[1], "200");
			 
		}
		
		
		@Test
		public void testGetEquipmentModes() throws ApplicationException{
			
			when(mockedEquipmentDao.getEquipmentModes(Mockito.anyString())).thenReturn(new EquipmentModeInfo[0]);
			when(mockedEquipmentHelperImpl.getEquipmentModes(Mockito.anyString())).thenReturn(new EquipmentModeInfo[0]);
			
		}
		
		@Test
		public void testGetIDENShippedToLocation() throws ApplicationException{
			long output=23;
			when(mockedEquipmentDao.getIDENShippedToLocation(Mockito.anyString(),Mockito.anyInt())).thenReturn(output);
			when(mockedEquipmentHelperImpl.getIDENShippedToLocation("")).thenReturn(output);
		}
		
		@Test
		public void testGetIMEIBySIM() throws ApplicationException{
			
			when(mockedEquipmentDao.getIMEIBySIM(Mockito.anyString())).thenReturn("123");
			when(mockedEquipmentHelperImpl.getIMEIBySIM(Mockito.anyString())).thenReturn("123");
			
		}
		
		@Test
		public void testGetMuleBySIM() throws ApplicationException{
			 EquipmentInfo equipmentInfo=new EquipmentInfo();
			 equipmentInfo.setProductGroupTypeID(10);
			 equipmentInfo.setProductTypeID(11);
			
			 when(mockedCreditAndPricingDao.retrieveContractTermCredits(equipmentInfo.getProductGroupTypeID(),equipmentInfo.getProductTypeID())).thenReturn(new double[4]);
			 when(mockedEquipmentDao.getMuleBySIM( Mockito.anyString())).thenReturn(equipmentInfo);
			
			 equipmentInfo=mockedEquipmentHelperImpl.getMuleBySIM(Mockito.anyString());
			 assertEquals(equipmentInfo.getContractTermCredits().length, 4);
			
		}
		
		@Test
		public void testGetPCSShippedToLocation() throws ApplicationException{
			long output=23;
			when(mockedEquipmentDao.getPCSShippedToLocation(Mockito.anyString(),Mockito.anyInt())).thenReturn(output);
			when(mockedEquipmentHelperImpl.getPCSShippedToLocation("")).thenReturn(output);
			
		}
		
		@Test
		public void testGetProductFeatures() throws ApplicationException{
			
			when(mockedEquipmentDao.getProductFeatures(Mockito.anyString())).thenReturn(new String[0]);
			when(mockedEquipmentHelperImpl.getProductFeatures(Mockito.anyString())).thenReturn(new String[0]);
			
		}
		
		@Test
		public void testGetSIMByIMEI() throws ApplicationException{
			
			when(mockedEquipmentDao.getSIMByIMEI(Mockito.anyString())).thenReturn(new String());
			when(mockedEquipmentHelperImpl.getSIMByIMEI(Mockito.anyString())).thenReturn(new String());
			
		}
		
		@Test
		public void testGetShippedToLocation() throws ApplicationException{
			long output=23;
			when(mockedEquipmentDao.getShippedToLocation(Mockito.anyString(),Mockito.anyInt())).thenReturn(output);
			when(mockedEquipmentHelperImpl.getShippedToLocation("")).thenReturn(output);
			
		}
		
		@Test
		public void testGetWarrantyInfo() throws ApplicationException{
			
			when(mockedEquipmentDao.getWarrantyInfo(Mockito.anyString())).thenReturn(new WarrantyInfo());
			when(mockedEquipmentHelperImpl.getWarrantyInfo("")).thenReturn(new WarrantyInfo() );
			
		}
		
		@Test
		public void testIsNewPrepaidHandset() throws ApplicationException{
			
			when(mockedEquipmentDao.isNewPrepaidHandset(Mockito.anyString(),Mockito.anyString())).thenReturn(false);
			when(mockedEquipmentHelperImpl.isNewPrepaidHandset("","")).thenReturn(false);
			
		}
		
		@Test
		public void testGetCardByFullCardNo_flow1() throws ApplicationException{
			
			String fullCardNo="012345678901234";
			String serialNo="01234567890";
			CardInfo cardInfo= new CardInfo();
			String cypherPIN="0000";
			String userId="user";
			String equipmentSerialNo="123";
			String phoneNumber="416";
						
			when(mockedCardDao.getCardInfobySerialNo(serialNo)).thenReturn(cardInfo);
			when(mockedCardDao.checkPINAttemps(serialNo)).thenReturn(2);
			when(mockedCardDao.getCypherPIN(serialNo)).thenReturn(cypherPIN);
			//when(mockedPrepaidDao.validateCardPIN(serialNo, fullCardNo, cypherPIN, userId, equipmentSerialNo, phoneNumber, cardInfo)).thenReturn(cardInfo);
			when(mockedEquipmentHelperImpl.getCardByFullCardNo(fullCardNo,phoneNumber,equipmentSerialNo,userId)).thenReturn(cardInfo);
			
		}
		
		@Test
		public void testGetCardByFullCardNo_flow2() throws ApplicationException{
			
			// test1- Invalid Card number 
			try{
				when(mockedEquipmentHelperImpl.getCardByFullCardNo(null,"123","123","user")).thenReturn(new CardInfo());
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.CARD_NUMBER_NULL_EN, e.getErrorMessage());
			}
			
			// test2- Invalid Card number length
			try{
				when(mockedEquipmentHelperImpl.getCardByFullCardNo("cardno","123","123","user")).thenReturn(new CardInfo());
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.INVALID_CARD_NUMBER_LENGTH_EN, e.getErrorMessage());
			}
			
			// test3- Invalid userId
			try{
				when(mockedEquipmentHelperImpl.getCardByFullCardNo("012345678901234","123","123",null)).thenReturn(new CardInfo());
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.UNKNOWN_USER_ID_EN, e.getErrorMessage());
			}
			
			// test4- Invalid Phone Number
			try{
				when(mockedEquipmentHelperImpl.getCardByFullCardNo("012345678901234",null,"123","userid")).thenReturn(new CardInfo());
			}catch(ApplicationException e){
				assertEquals(EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN, e.getErrorMessage());
			}
		}
		
		
		@Test
		public void testRetrieveVirtualEquipment() throws ApplicationException{
			
			when(mockedEquipmentDao.getVirtualEquipment(Mockito.anyString(),Mockito.anyString())).thenReturn(new EquipmentInfo());
			when(mockedEquipmentHelperImpl.retrieveVirtualEquipment(Mockito.anyString(),Mockito.anyString())).thenReturn(new EquipmentInfo());
			
		}
		
		@Test
		public void testGetActivationCreditsByProductCode() throws ApplicationException{
			
			when(mockedEquipmentDao.retrievePagerEquipmentInfo(Mockito.anyString())).thenReturn(new EquipmentInfo());
			when(mockedEquipmentHelperImpl.retrievePagerEquipmentInfo(Mockito.anyString())).thenReturn(new EquipmentInfo());
			
		}
			
		@SuppressWarnings("deprecation")
		@Test
		public void testGetActivationCreditsByProductCode(@Mocked("getUSE_ACME") final ProductEquipmentHelperImpl mockedEquHelperImpl) throws ApplicationException{
			
			String productCode="1234";
			String province="ON";
			String npa="416";
			int contractTermMonths=1;
//			String creditType="C";
			String productType="C";
			boolean isInitialActivation=false;
			ActivationCreditInfo[] actCreditInfo =new ActivationCreditInfo[1];
			
			
			mockedEquHelperImpl.setCreditAndPricingDao(mockedCreditAndPricingDao);
			
			// Used to mock the private method getUSE_ACME()
			new Expectations() {
                {
                      invoke(mockedEquHelperImpl, "getUSE_ACME"); result = "true";
                }
             };  
             
             when(mockedCreditAndPricingDao.getActivationCreditsByProductCodeFromACME(anyString(), 
            		 anyString(), anyString(), anyInt(), anyString(), any(Date.class), anyString(), anyBoolean())).thenReturn(actCreditInfo);
//             when(mockedCreditAndPricingDao.getActivationCreditsByProductCodeFromP3MS(productCode, 
// 					province, contractTermMonths, creditType, new Date(), productType, isInitialActivation)).thenReturn(actCreditInfo);
             ActivationCreditInfo[]  info= mockedEquHelperImpl.getActivationCreditsByProductCode(productCode, province,
            		 npa, contractTermMonths, new Date(), true, productType, isInitialActivation);
             assertNotNull(info);
             verify(mockedCreditAndPricingDao).getActivationCreditsByProductCodeFromACME(anyString(), 
            		 anyString(), anyString(), anyInt(), anyString(), any(Date.class), anyString(), anyBoolean());
             
		}
		
		@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
		@Test
		public void testGetActivationCreditsByProductCodes(@Mocked("getUSE_ACME") final ProductEquipmentHelperImpl mockedEquipmentHelperImpl) throws ApplicationException{
			String productCode="1234";
			String[] productCodes={"A"};
			String province="ON";
			String npa="416";
			int contractTermMonths=1;
			String creditType="C";
			String productType="C";
			String[] productTypes={"C"};
			boolean isInitialActivation=false;
			ActivationCreditInfo[] actCreditInfo =new ActivationCreditInfo[1];
			mockedEquipmentHelperImpl.setCreditAndPricingDao(mockedCreditAndPricingDao);
			
			// Used to mock the private method getUSE_ACME()
			new Expectations() {
                {
                      invoke(mockedEquipmentHelperImpl, "getUSE_ACME"); result = "true";
                }
             };  
             
             when(mockedCreditAndPricingDao.getActivationCreditsByProductCodeFromACME(anyString(), 
            		 anyString(), anyString(), anyInt(), anyString(), any(Date.class), anyString(), anyBoolean())).thenReturn(actCreditInfo);
             when(mockedCreditAndPricingDao.getActivationCreditsByProductCodeFromP3MS(productCode, 
 					province, contractTermMonths, creditType, new Date(), productType, isInitialActivation)).thenReturn(actCreditInfo);
             HashMap map=mockedEquipmentHelperImpl.getActivationCreditsByProductCodes(productCodes, 
            		 province, npa, contractTermMonths, new Date(), true, productTypes ,isInitialActivation);
             assertNotNull(map);
             
             verify(mockedCreditAndPricingDao).getActivationCreditsByProductCodeFromACME(anyString(), 
            		 anyString(), anyString(), anyInt(), anyString(), any(Date.class), anyString(), anyBoolean());
		}
		
		@SuppressWarnings("deprecation")
		@Test
		public void testGetActivationCredits_1() throws ApplicationException{
			ActivationCreditInfo[] actCreditInfo=new ActivationCreditInfo[1];
			when(mockedCreditAndPricingDao.getActivationCredits("serialNumber", "province", "npa", -1 ,"creditType", null)).thenReturn(actCreditInfo);
			
			ActivationCreditInfo[] info=mockedEquipmentHelperImpl.getActivationCredits("serialNumber", "province", "npa", "creditType");
			assertNotNull(info);
			
			verify(mockedCreditAndPricingDao).getActivationCredits("serialNumber", "province", "npa", -1 ,"creditType", null);
			
		}
		
		@SuppressWarnings("deprecation")
		@Test
		public void testGetActivationCredits_2() throws ApplicationException{
			ActivationCreditInfo[] actCreditInfo=new ActivationCreditInfo[1];
			when(mockedCreditAndPricingDao.getActivationCredits("serialNumber", "province","npa", 10, "%", null)).thenReturn(actCreditInfo);
			
			ActivationCreditInfo[] info=mockedEquipmentHelperImpl.getActivationCredits("serialNumber", "province", "npa", 10, true);
			assertNotNull(info);
			
			verify(mockedCreditAndPricingDao).getActivationCredits("serialNumber", "province","npa", 10, "%", null);
			
		}
		
		
		@SuppressWarnings("deprecation")
		@Test
		public void testGetActivationCredits_3() throws ApplicationException{
			ActivationCreditInfo[] actCreditInfos=new ActivationCreditInfo[1];
			ActivationCreditInfo actCreditInfo= new ActivationCreditInfo();
			actCreditInfos[0]=actCreditInfo;
			
			when(mockedCreditAndPricingDao.getActivationCredits("serialNumber", "province","npa", 10, "%", null)).thenReturn(actCreditInfos);
			when(activationCreditInfo.isPricePlanCredit()).thenReturn(false);
			
			ActivationCreditInfo[] info=mockedEquipmentHelperImpl.getActivationCredits("serialNumber", "province", "npa", 10, true);
			assertNotNull(info);
			
			verify(mockedCreditAndPricingDao).getActivationCredits("serialNumber", "province","npa", 10, "%", null);
			
		}
		
		@SuppressWarnings("deprecation")
		@Test
		public void testGetActivationCredits_4() throws ApplicationException{
			ActivationCreditInfo[] actCreditInfos=new ActivationCreditInfo[1];
			ActivationCreditInfo actCreditInfo= new ActivationCreditInfo();
			actCreditInfos[0]=actCreditInfo;
			Date date=new Date();
			
			when(mockedCreditAndPricingDao.getActivationCredits("serialNumber", "province","npa", 10, "%", date)).thenReturn(actCreditInfos);
			when(activationCreditInfo.isPricePlanCredit()).thenReturn(false);
			
			ActivationCreditInfo[] info=mockedEquipmentHelperImpl.getActivationCredits("serialNumber", "province", "npa", 10,date, true);
			assertNotNull(info);
			
			verify(mockedCreditAndPricingDao).getActivationCredits("serialNumber", "province","npa", 10, "%", date);
			
		}
		
		
}
