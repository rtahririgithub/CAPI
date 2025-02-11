package com.telus.cmb.productequipment.lifecyclefacade.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;
import java.util.Date;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CellularDigitalEquipmentUpgradeInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telusmobility.nrt.NRTException;
import com.telusmobility.nrt.NrtEligibilityManagerEJBRemote;
import com.telusmobility.nrt.PromotionSummaryBean;
import com.telusmobility.p3ms.dvo.CallerDVO;
import com.telusmobility.p3ms.ejb.facade.ProductFacadeEJB;
import com.telusmobility.p3ms.exception.P3MSException;

public class ProductEquipmentLifecycleFacadeImplTest {

	ProductEquipmentLifecycleFacadeImpl equipmentLifecycleFacadeImpl;
	
	@Mock
	ProductFacadeEJB p3msProductFacade;
	
	@Mock
	NrtEligibilityManagerEJBRemote nrtEligibilityManager;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		equipmentLifecycleFacadeImpl=new ProductEquipmentLifecycleFacadeImpl();
		
	}
	
	@Test
	public void testIsApple(@Mocked("getP3MSProductFacade") final ProductEquipmentLifecycleFacadeImpl equipmentLifecycleFacadeImpl) 
						throws ApplicationException, RemoteException, P3MSException{
		
		// Used to mock the private method getP3MSProductFacade()
		new Expectations() {
            {
                  invoke(equipmentLifecycleFacadeImpl, "getP3MSProductFacade"); result = p3msProductFacade;
            }
         }; 
        when(p3msProductFacade.isApple(Mockito.anyString(), Mockito.any(CallerDVO.class))).thenReturn(true);
		when(equipmentLifecycleFacadeImpl.isApple("PctCode")).thenReturn(true);
	
	}
	
	
	
	@Test
	public void testGetCellularDigitalEquipmentUpgrades(@Mocked("getNrtEligibilityManager")final ProductEquipmentLifecycleFacadeImpl equipmentLifecycleFacadeImpl)
								throws ApplicationException, RemoteException, NRTException{
		
		// Used to mock the private method getNrtEligibilityManager()
		new NonStrictExpectations() {
            {
                  invoke(equipmentLifecycleFacadeImpl, "getNrtEligibilityManager"); result = nrtEligibilityManager;
            }
         }; 
         
         EquipmentInfo equipmentInfo=new EquipmentInfo();
         equipmentInfo.setProductType("C");
         equipmentInfo.setEquipmentTypeClass("DIGITAL");
         equipmentInfo.setSerialNumber("01234567891");
         
         PromotionSummaryBean[]  bean=new PromotionSummaryBean[1];
         bean[0]= new PromotionSummaryBean();
         
         bean[0].setDescriptionEn("en");
         bean[0].setDescriptionFr("fr");
         bean[0].setPrlDescription("prl");
         bean[0].setReflashBrowserVersion("3.0");
         bean[0].setReflashSoftwareVersion("4.0");
         bean[0].setStartDate(new Date());
        
         when(nrtEligibilityManager.getHandsetEligibilityForNrt(equipmentInfo.getSerialNumber(), null)).thenReturn(bean);	
         CellularDigitalEquipmentUpgradeInfo[] upgradeInfos=equipmentLifecycleFacadeImpl.getCellularDigitalEquipmentUpgrades(equipmentInfo);
         assertEquals(upgradeInfos[0].getBrowserVersion(), "3.0");
         assertEquals(upgradeInfos[0].getPRLCode(), "prl");
	}
}
