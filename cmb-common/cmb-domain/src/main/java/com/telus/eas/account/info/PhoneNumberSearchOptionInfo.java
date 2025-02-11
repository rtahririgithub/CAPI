package com.telus.eas.account.info;

import com.telus.api.account.PhoneNumberSearchOption;
import com.telus.eas.framework.info.Info;

public class PhoneNumberSearchOptionInfo extends Info implements PhoneNumberSearchOption {
	private static final long serialVersionUID = 1L;
	private boolean searchWirelessNumber = true;
	private boolean searchVOIP;
	private boolean searchTollFree;
	private boolean searchHSIA;

	public boolean isSearchWirelessNumber() {

		return searchWirelessNumber;
	}

	public void setSearchWirelessNumber(boolean searchWirelessNumber) {
		this.searchWirelessNumber = searchWirelessNumber;
	}

	public boolean isSearchVOIP() {
		return searchVOIP;
	}

	public void setSearchVOIP(boolean searchVOIP) {
		this.searchVOIP = searchVOIP;

	}

	public boolean isSearchTollFree() {
		return searchTollFree;
	}

	public void setSearchTollFree(boolean searchTollFree) {
		this.searchTollFree = searchTollFree;
	}

	public boolean isSearchHSIA() {
		return searchHSIA;
	}

	public void setSearchHSIA(boolean searchHSIA) {
		this.searchHSIA = searchHSIA;
	}

}
