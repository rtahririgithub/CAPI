package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.TangoEquipmentInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.UpdateTangoConv;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateSubscriberDao;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

@Deprecated
public class UpdateTangoSubscriberDaoImpl extends UpdateSubscriberDaoImpl implements
UpdateSubscriberDao {
	
	private final Logger LOGGER = Logger.getLogger(UpdateTangoSubscriberDaoImpl.class);

	private Class<UpdateTangoConv> updateTangoConv = UpdateTangoConv.class;

	@Override
	public String[] retrieveAvailablePhoneNumbers(final int ban, final String subscriberId,
			final PhoneNumberReservationInfo phoneNumberReservation, final int maxNumbers,
			String sessionId) throws ApplicationException {		
		return super.retrieveAvailablePhoneNumbers(updateTangoConv, ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
	}

	@Override
	public void changePhoneNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			String dealerCode, String salesRepCode, String sessionId)
	throws ApplicationException {
		super.changePhoneNumber(updateTangoConv, subscriberInfo, newPhoneNumber, reasonCode, dealerCode, salesRepCode, sessionId);

	}

	@Override
	public void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan,
			Date activityDate, boolean transferOwnership,
			String activityReasonCode, String userMemoText, String dealerCode,
			String salesRepCode, String sessionId) throws ApplicationException {
		super.moveSubscriber(updateTangoConv, subscriberInfo, targetBan, activityDate
				, transferOwnership, activityReasonCode, userMemoText, dealerCode, salesRepCode, sessionId);

	}

	@Override
	public void resetVoiceMailPassword(int ban, String subscriberId,
			String[] voiceMailSocAndFeature, String sessionId)
	throws ApplicationException {
		super.resetVoiceMailPassword(updateTangoConv, ban, subscriberId, voiceMailSocAndFeature, sessionId);	
	}

	@Override
	public void changeSerialNumberAndMaybePricePlan(
			final SubscriberInfo subscriberInfo,
			final EquipmentInfo newPrimaryEquipmentInfo,
			EquipmentInfo[] newSecondaryEquipmentInfo,
			SubscriberContractInfo subscriberContractInfo, String dealerCode,
			String salesRepCode, PricePlanValidationInfo pricePlanValidation,
			String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateTangoConv amdocsUpdateTangoConv = transactionContext.createBean(UpdateTangoConv.class);

				try {
					// Set ProductPK (which also retrieves the BAN)
					amdocsUpdateTangoConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());

					// define old and new equipment arrays
					TangoEquipmentInfo[] changeEquipmentInfoArray = null;

					// get old equipment array
					TangoEquipmentInfo[] oldEquipmentInfoArray = amdocsUpdateTangoConv.getEquipmentInfo();

					int oldEquipmentInfoArraySize = oldEquipmentInfoArray != null ? oldEquipmentInfoArray.length : 0;

					for (int i = 0; i < oldEquipmentInfoArraySize; i++) {
						if (oldEquipmentInfoArray[i].serialNumber.equals(newPrimaryEquipmentInfo.getSerialNumber())) {
							oldEquipmentInfoArray[i].equipmentType = AttributeTranslator.byteFromString(newPrimaryEquipmentInfo.getEquipmentType());
							oldEquipmentInfoArray[i].possession = (byte) 'P';
							oldEquipmentInfoArray[i].equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.UPDATE;
							oldEquipmentInfoArray[i].activateInd = true;
							oldEquipmentInfoArray[i].primaryInd = true;

							changeEquipmentInfoArray = oldEquipmentInfoArray;
						}
						else {
							oldEquipmentInfoArray[i].equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.DELETE;
							oldEquipmentInfoArray[i].activateInd = false;
							oldEquipmentInfoArray[i].primaryInd = false;
						}
					}

					if (changeEquipmentInfoArray == null) {
						// size new array (# of old equipment + 1 for new)
						changeEquipmentInfoArray = new TangoEquipmentInfo[oldEquipmentInfoArraySize + 1];

						// move old equipment to change array and set mode to 'DELETE'
						for (int i = 0; i < oldEquipmentInfoArraySize; i++) {
							changeEquipmentInfoArray[i] = oldEquipmentInfoArray[i];
						}

						// add new equipment
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1] = new TangoEquipmentInfo();
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1].serialNumber = newPrimaryEquipmentInfo.getSerialNumber();
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1].equipmentType = AttributeTranslator.byteFromString(newPrimaryEquipmentInfo.getEquipmentType());
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1].possession = (byte) 'P';
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1].equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.INSERT;
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1].activateInd = true;
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1].primaryInd = true;
					}

					// Change EquipmentInfo
					amdocsUpdateTangoConv.changeEquipmentInfo(changeEquipmentInfoArray);

					return null;
				} catch(ValidateException ve) {
					ApplicationException rootException = new ApplicationException(SystemCodes.AMDOCS, String.valueOf(ve.getErrorInd())
							, ve.getErrorMsg(), "", ve);
					if ( ve.getErrorInd() == 1116210) {
						LOGGER.debug("New Serial Number In Use Exception Occurred", ve);
						String exceptionMsg = "The new serial number is in use. [pBan=" + subscriberInfo.getBanId() + ", pSubscriberId="
						+ subscriberInfo.getSubscriberId() + ", newSerialNumber=" + newPrimaryEquipmentInfo.getSerialNumber() + ", pNewEquipmentType=" + newPrimaryEquipmentInfo.getEquipmentType() + "]";
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.NEW_SERIAL_IS_IN_USE, exceptionMsg, "", rootException);				
					} else {
						throw ve;
					}					
				}			
			}
		});		
	}
	
	@Override
	public void resetCSCSubscription(int ban, String subscriberId, String[] cscFeature,String sessionId)
	throws ApplicationException {
		throw new UnsupportedOperationException("Method is not implemented.");
	}
}
