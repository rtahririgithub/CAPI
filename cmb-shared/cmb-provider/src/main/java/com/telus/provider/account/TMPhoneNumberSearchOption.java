package com.telus.provider.account;

import com.telus.api.account.PhoneNumberSearchOption;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;

public class TMPhoneNumberSearchOption implements PhoneNumberSearchOption {

	PhoneNumberSearchOptionInfo delegate;

	public TMPhoneNumberSearchOption() {
	     delegate = new PhoneNumberSearchOptionInfo();
	  }
	public PhoneNumberSearchOptionInfo getDelegate() {
		return delegate;
	}

	public boolean isSearchWirelessNumber() {
		return delegate.isSearchWirelessNumber();
	}

	public void setSearchWirelessNumber(boolean searchWirelessNumber) {
		delegate.setSearchWirelessNumber(searchWirelessNumber);
	}

	public boolean isSearchVOIP() {
		return delegate.isSearchVOIP();
	}

	public void setSearchVOIP(boolean searchVOIP) {
		delegate.setSearchVOIP(searchVOIP);
	}

	public boolean isSearchTollFree() {
		return delegate.isSearchTollFree();
	}

	public void setSearchTollFree(boolean searchTollFree) {
		delegate.setSearchTollFree(searchTollFree);
	}

	public boolean isSearchHSIA() {
		return delegate.isSearchHSIA();
	}

	public void setSearchHSIA(boolean searchHSIA) {
		delegate.setSearchHSIA(searchHSIA);
	}

}
