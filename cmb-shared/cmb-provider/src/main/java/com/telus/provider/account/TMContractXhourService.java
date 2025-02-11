package com.telus.provider.account;

/*
 *  This x hour logic not in use but keeping the copy here just for future reference 
 *  x hour logic mostly derived based on customer time zone and how we process it to match with Mountain zone as an effective time and also how socs will be laddered  if there are multiple x soc's. 
 */
public class TMContractXhourService{
//	
//	 private Service testAddition0(Service service, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional, boolean allowIncludedOptionalConflict) throws InvalidServiceChangeException, TelusAPIException {
//
//		 // check if this is duration service and test if correct times are requested
//		 if(isDurationService(service)) {
//			 if(effectiveDate == null) {
//				 Calendar mtCal = DateUtil.calendarToTimezone(null, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//				 testDurationServiceAddition(service, mtCal);
//				 effectiveDate = mtCal.getTime();
//				 mtCal.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours());
//				 expiryDate = mtCal.getTime();
//			 }
//		 }
//		 
//	 // check if this is duration service and test if correct times are requested
//	 if(isDurationService(service)) {
//		 boolean effectiveDateWasNull = effectiveDate == null;
//		 Calendar mtCal = Calendar.getInstance(TimeZone.getTimeZone(DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN));
//		 if(!effectiveDateWasNull) {
//			 mtCal.setTime(effectiveDate);
//		 } 
//		 testDurationServiceAddition(service, mtCal);
//		 if(effectiveDateWasNull) {
//			 mtCal.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours());
//			 mtCal.add(Calendar.MINUTE, getProvisioningLagTime()); 
//			 mtCal.add(Calendar.SECOND, -1);
//			 expiryDate = mtCal.getTime();
//		 }
//	 } 
//	 
//	 if(isDurationService(service)) {
//		 setFeatureParameterForDurationService(contractService, effectiveDate, service.getDurationServiceHours());
//	 }
//
//	/**
//	 * Used to add a duration service (x-hour SOC) to the contract multiple times – after calling save() method.  If requested
//	 * effectiveDate and effectiveStartTime overlap with the same x-hour SOC already on the subscriber’s profile,
//	 * the new ContractService will be “laddered”, that is, the effectiveDate and effectiveStartTime
//	 * of the returned ContractService will reflect the start time immediately following the start time of the
//	 * previous service instance.
//	 * 
//	 * All times are converted to "Canada/Mountain" time.
//	 *
//	 * @param service
//	 * @param effectiveDate
//	 * @param numberOfReplications - how many times the duration service should be added using laddering
//	 * @return instance of ContractService
//	 * @throws InvalidServiceChangeException, TelusAPIException
//	 */
//	public ContractService[] addDurationServices(Service service, Calendar effectiveDate, int numberOfReplications) throws TelusAPIException {
//		if(!this.isDurationService(service)) {
//			throw new TelusAPIException("Attempt using a duration servie addition interface for non-duration service with code " + service.getCode());
//		}
//		List listResult = new ArrayList();
//		String destinationTimeZone = effectiveDate!=null?
//					effectiveDate.getTimeZone().getID():
//					DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN;
//		if(effectiveDate == null) {
//			effectiveDate = DW(service, numberOfReplications);
//			//System.out.println("Found best effectiveDate: " + effectiveDate);
//		}
//		for(int i = 0; i < numberOfReplications; i++) {
//			// add service
//			ContractService addedService = addDurationService(service, effectiveDate);
//			listResult.add(addedService);
//			// adjust a start date of the next instance
//			ContractFeature xhourFeature = addedService.getFeatureBySwitchCode(DurationServiceCommitmentAttributeData.SWITCH_CODE_XHOUR);
//			DurationServiceCommitmentAttributeData durationServiceData = new DurationServiceCommitmentAttributeData(xhourFeature.getParameter());
//			// prepare EffectiveDate for the next iteration
//			effectiveDate = DateUtil.calendarToTimezone(durationServiceData.getXhrServiceEndTime(), destinationTimeZone);
//			effectiveDate.add(Calendar.SECOND, 1); // roll 1 sec otherwise overlap will be detected
//		}
//		ContractService[] arrayResult = new ContractService[listResult.size()];
//		for(int i = 0; i < listResult.size(); i++) {
//			arrayResult[i] = (ContractService)listResult.get(i);
//		}
//		return arrayResult;
//	}
//	
//	/**
//	 * Used to add a duration service (x-hour SOC) to the contract – after calling save() method.  If requested
//	 * effectiveDate and effectiveStartTime overlap with the same x-hour SOC already on the subscriber’s profile,
//	 * the new ContractService will be “laddered”, that is, the effectiveDate and effectiveStartTime
//	 * of the returned ContractService will reflect the start time immediately following the start time of the
//	 * previous service instance.
//	 * 
//	 * All times are converted to "Canada/Mountain" time.
//	 *
//	 * @param service
//	 * @param effectiveDate
//	 * @return instance of ContractService
//	 * @throws InvalidServiceChangeException, TelusAPIException
//	 */
//	private ContractService addDurationService(Service service, Calendar effectiveDate) throws TelusAPIException {
//		Calendar systemEffectiveDateCalendar = DateUtil.calendarToTimezone(effectiveDate, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//		if(effectiveDate != null) {
//			testDurationServiceAddition(service, systemEffectiveDateCalendar);
//		}
//		
//		Date serviceEffectiveDate = systemEffectiveDateCalendar.getTime();
//		Calendar systemExpiryDateCalendar = (Calendar)systemEffectiveDateCalendar.clone();
//		systemExpiryDateCalendar.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours());
//		systemExpiryDateCalendar.add(Calendar.SECOND, -1); // from 18:00 to 17:59:59
//		if(effectiveDate == null) {
//			// add configured expiration lag in case no specific effective date was requested
//			systemExpiryDateCalendar.add(Calendar.MINUTE, getProvisioningLagTime()); 
//		}
//		Date serviceExpiryDate = systemExpiryDateCalendar.getTime();
//		
//		ContractService contractService = addService(service, serviceEffectiveDate, serviceExpiryDate);
//		// the following code will override that has been done in addService since we know the user-requested timezone here from effectiveDate
//		ContractFeature feature = contractService.getFeatureBySwitchCode(DurationServiceCommitmentAttributeData.SWITCH_CODE_XHOUR);
//		// set XHOUR parameter feature based on the service effective and expiry time
//		DurationServiceCommitmentAttributeData durationServiceAttributesData = new DurationServiceCommitmentAttributeData();
//		// populate XHR-START - XHR-END as a duration of service in Canada/Mountain TZ 
//		durationServiceAttributesData.setXhrServiceStartTime(systemEffectiveDateCalendar);
//		durationServiceAttributesData.setXhrServiceEndTime(systemExpiryDateCalendar);
//		// populate destination TZ (XHR-TZ_ID) based on input effectiveDate's (or its calculated value) TZ
//		TimeZone displayTimeZone = effectiveDate!=null?
//				effectiveDate.getTimeZone():
//				TimeZone.getTimeZone(DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//		durationServiceAttributesData.setDisplayTimeZone(displayTimeZone);
//		// resolve subscriber's native TZ by subscriber market province (where subscriber is registered)
//		String subscriberNativeTimezoneCode = DurationServiceCommitmentAttributeData.getTimezoneByProvince(subscriber.getMarketProvince());
//		TimeZone subscriberNativeZone = subscriberNativeTimezoneCode!=null?TimeZone.getTimeZone(subscriberNativeTimezoneCode):TimeZone.getDefault(); 
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
//	/**
//	 * Method determines if duration service will be laddered.  If no exception is thrown, the duration service
//	 * specified by service can be added with no conflicts at the specified effectiveDate and effectiveStartTime.
//	 * If service to be added will result in the duration service being “laddered”, InvalidServiceChangeException
//	 * is thrown with a InvalidServiceChangeException.LADDERED_SERVICE reason code and the new laddered
//	 * service start date and time can be retrieved from InvalidServiceChangeException.getContractService().
//	 * 
//	 * @param service
//	 * @param effectiveDate
//	 * @throws InvalidServiceChangeException, TelusAPIException
//	 */
//	private void testDurationServiceAddition(Service service, Calendar effectiveDate) throws TelusAPIException {
//		if(effectiveDate != null) {
//			if(effectiveDate.before(new Date())) {
//				throw new InvalidServiceChangeException(
//						InvalidServiceChangeException.SERVICE_CONFLICT, 
//						"Effective date should not be in the past");
//			} else if(!isDurationService(service)) {
//				throw new InvalidServiceChangeException(
//						InvalidServiceChangeException.SERVICE_CONFLICT, 
//						"Service is not a duration service");
//			} else {
//				// all times are compared in Canada/Mountain
//				Calendar mtCal = DateUtil.calendarToTimezone(effectiveDate, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//				Date serviceEffectiveDate = mtCal.getTime();
//				mtCal.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours());
//				mtCal.add(Calendar.SECOND, -1);
//				Date serviceExpiryDate = mtCal.getTime();
//				List durationServices = getDurationServices(service.getCode());
//				for(int i = 0; i < durationServices.size(); i++) {
//					ContractService durationService = (ContractService)durationServices.get(i);
//					if(isPeriodOverlapping(
//							serviceEffectiveDate, 
//							serviceExpiryDate, 
//							durationService.getDurationServiceStartTime().getTime(),
//							durationService.getDurationServiceEndTime().getTime())) {
//						throw new InvalidServiceChangeException(
//								InvalidServiceChangeException.LADDERED_SERVICE,
//								"New duration service overlaps with an existing service. Service laddering is required.",
//								durationService, null); // TODO: what about feature code which is currently null?
//					}
//				}
//			}
//		}
//	}
//	
//	private Calendar findBestEffectiveDate(Service service, int numberOfReplications) {
//		try {
//			Calendar systemEffectiveDateCalendar = DateUtil.calendarToTimezone(null, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//			List durationServices = getDurationServices(service.getCode());
//			// if existing duration services with the same code found take an expiry date of the latest existing 
//			// service and make the new service effective right after it
//			if(!durationServices.isEmpty()) { 
//				sortDurationServiceByExpiryDate(durationServices);
//				
//				Date servicePeriodEffectiveDate = systemEffectiveDateCalendar.getTime();
//				Calendar tempCalendar = (Calendar)systemEffectiveDateCalendar.clone();
//				tempCalendar.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours() * numberOfReplications);
//				tempCalendar.add(Calendar.SECOND, -1);
//				Date servicePeriodExpiryDate = tempCalendar.getTime();
//				int i = 0;
//				while(i < durationServices.size()) {
//					ContractService durationService = (ContractService)durationServices.get(i);
//					if(isPeriodOverlapping(
//							servicePeriodEffectiveDate, 
//							servicePeriodExpiryDate, 
//							durationService.getDurationServiceStartTime().getTime(),
//							durationService.getDurationServiceEndTime().getTime())) {
//						// advance in the loop to the next existing service
//						systemEffectiveDateCalendar = (Calendar)durationService.getDurationServiceEndTime().clone();
//						systemEffectiveDateCalendar.add(Calendar.SECOND, 1);
//						servicePeriodEffectiveDate = systemEffectiveDateCalendar.getTime();
//						tempCalendar = (Calendar)systemEffectiveDateCalendar.clone();
//						tempCalendar.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours() * numberOfReplications);
//						tempCalendar.add(Calendar.SECOND, -1);
//						servicePeriodExpiryDate = tempCalendar.getTime();
//						i = 0;
//					} else {
//						++i;
//					}
//				}
//				// convert TZ to Canada/Mountain regardless of what TZ was defined for the last duration timeslot (see defect 32682)
//				return DateUtil.calendarToTimezone(systemEffectiveDateCalendar, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//			} else {
//				return null; // add immideately - there is a gap enough to accomodate the requested service sequence
//			}
//		} catch(TelusAPIException te) {
//			return null; // add immideately - no services found
//		}
//	}
//	
//
//	/**
//	 * Method determines if duration service will be laddered.  If no exception is thrown, the duration service
//	 * specified by service can be added with no conflicts at the specified effectiveDate and effectiveStartTime.
//	 * If service to be added will result in the duration service being “laddered”, InvalidServiceChangeException
//	 * is thrown with a InvalidServiceChangeException.LADDERED_SERVICE reason code and the new laddered
//	 * service start date and time can be retrieved from InvalidServiceChangeException.getContractService().
//	 * 
//	 * @param service
//	 * @param effectiveDate
//	 * @throws InvalidServiceChangeException, TelusAPIException
//	 */
//	public void testDurationServicesAddition(Service service, Calendar effectiveDate, int numberOfReplications) throws TelusAPIException {
//		if(!this.isDurationService(service)) {
//			throw new TelusAPIException("Attempt using a duration servie addition interface for non-duration service with code " + service.getCode());
//		}
//		// clone to prevent input Calendar parameter modification
//		effectiveDate = effectiveDate!=null?(Calendar)effectiveDate.clone():createCurrentEffectiveDate();
//		for(int i = 0; i < numberOfReplications; i++) {
//			// add service
//			testDurationServiceAddition(service, effectiveDate);
//			// adjust a start date of the next instance
//			effectiveDate.add(Calendar.HOUR_OF_DAY, service.getDurationServiceHours());
//		}
//	}
//	
//	/**
//	 * Gets all duration services of the current contract for a specified service code.
//	 * 
//	 * @param serviceCode
//	 * @return
//	 */
//	private List getDurationServices(String serviceCode) throws TelusAPIException {
//		ContractService[] optionalServices = getOptionalServices();
//		List durationServices = new ArrayList();
//		for(int i = 0; i < optionalServices.length; i++) {
//			ContractService contractService = optionalServices[i];
//			Service currService = contractService.getService();
//			if(isDurationService(currService) && currService.getCode().equals(serviceCode)) {
//				durationServices.add(contractService);
//			}
//		}
//		return durationServices;
//	}
//	
//
//	/**
//	 * Sorts a list of all duration services by their expiryDate property in the ascending order.
//	 * At the end the service with the latest expiration time will be at the end of the list.
//	 * 
//	 * @param durationServices
//	 */
//	private void sortDurationServiceByExpiryDate(List durationServices) {
//		Collections.sort(durationServices, new Comparator() {
//	        public int compare(Object obj1, Object obj2) {
//	        	ContractService cs1 = (ContractService)obj1;
//	        	ContractService cs2 = (ContractService)obj2;
//	            //Sorts by 'expiryDate' property in the ascending order
//	            return cs1.getDurationServiceEndTime().before(cs2.getDurationServiceEndTime())?-1:cs1.getDurationServiceEndTime().after(cs2.getDurationServiceEndTime())?1:0;
//	        }
//		});
//	}
//
//	/**
//	 * Used to add a duration service (x-hour SOC) to the contract multiple times – after calling save() method.  If requested
//	 * effectiveDate and effectiveStartTime overlap with the same x-hour SOC already on the subscriber’s profile,
//	 * the new ContractService will be “laddered”, that is, the effectiveDate and effectiveStartTime
//	 * of the returned ContractService will reflect the start time immediately following the start time of the
//	 * previous service instance.
//	 * 
//	 * All times are converted to "Canada/Mountain" time.
//	 *
//	 * @param service
//	 * @param effectiveDate
//	 * @param numberOfReplications - how many times the duration service should be added using laddering
//	 * @return instance of ContractService
//	 * @throws InvalidServiceChangeException, TelusAPIException
//	 */
//	public ContractService[] addDurationServices(Service service, Calendar effectiveDate, int numberOfReplications) throws TelusAPIException {
//		if(!this.isDurationService(service)) {
//			throw new TelusAPIException("Attempt using a duration servie addition interface for non-duration service with code " + service.getCode());
//		}
//		List listResult = new ArrayList();
//		String destinationTimeZone = effectiveDate!=null?
//					effectiveDate.getTimeZone().getID():
//					DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN;
//		if(effectiveDate == null) {
//			effectiveDate = findBestEffectiveDate(service, numberOfReplications);
//			//System.out.println("Found best effectiveDate: " + effectiveDate);
//		}
//		for(int i = 0; i < numberOfReplications; i++) {
//			// add service
//			ContractService addedService = addDurationService(service, effectiveDate);
//			listResult.add(addedService);
//			// adjust a start date of the next instance
//			ContractFeature xhourFeature = addedService.getFeatureBySwitchCode(DurationServiceCommitmentAttributeData.SWITCH_CODE_XHOUR);
//			DurationServiceCommitmentAttributeData durationServiceData = new DurationServiceCommitmentAttributeData(xhourFeature.getParameter());
//			// prepare EffectiveDate for the next iteration
//			effectiveDate = DateUtil.calendarToTimezone(durationServiceData.getXhrServiceEndTime(), destinationTimeZone);
//			effectiveDate.add(Calendar.SECOND, 1); // roll 1 sec otherwise overlap will be detected
//		}
//		ContractService[] arrayResult = new ContractService[listResult.size()];
//		for(int i = 0; i < listResult.size(); i++) {
//			arrayResult[i] = (ContractService)listResult.get(i);
//		}
//		return arrayResult;
//	}
//	
//	private Calendar createCurrentEffectiveDate() {
//		Calendar almostCurrent = Calendar.getInstance();
//		almostCurrent.add(Calendar.MINUTE, 1); // TODO: take the minutes value from LDAP or leave as is?
//		return almostCurrent;
//	}
//	
//	/**
//	 * Returns true if the service is durational, i.e. its durationServiceHours 
//	 * attribute is greater than 0.
//	 *  
//	 * @param service
//	 * @return
//	 */
//	private boolean isDurationService(Service service) {
//		return service != null && service.getDurationServiceHours() > 0;
//	}

//	 /**
//	  * Sets feature parameter value according to the added service validity attributes.
//	  * 
//	  * @param contractService
//	  * @param effectiveDate
//	  * @param serviceDuration
//	  * @throws UnknownObjectException
//	  */
//	 private void setFeatureParameterForDurationService(TMContractService contractService, Date effectiveDate, int serviceDuration) throws UnknownObjectException {
//		 ContractFeature feature = contractService.getFeatureBySwitchCode(DurationServiceCommitmentAttributeData.SWITCH_CODE_XHOUR);
//		 // figure service start and end dates in Canada/Mountain zone only
//		 TimeZone canadaMountainTz = TimeZone.getTimeZone(DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//		 Calendar startCal = DateUtil.dateToTimezoneCalendar(effectiveDate, DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN);
//		 Calendar endCal = (Calendar)startCal.clone();
//		 endCal.add(Calendar.HOUR_OF_DAY, serviceDuration);
//		 // set XHOUR parameter feature based on the service start and end dates in Canada/Mountain zone
//		 DurationServiceCommitmentAttributeData durationServiceAttributesData = new DurationServiceCommitmentAttributeData();
//		 durationServiceAttributesData.setXhrServiceStartTime(startCal);
//		 durationServiceAttributesData.setBpServiceStartTime((Calendar)startCal.clone());
//		 durationServiceAttributesData.setXhrServiceEndTime(endCal);
//		 durationServiceAttributesData.setBpServiceEndTime((Calendar)endCal.clone());
//		 durationServiceAttributesData.setSubscriberTimeZone(canadaMountainTz);
//		 durationServiceAttributesData.setDisplayTimeZone(canadaMountainTz);
//		 feature.setParameter(durationServiceAttributesData.serialize()); 
//	 }
//	 }
}
