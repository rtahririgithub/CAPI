package com.telus.api.account;


import java.rmi.RemoteException;

import com.telus.api.BaseTest;
import com.telus.api.BrandNotSupportedException;
import com.telus.api.TelusAPIException;
import com.telus.api.reference.NumberGroup;

public class PrepaidActivationIntTest extends BaseTest {

	static {
		//setupEASECA_PT168();
		setupEASECA_QA();
	}

	public PrepaidActivationIntTest(String name) throws Throwable {
		super(name);
	}

	protected NumberGroup findNumberGroupbyNpaNxx(Subscriber subscriber, String npanxx)
	throws TelusAPIException 
	{
			NumberGroup[] numberGroups = subscriber.getAvailableNumberGroups();
			NumberGroup numberGroup = null;
			for ( int i=0; i<numberGroups.length; i++ ) {
				String[] npaNXXs = numberGroups[i].getNpaNXX();
				
				for ( int j=0; j<npaNXXs.length; j++ ) {
					if ( npanxx.equals( npaNXXs[j]) ) {
						numberGroup =  numberGroups[i];
						break;
					}
				}
			}
			if ( numberGroup==null ) throw new RuntimeException( "Unable to find the NumberGroup for[" + npanxx+"]");

			return numberGroup;
	}
	protected PhoneNumberReservation setupPhoneNumberReservation(Subscriber subscriber, String numberPattern)throws TelusAPIException {
		

		String npanxx = null;
		if (numberPattern.length()>=6) {
			npanxx = numberPattern.substring(0, 6 );
		} 
		else {
			throw new RuntimeException( "Unable to parse the numberPattern [" + numberPattern+"]");
		}

		NumberGroup numberGroup = findNumberGroupbyNpaNxx(subscriber, npanxx);

		PhoneNumberReservation reservation = api.getAccountManager().newPhoneNumberReservation();
		reservation.setNumberGroup(numberGroup);
		reservation.setAsian(false);
		reservation.setWaiveSearchFee(true);
		
		if ( numberPattern.length()==10 ) {
			reservation.setPhoneNumberPattern(numberPattern);
		} else {
			reservation.setPhoneNumberPattern(npanxx+"****");
		}
		reservation.setLikeMatch(false);
		reservation.setProductType(subscriber.getProductType());
		
		return reservation;
	}
	
	public void testAddon_ReservePhoneNumber() throws UnknownBANException, BrandNotSupportedException, TelusAPIException, RemoteException {
		
		int banId = 4036958;
		String serialNumber =  "15603173774";

		PCSPrepaidConsumerAccount account = (PCSPrepaidConsumerAccount) api.getAccountManager().findAccountByBAN(banId);

		PCSSubscriber subscriber = account.newPCSSubscriber( serialNumber, true, "EN");
		
		// Reserve phone number
		PhoneNumberReservation reservation = setupPhoneNumberReservation((PCSSubscriber)subscriber, "902175" );
		reservation.setPhoneNumberPattern("**********");

		subscriber.reservePhoneNumber(reservation);
		System.out.println( "Phone number reserved successfully: " + subscriber.getPhoneNumber() );
		
		subscriber.unreserve();
		System.out.println( "Phone number released successfully" );
		
		System.out.println( "Done.");
	}
	
	
}
