package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.ActivityInfo;
import amdocs.APILink.datatypes.CancelInfo;
import amdocs.APILink.datatypes.ContractInfo;
import amdocs.APILink.datatypes.MoveSubscriberInfo;
import amdocs.APILink.datatypes.ServiceInfo;
import amdocs.APILink.datatypes.SocInfo;
import amdocs.APILink.datatypes.UpdateProductAdditionalInfo;
import amdocs.APILink.sessions.interfaces.ResourceMgmtServices;
import amdocs.APILink.sessions.interfaces.UpdateProductConv;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.Subscriber;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.amdocs.AmdocsConvBeanWrapper;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public abstract class UpdateSubscriberDaoImpl extends SubscriberDaoImpl {

	private final Logger LOGGER = Logger.getLogger(UpdateSubscriberDaoImpl.class);

	protected String[] retrieveAvailablePhoneNumbers(final Class<? extends UpdateProductConv> updateProductConv, final int ban,
			final String subscriberId, final PhoneNumberReservation phoneNumberReservation, final int maxNumbers,
			String sessionId) throws ApplicationException {
		String[] phoneNumbers = super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<String[]>() {

			@Override
			public String[] doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {

				UpdateProductConv updateConv = transactionContext.createBean(updateProductConv);

				updateConv.setProductPK(ban, subscriberId);

				return retrievePhoneNumbers(updateConv, ban, phoneNumberReservation, maxNumbers);
			}
		});
		return super.returnAvailablePhoneNumbers(phoneNumbers);
	}

	protected void changePhoneNumber(final Class<? extends UpdateProductConv> updateProductConv
			, final SubscriberInfo subscriberInfo
			, final AvailablePhoneNumberInfo newPhoneNumber, final String reasonCode
			, final String dealerCode, final String salesRepCode
			, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateProductConv updateConv = transactionContext.createBean(updateProductConv);

				updateConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());

				// Check that phone number is available
				LOGGER.debug("calling getAvailableSubscriberList()...");
				String[] phoneNumbersNonAsian = AmdocsConvBeanWrapper.getAvailableSubscriberList(updateConv, newPhoneNumber.getNumberGroup0().getCode(),"0",newPhoneNumber.getPhoneNumber(),false);
				String[] phoneNumbersAsian = AmdocsConvBeanWrapper.getAvailableSubscriberList(updateConv, newPhoneNumber.getNumberGroup0().getCode(),"0",newPhoneNumber.getPhoneNumber(),true);
				if (phoneNumbersNonAsian == null && phoneNumbersAsian == null) {
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.PHONE_NUMBER_NOT_AVAILABLE, "The supplied phone number is not available.[" +newPhoneNumber.getPhoneNumber() + "]", "");
				}

				if (dealerCode!=null) {
					ContractInfo ci = new ContractInfo();
					if (salesRepCode == null) {
						ci.salesCode = "0000"; 
					} else {
						ci.salesCode = salesRepCode;
					}

					ci.dealerCode = dealerCode;
					AmdocsConvBeanWrapper.changeSubscriberNumber(updateConv, newPhoneNumber.getPhoneNumber(), reasonCode, ci);
				} else {
					AmdocsConvBeanWrapper.changeSubscriberNumber(updateConv, newPhoneNumber.getPhoneNumber(), reasonCode);
				}

				return null;
			}

		});
	}

	protected void moveSubscriber(final Class<? extends UpdateProductConv> updateProductConv, 
			final SubscriberInfo subscriberInfo, final int targetBan,
			final Date activityDate, final boolean transferOwnership,
			final String activityReasonCode, final String userMemoText, final String dealerCode,
			final String salesRepCode, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateProductConv updateConv = transactionContext.createBean(updateProductConv);
				updateConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());


				MoveSubscriberInfo moveSubscriberInfo = new MoveSubscriberInfo();
				// Populate MoveSubscriberInfo
				moveSubscriberInfo.targetBan = targetBan;
				moveSubscriberInfo.activityDate = activityDate;
				moveSubscriberInfo.activityReason = activityReasonCode;
				moveSubscriberInfo.memoText = userMemoText == null ? new String("") : userMemoText;
				if (dealerCode != null && salesRepCode != null) {
					moveSubscriberInfo.dealerCode = dealerCode;
					moveSubscriberInfo.salesCode = salesRepCode;
				}

				// store
				if (transferOwnership) {					
					LOGGER.debug("Excecuting moveSubscriberChangeOwnership() - start...");
					updateConv.moveSubscriberChangeOwnership(moveSubscriberInfo);
					LOGGER.debug("Excecuting moveSubscriberChangeOwnership() - end...");
				} else {
					LOGGER.debug("Excecuting moveSubscriberAddToMasterBill() - start...");
					updateConv.moveSubscriberAddToMasterBill(moveSubscriberInfo);
					LOGGER.debug("Excecuting moveSubscriberAddToMasterBill() - end...");
				}

				return null;
			}

		});
	}

	protected void cancelPortedInSubscriber(
			final Class<? extends UpdateProductConv> updateProductConv,
			final int ban, 
			final String subscriberId,
			final String deactivationReason, 
			final Date activityDate, 
			final String portOutInd,
			final boolean isBrandPort, 
			String sessionId) throws ApplicationException {
		
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				CancelInfo cancelInfo = new CancelInfo();

				UpdateProductConv updateConv = transactionContext.createBean(updateProductConv);

				// Set ProductPK
				updateConv.setProductPK(ban, subscriberId);

				// Populate CancelInfo
				cancelInfo.activityDate = activityDate == null ? new Date() : activityDate;
				cancelInfo.activityReason = deactivationReason;
				cancelInfo.userText = "";
				cancelInfo.depositReturnMethod = 'O';
				cancelInfo.waiveReason = "";
				
				cancelInfo.isPortActivity = portOutInd.equals("Y") ? true : false;
				
				cancelInfo.isBrandPortActivity = isBrandPort;

				// store
				LOGGER.debug("Excecuting cancelSubscriber() - start...");
				updateConv.cancelSubscriber(cancelInfo);
				LOGGER.debug("Excecuting cancelSubscriber() - end...");

				return null;

			}
		});
	}

	protected void setPortTypeToPortIn(final Class<? extends UpdateProductConv> updateProductConv, 
			final int ban, final String subscriberId, final Date sysDate,
			String sessionId) throws ApplicationException {

		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {	
				UpdateProductConv updateConv = transactionContext.createBean(updateProductConv);

				// Set ProductPK (which also retrieves the BAN)
				updateConv.setProductPK(ban, subscriberId);

				UpdateProductAdditionalInfo info = new UpdateProductAdditionalInfo();
				byte[] tmp = (Subscriber.PORT_TYPE_PORT_IN).getBytes();
				info.portType = tmp[0];
				info.portDate = sysDate;
				info.updatePortInfoInd = true;

				updateConv.changeSubscriber(info);
				return null;
			}
		});
	}

	public void setPortTypeToSnapback(final String resourceNumber, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				ResourceMgmtServices amdocsResourceMgmtServices = transactionContext.createBean(ResourceMgmtServices.class);

				amdocsResourceMgmtServices.snapbackMdn(resourceNumber);

				return null;
			}			
		});
	}


	protected void suspendPortedInSubscriber(final Class<? extends UpdateProductConv> updateProductConv
			, final int ban, final String subscriberId,
			final String deactivationReason, final Date activityDate, final String portOutInd, String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(updateProductConv);
				// Set ProductPK
				amdocsUpdateProductConv.setProductPK(ban, subscriberId);

				ActivityInfo activityInfo = new ActivityInfo();
				// Populate ActivityInfo
				activityInfo.activityDate = activityDate == null ? new Date() : activityDate;
				activityInfo.activityReason = deactivationReason;
				activityInfo.userText = "";
				activityInfo.isPortActivity = portOutInd.equals("Y") ? true : false;

				// store
				LOGGER.debug("Excecuting suspendSubscriber() - start...");
				amdocsUpdateProductConv.suspendSubscriber(activityInfo);
				LOGGER.debug("Excecuting suspendSubscriber() - end...");

				return null;
			}
		});

	}

	protected void resetVoiceMailPassword(final Class<? extends UpdateProductConv> updateProductConv, 
			final int ban, final String subscriberId, final String[] voiceMailSocAndFeature, String sessionId) 
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(updateProductConv);
				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateProductConv.setProductPK(ban, subscriberId);

				SocInfo soc = new SocInfo();
				amdocs.APILink.datatypes.ServiceFeatureInfo[] feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[1];
				ServiceInfo[] serviceInfo = new ServiceInfo[1];

				// add/replace 'reset password' parameter
				String featureParameters = voiceMailSocAndFeature[2] == null ? "" : voiceMailSocAndFeature[2].trim();
				if (featureParameters.equals("")) {
					featureParameters = "RES-PASS=Y@"; // no parameters
				}
				else {
					if (featureParameters.indexOf("RES-PASS=") == -1)
						featureParameters = featureParameters + "RES-PASS=Y@"; // 'reset password' parameter not in list of parms
					else
						featureParameters = AttributeTranslator.replaceString(featureParameters, "RES-PASS=N", "RES-PASS=Y"); // set 'reset password' parameter to 'Yes'
				}

				feature[0] = new amdocs.APILink.datatypes.ServiceFeatureInfo();
				feature[0].featureCode = voiceMailSocAndFeature[1].trim();
				feature[0].ftrParam = featureParameters;
				feature[0].transactionType = amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE;

				soc.soc = voiceMailSocAndFeature[0].trim();
				soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;

				serviceInfo[0] = new ServiceInfo();
				serviceInfo[0].soc = soc;
				serviceInfo[0].feature = feature;

				// print serviceInfo
				LOGGER.debug("Services passed into for Subscriber " + subscriberId + ":");
				for (int i=0; i < serviceInfo.length; i++) {
					LOGGER.debug("Regular SOC    : " + serviceInfo[i].soc.soc + " transactionType:" + AttributeTranslator.stringFrombyte(serviceInfo[i].soc.transactionType) + " effDate:" + serviceInfo[i].soc.effDate + " expDate:" + serviceInfo[i].soc.expDate);
					if (serviceInfo[i].feature != null) {
						for (int j=0; j < serviceInfo[i].feature.length; j++) {
							LOGGER.debug("     Feature: " + serviceInfo[i].feature[j].featureCode + " transactionType:" + AttributeTranslator.stringFrombyte(serviceInfo[i].feature[j].transactionType) + " ftrParam:" + serviceInfo[i].feature[j].ftrParam  + " /msisdn:" + serviceInfo[i].feature[j].msisdn);
						}
					}
				}

				amdocsUpdateProductConv.changeServiceAgreement(serviceInfo);

				return null;
			}
		});

	}
	
	protected void resetCSCSubscription(final Class<? extends UpdateProductConv> updateProductConv, final int ban, final String subscriberId, final String[] cscFeature, final String sessionId) throws ApplicationException
	{
		super.getAmdocsTemplate().execute(sessionId,new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {
			UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(updateProductConv);
			// Set ProductPK (which also retrieves the BAN)
			amdocsUpdateProductConv.setProductPK(ban, subscriberId);

			SocInfo soc = new SocInfo();
			amdocs.APILink.datatypes.ServiceFeatureInfo[] feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[1];
			ServiceInfo[] serviceInfo = new ServiceInfo[1];

			// add/replace 'reset’ parameter
			String featureParameters = cscFeature[2] == null ? "" : cscFeature[2].trim();
			if (featureParameters.equals("")) {
			  // no parameters
			  featureParameters = "RES-NABEME=Y@"; 
			} else {
			  if (featureParameters.indexOf("RES-NABEME=") == -1) {
			    // 'reset CSC' parameter not in list of params
			    featureParameters = featureParameters + "RES-NABEME=Y@"; 
			  } else {
			    // set 'reset password' parameter to 'Yes'

			    featureParameters = AttributeTranslator.replaceString(featureParameters, "RES-NABEME=N", "RES-NABEME=Y");
			  }
			}

			feature[0] = new amdocs.APILink.datatypes.ServiceFeatureInfo();
			feature[0].featureCode = cscFeature[1].trim();
			feature[0].ftrParam = featureParameters;
			feature[0].transactionType = amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE;

			soc.soc = cscFeature[0].trim();
			soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;

			serviceInfo[0] = new ServiceInfo();
			serviceInfo[0].soc = soc;
			serviceInfo[0].feature = feature;

			amdocsUpdateProductConv.changeServiceAgreement(serviceInfo);

			return null;
			}
			}); 
	}
}
