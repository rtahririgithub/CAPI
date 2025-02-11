package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;

import amdocs.APILink.datatypes.EquipmentInfo;
import amdocs.APILink.datatypes.PagerEquipmentInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.NewPagerConv;
import amdocs.APILink.sessions.interfaces.NewProductConv;

import com.telus.api.ApplicationException;
import com.telus.api.account.ServicesValidation;
import com.telus.api.equipment.EquipmentManager;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewSubscriberDao;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.subscriber.info.PagerSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

@Deprecated 
public class NewPagerSubscriberDaoImpl extends NewSubscriberDaoImpl implements
NewSubscriberDao {

	private Class<NewPagerConv> newPagerConv = NewPagerConv.class;

	@Override
	public String[] retrieveAvailablePhoneNumbers(final int ban,
			final PhoneNumberReservationInfo phoneNumberReservation, final int maxNumbers,
			String sessionId) throws ApplicationException {
		return super.retrieveAvailablePhoneNumbers(newPagerConv, ban, phoneNumberReservation, maxNumbers, sessionId);
	}

	@Override
	public void createSubscriber(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, boolean activate,
			boolean dealerHasDeposit, boolean portedIn,
			ServicesValidation srvValidation, String portProcessType,
			int oldBanId, String oldSubscriberId, String sessionId)
	throws ApplicationException {
		super.createSubscriber(newPagerConv, subscriberInfo, subscriberContractInfo, activate
				, dealerHasDeposit, portedIn, srvValidation
				, portProcessType, oldBanId, oldSubscriberId, sessionId);

	}

	@Override
	protected void setProductEquipmentInfo(NewProductConv newProductConv,
			SubscriberInfo subscriberInfo) throws RemoteException,
			ValidateException {
		NewPagerConv newPagerConv = (NewPagerConv) newProductConv;

		PagerEquipmentInfo pagerEquipmentInfo = new PagerEquipmentInfo();

		pagerEquipmentInfo.serialNumber = EquipmentManager.Helper.getFormattedCapCode(subscriberInfo.
				getEquipment0().getCapCode(), subscriberInfo.getMarketProvince(),
				subscriberInfo.getEquipment0().getEncodingFormat(),
				subscriberInfo.getEquipment0().getEquipmentType());
		pagerEquipmentInfo.equipmentType = AttributeTranslator.byteFromString(subscriberInfo.getEquipment0().getEquipmentType());
		pagerEquipmentInfo.possession = AttributeTranslator.byteFromString(subscriberInfo.getEquipment0().getPossession());
		pagerEquipmentInfo.encodingFormat = subscriberInfo.getEquipment0().getEncodingFormat();
		pagerEquipmentInfo.coverageRegion = Integer.parseInt(((PagerSubscriberInfo)subscriberInfo).getCoverageRegionCode());
		pagerEquipmentInfo.activateInd = true;
		pagerEquipmentInfo.primaryInd = true;
		pagerEquipmentInfo.equipmentMode = EquipmentInfo.INSERT;
		newPagerConv.setEquipmentInfo(pagerEquipmentInfo);

	}

	@Override
	public void releaseSubscriber(SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {
		super.releaseSubscriber(newPagerConv, subscriberInfo, sessionId);
		
	}

	@Override
	public SubscriberInfo reserveLikePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation, String sessionId)
			throws ApplicationException {
		return super.reserveLikePhoneNumber(newPagerConv, subscriberInfo, phoneNumberReservation, sessionId);
	}

	@Override
	public SubscriberInfo reservePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation, boolean isOfflineReservation,String sessionId)
			throws ApplicationException {
		return super.reservePhoneNumber(newPagerConv, subscriberInfo, phoneNumberReservation, false,sessionId);
	}

}
