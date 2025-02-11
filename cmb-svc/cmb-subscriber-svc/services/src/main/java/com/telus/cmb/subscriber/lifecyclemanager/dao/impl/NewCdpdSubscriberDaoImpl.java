package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.CdpdEquipmentInfo;
import amdocs.APILink.datatypes.EquipmentInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.NewCdpdConv;
import amdocs.APILink.sessions.interfaces.NewProductConv;

import com.telus.api.ApplicationException;
import com.telus.api.account.ServicesValidation;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewSubscriberDao;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

@Deprecated
public class NewCdpdSubscriberDaoImpl extends NewSubscriberDaoImpl implements
		NewSubscriberDao {
	
	private final Logger LOGGER = Logger.getLogger(NewCdpdSubscriberDaoImpl.class);	
	
	private Class<NewCdpdConv> newCdpdConv = NewCdpdConv.class;

	@Override
	public String[] retrieveAvailablePhoneNumbers(final int ban,
			final PhoneNumberReservationInfo phoneNumberReservation, final int maxNumbers,
			String sessionId) throws ApplicationException {		
		return super.retrieveAvailablePhoneNumbers(newCdpdConv, ban, phoneNumberReservation, maxNumbers, sessionId);	
	}

	@Override
	public void createSubscriber(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, boolean activate,
			boolean dealerHasDeposit, boolean portedIn,
			ServicesValidation srvValidation, String portProcessType,
			int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException {
		super.createSubscriber(newCdpdConv, subscriberInfo, subscriberContractInfo
				, activate, dealerHasDeposit, portedIn, srvValidation, portProcessType, oldBanId, oldSubscriberId
				, sessionId);		
	}

	@Override
	protected void setProductEquipmentInfo(NewProductConv newProductConv,
			SubscriberInfo subscriberInfo) throws RemoteException,
			ValidateException {
		NewCdpdConv newCdpdConv = (NewCdpdConv) newProductConv;
		
		CdpdEquipmentInfo cdpdEquipmentInfo = new CdpdEquipmentInfo();

		//populate cdpdEquipmentInfo
		cdpdEquipmentInfo.serialNumber = subscriberInfo.getEquipment0().getSerialNumber();
		cdpdEquipmentInfo.equipmentType = AttributeTranslator.byteFromString(subscriberInfo.getEquipment0().getEquipmentType());
		cdpdEquipmentInfo.possession = AttributeTranslator.byteFromString(subscriberInfo.getEquipment0().getPossession());
		cdpdEquipmentInfo.activateInd = true;
		cdpdEquipmentInfo.primaryInd = true;
		cdpdEquipmentInfo.equipmentMode = EquipmentInfo.INSERT;
		newCdpdConv.setEquipmentInfo(cdpdEquipmentInfo);
		LOGGER.debug("EquipmentInfo set");		
	}

	@Override
	public void releaseSubscriber(SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {
		super.releaseSubscriber(newCdpdConv, subscriberInfo, sessionId);
		
	}

	@Override
	public SubscriberInfo reserveLikePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation, String sessionId)
			throws ApplicationException {
		return super.reserveLikePhoneNumber(newCdpdConv, subscriberInfo, phoneNumberReservation, sessionId);
	}

	@Override
	public SubscriberInfo reservePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation, boolean isOfflineReservation,String sessionId)
			throws ApplicationException {
		return super.reservePhoneNumber(newCdpdConv, subscriberInfo, phoneNumberReservation, false,sessionId);
	}
}
