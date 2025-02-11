package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.List;
import com.telus.api.account.SeatData;
import com.telus.api.resource.Resource;
import com.telus.eas.framework.info.Info;

public class SeatDataInfo extends Info implements SeatData {

	private static final long serialVersionUID = 1L;
	private String seatGroup;
	private String seatType;
	List seatResourceList = new ArrayList();

	public void setSeatType(String seatType) {
		this.seatType = seatType;

	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatGroup(String seatGroup) {
		this.seatGroup = seatGroup;

	}

	public String getSeatGroup() {
		return seatGroup;
	}

	

	public void addSeatResource(ResourceInfo resourceInfo) {
		seatResourceList.add(resourceInfo);

	}

	public void addSeatResource(String resourceType, String resourceNumber) {
		ResourceInfo resourceInfo = new ResourceInfo();
		resourceInfo.setResourceNumber(resourceNumber);
		resourceInfo.setResourceType(resourceType);
		seatResourceList.add(resourceInfo);

	}

	public Resource[] getResources() {
		return (Resource[]) seatResourceList.toArray(new Resource[seatResourceList.size()]);
	}

	public Resource[] removeResource(Resource resource) {
		// implement the logic to remove the resource from list , need to check if actual passed resource present in list or not,if not don't throw exception...
		return null;
	}

	public String toString() {
		String str1 = "SeatDataInfo [seatGroup=" + seatGroup + ", seatType="
				+ seatType + " ,  ";
		Resource[] resources = getResources();
		String str2 = "\n";
		if(resources.length ==0)
			str2 += "Seat Resource list is empty.";
		else str2 += "Avaliable seat resources are : \n" ;
		for (int i = 0; i < resources.length; i++) {
			str2 += resources[i].toString();
		}
		return str1 + str2 + "]";
	}
}
