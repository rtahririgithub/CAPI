package com.telus.api.portability;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Address;
import com.telus.api.account.BasePrepaidAccount;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.PostpaidBusinessRegularAccount;
import com.telus.api.account.PostpaidConsumerAccount;

public interface PortRequestManager {

	/**
	 * @deprecated Deprecated in favour of the testPortInEligibility method.  Calling this method is equivalent to
	 * 			   calling the new testPortInEligibility method with Brand.BRAND_ID_TELUS as the incoming brand.
	 **/
	PortInEligibility testEligibility(String phoneNumber, String portVisibility) throws PortRequestException, TelusAPIException;	
	PortInEligibility testPortInEligibility(String phoneNumber, String portVisibility, int incomingBrand) throws PortRequestException, TelusAPIException;	
	PortOutEligibility testPortOutEligibility(String phoneNumber, String ndpInd) throws PortRequestException, TelusAPIException;
  /**
   * @deprecated Please use getPRMReferenceData(String code, int brandId) instead.
   */
  PRMReferenceData getPRMReferenceData(String category, String code);
  PRMReferenceData getPRMReferenceData(String code);

	public static class Helper {
		
		public static PortRequest copyName(Account account, PortRequest portRequest){
			if (account instanceof PostpaidConsumerAccount || account instanceof BasePrepaidAccount){
				ConsumerName cName = null;
				if (account instanceof PostpaidConsumerAccount)
					cName = ((PostpaidConsumerAccount)account).getName();
				else if (account instanceof BasePrepaidAccount)
					cName = ((BasePrepaidAccount)account).getName();

				PortRequestName portRequestName = portRequest.getPortRequestName();
				portRequestName.setTitle(cName.getTitle());
				String firstName = cName.getFirstName();
				if (firstName == null) firstName = "";
				portRequestName.setFirstName(firstName);
				String lastName = cName.getLastName();
				if (lastName == null) lastName = "";
				portRequestName.setLastName(lastName);
				String middleName = cName.getMiddleInitial();
				if (middleName == null) middleName = "";
				portRequestName.setMiddleInitial(middleName);
				portRequestName.setGeneration(cName.getGeneration());

				portRequest.setPortRequestName(portRequestName);

				if (!middleName.equals(""))
					portRequest.setAgencyAuthorizationName(firstName + " " + middleName + " " + lastName);
				else
					portRequest.setAgencyAuthorizationName(firstName + " " + lastName);
			}
			else if (account instanceof PostpaidBusinessRegularAccount){
				String businessName = ((PostpaidBusinessRegularAccount)account).getLegalBusinessName();
				portRequest.setBusinessName(businessName);
				portRequest.setAgencyAuthorizationName(businessName);
			}
			return portRequest;
		}

		public static PortRequest copyAddress(Account account, PortRequest portRequest){
			Address address = account.getAddress();
			PortRequestAddress portRequestAddress = portRequest.getPortRequestAddress();
			portRequestAddress.setCity(address.getCity());
			portRequestAddress.setCountry(address.getCountry());
			portRequestAddress.setPostalCode(address.getPostalCode());
			portRequestAddress.setProvince(address.getProvince());
			portRequestAddress.setStreetDirection(address.getStreetDirection());
			String streetNumberSuffix = address.getStreetNumberSuffix();
			if (streetNumberSuffix == null) streetNumberSuffix = "";
			String streetNumber = address.getStreetNumber();
			if (streetNumber == null) streetNumber = "";
			portRequestAddress.setStreetNumber(streetNumber + " " + streetNumberSuffix);
			String streetName = address.getStreetName();
			if (streetName == null) streetName = "";
			portRequestAddress.setStreetName(streetName +
					nullToString(" ", address.getStreetType())+
					additionalAddressParameters(address));

			portRequest.setPortRequestAddress(portRequestAddress);
			return portRequest;
		}

		private static String additionalAddressParameters(Address address) {

			String addrParams = "";
			if (Address.ADDRESS_TYPE_CITY.equals(address.getAddressType()) || Address.ADDRESS_TYPE_RURAL.equals(address.getAddressType())) {
				addrParams += nullToString(nullToString(" ", address.getUnitType()) + " ", address.getUnit());   
				if (Address.ADDRESS_TYPE_RURAL.equals(address.getAddressType())) {
					addrParams +=
						nullToString(" ", (address.getRuralType() == null ? null :
							address.getRuralType().equals(Address.RURAL_TYPE_RURAL_ROUTE) ? "RR" :
								address.getRuralType().equals(Address.RURAL_TYPE_PO_BOX) ? "PO BOX" :
									address.getRuralType().equals(Address.RURAL_TYPE_GENERAL_DELIVERY) ? "GD" :
										address.getRuralType().equals(Address.RURAL_TYPE_STATION_MAIN) ? "MR" :
											address.getRuralType().equals(Address.RURAL_TYPE_TOWNSHIP) ? "SS" :
												null)) +
												nullToString(" ", address.getRuralNumber()) +
												nullToString(" ", address.getRuralLocation()) +
												nullToString(" ", address.getRuralDeliveryType()) +
												nullToString(" ", address.getRuralQualifier());

					addrParams +=
						nullToString(" SITE ", address.getRuralSite()) +
						nullToString(" BOX ", address.getPoBox()) +
						nullToString(" COMPARTMENT ", address.getRuralCompartment()) +
						nullToString(" GROUP ", address.getRuralGroup());
				}
			} 
			else if (Address.ADDRESS_TYPE_CITY.equals(address.getAddressType())) {
				addrParams += nullToString(" ", address.getStreetType());
			}
			else if (Address.ADDRESS_TYPE_FOREIGN.equals(address.getAddressType())) {
				addrParams = address.getPrimaryLine();
				addrParams += nullToString(" ", address.getSecondaryLine());
			}      
			return addrParams;
		}
		
		public static final String nullToString(String prefix, Object o) {
			return (o == null)?"":prefix + o.toString();
		}
	}
}
