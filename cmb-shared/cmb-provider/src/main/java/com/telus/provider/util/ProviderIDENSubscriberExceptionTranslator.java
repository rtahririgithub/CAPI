package com.telus.provider.util;

import com.telus.api.InvalidChangeMemberIdException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.DispatchResourceContractMismatchException;
import com.telus.api.account.InvalidFleetException;
import com.telus.api.account.MemberIdMatchException;
import com.telus.api.account.PhoneNumberException;
import com.telus.api.account.PhoneNumberInUseException;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.framework.exception.PhoneNumberAlreadyInUseException;

/**
 * @author Michael Liao
 * 
 * This is used in TMIDENSubscriber to translate various exception from ECA / CMB EJB
 *
 */
public class ProviderIDENSubscriberExceptionTranslator extends ProviderDefaultExceptionTranslator {
	private int urbanId;
	private int fleetId;
	private String memberId;
	
	public ProviderIDENSubscriberExceptionTranslator( ) {
	}
	
	public ProviderIDENSubscriberExceptionTranslator( String memberId ) {
		this.memberId = memberId;
	}
	
	public ProviderIDENSubscriberExceptionTranslator( int urbanId, int fleetId, String memberId ) {
		this.urbanId = urbanId;
		this.fleetId = fleetId;
		this.memberId = memberId;
	}
	
	public TelusAPIException translateException(Throwable throwable) {

		TelusAPIException exception = null;
		
		if (throwable instanceof PhoneNumberAlreadyInUseException) {
			//TMIDENSubscriber.changeFaxNumber()
			exception = new PhoneNumberInUseException(PhoneNumberInUseException.ADDITIONAL_RESERVATION_FAILED, throwable.getMessage(), throwable);
		}/* 
		else if (throwable instanceof PhoneNumberInUseException) {
			//TMIDENSubscriber.reserveIDENResourcesForPortIn()
			exception = ((TelusAPIException) throwable);
		} */
		else {
			exception = super.translateException(throwable);
		}
		return exception;
	}
	
	protected TelusAPIException getExceptionForErrorId(String errorId, Throwable cause) {
		
		if("VAL20033".equals(errorId)){
			return new MemberIdMatchException(cause, this.memberId);
		} 
		else if ("VAL20034".equals(errorId ) ) {
			return new InvalidChangeMemberIdException(InvalidChangeMemberIdException.PTN_BASEFLEET, cause);
		} 
		else if("VAL20035".equals(errorId)){
			return new DispatchResourceContractMismatchException(cause);
		} 
		else if("VAL20036".equals(errorId)){
			return new DispatchResourceContractMismatchException(cause);
		} 
		else if ("VAL20037".equals(errorId ) ) {
			return new InvalidChangeMemberIdException(0, cause);
		} 
		else if("VAL10010".equals(errorId)){
			return new InvalidFleetException(cause, new FleetIdentityInfo(this.urbanId, this.fleetId));
		} 
		else if("APP20001".equals(errorId)){
			//TMIDENSubscriber.changeFaxNumber()
			return new PhoneNumberException(PhoneNumberException.ADDITIONAL_RESERVATION_FAILED, cause.getMessage(), cause);
		} 
		else if("APP20004".equals(errorId)){
			//TMIDENSubscriber.localReserveMemberId(Fleet, String)
			return new MemberIdMatchException(cause, this.memberId);
		} 
		return null;
	}

	
}
