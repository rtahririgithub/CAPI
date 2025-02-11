package com.telus.cmb.subscriber.lifecyclemanager.dao.impl.amdocs;

import java.rmi.RemoteException;

import amdocs.APILink.datatypes.ContractInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.NewCdpdConv;
import amdocs.APILink.sessions.interfaces.NewCellularConv;
import amdocs.APILink.sessions.interfaces.NewPagerConv;
import amdocs.APILink.sessions.interfaces.NewProductConv;
import amdocs.APILink.sessions.interfaces.NewTangoConv;
import amdocs.APILink.sessions.interfaces.UpdateCdpdConv;
import amdocs.APILink.sessions.interfaces.UpdateCellularConv;
import amdocs.APILink.sessions.interfaces.UpdatePagerConv;
import amdocs.APILink.sessions.interfaces.UpdateProductConv;
import amdocs.APILink.sessions.interfaces.UpdateTangoConv;

import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.PhoneNumberReservation;

public class AmdocsConvBeanWrapper {

	private static <R> R endOfMethodReached(Object convBean) {
		throw new SystemException(SystemCodes.CMB_SLM_DAO, "Convenience bean object invalid, received: " + convBean.getClass().getSimpleName(),"");
	}

	public static String[] getAvailablePhoneNumbers(Object convBean,
			PhoneNumberReservation phoneNumberReservation, String phoneNumberPattern,
			String startFromPhoneNumberInternal) throws RemoteException, ValidateException {
		// CELLULAR
		if(convBean instanceof NewCellularConv) {		// New		
			return ((NewCellularConv) convBean).getAvailableSubscriberList(phoneNumberReservation.getNumberGroup().getCode(),
					startFromPhoneNumberInternal,
					phoneNumberPattern,
					phoneNumberReservation.isAsian());
		}
		if (convBean instanceof UpdateCellularConv) {	// Update
			return ((UpdateCellularConv) convBean).getAvailableSubscriberList(phoneNumberReservation.getNumberGroup().getCode(),
					startFromPhoneNumberInternal,
					phoneNumberPattern,
					phoneNumberReservation.isAsian());
		} 
		// TANGO
		if (convBean instanceof NewTangoConv) {		// New
			return ((NewTangoConv) convBean).getAvailableSubscriberList(phoneNumberReservation.getNumberGroup().getCode(),
					startFromPhoneNumberInternal,
					phoneNumberPattern,
					phoneNumberReservation.isAsian());
		}
		if (convBean instanceof UpdateTangoConv) {	// Update
			return ((UpdateTangoConv) convBean).getAvailableSubscriberList(phoneNumberReservation.getNumberGroup().getCode(),
					startFromPhoneNumberInternal,
					phoneNumberPattern,
					phoneNumberReservation.isAsian());
		}

		//CDPD
		if (convBean instanceof NewCdpdConv) {		// New
			return ((NewCdpdConv) convBean).getAvailableSubscriberList(phoneNumberReservation.getNumberGroup().getCode(),
					startFromPhoneNumberInternal,
					phoneNumberPattern,
					phoneNumberReservation.isAsian());
		}
		if (convBean instanceof UpdateCdpdConv) {	// Update
			return ((UpdateCdpdConv) convBean).getAvailableSubscriberList(phoneNumberReservation.getNumberGroup().getCode(),
					startFromPhoneNumberInternal,
					phoneNumberPattern,
					phoneNumberReservation.isAsian());			
		}

		// PAGER
		if (convBean instanceof NewPagerConv) {		// New
			return ((NewPagerConv) convBean).getAvailableSubscriberList(phoneNumberReservation.getNumberGroup().getCode(),
					startFromPhoneNumberInternal,
					phoneNumberPattern,
					phoneNumberReservation.isAsian());
		}
		if (convBean instanceof UpdatePagerConv) {		// Update
			return ((UpdatePagerConv) convBean).getAvailableSubscriberList(phoneNumberReservation.getNumberGroup().getCode(),
					startFromPhoneNumberInternal,
					phoneNumberPattern,
					phoneNumberReservation.isAsian());
		}

		// IDEN is special and this method is not called for it.	

		return endOfMethodReached(convBean);
	}

	public static String[] getAvailableSubscriberList(UpdateProductConv updateConvBean
			, String numberGroupCode, String startingNumber
			, String phoneNumber, boolean isAsian) throws RemoteException, ValidateException {
		// CELLULAR
		if (updateConvBean instanceof UpdateCellularConv) {	// Update
			return ((UpdateCellularConv) updateConvBean).getAvailableSubscriberList(numberGroupCode, startingNumber, phoneNumber, isAsian);
		}
		// TANGO
		if (updateConvBean instanceof UpdateTangoConv) {	// Update
			return ((UpdateTangoConv) updateConvBean).getAvailableSubscriberList(numberGroupCode, startingNumber, phoneNumber, isAsian);
		}
		//CDPD
		if (updateConvBean instanceof UpdateCdpdConv) {	// Update
			return ((UpdateCdpdConv) updateConvBean).getAvailableSubscriberList(numberGroupCode, startingNumber, phoneNumber, isAsian);
		}
		// PAGER
		if (updateConvBean instanceof UpdatePagerConv) {		// Update
			return ((UpdatePagerConv) updateConvBean).getAvailableSubscriberList(numberGroupCode, startingNumber, phoneNumber, isAsian);
		}

		return endOfMethodReached(updateConvBean);
	}
	
