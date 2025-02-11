package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.FleetSecuredInfo;
import amdocs.APILink.datatypes.UpdateFleetInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.IdenResourceServices;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.fleet.Fleet;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.eas.framework.exception.TelusApplicationException;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ExceptionInfo;

@Deprecated
class IdenSubscriberDaoHelperImpl extends SubscriberDaoImpl{
	
	private final Logger LOGGER = Logger.getLogger(IdenSubscriberDaoHelperImpl.class);
	
	public String[] retrieveAvailablePhoneNumbers(int ban, 
			PhoneNumberReservation phoneNumberReservation, int maxNumbers, String sessionId)
			throws ApplicationException {			   	  
	    String startFromPhoneNumber = "0000000000";

	    String wildCardAmdocs = super.getWildCardAmdocs(phoneNumberReservation.getPhoneNumberPattern());
	    
	    
	    String[] phoneNumbers = super.getAmdocsTemplate().execute(sessionId, new IdenRetrieveAvailablePhoneNumbersTransactionCallback
	    		(phoneNumberReservation, startFromPhoneNumber, wildCardAmdocs, maxNumbers));

	    return super.returnAvailablePhoneNumbers(phoneNumbers);
	   
	}
	
	private class IdenRetrieveAvailablePhoneNumbersTransactionCallback implements AmdocsTransactionCallback<String[]> {

		private PhoneNumberReservation phoneNumberReservation;
		private String startFromPhoneNumber;
		private String wildCardAmdocs;
		private int maxNumbers;

		private IdenRetrieveAvailablePhoneNumbersTransactionCallback(PhoneNumberReservation phoneNumberReservation, String startFromPhoneNumber,
				String wildCardAmdocs, int maxNumbers) {
			this.phoneNumberReservation = phoneNumberReservation;
			this.startFromPhoneNumber = startFromPhoneNumber;
			this.wildCardAmdocs = wildCardAmdocs;
			this.maxNumbers = maxNumbers;
			
		}
		
		@Override
		public String[] doInTransaction(
				AmdocsTransactionContext transactionContext) throws Exception {
			IdenResourceServices amdocsIdenResourceServices = transactionContext.createBean(IdenResourceServices.class);
			
			List<String> phoneNumbersReturn = new ArrayList<String>();
			boolean morePhoneNumbersAvailable = true;
			
			while (phoneNumbersReturn.size() < maxNumbers && morePhoneNumbersAvailable) {
				String[] phoneNumbers = amdocsIdenResourceServices.getAvailablePtnList( phoneNumberReservation.getNumberGroup().getNumberLocation(),
						phoneNumberReservation.getNumberGroup().getCode(), startFromPhoneNumber,
						wildCardAmdocs, phoneNumberReservation.isAsian());
				
				if (phoneNumbers == null || phoneNumbers.length < 10)
					morePhoneNumbersAvailable = false;
				
				if (phoneNumbers != null && phoneNumbers.length > 0) {
					for (int i=0; i < phoneNumbers.length; i++) {
						phoneNumbersReturn.add(phoneNumbers[i]);
						if (phoneNumbersReturn.size() == maxNumbers) {
							break;
						}
					}
				}
			}

			return phoneNumbersReturn.toArray(new String[phoneNumbersReturn.size()]);			
		}		
	}
	
	public void increaseExpectedNumberOfSubscribers(UpdateBanConv pUpdateBanConv, int pBan, int pUrbanId, int pFleetId) throws ValidateException, RemoteException, TelusException {

	    UpdateFleetInfo amdocsUpdateFleetInfo = new UpdateFleetInfo();

	    // Set BanPK (which also retrieves the BAN)
	    pUpdateBanConv.setBanPK(pBan);

	    // increase the number of subscribers by 1
	    // - get list of fleets associated with this ban
	    // - find fleet in question
	    // - increase expectedSubscriberNumber by 1
	    // (NOTE: future improvment would be to get a method that returns a specific fleet)
	    amdocs.APILink.datatypes.FleetInfo[] associatedFleets = pUpdateBanConv.getFleetList();
	    int fleetFound = -1;
	    if (associatedFleets != null) {
	      for (int i=0; i < associatedFleets.length; i++) {
	        if (associatedFleets[i].urbanId == pUrbanId &&
	            associatedFleets[i].fleetId == pFleetId) {
	          fleetFound = i;
	          break;
	        }
	      }
	    }
	    if (fleetFound < 0)
	      throw new TelusApplicationException(new ExceptionInfo("VAL10011","Fleet not associated to BAN!"));

	    // Populated UpdateFleetInfo
	    amdocsUpdateFleetInfo.urbanId = associatedFleets[fleetFound].urbanId;
	    amdocsUpdateFleetInfo.fleetId = associatedFleets[fleetFound].fleetId;
	    amdocsUpdateFleetInfo.expectedSubscriberNumber = associatedFleets[fleetFound].expectedSubscriberNumber + 1;
	    amdocsUpdateFleetInfo.scchInd = (byte)'S';
	    LOGGER.debug("Excecuting modifyFleet() - start...");
	    if (associatedFleets[fleetFound].fleetType == Fleet.TYPE_PUBLIC) {
	      pUpdateBanConv.modifyFleet(amdocsUpdateFleetInfo);
	    } else {
	      FleetSecuredInfo amdocsFleetSecuredInfo = new FleetSecuredInfo();
	      pUpdateBanConv.modifyFleet(amdocsUpdateFleetInfo,amdocsFleetSecuredInfo);
	    }
	    return;
	  }
}
