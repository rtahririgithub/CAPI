/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.ArrayList;
import java.util.Date;

import amdocs.APILink.datatypes.AccumulatedAdditionalChargesInfo;
import amdocs.APILink.datatypes.AccumulatedAdditionalChargesList;
import amdocs.APILink.datatypes.AdjustCallInfo;
import amdocs.APILink.datatypes.UsageDetailsInfo;
import amdocs.APILink.datatypes.UsageDetailsList;
import amdocs.APILink.datatypes.UsageDetailsRequestInfo;
import amdocs.APILink.datatypes.UsageRequestInfo;
import amdocs.APILink.sessions.interfaces.UpdateProductConv;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UsageDao;
import com.telus.cmb.subscriber.utilities.AmdocsConvBeanClassFactory;
import com.telus.eas.subscriber.info.CallInfo;
import com.telus.eas.subscriber.info.CallListInfo;
import com.telus.eas.subscriber.info.CallSummaryInfo;
import com.telus.eas.subscriber.info.UsageProfileAdditionalChargesListInfo;
import com.telus.eas.subscriber.info.UsageProfileListInfo;
import com.telus.eas.subscriber.info.UsageProfilePeriodDetailInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class UsageDaoAmdocsImpl extends AmdocsDaoSupport implements UsageDao {

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.dao.UsageDao#adjustCall(int, java.lang.String, java.lang.String, int, java.util.Date, java.lang.String, double, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void adjustCall(final int ban, final String subscriberId, final String productType, final int billSeqNo, 
			final Date channelSeizureDate, final String messageSwitchId, final double adjustmentAmount, 
			final String adjustmentReasonCode, final String memoText, final String usageProductType, 
			final String sessionId) throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				AdjustCallInfo adjustCallInfo = new AdjustCallInfo();

				adjustCallInfo.billSeqNo = (short) billSeqNo;
				adjustCallInfo.channelSeizureDate = channelSeizureDate;
				adjustCallInfo.messageSwitchId = messageSwitchId;
				adjustCallInfo.isBilled = ((billSeqNo > 0) ? true : false);
				adjustCallInfo.reasonCode = adjustmentReasonCode;
				adjustCallInfo.adjAmt = adjustmentAmount;
				adjustCallInfo.memoUserText = memoText;
				adjustCallInfo.usageProductType = productType;

				UpdateProductConv updateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));
				updateProductConv.setProductPK(ban, subscriberId);
				
				updateProductConv.adjustCall(adjustCallInfo);				
				
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.dao.UsageDao#retrieveUsageProfileList(int, java.lang.String, int)
	 */
	@Override
	public UsageProfileListInfo retrieveUsageProfileList(final int ban, final String subscriberId, final int billSeqNo, final String productType, String sessionId) throws ApplicationException {

		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<UsageProfileListInfo>() {
			
			@Override
			public UsageProfileListInfo doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				UpdateProductConv updateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));
				updateProductConv.setProductPK(ban, subscriberId);
				
		  		amdocs.APILink.datatypes.UsageProfileList usageProfileList = updateProductConv.getUsageProfileList((short)billSeqNo);
		  		amdocs.APILink.datatypes.UsageProfileInfo[] usageProfileInfo = usageProfileList.usageProfileInfo;

		  		int usageProfileInfoSize = usageProfileInfo != null ? usageProfileInfo.length : 0;
		  		com.telus.eas.subscriber.info.UsageProfileInfo[] usageProfiles = new com.telus.eas.subscriber.info.UsageProfileInfo[usageProfileInfoSize];

		  		for (int i = 0; i < usageProfileInfoSize; i++) {
		  			com.telus.eas.subscriber.info.UsageProfileInfo usageProfile = new com.telus.eas.subscriber.info.UsageProfileInfo();
		  			usageProfile.setAirtimeFeatureCode(usageProfileInfo[i].airTimeFeatureCode);
		  			usageProfile.setAirtimeFeatureDescription(usageProfileInfo[i].airTimeFeatureDescription);
		  			usageProfile.setStep(usageProfileInfo[i].step);
		  			usageProfile.setChargeAmount(usageProfileInfo[i].chargeAmount);
		  			usageProfile.setDirection(AttributeTranslator.stringFrombyte(usageProfileInfo[i].direction));
		  			usageProfile.setExtendedHomeArea(usageProfileInfo[i].extendedHmAreaInd);
		  			usageProfile.setFreeMinuteType(AttributeTranslator.stringFrombyte(usageProfileInfo[i].freeMinuteType));
		  			usageProfile.setAllowedIncomingMinutes(usageProfileInfo[i].incomingMinutesAllowed);
		  			usageProfile.setUsedIncomingMinutes(usageProfileInfo[i].incomingMinutesUsed);
		  			usageProfile.setNumberOfCalls(usageProfileInfo[i].numberOfCalls);
		  			usageProfile.setFreeAirCalls(usageProfileInfo[i].numberOfFreeAirCalls);
		  			usageProfile.setNumberOfPeriods(usageProfileInfo[i].numberOfPeriods);
		  			usageProfile.setSpecialCalls(usageProfileInfo[i].specialCalls);
		  			usageProfile.setTotalAirtime(usageProfileInfo[i].subscriberMinutes);
		  			usageProfile.setUnitOfMeasure(AttributeTranslator.stringFrombyte(usageProfileInfo[i].unitOfMeasure));

		  	  		amdocs.APILink.datatypes.PeriodUsageInfo[] periodUsageInfo = usageProfileInfo[i].periodInfoList;
		  	  		int periodUsageInfoSize = periodUsageInfo != null ? usageProfileInfo[i].numberOfPeriods : 0;
		  	  		UsageProfilePeriodDetailInfo[] usageProfilePeriodDetails = new UsageProfilePeriodDetailInfo[periodUsageInfoSize];
		  			for (int j = 0; j < periodUsageInfoSize; j++) {
		  	  			UsageProfilePeriodDetailInfo usageProfilePeriodDetailInfo = new UsageProfilePeriodDetailInfo();
		  	  			usageProfilePeriodDetailInfo.setAllowedIncludedMinutesInPeriod(periodUsageInfo[j].inclusiveMinutesAlowedInPeriod);
		  	  			usageProfilePeriodDetailInfo.setPeriodDescription(periodUsageInfo[j].periodDescription);
		  	  			usageProfilePeriodDetailInfo.setTotalCallsInPeriod(periodUsageInfo[j].totalCallsInPeriod);
		  	  			usageProfilePeriodDetailInfo.setTotalChargeAmountInPeriod(periodUsageInfo[j].totalChargeInPeriod);
		  	  			usageProfilePeriodDetailInfo.setTotalMinutesInPeriod(periodUsageInfo[j].totalMinutesInPeriod);
		  	  			usageProfilePeriodDetailInfo.setUsedIncludedMinutesInPeriod(periodUsageInfo[j].inclusiveMinutesUsedInPeriod);
		  	  			usageProfilePeriodDetails[j] = usageProfilePeriodDetailInfo;
		  			}
		  			usageProfile.setPeriodDetails(usageProfilePeriodDetails);
		  			usageProfiles[i] = usageProfile;
		  		}

		  	  	UsageProfileListInfo finalUsageProfileList = new UsageProfileListInfo();
		  	  	finalUsageProfileList.setUsageProfiles(usageProfiles);

		  	  	return finalUsageProfileList;
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.dao.UsageDao#retrieveUsageProfileAdditionalChargesList(int, java.lang.String, int, java.lang.String)
	 */
	@Override
	public UsageProfileAdditionalChargesListInfo retrieveUsageProfileAdditionalChargesList(final int ban, final String subscriberId, 
			final int billSeqNo, final String productType, String sessionId) throws ApplicationException {
		
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<UsageProfileAdditionalChargesListInfo>() {
			
			@Override
			public UsageProfileAdditionalChargesListInfo doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				UpdateProductConv updateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));
				updateProductConv.setProductPK(ban, subscriberId);

		  		AccumulatedAdditionalChargesList additionalChargesList = updateProductConv.getAccumulatedAdditionalChargesList((short)billSeqNo);
		  		AccumulatedAdditionalChargesInfo[] additionalChargesInfo = additionalChargesList.accumulatedAdditionalChargesInfo;

		  		int usageProfileAdditionalChargesInfoSize = additionalChargesInfo != null ? additionalChargesInfo.length : 0;
		  		com.telus.eas.subscriber.info.UsageProfileAdditionalChargesInfo[] usageProfileAdditionalCharges = new com.telus.eas.subscriber.info.UsageProfileAdditionalChargesInfo[usageProfileAdditionalChargesInfoSize];

		  		for (int i = 0; i < usageProfileAdditionalChargesInfoSize; i++) {
		  			com.telus.eas.subscriber.info.UsageProfileAdditionalChargesInfo additionalCharges = new com.telus.eas.subscriber.info.UsageProfileAdditionalChargesInfo();
		  			additionalCharges.setAirtimeFeatureCode(additionalChargesInfo[i].airTimeFeatureCode);
		  			additionalCharges.setAirtimeFeatureDescription(additionalChargesInfo[i].airTimeFeatureDescription);
		  			additionalCharges.setSecondaryFeatureCode(additionalChargesInfo[i].secondaryFeatureCode);
		  			additionalCharges.setSecondaryFeatureDescription(additionalChargesInfo[i].secondaryFeatureDescription);
		  			additionalCharges.setDirection(AttributeTranslator.stringFrombyte(additionalChargesInfo[i].direction));
		  			additionalCharges.setExtendedHomeArea(additionalChargesInfo[i].extendedHmAreaInd);
		  			additionalCharges.setNumberOfCalls(additionalChargesInfo[i].numberOfCalls);
		  			additionalCharges.setTotalAirtime(additionalChargesInfo[i].subscriberMinutes);
		  			usageProfileAdditionalCharges[i] = additionalCharges;
		  		}
		  	  	UsageProfileAdditionalChargesListInfo finalUsageProfileAdditionalChargesList = new UsageProfileAdditionalChargesListInfo();
		  	  	finalUsageProfileAdditionalChargesList.setUsageProfileAdditionalCharges(usageProfileAdditionalCharges);
		  	  	
		  	  	return finalUsageProfileAdditionalChargesList;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.dao.UsageDao#retrieveBilledCallsList(int, java.lang.String, java.lang.String, int, char, java.util.Date, java.util.Date, boolean, java.lang.String)
	 */
	@Override
	public CallListInfo retrieveBilledCallsList(final int ban, final String subscriberId, final String productType, 
			final int billSeqNo, final char callType, final Date fromDate, final Date toDate, 
			final boolean getAll, String sessionId) throws ApplicationException {

		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CallListInfo>() {
			
			@Override
			public CallListInfo doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				CallListInfo result = new CallListInfo();
				
				UpdateProductConv updateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));
				updateProductConv.setProductPK(ban, subscriberId);

				UsageRequestInfo usageRequestInfo = new UsageRequestInfo();

				usageRequestInfo.billSeqNo = (short)billSeqNo;
				usageRequestInfo.fromDate = fromDate;
				usageRequestInfo.untilDate = toDate;
				usageRequestInfo.pageNumber = 0;
				
				if (CallListInfo.CALL_TYPE_LOCAL == callType) {
					usageRequestInfo.callType = UsageRequestInfo.LOCAL_CALLS;
				} else if (CallListInfo.CALL_TYPE_ROAMING == callType) {
					usageRequestInfo.callType = UsageRequestInfo.ROAMING_CALLS;
				}

				UsageDetailsList usageDetailsList = null;
				ArrayList<CallSummaryInfo> callSummaryArrayList = new ArrayList<CallSummaryInfo>();
				
		  		do {
		  			usageDetailsList = updateProductConv.getBilledCallsList(usageRequestInfo);

		  			// Map the total row count to the CallList object
		  			if (usageDetailsList.pageNumber == 0) {
		  				result.setTotalCallCount(usageDetailsList.totalRowCount);
		  			}

		  			// Map amdocs info classes to telus info classes
		  			for (int i = 0; i < usageDetailsList.usageDetailsInfo.length; i++) {

		  				CallSummaryInfo callSummary = mapUsageDetailInfoToCallSummary(usageDetailsList.usageDetailsInfo[i]);

		  				// Add the CallSummary object to the array list
		  				callSummaryArrayList.add(callSummary);
		  			}
		  			// Check if the request was for all rows
		  			if (!getAll) {
		  				break;
		  			} else {
		  				// Increment the UsageRequestInfo page number
						usageRequestInfo.pageNumber++;
		  			}
		  		} while (usageDetailsList.hasMoreRows);				
				
		  		result.setCallSummaries(callSummaryArrayList.toArray(new CallSummaryInfo[callSummaryArrayList.size()]));
		  		
				return result;
			}
		});
	}

	private CallSummaryInfo mapUsageDetailInfoToCallSummary(UsageDetailsInfo usageDetailsInfo ) {
		
		CallSummaryInfo callSummary = new CallSummaryInfo();

		callSummary.setDate(usageDetailsInfo.channelSeizureDate);
		callSummary.setSwitchId(usageDetailsInfo.messageSwitchId);
		callSummary.setProductType(usageDetailsInfo.productType);
		callSummary.setLocationDescription(usageDetailsInfo.sidDescription);
		callSummary.setLocationProvince(usageDetailsInfo.sidProv);
		callSummary.setLocationCity(usageDetailsInfo.sidCity);
		callSummary.setCallToCity(usageDetailsInfo.callToCityDescription);
		callSummary.setCallToState(usageDetailsInfo.callToStateCode);
		callSummary.setCallToNumber(usageDetailsInfo.callToTn);
		callSummary.setCallDuration(usageDetailsInfo.duration);
		callSummary.setAirtimeChargeAmount(usageDetailsInfo.atChargeAmount);
		callSummary.setTollChargeAmount(usageDetailsInfo.tollChargeAmount);
		callSummary.setAdditionalChargeAmount(usageDetailsInfo.acAmt);
		callSummary.setTaxAmount(usageDetailsInfo.tax);
		callSummary.setCreditedAmount(usageDetailsInfo.creditedAmount);
		callSummary.setPeriodLevel(usageDetailsInfo.periodLevel);
		callSummary.setRoamingTaxTollAmount(usageDetailsInfo.rmTaxAmtToll);
		callSummary.setRoamingTaxAirtimeAmount(usageDetailsInfo.rmTaxAmtAir);
		callSummary.setRoamingTaxAdditionalAmount(usageDetailsInfo.rmTaxAmtAc);
		callSummary.setExtendedHomeArea(usageDetailsInfo.extendedHmAreaInd);
		callSummary.setBillPresentationNumber(usageDetailsInfo.billPresentationNo);
		callSummary.setMessageType( Character.getNumericValue((char)usageDetailsInfo.messageType) );
		callSummary.setCallActionCode(usageDetailsInfo.callActionCode);
		callSummary.setCallTypeFeature(usageDetailsInfo.callTypeFeature);
		callSummary.setLteHspaHandover(usageDetailsInfo.lteHspaHandoverInd);
		return callSummary;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.dao.UsageDao#retrieveCallDetails(int, java.lang.String, java.lang.String, int, java.util.Date, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public CallInfo retrieveCallDetails(final int ban, final String subscriberId, final String productType, final int billSeqNo, 
			final Date channelSeizureDate, final String messageSwitchId, final String callProductType, String sessionId) throws ApplicationException {

		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CallInfo>() {
			
			@Override
			public CallInfo doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				CallInfo result = new CallInfo();
				amdocs.APILink.datatypes.CallInfo callInfo = null;

				UpdateProductConv updateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));
				updateProductConv.setProductPK(ban, subscriberId);
				
		  		if (billSeqNo > 0) {
		  			//*********************
		  			// Note that the below call to getBilledCallsList is required
		  			// before calling getBilledCallDetails.  Calling method retrieveBilledCallsList
		  			// is not an option because we must use the same UpdateProductConv bean.
					UsageRequestInfo usageRequestInfo = new UsageRequestInfo();

					usageRequestInfo.billSeqNo = (short)billSeqNo;
					usageRequestInfo.fromDate = null;
					usageRequestInfo.untilDate = null;
					usageRequestInfo.pageNumber = 0;
					
			  		updateProductConv.getBilledCallsList(usageRequestInfo);
			  		//*********************
		  					  			
		  			// Retrieve billed call details
		  			callInfo = updateProductConv.getBilledCallDetails((short)billSeqNo, channelSeizureDate,
		  					messageSwitchId, callProductType);
		  		} else {
		  			//*********************
		  			// Note that the below call to getUnbilledUsageDetailsList is required
		  			// before calling getUnbilledCallDetails.  Calling method retrieveUnbilledCallsList
		  			// is not an option because we must use the same UpdateProductConv bean.		  			
					UsageRequestInfo usageRequestInfo = new UsageRequestInfo();
					
			  		usageRequestInfo.billSeqNo = 0;
			  		usageRequestInfo.fromDate = null;
			  		usageRequestInfo.untilDate = null;
			  		usageRequestInfo.pageNumber = 0;

		  			updateProductConv.getUnbilledUsageDetailsList(usageRequestInfo);
			  		//*********************		  			
		  			
		  	  		// Populate UsageDetailsRequestInfo
		  			UsageDetailsRequestInfo unbilledUsageDetailsRequestInfo = new UsageDetailsRequestInfo();
		  	  		unbilledUsageDetailsRequestInfo.channelSeizureDate = channelSeizureDate;
		  	  		unbilledUsageDetailsRequestInfo.messageSwitchId = messageSwitchId;
		  	  		unbilledUsageDetailsRequestInfo.productType = callProductType;

		  			// Retrieve unbilled call details
		  			callInfo = updateProductConv.getUnbilledCallDetails(unbilledUsageDetailsRequestInfo);
		  		}
		  		if(callInfo != null){
		  		// Map retrieved (billed or unbilled) call details to the callDetails object
				result.setSerialNumber(callInfo.unitEsn);
				result.setOrigCellTrunkId(callInfo.origCellTrunkId);
				result.setTermCellTrunkId(callInfo.termCellTrunkId);
				result.setTerminationCode(AttributeTranslator.stringFrombyte(callInfo.callTermCode));
				result.setAirtimeServiceCode(callInfo.atSoc);
				result.setAirtimeFeatureCode(callInfo.atFeatureCode);
				result.setTollServiceCode(callInfo.tollSoc);
				result.setTollFeatureCode(callInfo.tollFeatureCode);
				result.setAdditionalChargeServiceCode(callInfo.acSoc);
				result.setOrigRouteDescription(callInfo.origRouteDesc);
				result.setTermRouteDescription(callInfo.termRouteDesc);
				result.setGSTAmount(callInfo.callTaxGstAmt);
				result.setHSTAmount(callInfo.callTaxHstAmt);
				result.setPSTAmount(callInfo.callTaxPstAmt);
				result.setTaxableGSTAmount(callInfo.callTaxableGstAmt);
				result.setTaxableHSTAmount(callInfo.callTaxableHstAmt);
				result.setTaxablePSTAmount(callInfo.callTaxablePstAmt);
				result.setAdjustmentAmount(callInfo.adjustAmt);
				result.setAdjustmentGSTAmount(callInfo.adjustGstAmt);
				result.setAdjustmentHSTAmount(callInfo.adjustHstAmt);
				result.setAdjustmentPSTAmount(callInfo.adjustPstAmt);
				result.setAdjustmentRoamingTaxAmount(callInfo.adjustRoamTaxAmt);

				ArrayList<String> featureCodes = new ArrayList<String>();
				if (callInfo.mpsFeatureCode1 != null && !callInfo.mpsFeatureCode1.equalsIgnoreCase("")) {
					featureCodes.add(callInfo.mpsFeatureCode1);
				}
				if (callInfo.mpsFeatureCode2 != null && !callInfo.mpsFeatureCode2.equalsIgnoreCase("")) {
					featureCodes.add(callInfo.mpsFeatureCode2);
				}
				if (callInfo.mpsFeatureCode3 != null && !callInfo.mpsFeatureCode3.equalsIgnoreCase("")) {
					featureCodes.add(callInfo.mpsFeatureCode3);
				}
				if (callInfo.mpsFeatureCode4 != null && !callInfo.mpsFeatureCode4.equalsIgnoreCase("")) {
					featureCodes.add(callInfo.mpsFeatureCode4);
				}
				if (callInfo.mpsFeatureCode5 != null && !callInfo.mpsFeatureCode5.equalsIgnoreCase("")) {
					featureCodes.add(callInfo.mpsFeatureCode5);
				}
				if (callInfo.mpsFeatureCode6 != null && !callInfo.mpsFeatureCode6.equalsIgnoreCase("")) {
					featureCodes.add(callInfo.mpsFeatureCode6);
				}

				result.setFeatureCodes(featureCodes.toArray(new String[featureCodes.size()]));
		  		}
				return result;
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.dao.UsageDao#retrieveUnbilledCallsList(int, java.lang.String, java.lang.String, java.util.Date, java.util.Date, boolean, java.lang.String)
	 */
	@Override
	public CallListInfo retrieveUnbilledCallsList(final int ban, final String subscriberId, final String productType, 
			final Date fromDate, final Date toDate, final boolean getAll, String sessionId) throws ApplicationException {

		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CallListInfo>() {
			
			@Override
			public CallListInfo doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

			  	CallListInfo result = new CallListInfo();
				ArrayList<CallSummaryInfo> callSummaryArrayList = new ArrayList<CallSummaryInfo>();
			  	UsageDetailsList unbilledUsageDetailsList = null;
				
				UpdateProductConv updateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));
				updateProductConv.setProductPK(ban, subscriberId);
				
				UsageRequestInfo usageRequestInfo = new UsageRequestInfo();
				
		  		usageRequestInfo.billSeqNo = 0;
		  		usageRequestInfo.fromDate = fromDate;
		  		usageRequestInfo.untilDate = toDate;
		  		usageRequestInfo.pageNumber = 0;

		  		// Retrieve the UsageDetailsList page at least once
		  		do {

		  			// Retrieve list of unbilled calls
		  			unbilledUsageDetailsList = updateProductConv.getUnbilledUsageDetailsList(usageRequestInfo);

		  			// Map the total row count to the CallList object
		  			if (unbilledUsageDetailsList.pageNumber == 0) {
		  				result.setTotalCallCount(unbilledUsageDetailsList.totalRowCount);
		  			}

		  			// Map amdocs info classes to telus info classes
		  			for (int i = 0; i < unbilledUsageDetailsList.usageDetailsInfo.length; i++) {

		  				CallSummaryInfo callSummary = mapUsageDetailInfoToCallSummary( unbilledUsageDetailsList.usageDetailsInfo[i]);

		  				// print(getClass().getName(), methodName, ("callSummaries[" + i + "]:"));
		  				// print(getClass().getName(), methodName, (callSummary.toString()));

		  				// Add the CallSummary object to the array list
		  				callSummaryArrayList.add(callSummary);
		  			}
		  			// Check if the request was for all rows
		  			if (!getAll) {
		  				break;
		  			} else {
		  				// Increment the UsageRequestInfo page number
						usageRequestInfo.pageNumber++;
		  			}
		  		} while (unbilledUsageDetailsList.hasMoreRows);

		  		// Set the mapped CallSummary array to the CallList object
		  		result.setCallSummaries((CallSummaryInfo[])callSummaryArrayList.toArray(new CallSummaryInfo[callSummaryArrayList.size()]));
				
				return result;
			}
		});
	}
}