	public static String[] getAvailableSubscriberList(NewProductConv newConvBean
			, String numberGroupCode, String startingNumber
			, String phoneNumber, boolean isAsian) throws RemoteException, ValidateException {
		// CELLULAR
		if (newConvBean instanceof NewCellularConv) {	// New
			return ((NewCellularConv) newConvBean).getAvailableSubscriberList(numberGroupCode, startingNumber, phoneNumber, isAsian);
		}
		// TANGO
		if (newConvBean instanceof NewTangoConv) {	// New
			return ((NewTangoConv) newConvBean).getAvailableSubscriberList(numberGroupCode, startingNumber, phoneNumber, isAsian);
		}
		//CDPD
		if (newConvBean instanceof NewCdpdConv) {	// New
			return ((NewCdpdConv) newConvBean).getAvailableSubscriberList(numberGroupCode, startingNumber, phoneNumber, isAsian);
		}
		// PAGER
		if (newConvBean instanceof NewPagerConv) {		// New
			return ((NewPagerConv) newConvBean).getAvailableSubscriberList(numberGroupCode, startingNumber, phoneNumber, isAsian);
		}

		return endOfMethodReached(newConvBean);
	}

	public static void changeSubscriberNumber(UpdateProductConv updateConvBean, String phoneNumber, String reasonCode, ContractInfo contractInfo) 
	throws RemoteException, ValidateException {
		
		if (updateConvBean instanceof UpdateCellularConv) {		// Update CELLULAR
			((UpdateCellularConv) updateConvBean).changeSubscriberNumber(phoneNumber, reasonCode, contractInfo);
		}else if(updateConvBean instanceof UpdateTangoConv) {		// Update TANGO
			((UpdateTangoConv) updateConvBean).changeSubscriberNumber(phoneNumber, reasonCode, contractInfo);
		}else if(updateConvBean instanceof UpdateCdpdConv) {			// Update CDPD
			((UpdateCdpdConv) updateConvBean).changeSubscriberNumber(phoneNumber, reasonCode, contractInfo);
		}else if(updateConvBean instanceof UpdatePagerConv) {		// Update PAGER
			((UpdatePagerConv) updateConvBean).changeSubscriberNumber(phoneNumber, reasonCode, contractInfo);
		}else{
			endOfMethodReached(updateConvBean);
		}
		
	}

	public static void changeSubscriberNumber(UpdateProductConv updateConvBean, String phoneNumber, String reasonCode) 
	throws RemoteException, ValidateException {
		
		if (updateConvBean instanceof UpdateCellularConv) {		// Update CELLULAR
			((UpdateCellularConv) updateConvBean).changeSubscriberNumber(phoneNumber, reasonCode);
		}else if(updateConvBean instanceof UpdateTangoConv) {		// Update TANGO
			((UpdateTangoConv) updateConvBean).changeSubscriberNumber(phoneNumber, reasonCode);
		}else if(updateConvBean instanceof UpdateCdpdConv) {			// Update CDPD
			((UpdateCdpdConv) updateConvBean).changeSubscriberNumber(phoneNumber, reasonCode);
		}else if(updateConvBean instanceof UpdatePagerConv) {		// Update PAGER
			((UpdatePagerConv) updateConvBean).changeSubscriberNumber(phoneNumber, reasonCode);
		}else{
			endOfMethodReached(updateConvBean);
		}
			
	}
	
	public static void reserveSubscriber(NewProductConv newConvBean
			, String phoneNumber,boolean isOfflineReservation) throws RemoteException, ValidateException {
		
		if (newConvBean instanceof NewCellularConv) {	// New  CELLULAR
			if(isOfflineReservation) // if isOfflineReservation call kb api to reserve the subscriber from on-hold staus
			{
				((NewCellularConv) newConvBean).reserveSubscriber(phoneNumber,true);
			}else { // usual reservation
				((NewCellularConv) newConvBean).reserveSubscriber(phoneNumber);
			}
			
		}else if(newConvBean instanceof NewTangoConv) {	// New // TANGO
			((NewTangoConv) newConvBean).reserveSubscriber(phoneNumber);
		}else if(newConvBean instanceof NewCdpdConv) {	// New CDPD
			((NewCdpdConv) newConvBean).reserveSubscriber(phoneNumber);
		}else if(newConvBean instanceof NewPagerConv) {		// New PAGER
			((NewPagerConv) newConvBean).reserveSubscriber(phoneNumber);
		}else{
			endOfMethodReached(newConvBean);
		}
	}


}
