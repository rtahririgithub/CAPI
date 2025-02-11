package com.telus.eas.utility.info;

import java.util.ArrayList;
import java.util.Iterator;

import com.telus.eas.framework.info.Info;

public class BillNotificationDetailsInfo extends Info {

	static final long serialVersionUID = 1L;
	  
	private ArrayList notificationInfoList = new ArrayList();


	
	public ArrayList getNotificationInfoList() {
		return notificationInfoList;
	}



	public void setNotificationInfoList(
			ArrayList notificationInfoList) {
		this.notificationInfoList = notificationInfoList;
	}



	public void addNotificationInfo(BillNotificationItemInfo info) {
		this.notificationInfoList.add(info);
	}
	
	public String toString() {
		
		StringBuffer buf = new StringBuffer();
		buf.append("[BillNotificationDetailsInfo");
		Iterator iter = notificationInfoList.iterator();
		while (iter.hasNext()) {
			BillNotificationItemInfo itemInfo = (BillNotificationItemInfo)iter.next();
			buf.append(" | ");
			buf.append(itemInfo);
		}
		buf.append("]");
		
		return buf.toString();
	}
}
