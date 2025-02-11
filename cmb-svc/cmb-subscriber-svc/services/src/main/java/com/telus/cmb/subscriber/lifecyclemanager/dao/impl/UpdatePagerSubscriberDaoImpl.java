package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.ContractInfo;
import amdocs.APILink.datatypes.PagerEquipmentInfo;
import amdocs.APILink.datatypes.ProductServicesInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.UpdatePagerConv;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdatePagerSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateSubscriberDao;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

@Deprecated
public class UpdatePagerSubscriberDaoImpl extends UpdateSubscriberDaoImpl
implements UpdatePagerSubscriberDao, UpdateSubscriberDao {
	private static final Logger LOGGER = Logger.getLogger(UpdatePagerSubscriberDaoImpl.class);
	private Class<UpdatePagerConv> updatePagerConv = UpdatePagerConv.class;

	@Override
	public String[] retrieveAvailablePhoneNumbers(int ban, String subscriberId,
			PhoneNumberReservationInfo phoneNumberReservation, int maxNumbers,
			String sessionId) throws ApplicationException {
		return super.retrieveAvailablePhoneNumbers(updatePagerConv, ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
	}

	@Override
	public void changePhoneNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			String dealerCode, String salesRepCode, String sessionId)
	throws ApplicationException {
		super.changePhoneNumber(updatePagerConv, subscriberInfo, newPhoneNumber, reasonCode, dealerCode, salesRepCode, sessionId);

	}

	@Override
	public void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan,
			Date activityDate, boolean transferOwnership,
			String activityReasonCode, String userMemoText, String dealerCode,
			String salesRepCode, String sessionId) throws ApplicationException {
		super.moveSubscriber(updatePagerConv, subscriberInfo, targetBan, activityDate
				, transferOwnership, activityReasonCode, userMemoText, dealerCode, salesRepCode, sessionId);

	}

	@Override
	public void sendTestPage(final int ban, final String subscriberId, String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {

				UpdatePagerConv amdocsUpdatePagerConv = transactionContext.createBean(UpdatePagerConv.class);
				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdatePagerConv.setProductPK(ban,subscriberId);

				// sending test page
				amdocsUpdatePagerConv.sendTestPage();

				return null;
			}

		});

	}

	@Override
	public void resetVoiceMailPassword(int ban, String subscriberId,
			String[] voiceMailSocAndFeature, String sessionId)
	throws ApplicationException {
		super.resetVoiceMailPassword(updatePagerConv, ban, subscriberId, voiceMailSocAndFeature, sessionId);
	}

	@Override
	public void changeSerialNumberAndMaybePricePlan(
			final SubscriberInfo subscriberInfo,
			final EquipmentInfo newPrimaryEquipmentInfo,
			final EquipmentInfo[] newSecondaryEquipmentInfo,
			final SubscriberContractInfo subscriberContractInfo, final String dealerCode,
			final String salesRepCode, PricePlanValidationInfo pricePlanValidation,
			String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdatePagerConv amdocsUpdatePagerConv = transactionContext.createBean(UpdatePagerConv.class);
				
				PagerEquipmentInfo pagerEquipmentInfo = new PagerEquipmentInfo();

				try {
					// print input to this method
					LOGGER.debug("Input for subscriber: ban = [" + subscriberInfo.getBanId() + "], subscriberId = [" + subscriberInfo.getSubscriberId() + "]...");
					LOGGER.debug("newPrimaryEquipmentInfo: [");
					LOGGER.debug("    formattedCapCode: [" + newPrimaryEquipmentInfo.getFormattedCapCode() + "]");
					LOGGER.debug("    equipmentType: [" + newPrimaryEquipmentInfo.getEquipmentType() + "]");
					LOGGER.debug("    currentCoverageRegionCode: [" + newPrimaryEquipmentInfo.getCurrentCoverageRegionCode() + "]");
					LOGGER.debug("]");
					if (newSecondaryEquipmentInfo != null) {
						for (int i = 0; i < newSecondaryEquipmentInfo.length; i++) {
							LOGGER.debug("newSecondaryEquipmentInfo[" + i + "]: [");
							LOGGER.debug("    formattedCapCode: [" + newSecondaryEquipmentInfo[i].getFormattedCapCode() + "]");
							LOGGER.debug("    equipmentType: [" + newSecondaryEquipmentInfo[i].getEquipmentType() + "]");
							LOGGER.debug("    currentCoverageRegionCode: [" + newSecondaryEquipmentInfo[i].getCurrentCoverageRegionCode() + "]");
							LOGGER.debug("]");
						}
					} else {
						LOGGER.debug("newSecondaryEquipmentInfo is null.");
					}

					// Set ProductPK (which also retrieves the BAN)
					amdocsUpdatePagerConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());

					// populate new/changed equipment array
					PagerEquipmentInfo[] changeEquipmentInfoArray = DaoSupport.populateChangeEquipmentArray(amdocsUpdatePagerConv.getEquipmentInfo(), newPrimaryEquipmentInfo, newSecondaryEquipmentInfo);

					// print input to Amdocs
					LOGGER.debug("Array to be passed to API Link: ");
					for (int i = 0; i < changeEquipmentInfoArray.length; i++) {
						StringBuffer sb = new StringBuffer();

						sb.append("changeEquipmentInfoArray[").append(i).append("]: {\n");
						sb.append("    equipmentMode=[").append(AttributeTranslator.stringFrombyte(changeEquipmentInfoArray[i].equipmentMode)).append("]\n");
						sb.append("    equipmentType=[").append(changeEquipmentInfoArray[i].equipmentType).append("]\n");
						sb.append("    serialNumber=[").append(changeEquipmentInfoArray[i].serialNumber).append("]\n");
						sb.append("    coverageRegion=[").append(changeEquipmentInfoArray[i].coverageRegion).append("]\n");
						sb.append("    activateInd=[").append(changeEquipmentInfoArray[i].activateInd).append("]\n");
						sb.append("    primaryInd=[").append(changeEquipmentInfoArray[i].primaryInd).append("]\n");
						sb.append("    possession=[").append(changeEquipmentInfoArray[i].possession).append("]\n");
						sb.append("}\n");

						LOGGER.debug(sb.toString());
					}

					// change Equipment and Priceplan (and remove/add regular socs if required)
					if (subscriberContractInfo != null) {
						ProductServicesInfo productServicesInfo = DaoSupport.mapTelusContractToAmdocsProductServices(amdocsUpdatePagerConv, subscriberInfo, subscriberContractInfo, true, true, false);

						LOGGER.debug("Calling changePricePlan()...");
						ContractInfo contractInfo = new ContractInfo();
						if (dealerCode != null && !dealerCode.equals("")) {
							contractInfo.dealerCode = dealerCode;
							contractInfo.salesCode = salesRepCode == null ? "" : salesRepCode;
							LOGGER.debug("Calling changePricePlan() with dealerCode = " + contractInfo.dealerCode + " and salesCode = " + contractInfo.salesCode + "...");
							amdocsUpdatePagerConv.changePricePlan(productServicesInfo, contractInfo);
						}
						else {
							LOGGER.debug("Calling changePricePlan()...");
							amdocsUpdatePagerConv.changePricePlan(productServicesInfo);
						}
					}

					LOGGER.debug("Calling changeEquipmentInfo()...");
					amdocsUpdatePagerConv.changeEquipmentInfo(changeEquipmentInfoArray);
					
					return null;
				} catch(ValidateException ve) {
					ApplicationException rootException = new ApplicationException(SystemCodes.AMDOCS, String.valueOf(ve.getErrorInd())
							, ve.getErrorMsg(), "", ve);
					if ( ve.getErrorInd() == 1116210) {
						LOGGER.debug("New Serial Number In Use Exception Occurred", ve);
						String exceptionMsg = "The new serial number is in use. [pBan=" + subscriberInfo.getBanId() + ", pSubscriberId="
						+ subscriberInfo.getSubscriberId() + ", newSerialNumber=" + newPrimaryEquipmentInfo.getSerialNumber() + ", pNewEquipmentType=" + newPrimaryEquipmentInfo.getEquipmentType() + "]";
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.NEW_SERIAL_IS_IN_USE, exceptionMsg, "", rootException);
					} else if (ve.getErrorInd() == 1117000) {
						LOGGER.debug(ve.getErrorMsg(), ve);
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.APP20003, ve.getErrorMsg(), "", rootException);						
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
