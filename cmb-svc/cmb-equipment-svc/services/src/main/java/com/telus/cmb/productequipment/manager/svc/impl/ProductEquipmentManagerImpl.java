package com.telus.cmb.productequipment.manager.svc.impl;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.equipment.Card;
import com.telus.api.util.VersionReader;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.common.util.TelusConstants;
import com.telus.cmb.productequipment.EquipmentErrorMessages;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.productequipment.manager.dao.CardManagerDao;
import com.telus.cmb.productequipment.manager.dao.EquipmentManagerDao;
import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManager;
import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManagerTestPoint;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.framework.config.ConfigContext;


@Stateless(name="ProductEquipmentManager", mappedName="ProductEquipmentManager")
@Remote({ProductEquipmentManager.class, ProductEquipmentManagerTestPoint.class})
@RemoteHome(ProductEquipmentManagerHome.class)
@Interceptors({SpringBeanAutowiringInterceptor.class, ProductEquipmentManagerSvcInvocationInterceptor.class})

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class ProductEquipmentManagerImpl implements ProductEquipmentManager, ProductEquipmentManagerTestPoint {
	private static final Logger LOGGER = Logger.getLogger(ProductEquipmentManagerImpl.class);
	public final static int ESN_LENGTH = 11;

	@EJB
	private ProductEquipmentHelper productEquipmentHelper;
	
	@Autowired
	private CardManagerDao cardDao;	

	@Autowired
	private EquipmentManagerDao equipmentDao;
	
	@Autowired
	private DataSourceTestPointDao testPointDao;
		
	public void setCardDao(CardManagerDao cardDao) {
		this.cardDao = cardDao;
	}


	public void setEquipmentDao(EquipmentManagerDao equipmentDao) {
		this.equipmentDao = equipmentDao;
	}

	public void setProductEquipmentHelper(ProductEquipmentHelper productEquipmentHelper) {
		this.productEquipmentHelper = productEquipmentHelper;
	}
	@Override
	public void activateCard(String serialNo, int ban, String phoneNumber,
			String equipmentSerialNo, boolean autoRenewInd, String user)
			throws ApplicationException {
		//Need to first make sure that the serialNo string is not null and contains 11 chars..
	     if (serialNo == null) {
	    	 LOGGER.error("id=VAL10002; Serial number is null.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, ErrorCodes.SERIAL_NUMBER_NULL,
	    			 EquipmentErrorMessages.SERIAL_NUMBER_NULL_EN,"");
	     } else if (serialNo.length() != 11) {
	    	 LOGGER.error("id=VAL10002; Serial number not 11 in length.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, ErrorCodes.INVALID_SERAIL_NUMBER_LENGTH,
	    			 EquipmentErrorMessages.INVALID_SERAIL_NUMBER_LENGTH_EN,"");
	     }
	     
	     if (user == null) {
	    	 LOGGER.error("id=EQU30009; Unknown user id. User is null.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, ErrorCodes.UNKONWN_USER_ID,
	    			 EquipmentErrorMessages.UNKNOWN_USER_ID_EN,"");
	     }
	     if (phoneNumber == null) {
	    	 LOGGER.error("id=EQU30010; Phone number is null.");
	         throw new ApplicationException(SystemCodes.CMB_PEM_EJB, ErrorCodes.UNKONWN_PHONE_NUMBER,
	        		 EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN,"");
	     }
	     if (equipmentSerialNo == null) {
	    	 LOGGER.error("id=EQU30013; Equipment serial number is null.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, ErrorCodes.EQUIPMENT_SERIAL_NUMBER_NULL,
	    			 EquipmentErrorMessages.EQUIPMENT_SERIAL_NUMBER_NULL_EN,"");
	     }
	     
	     //invoke dao method to retrieve the current card status
	     //this info is necessary for some validation
	     int currentCardStatus =  cardDao.getCardStatus(serialNo);
	     
	     
	    //make sure that the current card status is pending or live.
	    //You cannot set to credit unless it is pending or live.
	     if (currentCardStatus == Card.STATUS_PENDING) {
	    	 cardDao.setCardStatus(serialNo, Card.STATUS_USED, user, ban, phoneNumber, equipmentSerialNo, autoRenewInd);
	     } else { //status is not pending or live, throw exception
	     	 LOGGER.error("id=EQU30008; Error. Cannot set status. Validation rule requirements failed.");
	       	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, ErrorCodes.VALIDATAION_FAILED,
	       			 EquipmentErrorMessages.VALIDATAION_FAILED_EN,"");
	    }
		
	}

	@Override
	public void activateSIMMule(String sim, String mule, Date activationDate)
			throws ApplicationException {
		 setSIMMule(sim, mule, activationDate, TelusConstants.SIM_IMEI_ACTIVATE);
	}

	@Override
	public void creditCard(String serialNo, int ban, String phoneNumber,
			String equipmentSerialNo, boolean autoRenewInd, String user)
			throws ApplicationException {
		//Need to first make sure that the serialNo string is not null and contains 11 chars..
	     if (serialNo == null) {
	    	 LOGGER.error("id=VAL10002; Serial number is null.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, ErrorCodes.SERIAL_NUMBER_NULL,
	    			 EquipmentErrorMessages.SERIAL_NUMBER_NULL_EN,"");
	     } else if (serialNo.length() != 11) {
	    	 LOGGER.error("id=VAL10002; Serial number not 11 in length.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, ErrorCodes.INVALID_SERAIL_NUMBER_LENGTH,
	    			 EquipmentErrorMessages.INVALID_SERAIL_NUMBER_LENGTH_EN,"");
	     }
	     
	     if (user == null) {
	    	 LOGGER.error("id=EQU30009; Unknown user id. User is null.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, ErrorCodes.UNKONWN_USER_ID,
	    			 EquipmentErrorMessages.UNKNOWN_USER_ID_EN,"");
	     }
	     if (phoneNumber == null) {
	    	 LOGGER.error("id=EQU30010; Phone number is null.");
	         throw new ApplicationException(SystemCodes.CMB_PEM_EJB, 
	        		 ErrorCodes.UNKONWN_PHONE_NUMBER,EquipmentErrorMessages.UNKNOWN_PHONE_NUMBER_EN,"");
	     }
	     if (equipmentSerialNo == null) {
	    	 LOGGER.error("id=EQU30013; Equipment serial number is null.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, 
	    			 ErrorCodes.EQUIPMENT_SERIAL_NUMBER_NULL,EquipmentErrorMessages.EQUIPMENT_SERIAL_NUMBER_NULL_EN,"");
	     }
	     
	     //invoke dao method to retrieve the current card status
	     //this info is necessary for some validation
	     int currentCardStatus =  cardDao.getCardStatus(serialNo);
	     
	     
	    //make sure that the current card status is pending or live.
	    //You cannot set to credit unless it is pending or live.
	    if (currentCardStatus == Card.STATUS_PENDING || currentCardStatus == Card.STATUS_LIVE) {
	       	cardDao.setCardStatus(serialNo, Card.STATUS_CREDITED, user, ban, phoneNumber, equipmentSerialNo, autoRenewInd);
	    } else { //status is not pending or live, throw exception
	     	 LOGGER.error("id=EQU30008; Error. Cannot set status. Validation rule requirements failed.");
	       	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, 
	       			 ErrorCodes.VALIDATAION_FAILED,EquipmentErrorMessages.VALIDATAION_FAILED_EN,"");
	    }
		
	}

	@Override
	public String getMasterLockbySerialNo(String pSerialNo, String pUserID,
			long pLockReasonID) throws ApplicationException {
		String serialNo = null;
		try {
			serialNo = equipmentDao.getMasterLockbySerialNo(pSerialNo, pUserID, pLockReasonID);
		} catch (Exception e) {
			throw new ApplicationException(SystemCodes.CMB_PEM_DAO, ErrorCodes.INVALID_INPUT_PARAMETERS, e.getMessage(), "", e);
		}
		
		return serialNo;

	}

	@Override
	public String getMasterLockbySerialNo(String pSerialNo, String pUserID,
			long pLockReasonID, long pOutletID, long pChnlOrgID)
			throws ApplicationException {
		 String serialNo = null;
			try {
				serialNo = equipmentDao.getMasterLockbySerialNo(pSerialNo, pUserID, pLockReasonID, pOutletID, pChnlOrgID);
			} catch (Exception e) {
				throw new ApplicationException(SystemCodes.CMB_PEM_DAO, ErrorCodes.INVALID_INPUT_PARAMETERS, e.getMessage(), "", e);
			}
			
			return serialNo;

	}

	@Override
	public void insertAnalogEquipment(String pSerialNo, String pUserID)
			throws ApplicationException {
		testAnalogEquipment(pSerialNo);
		equipmentDao.insertAnalogEquipment(pSerialNo, pUserID);
		
	}

	@Override
	public void insertPagerEquipment(String pSerialNo, String pCapCode,
			String pEncoderFormat, String pFrequencyCode, String pModelType,
			String pUserID) throws ApplicationException {
		equipmentDao.insertPagerEquipment(pSerialNo, pCapCode, pEncoderFormat, pFrequencyCode, pModelType, pUserID);
		
	}

	@Override
	public void setCardStatus(String serialNo, int statusId, String user)
			throws ApplicationException {
		
	     boolean setStatusFlag = true;
	     
	     //first, can only update to status live or pending from here
	     //the used status is activateCard method instead
	     //the credit status is creditCard method instead
	     if (statusId != Card.STATUS_LIVE && statusId != Card.STATUS_PENDING) {
	          LOGGER.error("id=EQU30007; Error. Cannot set status. Status not supported.");
	          throw new ApplicationException(SystemCodes.CMB_PEM_EJB, 
	        		  ErrorCodes.INVALID_CARD_STATUS,EquipmentErrorMessages.INVALID_CARD_STATUS_EN,"");
	     }

	     //Also, need to make sure that the user is not null
	     if (user == null) {
	    	 LOGGER.error("id=EQU30009; Unknown user id. User is null.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, 
	    			 ErrorCodes.UNKONWN_USER_ID,EquipmentErrorMessages.UNKNOWN_USER_ID_EN,"");
	     }
	     
	     //Need to first make sure that the serialNo string is not null and contains 11 chars..
	     if (serialNo == null) {
	    	 LOGGER.error("id=VAL10002; Serial number is null.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, 
	    			 ErrorCodes.SERIAL_NUMBER_NULL,EquipmentErrorMessages.SERIAL_NUMBER_NULL_EN,"");
	     } else if (serialNo.length() != 11) {
	    	 LOGGER.error("id=VAL10002; Serial number not 11 in length.");
	    	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, 
	    			 ErrorCodes.INVALID_SERAIL_NUMBER_LENGTH,EquipmentErrorMessages.INVALID_SERAIL_NUMBER_LENGTH_EN,"");
	     }
	     
	     int currentCardStatus =  cardDao.getCardStatus(serialNo);
	  
	     //now we do some validation
	        setStatusFlag = true;

	        //if the current card status is used or credited, it cannot be changed
	        //anymore through this method
	        if (currentCardStatus == Card.STATUS_USED || currentCardStatus == Card.STATUS_CREDITED) {
	          setStatusFlag = false;
	        }

	        //if going to pending, current status must be live
	        if (statusId == Card.STATUS_PENDING) {
	          if (currentCardStatus != Card.STATUS_LIVE) {
	            setStatusFlag = false;
	          }
	        }

	        //if going to live, current status must be pending
	        if (statusId == Card.STATUS_LIVE) {
	          if (currentCardStatus != Card.STATUS_PENDING) {
	            setStatusFlag = false;
	          }
	        }

	        if (setStatusFlag) {
	        	 cardDao.setCardStatus(serialNo, statusId, user, 0, null, null, false);
	        } else {
	        	 LOGGER.error("id=EQU30008; Error. Cannot set status. Validation rule requirements failed.");
	        	 throw new ApplicationException(SystemCodes.CMB_PEM_EJB, 
	        			 ErrorCodes.VALIDATAION_FAILED,EquipmentErrorMessages.VALIDATAION_FAILED_EN,"");
	        }
		
	}

	@Override
	public void setSIMMule(String sim, String mule, Date activationDate,
			String eventType) throws ApplicationException {
		equipmentDao.setSIMMule(sim, mule, activationDate, eventType);
		
	}

	@Override
	public void startSIMMuleRelation(String sim, Date activationDate)
			throws ApplicationException {
		 setSIMMule(sim, null, activationDate, TelusConstants.SIM_IMEI_ACTIVATE);
	}

	@Override
	public void updateStatus(String pSerialNo, String pUserID,
			long pEquipmentStatusTypeID, long pEquipmentStatusID,
			String pTechType, long pProductClassID) throws ApplicationException {
		equipmentDao.updateStatus(pSerialNo, pUserID, pEquipmentStatusTypeID, pEquipmentStatusID, pTechType, pProductClassID);
		
	}

	

	private void testAnalogEquipment(String pSerialNo) throws ApplicationException {
		// Check , if ESN is valid in KNowbility
		if (!productEquipmentHelper.isValidESNPrefix(pSerialNo)){
			LOGGER.error("id=VAL10007; Unknown Serial Number Manufacturer Prefix");
			throw new ApplicationException(SystemCodes.CMB_PEM_EJB, 
					ErrorCodes.UNKNOWN_SERIAL_NUMBER_MANUFACTURER,EquipmentErrorMessages.UNKNOWN_SERIAL_NUMBER_MANUFACTURER,"");
		}

		// Check , if the Equipment is virtual
		if (productEquipmentHelper.isVirtualESN(pSerialNo)){
			LOGGER.error("id=VAL20038; Invalid Serial Number");
			throw new ApplicationException(SystemCodes.CMB_PEM_EJB,
					ErrorCodes.INVALID_SERAIL_NUMBER,EquipmentErrorMessages.INVALID_SERAIL_NUMBER_EN,"" );
		}

		// MEID
		EquipmentInfo equipmentInfo = productEquipmentHelper.getEquipmentInfobySerialNumber(pSerialNo);
		if (equipmentInfo.getSerialNumber().length() != ESN_LENGTH  ){
			LOGGER.error("id=VAL20038; Invalid Serial Number");
			throw new ApplicationException(SystemCodes.CMB_PEM_EJB,
					ErrorCodes.INVALID_SERAIL_NUMBER,EquipmentErrorMessages.INVALID_SERAIL_NUMBER_EN,"");
		}
		
		// Pseudo ESN
		String [] serialNumbers = productEquipmentHelper.getESNByPseudoESN(pSerialNo);
		if (serialNumbers.length != 0) {           
			LOGGER.error("id=VAL20038; Invalid Serial Number");
			throw new ApplicationException(SystemCodes.CMB_PEM_EJB,
					ErrorCodes.INVALID_SERAIL_NUMBER,EquipmentErrorMessages.INVALID_SERAIL_NUMBER_EN,"");
		}
	}

	@Override
	public TestPointResultInfo testKnowbilityDataSource() {
		return testPointDao.testKnowbilityDataSource();
	}

	@Override
	public TestPointResultInfo testDistDataSource() {
		return testPointDao.testDistDataSource();
	}
	
	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

}
