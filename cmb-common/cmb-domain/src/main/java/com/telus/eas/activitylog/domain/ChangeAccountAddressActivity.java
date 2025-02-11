/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.eas.activitylog.domain;

import java.io.Serializable;

import com.telus.api.account.Address;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.util.ToStringBuilder;
import com.telus.eas.activitylog.queue.info.ActivityLoggingInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ChangeAccountAddressActivity extends ActivityLoggingInfo {

	private static final long serialVersionUID = 1L;
	
	private AddressHolder address = null;
	
	public ChangeAccountAddressActivity(ServiceRequestHeader header) {
		super(header);
	}
	
	public void setAddress(Address address) {
		this.address = createAddressHolder(address);
	}
	
	public AddressHolder getAddress() {
		return address;
	}
	
	public String getMessageType() {
		return MESSAGE_TYPE_CHANGE_ACCOUNT_ADDRESS;
	}
	
	private AddressHolder createAddressHolder(Address address) {
		return new AddressHolder(address.getProvince(), address.getCity(), 
				address.getPostalCode(), address.getStreetName(), address.getStreetNumber());
	}

	public class AddressHolder implements Serializable {
		
		private static final long serialVersionUID = 1L;

		private String province = null;
		
		private String city = null;
		
		private String postalCode = null;
		
		private String streetName = null;
		
		private String streetNumber = null;
		
		
		public AddressHolder(String province, String city, String postalCode, String streetName, String streetNumber) {
			this.province = province;
			this.city = city;
			this.postalCode = postalCode;
			this.streetName = streetName;
			this.streetNumber = streetNumber;
		}

		/**
		 * @return the province
		 */
		public String getProvince() {
			return province;
		}
		
		/**
		 * @param province the province to set
		 */
		public void setProvince(String province) {
			this.province = province;
		}
		
		/**
		 * @return the city
		 */
		public String getCity() {
			return city;
		}
		
		/**
		 * @param city the city to set
		 */
		public void setCity(String city) {
			this.city = city;
		}
		
		/**
		 * @return the postalCode
		 */
		public String getPostalCode() {
			return postalCode;
		}
		
		/**
		 * @param postalCode the postalCode to set
		 */
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
		
		/**
		 * @return the streetName
		 */
		public String getStreetName() {
			return streetName;
		}
		
		/**
		 * @param streetName the streetName to set
		 */
		public void setStreetName(String streetName) {
			this.streetName = streetName;
		}
		
		/**
		 * @return the streetNumber
		 */
		public String getStreetNumber() {
			return streetNumber;
		}
		
		/**
		 * @param streetNumber the streetNumber to set
		 */
		public void setStreetNumber(String streetNumber) {
			this.streetNumber = streetNumber;
		}
		
		public String toString() {
			return ToStringBuilder.toString(this);
		}
	}

}
