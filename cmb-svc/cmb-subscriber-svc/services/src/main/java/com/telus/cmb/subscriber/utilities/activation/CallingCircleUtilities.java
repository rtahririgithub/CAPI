package com.telus.cmb.subscriber.utilities.activation;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.account.ContractFeature;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.subscriber.utilities.FeatureTransactionContext;
import com.telus.eas.framework.info.BillingCycle;
import com.telus.eas.framework.info.Info;
import com.telus.eas.subscriber.info.CallingCircleCommitmentAttributeDataInfo;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.utility.info.FeatureInfo;

/**
 * This class capture following calling circle logic - pre-populate calling
 * circle list to feature with empty calling circle list - evaluate calling
 * circle feature commitment data attribute
 * 
 */
public class CallingCircleUtilities {

	private static final Log logger = LogFactory.getLog(CallingCircleUtilities.class);

	private int maxAllowedChangesPerPeriod;
	private Date logicalDate;
	private BillingCycle currentCycle;
	private boolean isPostpaid;

	private static String[] CALLING_CIRCLE_SWITCH_CODES = new String[] { FeatureInfo.SWITCH_CODE_CALLING_CIRCLE, FeatureInfo.SWITCH_CODE_CALL_HOME_FREE };

	public CallingCircleUtilities(int maxAllowedChangesPerPeriod, Date logicalDate, BillingCycle billCycle, boolean isPostpaid) {
		this.logicalDate = logicalDate;
		this.currentCycle = billCycle;
		this.maxAllowedChangesPerPeriod = maxAllowedChangesPerPeriod;
		this.isPostpaid = isPostpaid;
	}

	public CallingCircleUtilities(int maxAllowedChangesPerPeriod, Date logicalDate, int billCycleCloseDay, boolean isPostpaid) {
		this(maxAllowedChangesPerPeriod, logicalDate, new BillingCycle(logicalDate, billCycleCloseDay), isPostpaid);
	}

