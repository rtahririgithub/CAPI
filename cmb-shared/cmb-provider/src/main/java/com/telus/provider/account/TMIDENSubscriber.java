/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;


import java.util.ArrayList;
import java.util.List;

import com.telus.api.InvalidChangeMemberIdException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.Contract;
import com.telus.api.account.DispatchResourceContractMismatchException;
import com.telus.api.account.IDENAccount;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.InvalidEquipmentChangeException;
import com.telus.api.account.InvalidFleetException;
import com.telus.api.account.MemberIdMatchException;
import com.telus.api.account.NumberMatchException;
import com.telus.api.account.PhoneNumberException;
import com.telus.api.account.PhoneNumberInUseException;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.equipment.Equipment;
import com.telus.api.fleet.Fleet;
import com.telus.api.fleet.MemberIdentity;
import com.telus.api.fleet.ReservePortInMemberIdentity;
import com.telus.api.fleet.TalkGroup;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequestException;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.ReservePortInMemberIdentityInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.framework.exception.PhoneNumberAlreadyInUseException;
import com.telus.eas.framework.exception.ReserveMemberIDFailedException;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.fleet.TMFleet;
import com.telus.provider.fleet.TMFleetIdentity;
import com.telus.provider.fleet.TMMemberIdentity;
import com.telus.provider.fleet.TMTalkGroup;
import com.telus.provider.util.ProviderDefaultExceptionTranslator;
import com.telus.provider.util.ProviderIDENSubscriberExceptionTranslator;

@Deprecated
public class TMIDENSubscriber extends TMSubscriber implements IDENSubscriber {

	/** @link aggregation */
	private final IDENSubscriberInfo delegate;
	private final TMMemberIdentity memberIdentity;
	private boolean pTNBasedFleet;
	private ArrayList talkGroupsArray = new ArrayList();

	public TMIDENSubscriber(TMProvider provider, IDENSubscriberInfo delegate, boolean activation,
			String activationFeeChargeCode, TMAccountSummary accountSummary, boolean dealerHasDeposit, Equipment equipment) {
		super(provider, delegate, activation, activationFeeChargeCode, accountSummary,
				dealerHasDeposit, equipment);
		this.delegate = delegate;
		TMFleetIdentity fleetIdentity = new TMFleetIdentity(provider, delegate.getMemberIdentity0().getFleetIdentity0());
		memberIdentity = new TMMemberIdentity(provider, delegate.getMemberIdentity0(), fleetIdentity);
	}

