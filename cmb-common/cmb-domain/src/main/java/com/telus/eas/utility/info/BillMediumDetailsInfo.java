package com.telus.eas.utility.info;

import java.util.ArrayList;
import java.util.Iterator;

import com.telus.eas.framework.info.Info;

public class BillMediumDetailsInfo extends Info {

	static final long serialVersionUID = 1L;
	  
	private ArrayList billMediumItemList = new ArrayList();

	public ArrayList getBillMediumItemList() {
		return billMediumItemList;
	}

	public void setBillMediumItemList(ArrayList billMediumItemList) {
		this.billMediumItemList = billMediumItemList;
	}

	public void addBillMediumItem(BillMediumItemInfo itemInfo) {
		billMediumItemList.add(itemInfo);
	}
	
	public String toString() {
		
		StringBuffer buf = new StringBuffer();
		buf.append("[BillMediumDetailsInfo");
		Iterator iter = billMediumItemList.iterator();
		while (iter.hasNext()) {
			BillMediumItemInfo itemInfo = (BillMediumItemInfo)iter.next();
			buf.append(" | ");
			buf.append(itemInfo);
		}
		buf.append("]");
		
		return buf.toString();
	}
}
