package com.telus.eas.contactevent.info;

import java.util.ArrayList;
import java.util.List;

import com.telus.eas.framework.info.Info;
import com.telus.eas.contactevent.info.RoamingServiceInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class NotificationServiceChangeInfo extends Info {

	private static final long serialVersionUID = 1L;
	private List<ServiceInfo> addedServices  = new ArrayList<ServiceInfo>();
	private List<ServiceInfo> removedServices= new ArrayList<ServiceInfo>();
	private List<NotificationFavouriteNumberInfo> favoriteNumber =  new ArrayList<NotificationFavouriteNumberInfo>();
	private Double proratedCharge;
	private boolean containNewCallingCircleService = false;
	private List <RoamingServiceInfo> roamingServiceList;
	
    
	public List<NotificationFavouriteNumberInfo> getFavoriteNumber() {
		return favoriteNumber;
	}
	
	public void setFavoriteNumber(List<NotificationFavouriteNumberInfo> favoriteNumber) {
		this.favoriteNumber = favoriteNumber;
	}
	
	
	public List<ServiceInfo> getAddedServices() {
		return addedServices;
	}
	
	public void setAddedServices(List<ServiceInfo> addedServices) {
		this.addedServices = addedServices;
	}
	public List<ServiceInfo> getRemovedServices() {
		return removedServices;
	}
	public void setRemovedServices(List<ServiceInfo> removedServices) {
		this.removedServices = removedServices;
	}
	public Double getProratedCharge() {
		return proratedCharge;
	}
	public void setProratedCharge(Double proratedCharge) {
		this.proratedCharge = proratedCharge;
	}
	public boolean isContainNewCallingCircleService() {
		return containNewCallingCircleService;
	}
	public void setContainNewCallingCircleService(
			boolean containNewCallingCircleService) {
		this.containNewCallingCircleService = containNewCallingCircleService;
	}
	

	public List <RoamingServiceInfo> getRoamingServiceList() {
		return roamingServiceList;
	}
	public void setRoamingServiceList(List <RoamingServiceInfo> roamingServiceList) {
		this.roamingServiceList = roamingServiceList;
	}
	
	
	

	@Override
	public String toString() {
		return "NotificationServiceChangeInfo [addedServices=" + addedServices
				+ ", removedServices=" + removedServices + ", favoriteNumber="
				+ favoriteNumber + ", proratedCharge=" + proratedCharge
				+ ", containNewCallingCircleService="
				+ containNewCallingCircleService + ", roamingServiceInfoList="
				+ roamingServiceList  + "]";
	}

}