	private boolean isCCListStillOnContract(FeatureParameterHistoryInfo parameterInfo, FeatureTransactionContext[] existingCCFeatures) {

		if (existingCCFeatures != null) {
			for (FeatureTransactionContext exisingCCFeature : existingCCFeatures) {
				if (exisingCCFeature.isSameFeature(parameterInfo)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return the most recent non empty calling circle parameter. Can return
	 * null if all parameter contain empty phone number list.
	 * 
	 * @param parameters
	 * @return
	 */
	private FeatureParameterHistoryInfo pickMostRecentCallingCircleList(FeatureParameterHistoryInfo[] parameters, FeatureTransactionContext[] existingCCFeatures) {

		for (FeatureParameterHistoryInfo parameterInfo : parameters) {

			// ignore feature parameter record if it belongs to feature that
			// still on contract
			if (isCCListStillOnContract(parameterInfo, existingCCFeatures)) {
				if (logger.isInfoEnabled())
					logger.info(parameterInfo + " belongs to service feature still on contract, ignored.");
				continue;
			}

			if (parameterInfo.getParameterValue() != null && parameterInfo.getParameterValue().trim().length() > 0) {
				if (parameterInfo.getParameterValue() != null) {
					return parameterInfo;
				}
			}
		}
		return null;
	}

	private FeatureTransactionContext pickMostVacantCallingCircleFeature(FeatureTransactionContext[] emptyCCFeatures) {
		FeatureTransactionContext featureToBePopulated = null;
		for (FeatureTransactionContext feature : emptyCCFeatures) {
			if (featureToBePopulated == null) {
				featureToBePopulated = feature;
			} else if (feature.getServiceFeatureInfo().getCallingCircleSize() > featureToBePopulated.getServiceFeatureInfo().getCallingCircleSize()) {
				featureToBePopulated = feature;
			}
		}

		return featureToBePopulated;
	}

	private void prepopulateCallingCircleInfo(FeatureTransactionContext featureTxn, FeatureParameterHistoryInfo paramInfo) throws ApplicationException {

		String[] ccNumbers = (paramInfo == null) ? new String[0] : paramInfo.getParameterValue().split(";");

		ServiceFeatureInfo serviceFeatureInfo = featureTxn.getServiceFeatureInfo();
		if (serviceFeatureInfo.getCallingCircleSize() <= 0) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Something is wrong: calling-circle feature[" + serviceFeatureInfo + "]'s ccSize is:"
					+ serviceFeatureInfo.getCallingCircleSize(), "");
		}

		String[] phoneNumbers = ccNumbers;
		if (phoneNumbers.length > serviceFeatureInfo.getCallingCircleSize()) {
			// make sure we don't exceed the calling circle size on the target
			// feature
			phoneNumbers = new String[serviceFeatureInfo.getCallingCircleSize()];
			System.arraycopy(ccNumbers, 0, phoneNumbers, 0, phoneNumbers.length);
		}
		serviceFeatureInfo.setCallingCirclePhoneNumberList(phoneNumbers);

		Date featureEffectiveDate = featureTxn.getEffectiveDate();
		// Create dummy CallingCircleParameter to reflect what it is going to be
		// returned by KB should this change get committed.
		CallingCircleParametersInfo ccp = new CallingCircleParametersInfo();
		if (DateUtil.isAfter(featureEffectiveDate, logicalDate)) { // future
																	// dated
			CallingCirclePhoneListInfo futurePhoneList = new CallingCirclePhoneListInfo();
			futurePhoneList.setPhoneNumberList(phoneNumbers);
			futurePhoneList.setEffectiveDate(featureEffectiveDate);
			ccp.setCallingCircleFuturePhoneNumberList(futurePhoneList);

			// current list will be empty for all attributes
			CallingCirclePhoneListInfo currentPhoneList = new CallingCirclePhoneListInfo();
			ccp.setCallingCircleCurrentPhoneNumberList(currentPhoneList);

		} else { // back dated or current date
			CallingCirclePhoneListInfo currentPhoneList = new CallingCirclePhoneListInfo();
			currentPhoneList.setPhoneNumberList(phoneNumbers);
			currentPhoneList.setEffectiveDate(featureEffectiveDate);
			ccp.setCallingCircleCurrentPhoneNumberList(currentPhoneList);

			CallingCirclePhoneListInfo futurePhoneList = new CallingCirclePhoneListInfo();
			// shall we align the future list (pending list) with the current
			// list? KB is not going to return anything
			futurePhoneList.setPhoneNumberList(phoneNumbers);
			futurePhoneList.setEffectiveDate(currentPhoneList.getEffectiveDate());
			ccp.setCallingCircleFuturePhoneNumberList(futurePhoneList);
		}
		serviceFeatureInfo.setCallingCirclParameters(ccp);

		populateCommitmentData(featureTxn);

		// replicate the information from prepaid feature to the mapped KB
		// feature
		if (featureTxn.isPrepaid() && featureTxn.getAssociatedFeature() != null) {
			featureTxn.getAssociatedFeature().getServiceFeatureInfo().copyCallingCircleInfo(serviceFeatureInfo);
		}
	}

	private FeatureTransactionContext[] getExistingCCFeatures(SubscriberContractInfo contractInfo, FeatureTransactionContext[] emptyCCFeatures) {

		@SuppressWarnings("unchecked")
		List<ServiceFeatureInfo> list = contractInfo.getCallingCircleFeatures(false, true);

		Iterator<ServiceFeatureInfo> it = list.iterator();
		while (it.hasNext()) {
			ServiceFeatureInfo serviceFeatureInfo = it.next();
			if (serviceFeatureInfo.getServiceSequenceNo() == null) {// this is a
																	// new
																	// instance
																	// or
																	// prepaid
																	// CC
				it.remove();
			} else {
				for (FeatureTransactionContext featureCtx : emptyCCFeatures) {
					if (featureCtx.getServiceFeatureInfo() == serviceFeatureInfo) {
						it.remove();
						break;
					}
				}
			}
		}

		return FeatureTransactionContext.newFeatureTransactionContexts(list.toArray(new ServiceFeatureInfo[list.size()]), contractInfo, false, logicalDate);
	}

	/**
	 * @param emptyCCFeatures
	 *            - features that contain empty calling circle list
	 * @param featureParameters
	 *            - feature parameters retrieved from service_feature_parameters
	 *            table
	 */
	public void populateCallingCircleInfo(FeatureTransactionContext[] emptyCCFeatures, FeatureParameterHistoryInfo[] featureParameters, SubscriberContractInfo contractInfo)
			throws ApplicationException {

		FeatureTransactionContext[] existingCCFeatures = getExistingCCFeatures(contractInfo, emptyCCFeatures);

		FeatureParameterHistoryInfo paramInfo = pickMostRecentCallingCircleList(featureParameters, existingCCFeatures);

		// FR says we only populate one empty calling circle feature,
		FeatureTransactionContext featureToBePopulated = pickMostVacantCallingCircleFeature(emptyCCFeatures);

		if (logger.isInfoEnabled()) {
			logger.info("prepopulate " + featureToBePopulated + " with " + paramInfo);
		}

		prepopulateCallingCircleInfo(featureToBePopulated, paramInfo);
	}

	/**
	 * if a record effectiveDate = initial list's expirationDate+1 and
	 * parameterValue is same as as the initial list, then we know initial
	 * record's parameter value is result of KB's back-fill logic.
	 * 
	 * @param initialRecord
	 *            the record's effective date is same as service effective date
	 * @param secondRecord
	 *            - the record get take after initial record.
	 * @return
	 */
	protected boolean isInitialRecordBackfilled(FeatureParameterHistoryInfo initialRecord, FeatureParameterHistoryInfo secondRecord) {
		boolean result = false;
		if (secondRecord != null) {
			result = secondRecord.getEffectiveDate().equals(DateUtil.addDay(initialRecord.getExpirationDate(), 1))
					&& Info.compare(initialRecord.getParameterValue(), secondRecord.getParameterValue());
		}

		return result;
	}

	private abstract class CommitmentDataEvaluator {
		abstract void evaluate(FeatureTransactionContext feature, FeatureParameterHistoryInfo[] parameterHistory);
	}

	private class PostpaidEvaluator extends CommitmentDataEvaluator {

		private Date currentPeriodStartDate;
		private Date nextPeriodStartDate;

		private PostpaidEvaluator() {
			this.currentPeriodStartDate = currentCycle.getStartDate();
			this.nextPeriodStartDate = currentCycle.getNextCycleStartDate();
		}

		private void finalizeCommitmentData(FeatureTransactionContext feature, boolean hasInitialList) {

			CallingCircleCommitmentAttributeDataInfo ccCommitment = feature.getServiceFeatureInfo().getCallingCircleCommitmentAttributeData0();
			int modificationCount = ccCommitment.getModificationCount();

			Date pendingListEffDate = ccCommitment.getEffectiveDate();
			if (pendingListEffDate != null && pendingListEffDate.equals(feature.getEffectiveDate())) {
				// this must be future date transaction
				// do nothing, but preserve the date.
				return;
			}

			Date paramEffectiveDate = null;
			boolean effectImmediate = false;
			if (modificationCount == 0) { // no modification in current period
				if (hasInitialList == false) {
					// This is the case where CC feature was added without CC
					// list, new CC list will be effective immediately
					paramEffectiveDate = logicalDate;
					effectImmediate = true;
				} else {
					if (maxAllowedChangesPerPeriod <= 0) { // this is rollback
															// mode
						paramEffectiveDate = nextPeriodStartDate;
					} else {
						// next day
						paramEffectiveDate = DateUtil.addDay(logicalDate, 1);
					}
				}
			} else {
				if (modificationCount < maxAllowedChangesPerPeriod) {
					// next day
					paramEffectiveDate = DateUtil.addDay(logicalDate, 1);

				} else {
					if (pendingListEffDate != null) {
						paramEffectiveDate = pendingListEffDate;
					} else {
						// next billing cycle
						paramEffectiveDate = nextPeriodStartDate;
					}
				}
			}
			ccCommitment.setEffectiveDate(paramEffectiveDate);

			int remaining = maxAllowedChangesPerPeriod - modificationCount;
			if (effectImmediate == false) {
				// if CC list is not going to be effective immediately, so that
				// remain
				remaining--;
			}
			if (remaining < 0)
				remaining = 0;
			ccCommitment.setRemainingAllowedModifications(remaining);

		}

		/**
		 * @param feature
		 * @param parameterHistory
		 *            - array of FeatureParameterHistory in descending order by
		 *            effective date
		 */
		void evaluate(FeatureTransactionContext feature, FeatureParameterHistoryInfo[] parameterHistory) {

			CallingCircleCommitmentAttributeDataInfo ccCommitment = new CallingCircleCommitmentAttributeDataInfo(maxAllowedChangesPerPeriod);
			feature.getServiceFeatureInfo().setCallingCircleCommitmentAttributeData(ccCommitment);

			if (feature.isAdd()) {
				// when add new SOC, KB ignores parameter effective date, so
				// here we just return the date for front-end to use
				ccCommitment.setEffectiveDate(feature.getEffectiveDate());
			} else if (DateUtil.isAfter(feature.getEffectiveDate(), logicalDate)) { // the
																					// SOC
																					// is
																					// effective
																					// in
																					// the
																					// future
				ccCommitment.setEffectiveDate(feature.getEffectiveDate());
			} else {

				int currentPeriodModificationCount = 0;
				boolean hasInitialList = false;
				FeatureParameterHistoryInfo previousRecord = null;

				for (FeatureParameterHistoryInfo ftrParam : parameterHistory) {
					// search for parameter record that matches current feature.
					if (feature.isSameFeature(ftrParam)) {

						// make sure the record is effective ( expirationDate is
						// null or >= effectiveDate ).
						if (ftrParam.getExpirationDate() == null || DateUtil.isBefore(ftrParam.getExpirationDate(), ftrParam.getEffectiveDate()) == false) {

							// to see if this is the first record
							if (ftrParam.getEffectiveDate().equals(feature.getEffectiveDate())) {
								hasInitialList = ftrParam.getParameterValue() != null;
								if (isInitialRecordBackfilled(ftrParam, previousRecord)
								// && ftrParam.getEffectiveDate().after(
								// currentPeriodStartDate )
										&& currentPeriodModificationCount > 0) {
									currentPeriodModificationCount--;
								}
								break;
							}

							previousRecord = ftrParam;

							if (DateUtil.isAfter(ftrParam.getEffectiveDate(), logicalDate) && ftrParam.getExpirationDate() == null) {
								// this is a pending effective list
								if (maxAllowedChangesPerPeriod == 0) {
									// this is special indicator that CDR in
									// roll back mode , in which case , we will
									// stop examining other history records
									// and keep the existing pending list
									ccCommitment.setEffectiveDate(ftrParam.getEffectiveDate());
									currentPeriodModificationCount = 1;
									break;
								}

								if (DateUtil.isBefore(ftrParam.getEffectiveDate(), nextPeriodStartDate)) {
									// in this case preserve the pending list
									// effective date
									ccCommitment.setEffectiveDate(ftrParam.getEffectiveDate());
								}
							}

							if (ftrParam.getEffectiveDate().getTime() <= currentPeriodStartDate.getTime()) {

								if (ftrParam.getExpirationDate() == null) {
									// this is the current list
									if (ftrParam.getParameterValue() == null) {
										// current list is empty.
										// note: assuming if current list is
										// empty then all previous SFP record
										// are also empty, so no initial list
										hasInitialList = false;
									} else {
										// current list is not empty
										hasInitialList = true;
									}
									// once we reach or pass beginning or this
									// cycle, we can stop checking now.
									break;
								} else if (currentPeriodModificationCount > 0) {
									// there is already modification made in
									// this cycle, no need to check history
									// beyond this cycle
									// start date
									break;
								} else {
									// we need to continue examining the history
									// until we reach the first record.
									continue;
								}
							}

							currentPeriodModificationCount++;
						}
					}
				}

				ccCommitment.setModificationCount(currentPeriodModificationCount);
				finalizeCommitmentData(feature, hasInitialList);
			}
		}

	}

	private class PrepaidEvaluator extends CommitmentDataEvaluator {

		/**
		 * @param prepaidFeature
		 * @param parameterHistory
		 *            - array of FeatureParameterHistory in ascending order by
		 *            effective date
		 */
		void evaluate(FeatureTransactionContext prepaidFeature, FeatureParameterHistoryInfo[] parameterHistory) {

			CallingCircleCommitmentAttributeDataInfo ccCommitment = new CallingCircleCommitmentAttributeDataInfo(maxAllowedChangesPerPeriod);
			ccCommitment.setEffectiveDate(logicalDate);

			prepaidFeature.getServiceFeatureInfo().setCallingCircleCommitmentAttributeData(ccCommitment);

			FeatureTransactionContext kbFeature = prepaidFeature.getAssociatedFeature();

			// since calling circle history is in KB, if KB feature does not
			// exist, no point to continue
			if (kbFeature == null)
				return;

			Date currentPeriodStartDate = prepaidFeature.getEffectiveDate();

			if (prepaidFeature.isAdd() == false) {

				int currentPeriodModificationCount = 0;
				FeatureParameterHistoryInfo previousRecord = null;

				for (FeatureParameterHistoryInfo ftrParam : parameterHistory) {
					// search for parameter record that matches current feature.
					if (prepaidFeature.isSameFeature(ftrParam)) {

						// make sure the record is effective ( expirationDate is
						// null or >= effectiveDate ).
						if (ftrParam.getExpirationDate() == null || DateUtil.isBefore(ftrParam.getExpirationDate(), ftrParam.getEffectiveDate()) == false) {

							// to see if this is the first record
							if (ftrParam.getEffectiveDate().equals(prepaidFeature.getEffectiveDate())) {
								if (isInitialRecordBackfilled(ftrParam, previousRecord) && DateUtil.isAfter(ftrParam.getEffectiveDate(), currentPeriodStartDate)) {
									currentPeriodModificationCount--;
								}
								break;
							}

							previousRecord = ftrParam;

							if (DateUtil.isAfter(ftrParam.getEffectiveDate(), logicalDate) && ftrParam.getExpirationDate() == null) {
								continue;
							}

							if (ftrParam.getEffectiveDate().getTime() <= currentPeriodStartDate.getTime()) {
								// we need to continue examining the history
								// until we reach the first record.
								continue;
							}

							currentPeriodModificationCount++;
						}
					}
				}

				if (ccCommitment.getRemainingAllowedModifications() == 0) {
					ccCommitment.setPrepaidModificationBlocked(true);
				}

				kbFeature.getServiceFeatureInfo().setCallingCircleCommitmentAttributeData(ccCommitment);
			}
		}
	}

	private CommitmentDataEvaluator getEvaluator() {
		if (isPostpaid) {
			return new PostpaidEvaluator();
		} else {
			return new PrepaidEvaluator();
		}
	}

	public void evaluateCommitmentData(FeatureTransactionContext[] features, FeatureParameterHistoryInfo[] parameterHistory) {

		CommitmentDataEvaluator evaluator = getEvaluator();
		for (FeatureTransactionContext feature : features) {
			evaluator.evaluate(feature, parameterHistory);
		}
	}

	public void populateCommitmentData(FeatureTransactionContext[] features) {

		for (FeatureTransactionContext featureTxn : features) {
			populateCommitmentData(featureTxn);
			// replicate the information from prepay feature to the mapped KB
			// feature
			if (featureTxn.isPrepaid() && featureTxn.getAssociatedFeature() != null) {
				featureTxn.getAssociatedFeature().getServiceFeatureInfo().copyCallingCircleInfo(featureTxn.getServiceFeatureInfo());
			}
		}
	}

	private void populateCommitmentData(FeatureTransactionContext featureTxn) {
		CallingCircleCommitmentAttributeDataInfo ccCommitment = new CallingCircleCommitmentAttributeDataInfo(maxAllowedChangesPerPeriod);

		Date featureEffectiveDate = featureTxn.getEffectiveDate();
		ccCommitment.setEffectiveDate(featureEffectiveDate);
		ccCommitment.setPrepaidModificationBlocked(false);
		featureTxn.getServiceFeatureInfo().setCallingCircleCommitmentAttributeData(ccCommitment);
	}

	public static boolean hasCallingCircleServiceSwitchCodes(ServiceAgreementInfo agreementInfo) {
		for (String callingCircleSwitchCode : CALLING_CIRCLE_SWITCH_CODES) {
			if (agreementInfo.getService().containsSwitchCode(callingCircleSwitchCode)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasCallingCircleFeatureSwitchCodes(ContractFeature feature) {
		for (String callingCircleSwitchCode : CALLING_CIRCLE_SWITCH_CODES) {
			if (feature.getFeature().getSwitchCode().trim().equals(callingCircleSwitchCode)) {
				return true;
			}
		}
		return false;
	}

	public static ContractFeature getCallingCircleFeature(ServiceAgreementInfo serviceAgreement) {
		for (ContractFeature feature : serviceAgreement.getFeatures()) {
			if (hasCallingCircleFeatureSwitchCodes(feature)) {
				return feature;
			}
		}
		return null;
	}

	public static ServiceAgreementInfo getPrepaidCallingCircleService(ServiceAgreementInfo[] allServices) {
		for (ServiceAgreementInfo service : allServices) {
			if (service.getService().isWPS() && CallingCircleUtilities.hasCallingCircleServiceSwitchCodes(service)) {
				return service;
			}
		}
		return null;
	}

}