	public TMIDENSubscriber(TMProvider provider, IDENSubscriberInfo delegate, boolean activation,
			String activationFeeChargeCode, TMAccountSummary accountSummary, boolean dealerHasDeposit) {
		super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit);
		this.delegate = delegate;
		TMFleetIdentity fleetIdentity = new TMFleetIdentity(provider, delegate.getMemberIdentity0().getFleetIdentity0());
		memberIdentity = new TMMemberIdentity(provider, delegate.getMemberIdentity0(), fleetIdentity);
	}

	public TMIDENSubscriber(TMProvider provider, IDENSubscriberInfo delegate, boolean activation,
			String activationFeeChargeCode) {
		super(provider, delegate, activation, activationFeeChargeCode);
		this.delegate = delegate;
		TMFleetIdentity fleetIdentity = new TMFleetIdentity(provider, delegate.getMemberIdentity0().getFleetIdentity0());
		memberIdentity = new TMMemberIdentity(provider, delegate.getMemberIdentity0(), fleetIdentity);
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public String getIPAddress() {
		return delegate.getIPAddress();
	}

	/**
	 * @see IDENSubscriber#getIMSI()
	 */
	public String getIMSI() {
		return delegate.getIMSI();
	}

	public void setSubscriberAlias(String subscriberAlias) {
		delegate.setSubscriberAlias(subscriberAlias);
	}

	public String getSubscriberAlias() {
		return delegate.getSubscriberAlias();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public void clear() {
		super.clear();
		delegate.clear();
		memberIdentity.clear();
		talkGroupsArray.clear();
	}

	private void associateFleetToBan(Fleet fleet) throws TelusAPIException {
		if (!((IDENAccount)getAccount()).contains(fleet)) {
			try {
				((IDENAccount)getAccount()).addFleet(fleet);
			} catch (TelusAPIException e) {
				throw e;
			} catch (Throwable e) {
				throw new TelusAPIException(e);
			}
		}
	}

	public void changeMemberId(String newMemberId) throws MemberIdMatchException, InvalidChangeMemberIdException, TelusAPIException {
		String memberId = newMemberId;

		memberId = memberId == null ? "*****" : memberId.trim().equals("") ? "*****" : memberId;
		try {
			provider.getSubscriberManagerBean().changeMemberId(delegate, memberId);
		}
/* exception handling logic has been moved to catch Throwable block, keep them here for reference, TODO remove in cleanup release		
		catch(TelusException e) {
			if ("VAL20033".equals(e.id)) {
				throw new MemberIdMatchException(e, newMemberId);
			}
			else if ("VAL20034".equals(e.id)) {
				throw new InvalidChangeMemberIdException(InvalidChangeMemberIdException.PTN_BASEFLEET, e);
			}
			else if ("VAL20037".equals(e.id)) {
				throw new InvalidChangeMemberIdException(0, e);
			}
			throw new TelusAPIException(e);
		}
*/		
		catch(Throwable e) {
			provider.getExceptionHandler().handleException(e, new ProviderIDENSubscriberExceptionTranslator(newMemberId));
		}

	}


	public void changeMemberIdentity(int newUrbanId, int newFleetId, String newMemberId) throws MemberIdMatchException, InvalidChangeMemberIdException, InvalidFleetException, TelusAPIException {
		String memberId = newMemberId;

		memberId = memberId == null ? "*****" : memberId.trim().equals("") ? "*****" : memberId;
		try{
			if (delegate.getFleetIdentity().getUrbanId() == newUrbanId &&
					delegate.getFleetIdentity().getFleetId() == newFleetId) {
				provider.getSubscriberManagerBean().changeMemberId(delegate, memberId);
			} else {
				provider.getSubscriberLifecycleFacade().changeMemberIdentity(delegate, newUrbanId, newFleetId, memberId, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
/* exception handling logic has been moved to catch Throwable block, keep them here for reference, TODO remove in cleanup release		}catch (TelusException e) {
			if("VAL20033".equals(e.id)){
				throw new MemberIdMatchException(e, newMemberId);
			}else if("VAL20034".equals(e.id)){
				throw new InvalidChangeMemberIdException(InvalidChangeMemberIdException.PTN_BASEFLEET, e);
			}else if("VAL10010".equals(e.id)){
				throw new InvalidFleetException(e, new FleetIdentityInfo(newUrbanId, newFleetId));
			}
			else if ("VAL20037".equals(e.id)) {
				throw new InvalidChangeMemberIdException(0, e);
			}
			throw new TelusAPIException(e);
*/
			}
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e, new ProviderIDENSubscriberExceptionTranslator(newUrbanId, newFleetId, newMemberId));
		}

	}


	public void addMemberIdentity(int pUrbanId, int pFleetId, String pMemberId, String pDealerCode, String pSalesRepCode, Contract pContract) throws MemberIdMatchException, DispatchResourceContractMismatchException, InvalidFleetException, TelusAPIException {

		pMemberId = pMemberId == null ? "*****" : pMemberId.trim().equals("") ? "*****" : pMemberId;
		try{
			// Price Plan Change - price plan code has changed
			if (pContract.getPricePlan().getCode().equals(getContract0().getPricePlan().getCode()))
				provider.getSubscriberManagerBean().addMemberIdentity(delegate, ((TMContract)pContract).getDelegate(), pDealerCode, pSalesRepCode, pUrbanId, pFleetId, pMemberId, false);
			// No Price Plan Change - services/features have been added
			else
				provider.getSubscriberManagerBean().addMemberIdentity(delegate, ((TMContract)pContract).getDelegate(), pDealerCode, pSalesRepCode, pUrbanId, pFleetId, pMemberId, true);

/*	exception handling logic has been moved to catch Throwable block, keep them here for reference, TODO remove in cleanup release		
		}catch (TelusException e) {
			if("VAL20033".equals(e.id)){
				throw new MemberIdMatchException(e, pMemberId);
			}else if("VAL20035".equals(e.id)){
				throw new DispatchResourceContractMismatchException(e);
			}else if("VAL10010".equals(e.id)){
				throw new InvalidFleetException(e, new FleetIdentityInfo(pUrbanId, pFleetId));
			}
			throw new TelusAPIException(e);
*/			
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e, new ProviderIDENSubscriberExceptionTranslator(pUrbanId, pFleetId, pMemberId));
		}

	}


	public void removeMemberIdentity(String pDealerCode, String pSalesRepCode, Contract pContract) throws DispatchResourceContractMismatchException, TelusAPIException {

		try{
			// Price Plan Change - price plan code has changed
			if (pContract.getPricePlan().getCode().equals(getContract0().getPricePlan().getCode()))
				provider.getSubscriberManagerBean().removeMemberIdentity(delegate, ((TMContract)pContract).getDelegate(), pDealerCode, pSalesRepCode, false);
			// No Price Plan Change - services/features have been added
			else
				provider.getSubscriberManagerBean().removeMemberIdentity(delegate, ((TMContract)pContract).getDelegate(), pDealerCode, pSalesRepCode, true);
/* exception handling logic has been moved to catch Throwable block, keep them here for reference, TODO remove in cleanup release
		}catch (TelusException e) {
			if("VAL20036".equals(e.id)){
				throw new DispatchResourceContractMismatchException(e);
			}
			throw new TelusAPIException(e);
*/			
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e , new ProviderIDENSubscriberExceptionTranslator());
		}

	}


	public void changeIPAddress() throws TelusAPIException {
		try{
			provider.getSubscriberLifecycleFacade().changeIP(delegate.getBanId(), delegate.getSubscriberId(), null, delegate.RESOURCE_TYPE_IP_PRIVATE, "", SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

	}


	public String[] retrieveAvailableMemberIds(String memberIdPattern,
			int maxMemberIds) throws TelusAPIException {

		return retrieveAvailableMemberIds(delegate.getFleetIdentity()
				.getUrbanId(), delegate.getFleetIdentity().getFleetId(),
				memberIdPattern, maxMemberIds);
	}

	public String[] retrieveAvailableMemberIds(int urbanId, int fleetId,
			String memberIdPattern, int maxMemberIds) throws TelusAPIException {

		String memberId = memberIdPattern;
		memberId = memberId == null ? "*****"
				: memberId.trim().equals("") ? "*****" : memberId;
		try {
			return provider.getSubscriberManagerBean()
			.retrieveAvailableMemberIds(urbanId, fleetId, memberId,
					maxMemberIds);
/*	exception handling logic has been moved to catch Throwable block, keep them here for reference, TODO remove in cleanup release		
		} catch (TelusException e) {
			if ("APP20004".equals(e.id)) {
				return new String[0];
			}
			throw new TelusAPIException(e);
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
*/		
		} catch (Throwable t ) {
			
			provider.getExceptionHandler().handleException(t, new ProviderDefaultExceptionTranslator() {

				protected TelusAPIException getExceptionForErrorId(
						String errorId, Throwable cause) {
					if ( "APP20004".equals(errorId) ||  "1110560".equals( errorId )) {
						return null;
					}
					return  new TelusAPIException( cause );
				} 
			});
			return new String[0];
		}
	}

	public MemberIdentity getMemberIdentity() {
		if (memberIdentity.isValid() &&
				(getStatus() == Subscriber.STATUS_CANCELED ||
						!memberIdentity.getResourceStatus().equals("C"))) {
			return memberIdentity;
		} else {
			return null;
		}
	}

	private void setupFleet(Fleet fleet) throws TelusAPIException {
		TMFleetIdentity fleetIdentity = (TMFleetIdentity)fleet.getIdentity();
		delegate.getMemberIdentity0().getFleetIdentity0().copyFrom(fleetIdentity.getDelegate());
	}


	public void reservePTNBasedMemberId () throws UnknownSubscriberException, TelusAPIException{
		try{
			localReservePTNBasedMemberId();
		} catch (ReserveMemberIDFailedException e){
			try{
				localReservePTNBasedMemberId();
			} catch (ReserveMemberIDFailedException ex){
				throw new TelusAPIException(ex);
			}
		}
	}


	public void localReservePTNBasedMemberId() throws ReserveMemberIDFailedException,  TelusAPIException {
		//    associateFleetToBan(fleet);
		try{
			IDENSubscriberInfo info = provider.getSubscriberManagerBean().reserveMemberId(delegate);
			delegate.copyFrom(info);
			provider.registerNewSubscriber(delegate);
			Fleet fleet = provider.getFleetManager().getFleetById(info.getFleetIdentity().getUrbanId(), info.getFleetIdentity().getFleetId());
			setupFleet(fleet);
/* exception handling logic has been moved to catch Throwable block, keep them here for reference, TODO remove in cleanup release		
		} catch (ReserveMemberIDFailedException e) {
			delegate.setSubscriberId(e.getSubscriberId());
			delegate.setPhoneNumber(e.getPhoneNumber());

			char cha = ((ReserveMemberIDFailedException)e).getReason();
			if (cha == ReserveMemberIDFailedException.RESOURCE_IN_USE  || cha == ReserveMemberIDFailedException.URBAN_FLEET_IN_USE)
			{
				throw e;
			}
			throw new TelusAPIException(e);
		}catch(TelusException e){
			throw new TelusAPIException(e);
*/		
		}catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}


	public void reserveMemberId(Fleet fleet, String memberIdPatttern) throws UnknownSubscriberException, MemberIdMatchException, TelusAPIException {
		try {
			localReserveMemberId(fleet, memberIdPatttern);
		} catch (ReserveMemberIDFailedException e)
		{
			try{
				localReserveMemberId(fleet, memberIdPatttern);
			} catch (ReserveMemberIDFailedException ex){
				throw new TelusAPIException(ex);
			}

		}

	}

	public void reserveMemberId(Fleet fleet) throws UnknownSubscriberException, MemberIdMatchException, TelusAPIException {
		try {
			localReserveMemberId(fleet, "****");
		} catch (ReserveMemberIDFailedException e)
		{
			try{
				localReserveMemberId(fleet, "****");
			} catch (ReserveMemberIDFailedException ex){
				throw new TelusAPIException(ex);
			}
		}

	}


	public void localReserveMemberId(Fleet fleet, String memberIdPatttern) throws ReserveMemberIDFailedException, MemberIdMatchException, TelusAPIException {
		//     associateFleetToBan(fleet);
		try{
			IDENSubscriberInfo info = provider.getSubscriberManagerBean().reserveMemberId(delegate, ((TMFleet)fleet).getIdentity0().getDelegate(), memberIdPatttern);
			delegate.copyFrom(info);
			provider.registerNewSubscriber(delegate);
			setupFleet(fleet);
/* exception handling logic has been moved to catch Throwable block, keep them here for reference, TODO remove in cleanup release		
		} catch (ReserveMemberIDFailedException e) {
			delegate.setSubscriberId(e.getSubscriberId());
			delegate.setPhoneNumber(e.getPhoneNumber());

			char cha = ((ReserveMemberIDFailedException)e).getReason();
			if (cha == ReserveMemberIDFailedException.RESOURCE_IN_USE  || cha == ReserveMemberIDFailedException.URBAN_FLEET_IN_USE)
			{
				throw e;
			}
			throw new TelusAPIException(e);
		}catch(TelusException e){
			if ("APP20004".equals(e.id))
			{
				throw new MemberIdMatchException(e, memberIdPatttern);
			}
			throw new TelusAPIException(e);
*/			
		}catch (Throwable e) {
			provider.getExceptionHandler().handleException(e, new ProviderIDENSubscriberExceptionTranslator(memberIdPatttern));
		}
	}




	public void reserveIPAddress() throws UnknownSubscriberException, TelusAPIException {
		//assertSubscriberExists();
	}

	public Fleet getFleet() throws UnknownSubscriberException, TelusAPIException {
		//assertSubscriberExists();
		if (getMemberIdentity() != null){
			return memberIdentity.getFleetIdentity().getFleet();
		}
		else {
			return null;
		}
	}

	private TalkGroupInfo[] convertTalkGroupInfo(TalkGroup[] talkGroup) {
		TalkGroupInfo[] talkGroupInfo = new TalkGroupInfo[talkGroup.length];
		for (int i = 0; i < talkGroup.length; i++) {
			talkGroupInfo[i] = (TalkGroupInfo)talkGroup[i];
		}
		return talkGroupInfo;
	}

	public void setTalkGroups(TalkGroup[] talkGroup) throws UnknownSubscriberException,
	InvalidFleetException, TelusAPIException {
		try {
			TalkGroupInfo[] info = new TalkGroupInfo[talkGroup.length];
			for (int i = 0; i < talkGroup.length; i++) {
				info[i] = ((TMTalkGroup)talkGroup[i]).getDelegate();
				if (((TMFleetIdentity)memberIdentity.getFleetIdentity()).isValid() &&
						!info[i].getFleetIdentity().equals(delegate.getFleetIdentity())) {
					throw new InvalidFleetException("Fleet mismatch ", info[i].getFleetIdentity());
				}
			}
			delegate.setTalkGroups0(info);
		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}
	public void addAndRemoveTalkGroups(TalkGroup[] talkGroupsToAdd,
			TalkGroup[] talkGroupsToRemove) throws UnknownSubscriberException,
			InvalidFleetException, TelusAPIException {
		assertSubscriberExists();
		try {
			TalkGroupInfo[] infoAdd = new TalkGroupInfo[talkGroupsToAdd.length];
			for (int i = 0; i < talkGroupsToAdd.length; i++) {
				infoAdd[i] = ((TMTalkGroup)talkGroupsToAdd[i]).getDelegate();
				if (((TMFleetIdentity)memberIdentity.getFleetIdentity()).isValid() &&
						!infoAdd[i].getFleetIdentity().equals(delegate.getFleetIdentity())) {
					throw new InvalidFleetException("Fleet mismatch ", infoAdd[i].getFleetIdentity());
				}
			}
			TalkGroupInfo[] infoRemove = new TalkGroupInfo[talkGroupsToRemove.length];
			for (int i = 0; i < talkGroupsToRemove.length; i++) {
				infoRemove[i] = ((TMTalkGroup)talkGroupsToRemove[i]).getDelegate();
				if (((TMFleetIdentity)memberIdentity.getFleetIdentity()).isValid() &&
						!infoRemove[i].getFleetIdentity().equals(delegate.getFleetIdentity())) {
					throw new InvalidFleetException("Fleet mismatch ", infoRemove[i].getFleetIdentity());
				}
			}
			provider.getSubscriberManagerBean().changeTalkGroups(delegate,
					infoAdd, infoRemove);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	private TalkGroup[] decorate(TalkGroupInfo[] talkGroups) throws TelusAPIException, UnknownBANException {
		TMTalkGroup[] tmTalkGroup = new TMTalkGroup[talkGroups.length];
		for (int i = 0; i < talkGroups.length; i++) {
			tmTalkGroup[i] = new TMTalkGroup(provider, talkGroups[i],
					new TMFleetIdentity(provider, talkGroups[i].getFleetIdentity()));
		}
		return tmTalkGroup;
	}

	public TalkGroup[] getTalkGroups() throws UnknownSubscriberException, TelusAPIException {
		//assertSubscriberExists();
		try {
			if ( !talkGroupsArray.isEmpty() )
				return (TMTalkGroup[])talkGroupsArray.toArray(new TMTalkGroup[talkGroupsArray.size()]);
			
			List tempList = provider.getSubscriberLifecycleHelper().retrieveTalkGroupsBySubscriber(delegate.getSubscriberId());
			TalkGroupInfo[] talkGroupsInfo = (TalkGroupInfo[])tempList.toArray(new TalkGroupInfo[tempList.size()]);
				
			TalkGroup[] talkGroups = decorate(talkGroupsInfo);
			for(int i = 0; i < talkGroups.length; i++){
				talkGroupsArray.add(talkGroups[i]);
			}
			return talkGroups;

		} catch (TelusException e) {
			return new TalkGroup[0];
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public boolean contains(TalkGroup talkGroup) throws UnknownSubscriberException, TelusAPIException {
		TalkGroup[] talkGroups = getTalkGroups();
		for (int i = 0; i < talkGroups.length; i++) {
			if (talkGroup.getTalkGroupId() == talkGroups[i].getTalkGroupId()) {
				return true;
			}
		}
		return false;
	}


	public boolean isPTNBasedFleet() throws  TelusAPIException{
		try {
			if (this.getFleet()==null)
			{ return false;
			}
			else
			{ return this.getFleet().isPTNBased();
			}
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	public void refresh() throws TelusAPIException {
		super.refresh();
		talkGroupsArray.clear();
	}

	/**
	 * @see IDENSubscriber#changeIMSI()
	 */
	public void changeIMSI() throws TelusAPIException {
		try {
			provider.getSubscriberManagerBean().changeIMSI(delegate.getBanId(),
					delegate.getProductType(),
					delegate.getSubscriberId());
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	public void changeFaxNumber() throws PhoneNumberException, PhoneNumberInUseException, TelusAPIException {
		try {
			provider.getSubscriberLifecycleFacade().changeFaxNumber(delegate, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
/* exception handling logic has been moved to catch Throwable block, keep them here for reference, TODO remove in cleanup release			
		} catch(PhoneNumberAlreadyInUseException pnaiue) {
			throw new PhoneNumberInUseException(PhoneNumberInUseException.ADDITIONAL_RESERVATION_FAILED, pnaiue.getMessage(), pnaiue);
		} catch(TelusException te) {
			if ("APP20001".equals(te.id)) {
				throw new PhoneNumberException(PhoneNumberException.ADDITIONAL_RESERVATION_FAILED, te.getMessage(), te);
			} else {
				throw new TelusAPIException(te);
			}
		} catch(TelusAPIException tae) {
			throw tae;
*/			
		} catch(Throwable t) {
			provider.getExceptionHandler().handleException(t, new ProviderIDENSubscriberExceptionTranslator());
		}
	}

	public void changeFaxNumber(AvailablePhoneNumber availableFaxNumber) throws PhoneNumberInUseException, TelusAPIException {
		try {
			provider.getSubscriberLifecycleFacade().changeFaxNumber(delegate, (AvailablePhoneNumberInfo)availableFaxNumber, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
/* exception handling logic has been moved to catch Throwable block, keep them here for reference, TODO remove in cleanup release
		} catch(PhoneNumberAlreadyInUseException pnaiue) {
			throw new PhoneNumberInUseException(PhoneNumberInUseException.ADDITIONAL_RESERVATION_FAILED, pnaiue.getMessage(), pnaiue);
		} catch (TelusException te) {
			throw new TelusAPIException(te);
		} catch(TelusAPIException tae) {
			throw tae;
*/			
		} catch(Throwable t) {
			provider.getExceptionHandler().handleException(t, new ProviderIDENSubscriberExceptionTranslator());
		}
	}

	public void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation, PortInEligibility portInEligibility) 
	throws PortRequestException, PhoneNumberInUseException, TelusAPIException {
		
		String methodName = "reservePhoneNumber";
		String activity = null;

		// Determine the port process from the PortInEligibility object.
		determinePortProcessType(portInEligibility);
		
		if (portedIn) {
			throw new PortRequestException("unknown port process exception [" + portProcess + "]");
			
		} else {
			// Otherwise, call reservePhoneNumber method for normal activations.
			reservePhoneNumber(phoneNumberReservation);
		}
	}
	
	/**
	 * @deprecated
	 */
	public void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation, boolean portIn)
	throws PortRequestException, PhoneNumberInUseException, TelusAPIException {		
		if (!portIn)
			reservePhoneNumber(phoneNumberReservation);
	}

	public ReservePortInMemberIdentity getReservePortInMemberIdentity() throws TelusAPIException {
		return new ReservePortInMemberIdentityInfo();
	}
	
	public void reserveIDENResourcesForPortIn(PhoneNumberReservation phoneNumberReservation, boolean reserveUfmi, boolean ptnBased, ReservePortInMemberIdentity memberIdentity, String dealerCode) throws TelusAPIException {
		
		byte ufmiReserveMethod = '\0'; //	This should be either 77 (M - manual) or 82 (R - random) and controls the allocation method
		int urbanId = 0;
		int fleetId = 0;
		int memberId = 0;
		
		if (!ptnBased) {
			urbanId = memberIdentity.getUrbanId();
			fleetId = memberIdentity.getFleetId();
			if (memberIdentity.getMemberId() == null || memberIdentity.getMemberId().equals("") )
				memberId = 0;
			else
				memberId = Integer.parseInt(memberIdentity.getMemberId());

			if (reserveUfmi && ( urbanId!= 0 &&  fleetId!= 0 && memberId != 0))
				ufmiReserveMethod = 'M';
			else if (reserveUfmi && (urbanId != 0 && fleetId != 0 && memberId == 0))
				ufmiReserveMethod = 'R';
		
		} else {			
			urbanId = Integer.parseInt(phoneNumberReservation.getPhoneNumberPattern().substring(0,3));
			fleetId = Integer.parseInt(phoneNumberReservation.getPhoneNumberPattern().substring(3,6));
			memberId = Integer.parseInt(phoneNumberReservation.getPhoneNumberPattern().substring(6,10));
			if (memberId == 0) { //defect 137962 fix. id=1111360; Member ID should lie between 1 and 999999
				memberId = 10000; //not to use reserve method = 'R' because it will pick the next available number. which means the number with same NPA and NXX ending 0001 will get a collision.
			}
			ufmiReserveMethod = 'P';
			reserveUfmi = true;
		}

		try {
			AvailablePhoneNumber availPhoneNumber = provider.getReferenceDataManager().getAvailablePhoneNumber(
					phoneNumberReservation.getPhoneNumberPattern(), Subscriber.PRODUCT_TYPE_IDEN, dealerCode);

			SubscriberInfo info = null;
			//provider.getSubscriberManagerEJB().setIDENResourcesForPortIn(reserveUfmi, ptnBased, ufmiReserveMethod, urbanId, fleetId, memberId, availPhoneNumber);
			//info = provider.getSubscriberManagerEJB().reservePortedInPhoneNumber(delegate, ((TMPhoneNumberReservation)phoneNumberReservation).getPhonenumberReservation0(), false);
			info = provider.getSubscriberManagerBean().reservePortedInPhoneNumberForIden(
					delegate, 
					((TMPhoneNumberReservation)phoneNumberReservation).getPhonenumberReservation0(),
					false, 
					reserveUfmi, 
					ptnBased, 
					ufmiReserveMethod, 
					urbanId, 
					fleetId, 
					memberId, 
					availPhoneNumber 
					);
			info.setMarketProvince(availPhoneNumber.getNumberGroup().getProvinceCode());
			info.setNumberGroup(availPhoneNumber.getNumberGroup());
			waiveSearchFee = phoneNumberReservation.getWaiveSearchFee();

			if (ptnBased){
				Fleet fleet = provider.getFleetManager().getFleetById(urbanId, fleetId);
				setupFleet(fleet);
			}
			info.setPortInd(true);
			delegate.copyFrom(info);
			provider.registerNewSubscriber(delegate);
			portedIn = true;
/*	exception handling logic has been moved to catch Throwable block, keep them here for reference, TODO remove in cleanup release		
		} catch (PhoneNumberInUseException pe) {
			throw pe;
*/			
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e, new ProviderIDENSubscriberExceptionTranslator());
		}
	}

	public void changeEquipmentToVirtual(String dealerCode, String salesRepCode, String requestorId) throws TelusAPIException, InvalidEquipmentChangeException {

		TMEquipment oldEquipmentTM = (TMEquipment) getEquipment();
		Equipment virtualEquipment = getVirtualEquipment();
		TMEquipment virtualEquipmentTM = (TMEquipment) virtualEquipment;
		String methodName = "changeEquipmentToVirtual";
		String activity = "changeEquipmentToVirtual";

		try {		  
			if (oldEquipmentTM.getDelegate().isMule() && virtualEquipmentTM.getDelegate().isMule()) {
				// moved SEMS interaction from ECA to Provider layer
				// IDEN mule to mule swap - call SEMS warranty swap EJB (SEMS Sustainment, September 2007)

				// Holborn R2 , 2009 - remove call to deprecated method processSwapWarranty(...)
				// activity = "SEMS process swap warranty";
				// String returnMessage = provider.getSemsManager().processSwapWarranty(oldEquipmentTM.getTechType(),
				//		oldEquipmentTM.getSerialNumber(), oldEquipmentTM.getProductClassCode(),
				//		virtualEquipmentTM.getTechType(), virtualEquipmentTM.getSerialNumber(),
				//		virtualEquipmentTM.getProductClassCode(), "", // associatedMuleSerialNumber
				//		Equipment.SWAP_TYPE_REPLACEMENT, new Date(), requestorId);
				// logSuccess(methodName, activity, delegate, "SEMS swap warranty successful [" + returnMessage + "]");
			} else {
				// IDEN (not mule to mule) swap - call KB to change equipment
				activity = "KB equipment change";
				PricePlanValidationInfo   ppValidationInfo = new PricePlanValidationInfo();
				provider.getSubscriberLifecycleFacade().changeEquipment(delegate,
						oldEquipmentTM.getDelegate(),
						virtualEquipmentTM.getDelegate(),
						null, dealerCode, salesRepCode,
						requestorId,
						Equipment.SWAP_TYPE_REPLACEMENT,
						null,
						ppValidationInfo, 
						null, true, null,
						SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
				logSuccess(methodName, activity, delegate, "KB equipment change successful");			  
			}

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

			// Save the contract
			try {
				activity = "save contract";
				String[] KBDealer = getKBDealer(dealerCode, salesRepCode);
				contract.save(KBDealer[0], KBDealer[1]);
			} catch(Throwable t) {
				logFailure(methodName, activity, t, "contract.save(): SubscriberManagerEJB().changeServiceAgreement() failed");
			}

		} catch(Throwable t) {
			logFailure(methodName, activity, t, "Throwable occurred");
			provider.getExceptionHandler().handleException(t);
		}
	}

}