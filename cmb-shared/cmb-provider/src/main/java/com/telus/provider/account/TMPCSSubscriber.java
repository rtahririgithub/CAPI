/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.ArrayList;
import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractService;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.InvalidEquipmentChangeException;
import com.telus.api.account.PCSSubscriber;
import com.telus.api.account.PhoneNumberException;
import com.telus.api.account.PhoneNumberInUseException;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.SeatData;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnsupportedEquipmentException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.USIMCardEquipment;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.NetworkType;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.PCSSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.equipment.TMUSIMCardEquipment;
import com.telus.provider.servicerequest.TMServiceRequestManager;
import com.telus.provider.util.Logger;

public class TMPCSSubscriber extends TMSubscriber implements PCSSubscriber {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @link aggregation
	 */
	private final PCSSubscriberInfo delegate;

	// cached list of the secondary equipment list
	private Equipment[] secondaryEquipments = null;

	// RCM API reference
	//private static final RcmAPI rcmApi = new RcmAPI(); //RCM-Cleanup

	public TMPCSSubscriber(TMProvider provider, PCSSubscriberInfo delegate, boolean activation, String activationFeeChargeCode,
			TMAccountSummary accountSummary, boolean dealerHasDeposit, Equipment equipment) {
		super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit, equipment);
		this.delegate = delegate;
	}

	public TMPCSSubscriber(TMProvider provider, PCSSubscriberInfo delegate, boolean activation, String activationFeeChargeCode,
			TMAccountSummary accountSummary, boolean dealerHasDeposit) {
		super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit);
		this.delegate = delegate;
	}

	public TMPCSSubscriber(TMProvider provider, PCSSubscriberInfo delegate, boolean activation, String activationFeeChargeCode) {
		super(provider, delegate, activation, activationFeeChargeCode);
		this.delegate = delegate;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public void clear() {
		super.clear();
		delegate.clear();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	public String getNextPhoneNumber() {
		return delegate.getNextPhoneNumber();
	}

	public String getPreviousPhoneNumber() {
		return delegate.getPreviousPhoneNumber();
	}

	public Date getNextPhoneNumberChangeDate() {
		return delegate.getNextPhoneNumberChangeDate();
	}

	public Date getPreviousPhoneNumberChangeDate() {
		return delegate.getPreviousPhoneNumberChangeDate();
	}

	public boolean isFidoConversion() {
		return delegate.isFidoConversion();
	}

	public String getMIN() {
		String min = "";
		try {
			 if (getSubscriberNetworkType().equals(NetworkType.NETWORK_TYPE_HSPA)) {
		 		 //	  RCM Webservice will return MIN only for CDMA, not for HSPA network
		 		return min;
			 } else {
				min = provider.getSubscriberLifecycleFacade().retrieveTNProvisionAttributes(getPhoneNumber(), getSubscriberNetworkType());
				return min;
			 }
		} catch(Throwable t) {
			Logger.debug(t);
		}
		return min;
	}

	public Equipment[] getSecondaryEquipments() throws TelusAPIException {
		if (secondaryEquipments == null) {
			String[] secondarySerialNumbers = getSecondarySerialNumbers();

			if (secondarySerialNumbers != null) {
				ArrayList secondaryEquipmentList = new ArrayList();

				for (int i = 0; i < secondarySerialNumbers.length; i++) {
					if (secondarySerialNumbers[i] != null) {
						TMEquipment equipment = getEquipment0(secondarySerialNumbers[i]);

						if (equipment != null) {
							equipment.setPrimary(false);
							secondaryEquipmentList.add(equipment);
						}
					}
				}

				secondaryEquipments = (Equipment[]) secondaryEquipmentList.toArray(new Equipment[secondaryEquipmentList.size()]);
			}
			else {
				secondaryEquipments = new Equipment[0];
			}
		}

		return secondaryEquipments;
	}

	public ApplicationMessage[] changeEquipment(Equipment newEquipment,
			Equipment[] newSecondaryEquipments,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			boolean preserveDigitalServices,
			boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		char allowDuplicateSerialNo = convertAllowDuplicateFlag(ignoreSerialNoInUse);
		return changeEquipment(newEquipment, newSecondaryEquipments, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo);
	}

	public ApplicationMessage[] changeEquipment(Equipment newEquipment,
			Equipment[] newSecondaryEquipments,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			boolean preserveDigitalServices,
			char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {

		TMEquipment oldEquipmentTM = (TMEquipment) getEquipment();
		TMEquipment newEquipmentTM = (TMEquipment) newEquipment;

		try {
			// 1. Test Equipment Change Ability if new equipment is different from the current one.
			ApplicationMessage[] swapMessages = testChangeEquipment(newEquipmentTM, newSecondaryEquipments, dealerCode, salesRepCode, requestorId, repairId, swapType, allowDuplicateSerialNo);

			// 2. Change equipment
			int newSecondaryEquipmentsSz = newSecondaryEquipments != null ? newSecondaryEquipments.length : 0;

			EquipmentInfo[] newSecondaryEquipmentsInfo = new EquipmentInfo[newSecondaryEquipmentsSz];

			for (int i = 0; i < newSecondaryEquipmentsSz; i++) {
				newSecondaryEquipmentsInfo[i] = ((TMEquipment) newSecondaryEquipments[i]).getDelegate();

			}
			PricePlanValidationInfo   ppValidationInfo = new PricePlanValidationInfo();
			provider.getSubscriberLifecycleFacade().changeEquipment(delegate,
					oldEquipmentTM.getDelegate(),
					newEquipmentTM.getDelegate(),
					newSecondaryEquipmentsInfo,
					dealerCode,
					salesRepCode,
					requestorId,
					swapType,
					null,
					ppValidationInfo,
					null, true, null,
					SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));

			// 3. If lost - update to found - the swap was previously checked and allowed.
			if (newEquipment.isStolen())
				newEquipment.reportFound();

			// Do the same for secondary equipments
			for (int i = 0; i < newSecondaryEquipmentsSz; i++) {
				if (newSecondaryEquipments[i].isStolen())
					newSecondaryEquipments[i].reportFound();
			}

			provider.getInteractionManager0().subscriberChangeEquipment(this,
					oldEquipmentTM,
					newEquipmentTM,
					dealerCode,
					salesRepCode,
					requestorId,
					repairId,
					swapType,
					null);

			this.equipment = newEquipment;

			// make sure the subscriber on the contract is fresh
			// and new equipment is on the subscriber already at this point
			if (contract == null)
				contract = (TMContract) getContract();

			contract.setSubscriber(this);

			// 4. Different Equipment Types: Add and/or remove services to/from the contract as necessary
			addRemoveServices(contract, oldEquipmentTM, newEquipmentTM, preserveDigitalServices);

			if (newEquipment.isCellular() &&
					!((oldEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_DIGITAL) || oldEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG)) &&
							newEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG) &&
							preserveDigitalServices)) {
				removeNonMatchingServices(newEquipment, contract); // For Cellular RIM
			}

			// 5. Remove Dispatch only conflicts
			if (!((oldEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_DIGITAL) || oldEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG)) &&
					newEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG) &&
					preserveDigitalServices)) {
				removeDispatchOnlyConflicts(contract);
			}

			// 6. Save the contract
			try {
				String[] KBDealer = getKBDealer(dealerCode, salesRepCode);
				contract.save(KBDealer[0], KBDealer[1]);
			}
			catch(Throwable t) {
				Logger.debug("contract.save(): SubscriberManagerEJB().changeServiceAgreement() failed: " + t);
			}

			return swapMessages;
		}
		catch(Throwable t) {
			provider.getExceptionHandler().handleException(t);
			return null;
		}
	}
	
	//Holborn R1
	public ApplicationMessage[] changeEquipment(Equipment newEquipment, Equipment associatedHandset, String dealerCode,
			String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices,
			char allowDuplicateSerialNo, ServiceRequestHeader header) throws TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException, UnsupportedEquipmentException {

		//!!! this method only support change equipment to HSPA!!! 
		Equipment currentEquipment = getEquipment();

		Equipment currentHandset = null;
		if ( associatedHandset!=null ) { //transaction involve new handset
			if (  currentEquipment.isUSIMCard() ) {
				//HSPA to HSPA plus handset swap
				currentHandset = ((USIMCardEquipment) currentEquipment).getLastAssociatedHandset();
			} else {
				//CDMA to HSPA: currentHandset is null 
			}
		} else { //no new handset
			if ( currentEquipment.isUSIMCard() ) {
				//HSPA to HSPA USIM card swap only: currentHandset is null
				
			} else {
				//CDMA to HSPA, USIM card only: currentHandset is null
			}
		}
		
		ApplicationMessage[] messages = null;
		if (newEquipment.isUSIMCard()) {
			messages = changeHSPAEquipment(newEquipment, associatedHandset, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo);
		} else {
			messages = changeEsimEnabledEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo);
		}

		
		if ( needToCallSRPDS(header)) {
			
			Equipment newEquip = newEquipment;
			if ( currentEquipment.getSerialNumber().equals(newEquipment.getSerialNumber()) ) {
				//handset only swap, don't send old/new primary equipment to SRPDS
				newEquip = null;
				currentEquipment = null;
			}
			
			((TMServiceRequestManager)provider.getServiceRequestManager())
				.reportChangeEquipment(delegate.getBanId(), delegate.getSubscriberId(),
						delegate.getDealerCode(), delegate.getSalesRepId(), provider.getUser(), 
						currentEquipment, newEquip, repairId, swapType, currentHandset, associatedHandset, header);
		}
		
		return messages;
	}

	//Holborn R1
	public ApplicationMessage[] testChangeEquipment(Equipment newEquipment, Equipment newAssociatedHandset, String dealerCode,
			String salesRepCode, String requestorId, String repairId, String swapType, char allowDuplicateSerialNo)
			throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException, UnsupportedEquipmentException{
		
		if ( newEquipment == null ) 
			throw new InvalidEquipmentChangeException("new equipment is null");
		if ( newEquipment.isVirtual() ) 
			throw new InvalidEquipmentChangeException("new equipment is virtual, sn[" + newEquipment.getSerialNumber() +"]" );
		if ( newEquipment.isUSIMCard()==false) 
			throw new UnsupportedEquipmentException("new equipment is not a USIMCard, sn[" + newEquipment.getSerialNumber() +"]");
		
		if ( ((USIMCardEquipment)newEquipment).isAssignable()==false) 
			throw new UnsupportedEquipmentException("The USIMCard is not assignable, sn[" + newEquipment.getSerialNumber() +"]");
		
		if ( newAssociatedHandset!=null && newAssociatedHandset.isHSPA()==false ) {
			throw new UnsupportedEquipmentException("new associated handset is not a HSPA equipment, sn[" + newEquipment.getSerialNumber() +"]");	
		}
		
		if ( newEquipment.equals(getEquipment())) {
			return  new ApplicationMessage[0];
		}
		else {
			return testChangeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, allowDuplicateSerialNo );
		}
	}

	//Holborn R1
	public EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment, Equipment newAssociatedHandset, String dealerCode,
			String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices)
			throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException, UnsupportedEquipmentException{
		
		if ( newEquipment.isHSPA() 
				&& newEquipment.getSerialNumber().equals(getEquipment().getSerialNumber())
				&& newAssociatedHandset==null ) {
			throw new InvalidEquipmentChangeException ("Missing Handset equipment for Handset only swap");
		} else {
			testChangeEquipment( newEquipment, newAssociatedHandset, dealerCode, salesRepCode, requestorId, repairId, swapType, SWAP_DUPLICATESERIALNO_DONOTALLOW );
		}
		
		EquipmentChangeRequestInfo info = new EquipmentChangeRequestInfo();

		info.setNewEquipment(newEquipment);
		info.setAssociatedHandset(newAssociatedHandset);
		if (newEquipment.isHSPA()) {
			((TMUSIMCardEquipment) newEquipment).setLastAssociatedHandset(newAssociatedHandset);
		}
		info.setDealerCode(dealerCode);
		info.setSalesRepCode(salesRepCode);
		info.setRequestorId(requestorId);
		info.setRepairId(repairId);
		info.setSwapType(swapType);
		info.setPreserveDigitalServices(preserveDigitalServices);
		return info;
	}

	public void changeEquipmentToVirtual(Equipment[] newSecondaryEquipments,
			String dealerCode,
			String salesRepCode,
			String requestorId) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {

		TMEquipment oldEquipmentTM = (TMEquipment) getEquipment();
		Equipment virtualEquipment = getVirtualEquipment();
		TMEquipment virtualEquipmentTM = (TMEquipment) virtualEquipment;

		try {

			// 2. Change equipment
			int newSecondaryEquipmentsSz = newSecondaryEquipments != null ? newSecondaryEquipments.length : 0;

			EquipmentInfo[] newSecondaryEquipmentsInfo = new EquipmentInfo[newSecondaryEquipmentsSz];

			for (int i = 0; i < newSecondaryEquipmentsSz; i++)
				newSecondaryEquipmentsInfo[i] = ((TMEquipment) newSecondaryEquipments[i]).getDelegate();

			PricePlanValidationInfo   ppValidationInfo = new PricePlanValidationInfo();
			provider.getSubscriberLifecycleFacade().changeEquipment(delegate,
					oldEquipmentTM.getDelegate(),
					virtualEquipmentTM.getDelegate(),
					newSecondaryEquipmentsInfo,
					dealerCode,
					salesRepCode,
					requestorId,
					Equipment.SWAP_TYPE_REPLACEMENT,
					null,
					ppValidationInfo,
					null, true, null,
					SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));

			provider.getInteractionManager0().subscriberChangeEquipment(this,
					oldEquipmentTM,
					virtualEquipmentTM,
					dealerCode,
					salesRepCode,
					requestorId,
					null,
					Equipment.SWAP_TYPE_REPLACEMENT,
					null);

			this.equipment = virtualEquipment;

			// make sure the subscriber on the contract is fresh
			// and new equipment is on the subscriber already at this point
			if (contract == null)
				contract = (TMContract) getContract();

			contract.setSubscriber(this);

			// 6. Save the contract
			try {
				String[] KBDealer = getKBDealer(dealerCode, salesRepCode);
				contract.save(KBDealer[0], KBDealer[1]);
			}
			catch(Throwable t) {
				Logger.debug("contract.save(): SubscriberManagerEJB().changeServiceAgreement() failed: " + t);
			}
		}

		catch(Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	//Feb 23,2011, HT end state contract CR: 
	//change this method to delegate to the new overloaded version with flag Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW
	public EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment,
			Equipment[] newSecondaryEquipments,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			boolean preserveDigitalServices)
	throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		return newEquipmentChangeRequest(newEquipment, newSecondaryEquipments, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices
				,Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW );
	}
	
	//Feb 23,2011, HT end state contract CR: 
	//overload above API to support SmartDestkop triangle swap scenario, in which case, the new equipment is in used.
	public EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment,
			Equipment[] newSecondaryEquipments,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			boolean preserveDigitalServices,
			char allowDuplicateSerialNo)
	throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {

		if ( newEquipment.isCDMA()==false)
			throw new UnsupportedEquipmentException("new equipment is not CDMA, sn(" + newEquipment.getSerialNumber()+")");
		
		EquipmentChangeRequestInfo info = new EquipmentChangeRequestInfo();

		info.setNewEquipment(newEquipment);
		info.setSecondaryEquipments(newSecondaryEquipments);
		info.setDealerCode(dealerCode);
		info.setSalesRepCode(salesRepCode);
		info.setRequestorId(requestorId);
		info.setRepairId(repairId);
		info.setSwapType(swapType);
		info.setPreserveDigitalServices(preserveDigitalServices);

		// validate
		testChangeEquipment(newEquipment, newSecondaryEquipments, dealerCode, salesRepCode, requestorId, repairId, swapType, allowDuplicateSerialNo );

		return info;
	}

	/**
	 * @deprecated
	 */
	public ApplicationMessage[] testChangeEquipment(Equipment newEquipment, Equipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		char allowDuplicateSerialNo = convertAllowDuplicateFlag(ignoreSerialNoInUse);
		return testChangeEquipment(newEquipment, newSecondaryEquipments, dealerCode, salesRepCode, requestorId, repairId, swapType, allowDuplicateSerialNo);
	}
	
	public ApplicationMessage[] testChangeEquipment(Equipment newEquipment, Equipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		if (newEquipment == null) {
			String exceptionMsg = "newEquipment is null.";
			throw new InvalidEquipmentChangeException(exceptionMsg, InvalidEquipmentChangeException.MANDATORY_EQUIPMENT_INFO_NULL);
		}

		if (newEquipment.isVirtual()) {
			throw new InvalidEquipmentChangeException("Invalid Equipment Change Exception", InvalidEquipmentChangeException.IMPOSSIBLE_SWAP_TYPES);
		}

		//Holborn change:
		if ( newEquipment.isCDMA()==false ) throw new UnsupportedEquipmentException("new equipment is not CDMA, sn("+newEquipment.getSerialNumber()+")");


		ApplicationMessage[] primarySwapMessages = null;

		// test primary equipment
		if (!newEquipment.equals(getEquipment()))
			primarySwapMessages = testChangeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, allowDuplicateSerialNo);

		// test secondary equipment
		if (newSecondaryEquipments != null) {
			Equipment[] oldSecondaryEquipments = getSecondaryEquipments();
			int oldSecondaryEquipmentsSz = oldSecondaryEquipments != null ? oldSecondaryEquipments.length : 0;

			for (int i = 0; i < newSecondaryEquipments.length; i++) {

				if ((newSecondaryEquipments[i]).isVirtual())
					throw new InvalidEquipmentChangeException("Invalid Equipment Change Exception", InvalidEquipmentChangeException.IMPOSSIBLE_SWAP_TYPES);

				boolean isNew = true;

				for (int j = 0; j < oldSecondaryEquipmentsSz; j++)
					isNew &= !newSecondaryEquipments[i].equals(oldSecondaryEquipments[j]);

				if (isNew) {
					// check if the equipment's product type is the same as the subscriber's one
					if (newSecondaryEquipments[i].getProductType() == null || !newSecondaryEquipments[i].getProductType().equals(getProductType()))
					{
						String exceptionMsg = "The new secondary equipment's ProductType, [" + newEquipment.getProductType() +
						"], is different from the subscriber's one, [" + getProductType() + "].";

						throw new InvalidEquipmentChangeException(exceptionMsg, InvalidEquipmentChangeException.IMPOSSIBLE_SWAP_TYPES);
					}

					// check if the secondary equipment is already is use
					if (checkEquipmentInUse(((TMEquipment) newSecondaryEquipments[i]), allowDuplicateSerialNo))
					{
						String exceptionMsg = "The new secondary equipment's serial number, [" + newSecondaryEquipments[i].getSerialNumber() + "], is in use.";

						throw new SerialNumberInUseException(exceptionMsg, newSecondaryEquipments[i].getSerialNumber());
					}

					// check lost/stolen
					if (newSecondaryEquipments[i].isStolen() && !((TMEquipment) newSecondaryEquipments[i]).isInUseOnBan(getBanId(), false))
						throw new InvalidEquipmentChangeException("The new equipment is lost or stolen.", InvalidEquipmentChangeException.NEW_EQUIPMENT_IS_LOST_STOLEN);
				}
			}
		}

		ApplicationMessage[] secondarySwapMessages = getSwapWarningMessages(newSecondaryEquipments);

		int primarySwapMessagesSz = primarySwapMessages != null ? primarySwapMessages.length : 0;
		int secondarySwapMessagesSz = secondarySwapMessages != null ? secondarySwapMessages.length : 0;

		ApplicationMessage[] allSwapMessages = new ApplicationMessage[primarySwapMessagesSz + secondarySwapMessagesSz];

		if (primarySwapMessages != null)
			System.arraycopy(primarySwapMessages, 0, allSwapMessages, 0, primarySwapMessagesSz);

		if (secondarySwapMessages != null)
			System.arraycopy(secondarySwapMessages, 0, allSwapMessages, primarySwapMessagesSz, secondarySwapMessagesSz);

		return allSwapMessages;
	}

	/* (non-Javadoc)
	 * @see com.telus.api.account.PCSSubscriber#changePhoneNumber(com.telus.api.account.AvailablePhoneNumber, java.lang.String, boolean)
	 */
	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, String reasonCode, boolean changeOtherNumbers) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException {
		changePhoneNumber0(availablePhoneNumber, reasonCode, changeOtherNumbers,null,null);
	}

	/**
	 * Warning messages for the secondary equipment.
	 *
	 * @param newSecondaryEquipments
	 * @return
	 * @throws TelusAPIException
	 */
	private ApplicationMessage[] getSwapWarningMessages(Equipment[] newSecondaryEquipments) throws TelusAPIException {
		ArrayList messageList = new ArrayList();
		Contract contract = getContract();

		boolean hasRuimService = false;
		boolean hasInternationalCallingService = false;

		ContractService[] optionalServices = contract.getOptionalServices();
		for (int i = 0; i < (optionalServices != null ? optionalServices.length : 0); i++) {
			hasRuimService |= optionalServices[i].getService().isRUIM();
			hasInternationalCallingService |= optionalServices[i].getService().isInternationalCalling();
		}

		boolean isRuimMessageRequired = false;

		for (int i = 0; i < (newSecondaryEquipments != null ? newSecondaryEquipments.length : 0); i++) {
			isRuimMessageRequired |= (!hasRuimService || !hasInternationalCallingService) && newSecondaryEquipments[i].isRUIMCard();
		}

		if (isRuimMessageRequired)
			messageList.add(provider.getApplicationMessage(82));

		return (ApplicationMessage[]) messageList.toArray(new ApplicationMessage[messageList.size()]);
	}

	public void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation, PortInEligibility portInEligibility)  
	throws PortRequestException, PhoneNumberInUseException ,TelusAPIException {

		// During a regular activation and inter-carrier porting, the front-end applications will supply a valid
		// PhoneNumberReservation object, from which we set proper numberGroup, marketProvince, waiveSearchFee.
		// However, for inter-brand porting, we have to populate this information using reference data for the
		// number to be ported-in, since it is already active on the TELUS network.

		String methodName = "reservePhoneNumber";
		String activity = null;

		// Determine the port process from the PortInEligibility object.
		determinePortProcessType(portInEligibility);

		if (portedIn) {		  
			if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
				activity = "reserve phone number for inter-brand ports";
				// If this is an inter-brand port, set the phone number, number group and market province based on the
				// PortInEligibility object and ignore the PhoneNumberReservation object.
				delegate.setPhoneNumber(portInEligibility.getPhoneNumber());
				AvailablePhoneNumber number = provider.getReferenceDataManager().getAvailablePhoneNumber(getPhoneNumber(), getProductType(), getDealerCode());
				delegate.setNumberGroup(number.getNumberGroup());
				delegate.setMarketProvince(number.getNumberGroup().getProvinceCode());
				delegate.setPortInd(true);
				waiveSearchFee = true; // This is always true for now as charges are handled through the SSF API.
				logSuccess(methodName, activity, null);

			} else if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT) || portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT)) {
				activity = "reserve phone number for inter-carrier ports";
				try {
					// If this is an inter-carrier port, set the phone number, number group and market province based on the
					// the PhoneNumberReservation object.
					SubscriberInfo info = provider.getSubscriberManagerBean().reservePortedInPhoneNumber(delegate, ((TMPhoneNumberReservation)phoneNumberReservation).getPhonenumberReservation0(), false);
					info.setMarketProvince(phoneNumberReservation.getNumberGroup().getProvinceCode());
					info.setNumberGroup(phoneNumberReservation.getNumberGroup());
					waiveSearchFee = phoneNumberReservation.getWaiveSearchFee();
					info.setPortInd(true);
					delegate.copyFrom(info);
					provider.registerNewSubscriber(delegate);
					logSuccess(methodName, activity, null);

				} catch(PhoneNumberInUseException pniue) {
					logFailure(methodName, activity, pniue, null);
					throw pniue;
				} catch(Throwable t) {
					logFailure(methodName, activity, t, null);
					provider.getExceptionHandler().handleException(t);
				}
			} else {
				throw new PortRequestException("unknown port process exception [" + portProcess + "]");
			}

		} else {
			// Otherwise, call reservePhoneNumber method for normal activations.
			reservePhoneNumber(phoneNumberReservation);
		}
	}

	/**
	 * @deprecated
	 */
	public void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation, boolean portIn)  throws PortRequestException, PhoneNumberInUseException ,TelusAPIException{
		if (portIn){
			try {
				SubscriberInfo info = provider.getSubscriberManagerBean().reservePortedInPhoneNumber(delegate, ((TMPhoneNumberReservation)phoneNumberReservation).getPhonenumberReservation0(),false);
				
				info.setMarketProvince(phoneNumberReservation.getNumberGroup().getProvinceCode());
				info.setNumberGroup(phoneNumberReservation.getNumberGroup());
				waiveSearchFee = phoneNumberReservation.getWaiveSearchFee();
				info.setPortInd(true);
				delegate.copyFrom(info);
				provider.registerNewSubscriber(delegate);
			} catch (PhoneNumberInUseException pe) {
				throw pe;
			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}
		}
		else
			reservePhoneNumber(phoneNumberReservation);
	}

	public ApplicationMessage[] changeEquipment(Equipment newEquipment,
			Equipment[] newSecondaryEquipments,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			boolean preserveDigitalServices,
			char allowDuplicateSerialNo,
			ServiceRequestHeader header) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		
		Equipment currentEquipment = getEquipment();
		ApplicationMessage[] messages = changeEquipment(newEquipment, newSecondaryEquipments, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo);
		
		if ( needToCallSRPDS(header)) {
			((TMServiceRequestManager)provider.getServiceRequestManager())
				.reportChangeEquipment(delegate.getBanId(), delegate.getSubscriberId(),
						delegate.getDealerCode(), delegate.getSalesRepId(), provider.getUser(), 
						currentEquipment, getEquipment(), repairId, swapType, null, null, header);
		}
		
		return messages;
		
	}
	
	
	

	public void reserveOnHoldPhoneNumber(PhoneNumberReservation phoneNumberReservation) throws TelusAPIException {
		reservePhoneNumberOffline(phoneNumberReservation);

	}

	
}




