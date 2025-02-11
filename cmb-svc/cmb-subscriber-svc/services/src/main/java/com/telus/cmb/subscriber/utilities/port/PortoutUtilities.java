package com.telus.cmb.subscriber.utilities.port;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public class PortoutUtilities {
	private static final Log logger = LogFactory.getLog(PortoutUtilities.class);
	
	public static final String PORT_OUT_PORT_TYPE = "O";
	public static final char SUB_STATUS_CANCEL = 'C';
	public static final char SUB_STATUS_ACTIVE = 'A';
	public static final char SUB_STATUS_SUSPEND = 'S';
	public static final char ACCOUNT_STATUS_NO_CHANGE = '\0'; 
	public static final char ACCOUNT_STATUS_SUSPENDED = 'S';
	public static final char ACCOUNT_STATUS_CANCELED  = 'N';
	public static final char ACCOUNT_TYPE_CORPORATE = 'C';

	@SuppressWarnings("unchecked")
	public static SubscriberInfo getSubscriberInfo(String incomingBan, String phoneNumber, SubscriberLifecycleHelper subscriberLifecycleHelper) throws ApplicationException {
		if (incomingBan != null) {
			return subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(Integer.valueOf(incomingBan), phoneNumber);
		} else {
			Collection<SubscriberInfo> subscriberInfoList = subscriberLifecycleHelper.retrieveSubscriberListByPhoneNumber(phoneNumber, 1000, true);
			if (subscriberInfoList == null || subscriberInfoList.size() < 1) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.BAN_NOT_FOUND, "BAN Not Found", "");
			} else {
				for (SubscriberInfo sInfo : subscriberInfoList) {
					if (sInfo.getStatus() != SUB_STATUS_CANCEL) 
						return sInfo;
				}
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Subscriber is already cancelled.", "");
			}
		}
	}
	
	public static Date getLogicalDate(ReferenceDataHelper referenceDataHelper) { 
		Date logicalDate = null;
		try {
			logicalDate = referenceDataHelper.retrieveLogicalDate();
		} catch (TelusException e) {
			throw new SystemException(SystemCodes.CMB_SLF_EJB, "Reference data helper threw exception.", "", e);
		}
		return logicalDate;
	}

	public static Date getBillCycleCloseDate(int billCycleCloseDay, Date effectiveDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(effectiveDate);
		int currMonth = cal.get(Calendar.MONTH);
		int currYear = cal.get(Calendar.YEAR);
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		if (billCycleCloseDay > cal.getActualMaximum(Calendar.DATE))
			billCycleCloseDay = cal.getActualMaximum(Calendar.DATE);

		cal.set(currYear, currMonth, billCycleCloseDay);
		    
		Date billCycleCloseDate = cal.getTime();
		if (billCycleCloseDate.before(effectiveDate)) {
			cal.add(Calendar.MONTH, 1);
			billCycleCloseDate = cal.getTime();
		}
		
		return billCycleCloseDate;
	}

	public static Date getNextBillCycleCloseDate(AccountInfo accInfo, Date effectiveDate) {
		int nextBillCycleCloseDay = accInfo.getNextBillCycleCloseDay() == 0 ? accInfo.getBillCycleCloseDay() : accInfo.getNextBillCycleCloseDay();
		return getNextBillCycleCloseDate(nextBillCycleCloseDay, effectiveDate);
	}
	
	public static Date getNextBillCycleCloseDate(int nextBillCycleCloseDay, Date today) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MONTH, 1);

		if (nextBillCycleCloseDay > cal.getActualMaximum(Calendar.DATE))
			nextBillCycleCloseDay = cal.getActualMaximum(Calendar.DATE);

		cal.set(Calendar.DAY_OF_MONTH, nextBillCycleCloseDay);
		return cal.getTime();
	}


	public static boolean cancelImmediately(boolean forceCancelImmediateIndicator, SubscriberInfo subInfo, Date effectiveDate, Date billCycleCloseDate, char accountType) {
		return forceCancelImmediateIndicator || accountType == ACCOUNT_TYPE_CORPORATE || effectiveDate.equals(billCycleCloseDate);
	}
	
	
	public static char getAccountStatusChangeAfterCancel(AccountInfo accInfo, SubscriberInfo subInfo, CommunicationSuiteInfo commSuiteInfo) {
	    char result = ' ';

		int activeCompanionSubscribersCountNowCancel = 0;
		int suspendCompanionSubscribersCountNowCancel = 0;
		
		if(commSuiteInfo!=null && commSuiteInfo.isRetrievedAsPrimary()==true && commSuiteInfo.getActiveAndSuspendedCompanionPhoneNumberList().isEmpty()==false ){
			activeCompanionSubscribersCountNowCancel += commSuiteInfo.getActiveCompanionCount();
			suspendCompanionSubscribersCountNowCancel += commSuiteInfo.getSuspendedCompanionCount();
		}
		
	    int activeCountOnAccount = accInfo.getAllActiveSubscribersCount() - activeCompanionSubscribersCountNowCancel;
	    int suspendCountOnAccount = accInfo.getAllSuspendedSubscribersCount() - suspendCompanionSubscribersCountNowCancel;
	    
	    if(subInfo.getStatus() == SubscriberInfo.STATUS_ACTIVE){
	    	activeCountOnAccount --;
		}else if(subInfo.getStatus() == SubscriberInfo.STATUS_SUSPENDED){
			suspendCountOnAccount --;
		}
	    
	    if (logger.isDebugEnabled()) {
	    	logger.debug("activeCountOnAccount=["+activeCountOnAccount+"], suspendCountOnAccount=["+suspendCountOnAccount+"], ban=["+accInfo.getBanId()+"], subId=["+subInfo.getSubscriberId()+"], commSuiteInfo=["+commSuiteInfo+"]");
	    }
	    
	    if (activeCountOnAccount > 0) {
	        // if there is one active subscriber, then the account must be (and remain) in opened status
	        result = ACCOUNT_STATUS_NO_CHANGE;
	    } else if (suspendCountOnAccount > 0) {
	        if (accInfo.getStatus() == ACCOUNT_STATUS_SUSPENDED) {
	            result = ACCOUNT_STATUS_NO_CHANGE;
	        } else {
	            result = ACCOUNT_STATUS_SUSPENDED;
	        }
	    } else {
	        // both active subscriber count and suspended subscriber count will be 0.
	        result = ACCOUNT_STATUS_CANCELED;
	    }
	    return result;
	}
	
	
	public static char getAccountStatusChangeAfterSuspend(SubscriberInfo subInfo, AccountInfo accInfo, int numberOfActiveSubsToSuspend)  {
		char result = ' ';
		
		int activeCountOnAccount = accInfo.getAllActiveSubscribersCount() - numberOfActiveSubsToSuspend;
		
	    if (logger.isDebugEnabled()) {
	    	logger.debug("activeCountOnAccount=["+activeCountOnAccount+"], ban=["+accInfo.getBanId()+"], subId=["+subInfo.getSubscriberId()+"], numberOfActiveSubsToSuspend=["+numberOfActiveSubsToSuspend+"]");
	    }
		
		if (activeCountOnAccount == 0) {
			result = ACCOUNT_STATUS_SUSPENDED;
		}else {
			result = ACCOUNT_STATUS_NO_CHANGE;
		}
				
		return result;
	}
	
}
