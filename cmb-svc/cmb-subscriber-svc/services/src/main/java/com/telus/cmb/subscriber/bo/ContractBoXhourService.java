package com.telus.cmb.subscriber.bo;

import java.util.Calendar;

import com.telus.api.UnknownObjectException;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.DurationServiceCommitmentAttributeData;
import com.telus.api.util.DateUtil;

public class ContractBoXhourService {
//	
//	/**
//	 * Used solely for adding a single duration service to a contract.
//	 * 
//	 * @param serviceCode
//	 * @param effectiveDate
//	 * @return
//	 * @throws ApplicationException
//	 * @throws TelusAPIException
//	 */
//	public ServiceAgreementInfo addDurationService(String serviceCode, ServiceInfo service, Calendar effectiveDate) throws ApplicationException, TelusAPIException {
//		if (service == null) {
//			service = changeContext.getRefDataFacade().getRegularService(serviceCode);
//			//throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, "Unknown service with code [" + serviceCode + "]", "");
//		}
//
//		if (!isDurationService(service)) {
//			throw new TelusAPIException("Attempt using a duration servie addition interface for non-duration service with code " + service.getCode());
//		}
//
//		Calendar systemEffectiveDateCalendar = DateUtil.calendarToTimezone(effectiveDate, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//		if (effectiveDate != null) {
//			systemEffectiveDateCalendar = DateUtil.calendarToTimezone(effectiveDate, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//			testDurationServiceAddition(service, systemEffectiveDateCalendar);
//		} else {
//			systemEffectiveDateCalendar = this.findBestEffectiveDate(service);
//
//		}
//
//		Date serviceEffectiveDate = systemEffectiveDateCalendar.getTime();
//		Calendar systemExpiryDateCalendar = (Calendar) systemEffectiveDateCalendar.clone();
//		systemExpiryDateCalendar.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours());
//		systemExpiryDateCalendar.add(Calendar.SECOND, -1); // from 18:00 to 17:59:59
//		if (effectiveDate == null && serviceEffectiveDate.before(new Date())) {
//			// add configured expiration lag in case no specific effective date was requested
//			systemExpiryDateCalendar.add(Calendar.MINUTE, getProvisioningLagTime());
//		}
//		Date serviceExpiryDate = systemExpiryDateCalendar.getTime();
//
//		ServiceAgreementInfo contractService = addService(service, serviceEffectiveDate, serviceExpiryDate);
//		// the following code will override that has been done in addService since we know the user-requested timezone here from effectiveDate
//		ContractFeature feature = contractService.getFeatureBySwitchCode(DurationServiceCommitmentAttributeData.SWITCH_CODE_XHOUR);
//		// set XHOUR parameter feature based on the service effective and expiry time
//		DurationServiceCommitmentAttributeData durationServiceAttributesData = new DurationServiceCommitmentAttributeData();
//		// populate XHR-START - XHR-END as a duration of service in Canada/Mountain TZ 
//		durationServiceAttributesData.setXhrServiceStartTime(systemEffectiveDateCalendar);
//		durationServiceAttributesData.setXhrServiceEndTime(systemExpiryDateCalendar);
//		// populate destination TZ (XHR-TZ_ID) based on input effectiveDate's (or its calculated value) TZ
//		TimeZone displayTimeZone = effectiveDate != null ? effectiveDate.getTimeZone() : TimeZone.getTimeZone(DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//		durationServiceAttributesData.setDisplayTimeZone(displayTimeZone);
//		// resolve subscriber's native TZ by subscriber market province (where subscriber is registered)
//		String subscriberNativeTimezoneCode = DurationServiceCommitmentAttributeData.getTimezoneByProvince(subscriber.getMarketProvince());
//		TimeZone subscriberNativeZone = subscriberNativeTimezoneCode != null ? TimeZone.getTimeZone(subscriberNativeTimezoneCode) : TimeZone.getDefault();
//		// populate BP_START - BP_END as a duration in subscriber's native zone
//		// BP_START
//		Calendar displayEffectiveDateCalendar = DateUtil.calendarToTimezone(systemEffectiveDateCalendar, subscriberNativeZone.getID());
//		durationServiceAttributesData.setBpServiceStartTime(displayEffectiveDateCalendar);
//		// BP_END
//		Calendar displayExpiryDateCalendar = DateUtil.calendarToTimezone(systemExpiryDateCalendar, subscriberNativeZone.getID());
//		durationServiceAttributesData.setBpServiceEndTime(displayExpiryDateCalendar);
//		// populate BP_TZ as UTC-xxxx code derived from subscriber's native province/timezone
//		durationServiceAttributesData.setSubscriberTimeZone(subscriberNativeZone); // how to populate subscriber TZ?
//		// serialize the feature and set as a feature parameter
//		feature.setParameter(durationServiceAttributesData.serialize());
//
//		return contractService;
//	}
//
//	// =========================================================================================================================
//		/**
//		 * Method determines if duration service will be laddered.  If no exception is thrown, the duration service
//		 * specified by service can be added with no conflicts at the specified effectiveDate and effectiveStartTime.
//		 * If service to be added will result in the duration service being ?laddered?, InvalidServiceChangeException
//		 * is thrown with a InvalidServiceChangeException.LADDERED_SERVICE reason code and the new laddered
//		 * service start date and time can be retrieved from InvalidServiceChangeException.getContractService().
//		 * 
//		 * @param service
//		 * @param effectiveDate
//		 * @throws InvalidServiceChangeException, TelusAPIException
//		 */
//		private void testDurationServiceAddition(Service service, Calendar effectiveDate) throws TelusAPIException {
//			if (effectiveDate != null) {
//				if (effectiveDate.before(new Date())) {
//					throw new InvalidServiceChangeException(InvalidServiceChangeException.SERVICE_CONFLICT, "Effective date should not be in the past");
//				} else if (!isDurationService(service)) {
//					throw new InvalidServiceChangeException(InvalidServiceChangeException.SERVICE_CONFLICT, "Service is not a duration service");
//				} else {
//					// all times are compared in Canada/Mountain
//					Calendar mtCal = DateUtil.calendarToTimezone(effectiveDate, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//					Date serviceEffectiveDate = mtCal.getTime();
//					mtCal.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours());
//					mtCal.add(Calendar.SECOND, -1);
//					Date serviceExpiryDate = mtCal.getTime();
//					List<ContractService> durationServices = getDurationServices(service.getCode());
//					for (ContractService durationService : durationServices) {
//						if (isPeriodOverlapping(serviceEffectiveDate, serviceExpiryDate, durationService.getDurationServiceStartTime().getTime(), durationService.getDurationServiceEndTime().getTime())) {
//							throw new InvalidServiceChangeException(InvalidServiceChangeException.LADDERED_SERVICE,
//									"New duration service overlaps with an existing service. Service laddering is required.", durationService, null); // TODO: what about feature code which is currently null?
//						}
//					}
//				}
//			}
//		}
//		
//		/**
//		 * Sets feature parameter value according to the added service validity attributes. Used only for single immediate add
//		 * 
//		 * @param contractService
//		 * @param effectiveDate
//		 * @param serviceDuration
//		 * @throws UnknownObjectException
//		 */
//		private void setFeatureParameterForDurationService(ServiceAgreementInfo contractService, Date effectiveDate, Date expiryDate) throws UnknownObjectException {
//			ContractFeature feature = contractService.getFeatureBySwitchCode(DurationServiceCommitmentAttributeData.SWITCH_CODE_XHOUR);
//			// figure service start and end dates in Canada/Mountain zone only
//			TimeZone canadaMountainTz = TimeZone.getTimeZone(DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//			Calendar startCal = DateUtil.dateToTimezoneCalendar(null, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//			startCal.setTime(effectiveDate);
//			Calendar endCal = (Calendar) startCal.clone();
//			endCal.setTime(expiryDate);
//			// set XHOUR parameter feature based on the service start and end dates in Canada/Mountain zone
//			DurationServiceCommitmentAttributeData durationServiceAttributesData = new DurationServiceCommitmentAttributeData();
//			// set XHR-START/END
//			durationServiceAttributesData.setXhrServiceStartTime(startCal);
//			durationServiceAttributesData.setXhrServiceEndTime(endCal);
//			// resolve subscriber native TZ
//			String subscriberNativeTimezoneCode = DurationServiceCommitmentAttributeData.getTimezoneByProvince(subscriber.getMarketProvince());
//			TimeZone subscriberNativeZone = subscriberNativeTimezoneCode != null ? TimeZone.getTimeZone(subscriberNativeTimezoneCode) : TimeZone.getDefault();
//			// set BP-START in subscriber's native zone
//			Calendar displayEffectiveDateCalendar = DateUtil.calendarToTimezone(startCal, subscriberNativeZone.getID());
//			durationServiceAttributesData.setBpServiceStartTime(displayEffectiveDateCalendar);
//			// set BP-END in subscriber's native zone
//			Calendar displayExpiryDateCalendar = DateUtil.calendarToTimezone(endCal, subscriberNativeZone.getID());
//			durationServiceAttributesData.setBpServiceEndTime(displayExpiryDateCalendar);
//			// set timezones
//			durationServiceAttributesData.setSubscriberTimeZone(subscriberNativeZone);
//			durationServiceAttributesData.setDisplayTimeZone(canadaMountainTz);
//			// serialize XHOUR parameter value
//			feature.setParameter(durationServiceAttributesData.serialize());
//		}
//		
//		/**
//		 * Sorts a list of all duration services by their expiryDate property in the ascending order.
//		 * At the end the service with the latest expiration time will be at the end of the list.
//		 * 
//		 * @param durationServices
//		 */
//		private void sortDurationServiceByExpiryDate(List durationServices) {
//			Collections.sort(durationServices, new Comparator() {
//				public int compare(Object obj1, Object obj2) {
//					ContractService cs1 = (ContractService) obj1;
//					ContractService cs2 = (ContractService) obj2;
//					//Sorts by 'expiryDate' property in the ascending order
//					return cs1.getDurationServiceEndTime().before(cs2.getDurationServiceEndTime()) ? -1 : cs1.getDurationServiceEndTime().after(cs2.getDurationServiceEndTime()) ? 1 : 0;
//				}
//			});
//		}
//		
//		/**
//		 * Gets all duration services of the current contract for a specified service code.
//		 * 
//		 * @param serviceCode
//		 * @return
//		 */
//		private List<ContractService> getDurationServices(String serviceCode) throws TelusAPIException {
//			ContractService[] optionalServices = getOptionalServices();
//			List<ContractService> durationServices = new ArrayList<ContractService>();
//			for (ContractService contractService : optionalServices) {
//				Service currService = contractService.getService();
//				if (isDurationService(currService) && currService.getCode().equals(serviceCode)) {
//					durationServices.add(contractService);
//				}
//			}
//			return durationServices;
//		}
//		
//		/**
//		 * Returns true if the service is durational, i.e. its durationServiceHours 
//		 * attribute is greater than 0.
//		 *  
//		 * @param service
//		 * @return
//		*/
//		private boolean isDurationService(Service service) {
//			return service != null && service.getDurationServiceHours() > 0;
//		}
//
//
//		
//		private boolean isPeriodOverlapping(Date firstEffectiveDate, Date firstExpiryDate, Date secondEffectiveDate, Date secondExpiryDate) {
//			if (secondEffectiveDate != null && firstEffectiveDate != null && firstExpiryDate != null) {
//				if (secondExpiryDate == null) {
//					return firstExpiryDate.compareTo(secondEffectiveDate) > 0;
//				} else {
//					return (!((firstExpiryDate.compareTo(secondEffectiveDate) <= 0) || (firstEffectiveDate.compareTo(secondExpiryDate) >= 0)));
//				}
//			}
//			if (firstEffectiveDate != null) { //expiryDate==null
//				if (secondExpiryDate == null) {
//					return true;
//				}
//				return firstEffectiveDate.compareTo(secondExpiryDate) < 0;
//			}
//			return true;
//		}
//
//		
//		private Calendar findBestEffectiveDate(Service service) {
//			Calendar systemEffectiveDateCalendar = DateUtil.calendarToTimezone(null, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//			try {
//
//				List<ContractService> durationServices = getDurationServices(service.getCode());
//				// if existing duration services with the same code found take an expiry date of the latest existing 
//				// service and make the new service effective right after it
//				if (!durationServices.isEmpty()) {
//					sortDurationServiceByExpiryDate(durationServices);
//
//					Date servicePeriodEffectiveDate = systemEffectiveDateCalendar.getTime();
//					Calendar tempCalendar = (Calendar) systemEffectiveDateCalendar.clone();
//					tempCalendar.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours());
//					tempCalendar.add(Calendar.SECOND, -1);
//					Date servicePeriodExpiryDate = tempCalendar.getTime();
//					int i = 0;
//					while (i < durationServices.size()) {
//						ContractService durationService = (ContractService) durationServices.get(i);
//						if (isPeriodOverlapping(servicePeriodEffectiveDate, servicePeriodExpiryDate, durationService.getDurationServiceStartTime().getTime(), durationService.getDurationServiceEndTime()
//								.getTime())) {
//							// advance in the loop to the next existing service
//							systemEffectiveDateCalendar = (Calendar) durationService.getDurationServiceEndTime().clone();
//							systemEffectiveDateCalendar.add(Calendar.SECOND, 1);
//							servicePeriodEffectiveDate = systemEffectiveDateCalendar.getTime();
//							tempCalendar = (Calendar) systemEffectiveDateCalendar.clone();
//							tempCalendar.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours());
//							tempCalendar.add(Calendar.SECOND, -1);
//							servicePeriodExpiryDate = tempCalendar.getTime();
//							i = 0;
//						} else {
//							++i;
//						}
//					}
//				}
//			} catch (TelusAPIException te) {
//				// add immediately - no services found - legitimate scenario
//			}
//			return systemEffectiveDateCalendar;
//		}
	//FROM ServiceAgreementInfo
//	public Calendar getDurationServiceStartTime() {
//		ContractFeature xhourFeature = getXhourFeature();
//		DurationServiceCommitmentAttributeData durationAttributeData = new DurationServiceCommitmentAttributeData(xhourFeature.getParameter());
//		Calendar cal = durationAttributeData.getDisplayTimeZone().getID().equals(DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN) ? durationAttributeData.getXhrServiceStartTime()
//				: DateUtil.calendarToTimezone(durationAttributeData.getXhrServiceStartTime(), durationAttributeData.getDisplayTimeZone().getID());
//		return cal;
//	}
//
//	public Calendar getDurationServiceEndTime() {
//		ContractFeature xhourFeature = getXhourFeature();
//		DurationServiceCommitmentAttributeData durationAttributeData = new DurationServiceCommitmentAttributeData(xhourFeature.getParameter());
//		Calendar cal = durationAttributeData.getDisplayTimeZone().getID().equals(DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN) ? durationAttributeData.getXhrServiceEndTime()
//				: DateUtil.calendarToTimezone(durationAttributeData.getXhrServiceEndTime(), durationAttributeData.getDisplayTimeZone().getID());
//		return cal;
//	}
//	/**
//	 * Returns XHOUR feature value based on a SwitchCode value (and not the
//	 * featureCode)
//	 * 
//	 * @return
//	 */
//	public ContractFeature getXhourFeature() {
//		try {
//			return getFeatureBySwitchCode(DurationServiceCommitmentAttributeData.SWITCH_CODE_XHOUR);
//		} catch (UnknownObjectException ex) {
//			try {
//				return getFeature(DurationServiceCommitmentAttributeData.SWITCH_CODE_XHOUR);
//			} catch (UnknownObjectException ex1) {
//				try {
//					return getFeature(DurationServiceCommitmentAttributeData.SWITCH_CODE_3XHOUR);
//				} catch (UnknownObjectException ex2) {
//				}
//			}
//			return null;
//		}
//	}
//
//	/**
//	 * Returns true if underlying service has a duration hour greater than 0,
//	 * which means the service is durational.
//	 */
//	public boolean isDurationService() {
//		return ((service != null && service.getDurationServiceHours() > 0) || getXhourFeature() != null);
//	}
}
