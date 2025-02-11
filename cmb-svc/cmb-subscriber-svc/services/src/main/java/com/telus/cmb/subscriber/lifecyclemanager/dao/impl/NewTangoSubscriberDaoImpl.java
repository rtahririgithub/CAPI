package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.EquipmentInfo;
import amdocs.APILink.datatypes.TangoEquipmentInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.NewProductConv;
import amdocs.APILink.sessions.interfaces.NewTangoConv;

import com.telus.api.ApplicationException;
import com.telus.api.account.ServicesValidation;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewSubscriberDao;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

@Deprecated
public class NewTangoSubscriberDaoImpl extends NewSubscriberDaoImpl implements NewSubscriberDao{
	private final Logger LOGGER = Logger.getLogger(NewTangoSubscriberDaoImpl.class);	

	private Class<NewTangoConv> newTangoConv = NewTangoConv.class;

	@Override
	public String[] retrieveAvailablePhoneNumbers(final int ban,
			final PhoneNumberReservationInfo phoneNumberReservation, final int maxNumbers,
			String sessionId) throws ApplicationException {
		return super.retrieveAvailablePhoneNumbers(newTangoConv, ban, phoneNumberReservation, maxNumbers, sessionId);		
	}

	@Override
	public void createSubscriber(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, boolean activate,
			boolean dealerHasDeposit, boolean portedIn,
			ServicesValidation srvValidation, String portProcessType,
			int oldBanId, String oldSubscriberId, String sessionId)
	throws ApplicationException {
		super.createSubscriber(newTangoConv, subscriberInfo, subscriberContractInfo
				, activate, dealerHasDeposit, portedIn, srvValidation
				, portProcessType, oldBanId, oldSubscriberId, sessionId);

	}

	@Override
	protected void setProductEquipmentInfo(NewProductConv newProductConv,
			SubscriberInfo subscriberInfo) throws RemoteException,
			ValidateException {		
		NewTangoConv newTangoConv = (NewTangoConv) newProductConv;
		//populate TangoEquipmentInfo
		TangoEquipmentInfo tangoEquipmentInfo = new TangoEquipmentInfo();
		tangoEquipmentInfo.serialNumber = subscriberInfo.getEquipment0().getSerialNumber();
		tangoEquipmentInfo.equipmentType = AttributeTranslator.byteFromString(subscriberInfo.getEquipment0().getEquipmentType());
		tangoEquipmentInfo.possession = AttributeTranslator.byteFromString(subscriberInfo.getEquipment0().getPossession());
		tangoEquipmentInfo.activateInd = true;
		tangoEquipmentInfo.primaryInd = true;
		tangoEquipmentInfo.equipmentMode = EquipmentInfo.INSERT;
		newTangoConv.setEquipmentInfo(tangoEquipmentInfo);
		LOGGER.debug("EquipmentInfo set");
	}

	@Override
	public void releaseSubscriber(SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {
		super.releaseSubscriber(newTangoConv, subscriberInfo, sessionId);
	}

	@Override
	public SubscriberInfo reserveLikePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation, String sessionId)
			throws ApplicationException {
		return super.reserveLikePhoneNumber(newTangoConv, subscriberInfo, phoneNumberReservation, sessionId);
	}

	@Override
	public SubscriberInfo reservePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation, boolean isOfflineReservation,String sessionId)
			throws ApplicationException {
		return super.reservePhoneNumber(newTangoConv, subscriberInfo, phoneNumberReservation, false,sessionId);
	}
}
