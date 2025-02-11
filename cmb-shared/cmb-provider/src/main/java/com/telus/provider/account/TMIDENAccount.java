/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.ArrayList;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.api.DuplicateObjectException;
import com.telus.api.TelusAPIException;
import com.telus.api.TooManyObjectsException;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Address;
import com.telus.api.account.FollowUp;
import com.telus.api.account.IDENAccount;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.InvalidFleetException;
import com.telus.api.account.InvalidNetworkException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.IDENEquipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.fleet.Fleet;
import com.telus.api.fleet.TalkGroup;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.fleet.TMFleet;
import com.telus.provider.fleet.TMFleetIdentity;
import com.telus.provider.fleet.TMTalkGroup;
import com.telus.provider.util.Logger;


@Deprecated
public class TMIDENAccount extends BaseProvider implements IDENAccount {
  /**
   * @link aggregation
   */
  private final IDENAccount delegate;
  private final TMAddress alternateCreditCheckAddress;
  private ArrayList fleetArray = new ArrayList();
  private ArrayList talkGroupArray = new ArrayList();
 
  /**
   * @link aggregation
   */
  private final TMAccount account;

  public TMIDENAccount(TMProvider provider, IDENAccount delegate, TMAccount account) {
    super(provider);
    this.delegate = delegate;
    this.account = account;
    alternateCreditCheckAddress = new TMAddress(provider, ((AccountInfo)delegate).getAlternateCreditCheckAddress0());
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------


  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------

  public Address getAlternateCreditCheckAddress() {
    return alternateCreditCheckAddress;
  }


  public IDENSubscriber newIDENSubscriber(String serialNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
    return newIDENSubscriber(serialNumber, null, dealerHasDeposit, null, voiceMailLanguage);
  }

  public IDENSubscriber newIDENSubscriber(String serialNumber, String muleNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
    return newIDENSubscriber(serialNumber, muleNumber, dealerHasDeposit, null, voiceMailLanguage);
  }

  public IDENSubscriber newIDENSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
    return newIDENSubscriber(serialNumber, null, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
  }

  public IDENSubscriber newIDENSubscriber(String serialNumber, String muleNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
    account.assertAccountExists();

    try {
      Equipment equipment = provider.getEquipmentManager().validateSerialNumber(serialNumber);


      if(!equipment.isIDEN() || ((IDENEquipment)equipment).isMule()) {
        throw new InvalidSerialNumberException(serialNumber, InvalidSerialNumberException.EQUIPMENT_NOT_IDEN);
      }

      if(equipment.isSIMCard() && isNull(muleNumber)){
        throw new InvalidSerialNumberException(serialNumber, InvalidSerialNumberException.EQUIPMENT_REQUIRE_A_MULE);
      }

      if (! isNull(muleNumber)) {
         Equipment muleEquip = provider.getEquipmentManager().validateSerialNumber(muleNumber);

        if(!muleEquip.isIDEN() || !((IDENEquipment)muleEquip).isMule() ) {
          throw new InvalidSerialNumberException(muleNumber, InvalidSerialNumberException.EQUIPMENT_NOT_MULE);
        }
        if ( ! equipment.isSIMCard()) {
          // if you provide the mule, the equipment must be of SIM card
          throw new InvalidSerialNumberException(muleNumber, InvalidSerialNumberException.EQUIPMENT_NOT_SIM);
        }
        if (equipment.isSIMCard()) {
          // we may need to set it, even to null to avoid misunderstanding down the road
          ((SIMCardEquipment)equipment).setLastMule(((MuleEquipment)muleEquip));

        }
    }



      IDENSubscriberInfo info = new IDENSubscriberInfo();
      info.setBanId(account.getBanId());
      info.setProductType(AccountManager.PRODUCT_TYPE_IDEN);
      info.setSerialNumber(serialNumber);
      info.setEquipmentType(equipment.getEquipmentType());
      info.setDealerCode(account.getDealerCode());
      info.setSalesRepId(account.getSalesRepCode());
      info.setLanguage(account.getLanguage());
      info.setVoiceMailLanguage(voiceMailLanguage);

      TMIDENSubscriber tmIDENSubscriber  = new TMIDENSubscriber(provider, info, true, activationFeeChargeCode, account, dealerHasDeposit, equipment);
      tmIDENSubscriber.setEquipment(((TMEquipment)equipment));
	  return tmIDENSubscriber;
    } catch (TelusAPIException e) {
      throw e;
    } catch (Throwable e) {
      throw new TelusAPIException(e);
    }
  }

  private TMFleet[] decorate(Fleet[] fleets) throws TelusAPIException, UnknownBANException {
    TMFleet[] newFleets = new TMFleet[fleets.length];
    for(int i=0; i<fleets.length; i++) {
      newFleets[i] = new TMFleet(provider, (FleetInfo)fleets[i]);
    }
   return newFleets;
  }

  private TMTalkGroup[] decorate(TalkGroup[] talkGroups) throws TelusAPIException, UnknownBANException {
    TMTalkGroup[] newTalkGroups = new TMTalkGroup[talkGroups.length];
    for(int i=0; i<talkGroups.length; i++) {
      TalkGroupInfo info = (TalkGroupInfo)talkGroups[i];
      newTalkGroups[i] = new TMTalkGroup(provider, info, new TMFleetIdentity(provider, info.getFleetIdentity()));
    }
   return newTalkGroups;
  }

  public Fleet newFleet(final int networkId, final String name, int numberOfSubscriber) throws InvalidNetworkException, UnknownBANException, DuplicateObjectException, TelusAPIException {
	  account.assertAccountExists();
	  FleetInfo info = new FleetInfo();
	  info.setName(name);
	  info.setType(FleetInfo.TYPE_SHARED);
	  info.setBanId0(account.getBanId());
	  info.getIdentity0().setUrbanId(networkId);

	  //numberOfSubscriber = provider.getReferenceDataManager().getAccountType(account).getMinimumSubscribersForFleet();
	  TMFleet newFleet =null;
	  try { 
		  newFleet = new TMFleet(provider,   provider.getAccountLifecycleManager().createFleet(account.getBanId(),
				  (short)networkId, info, numberOfSubscriber, SessionUtil.getSessionId( provider.getAccountLifecycleManager())));
		  refreshFleets();
		  refreshTalkGroups();
	  } catch (Throwable e) {
		  provider.getExceptionHandler().handleException(e, new TelusExceptionTranslator() {

			  public TelusAPIException translateException(Throwable throwable) {
				  if(throwable instanceof TelusException){
					  return	getExceptionForErrorId(((TelusException)throwable).id, (TelusException)throwable);
				  }else if (throwable instanceof ApplicationException){
					  return	getExceptionForErrorId(((ApplicationException)throwable).getErrorCode(), (ApplicationException)throwable);
				  }
				  return null;
			  }

			  private TelusAPIException getExceptionForErrorId(String errorId, Throwable cause) {
				  if("VAL10016".equals(errorId)) {
					  return new DuplicateObjectException("Duplicate Fleet Name", cause, name);
				  }else if("APP10012".equals(errorId)) {
					  return new InvalidNetworkException("Invalid Network ID", cause, networkId);
				  }
				  return null;
			  }

		  });
	  }
	  return newFleet;
  }

  public void addFleets(Fleet[] fleet) throws UnknownBANException, InvalidFleetException, TelusAPIException {
    account.assertAccountExists();
    FleetInfo info = null;
    for(int i = 0; i < fleet.length; i++){
      addFleet(fleet[i]);
    }

  }


  public void addFleet(Fleet fleet) throws UnknownBANException, InvalidFleetException, TelusAPIException{
	  account.assertAccountExists();
	  FleetInfo info = ((TMFleet)fleet).getFleet0();
	  if(info.getType() == Fleet.TYPE_PRIVATE){
		  throw new InvalidFleetException("Cannot do associate private fleet", info.getIdentity());
	  }
	  try{
		  provider.getAccountLifecycleManager().addFleet(account.getBanId(),(short)info.getIdentity0().getUrbanId(), 
				  info, 0, SessionUtil.getSessionId( provider.getAccountLifecycleManager()));
		  getFleets();
		  fleetArray.add(fleet);
		  talkGroupArray.clear();
		  if(info.getType() != Fleet.TYPE_PUBLIC){
			  FollowUp followUP =account.newFollowUp();
			  followUP.setAssignedToWorkPositionId("19730");
			  followUP.setFollowUpType("CMFA");
			  followUP.setText("Request to share Fleet with Ban "+ fleet.getOwner().getBanId());
			  followUP.create();
		  }
	  }catch (Throwable e) {
		  provider.getExceptionHandler().handleException(e);
	  }
  }


  public void addTalkGroups(TalkGroup[] talkGroup) throws UnknownBANException, InvalidFleetException, TelusAPIException {
    account.assertAccountExists();
    for(int i =0; i < talkGroup.length; i++){
      addTalkGroup(talkGroup[i]);
    }
  }

  public void addTalkGroup(TalkGroup talkGroup) throws UnknownBANException, InvalidFleetException, TelusAPIException {
	  account.assertAccountExists();
	  if(talkGroup.getFleet().getType() == Fleet.TYPE_PRIVATE){
		  throw new InvalidFleetException("Cannot do associate private fleet", ((TMTalkGroup)talkGroup).getTalkGroup0().getFleetIdentity());
	  }
	  try{
		  provider.getAccountLifecycleManager().addTalkGroup(account.getBanId(), ((TMTalkGroup)talkGroup).getTalkGroup0(),
				  SessionUtil.getSessionId( provider.getAccountLifecycleManager()));
		  talkGroupArray.add(talkGroup);
	  }catch(Throwable e){
		  provider.getExceptionHandler().handleException(e);
	  }
  }

  public void removeTalkGroup(TalkGroup talkGroup) throws TooManyObjectsException, TelusAPIException {
	  account.assertAccountExists();
	  if (talkGroup.getAttachedSubscriberCount(account.getBanId()) > 0) {
		  throw new TooManyObjectsException("Cannot disassociate talkgroup with attached subscribers");
	  }
	  try{
		  provider.getAccountLifecycleManager().removeTalkGroup(account.getBanId(), ((TMTalkGroup)talkGroup).getTalkGroup0(),
				  SessionUtil.getSessionId( provider.getAccountLifecycleManager()));
		  talkGroupArray.remove(talkGroup);
	  }catch(Throwable e){
		  provider.getExceptionHandler().handleException(e);
	  }
  }

  public Fleet[] getFleets() throws UnknownBANException, TelusAPIException {
    return getFleets0();
  }

  public TMFleet[] getFleets0() throws UnknownBANException, TelusAPIException {
	  account.assertAccountExists();
	  TMFleet[] tmFleets =null;
	  try{
		  if ( !fleetArray.isEmpty() )
			  return (TMFleet[])fleetArray.toArray(new TMFleet[fleetArray.size()]);
		  Fleet[] fleets =null;
		  fleets=  provider.getAccountInformationHelper().retrieveFleetsByBan(account.getBanId());

		  tmFleets = decorate(fleets);
		  for(int i = 0; i < tmFleets.length; i++){
			  fleetArray.add(tmFleets[i]);
		  }
	  }catch(Throwable e) {
		  if (((e instanceof TelusException)&& ("APP10012".equals(((TelusException)e).id)))
				  || ((e instanceof ApplicationException) && ("APP10012".equals(((ApplicationException)e).getErrorCode())))){
			  return new TMFleet[0];
		  }
		  provider.getExceptionHandler().handleException(e);	
	  }

	  return tmFleets;
  }

  public TalkGroup[] getTalkGroups() throws UnknownBANException, TelusAPIException {
	  account.assertAccountExists();
	  TMTalkGroup[] tmtalkGroups=null;
	  try{
		  TalkGroup[] talkGroups =null;
		  if ( !talkGroupArray.isEmpty() )
			  return (TMTalkGroup[])talkGroupArray.toArray(new TMTalkGroup[talkGroupArray.size()]);

		  List list =  provider.getAccountInformationHelper().retrieveTalkGroupsByBan(account.getBanId());
		  talkGroups= (TalkGroupInfo[])list.toArray(new TalkGroupInfo[list.size()]);

		  tmtalkGroups = decorate(talkGroups);
		  for(int i = 0; i < tmtalkGroups.length; i++){
			  talkGroupArray.add(tmtalkGroups[i]);
		  }

	  }catch(Throwable e) {
		  provider.getExceptionHandler().handleException(e);	
	  }
	  return tmtalkGroups;
  }

  public TalkGroup[] getTalkGroups(int urbanId, int fleetId) throws UnknownBANException, TelusAPIException {

    ArrayList talkGroupsForGivenFleetArray = new ArrayList();
    
    try{

  		// get all talk groups for this account
  	    TalkGroup[] allTalkGroupsForBan = getTalkGroups();
        
  	    // return talk groups for selected fleet
  	    for(int i = 0; i < allTalkGroupsForBan.length; i++){
  	    	if (allTalkGroupsForBan[i].getFleet().getIdentity().getUrbanId() == urbanId &&
    			allTalkGroupsForBan[i].getFleet().getIdentity().getFleetId() == fleetId)
  	    	talkGroupsForGivenFleetArray.add(allTalkGroupsForBan[i]);
          }
  	    return (TMTalkGroup[])talkGroupsForGivenFleetArray.toArray(new TMTalkGroup[talkGroupsForGivenFleetArray.size()]);
        }catch(Throwable e) {
          throw new TelusAPIException(e);
        }
  }

  public boolean contains(Fleet fleet) throws UnknownBANException, TelusAPIException {
    account.assertAccountExists();
    Fleet[] fleets = getFleets();
    for(int i = 0; i < fleets.length; i++){
     if(fleet.getIdentity().equals(fleets[i].getIdentity())){
       return true;
     }
    }
   return false;
  }

  public boolean contains(TalkGroup talkGroup) throws UnknownBANException, TelusAPIException {
    account.assertAccountExists();
    TalkGroup[] talkGroups = getTalkGroups();
    for(int i = 0; i < talkGroups.length; i++){
     if(talkGroup.getTalkGroupId() == talkGroups[i].getTalkGroupId()){
      return true;
     }
    }
    return false;
  }

  public void refresh() throws TelusAPIException {
  	refreshFleets();
  	refreshTalkGroups();
  }
  
  public void refreshFleets() throws TelusAPIException {
    Logger.debug("TMIDENAccount.refreshFleets - clearing fleetArray");
  	fleetArray.clear();
  }
  
  public void refreshTalkGroups() throws TelusAPIException {
  	Logger.debug("TMIDENAccount.refreshTalkGroups - clearing talkGroupArray");
  	talkGroupArray.clear();
  }
  
  public void removeFleet(Fleet fleet) throws InvalidFleetException, TelusAPIException {
	  account.assertAccountExists();
	  FleetInfo fleetInfo = ((TMFleet)fleet).getFleet0();      
	  if (fleetInfo.getType() == Fleet.TYPE_PRIVATE) {
		  throw new InvalidFleetException("Cannot remove a private fleet.", fleetInfo.getIdentity());
	  }
	  try {
		  provider.getAccountLifecycleManager().dissociateFleet(account.getBanId(), fleetInfo,
				  SessionUtil.getSessionId( provider.getAccountLifecycleManager()));
		  getFleets();
		  fleetArray.remove(fleet);
		  talkGroupArray.clear();
	  } catch (Throwable e) {
		  provider.getExceptionHandler().handleException(e);	
	  }      
  }
  public IDENSubscriber[] getSubscribers(int urbanId, int fleetId, int maximum) throws UnknownBANException, TelusAPIException {
	account.assertAccountExists();
	IDENSubscriber[] subscribers = provider.getAccountManager0().findSubscribersByBanAndFleet(account.getBanId(), urbanId, fleetId, maximum);
	for (int i = 0; i < subscribers.length; i++) {
	  TMIDENSubscriber s = (TMIDENSubscriber)subscribers[i];
	  s.setAccountSummary(account);
	}
	return subscribers;
  }

}