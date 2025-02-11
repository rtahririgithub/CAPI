package com.telus.api.account;

public interface PhoneNumberSearchOption {

	/**
	 * @author x146449 PhoneNumberSearch Criteria class for wirelessPhoneNumber and SeatResource
	 */

	public boolean isSearchWirelessNumber();

	public void setSearchWirelessNumber(boolean searchWirelessNumber);

	public boolean isSearchVOIP();

	public void setSearchVOIP(boolean searchVOIP);

	public boolean isSearchTollFree();

	public void setSearchTollFree(boolean searchTollFree);

	public boolean isSearchHSIA();

	public void setSearchHSIA(boolean searchHSIA);

}
