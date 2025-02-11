package com.telus.eas.account.info;

import java.util.ArrayList;
import java.util.List;
import com.telus.eas.framework.info.Info;

public class BillMediumInfo extends Info {
	
	private static final long serialVersionUID = 1L;

	public static final String BILL_MEDIUM_PAPER = "PAPER";
	public static final String BILL_MEDIUM_FULL_PAPER = "FULL_PAPER";
	public static final String BILL_MEDIUM_EBILL = "EBILL";

	private List<String> billTypeList = new ArrayList<String>();
	private List<String> addedBillTypeList = new ArrayList<String>();
	private List<String> removedBillTypeList = new ArrayList<String>();

	public List<String> getAddedBillTypeList() {
		return addedBillTypeList;
	}

	public void setAddedBillTypeList(List<String> addedBillTypeList) {
		this.addedBillTypeList = addedBillTypeList;
	}

	public List<String> getRemovedBillTypeList() {
		return removedBillTypeList;
	}

	public void setRemovedBillTypeList(List<String> removedBillTypeList) {
		this.removedBillTypeList = removedBillTypeList;
	}

	public List<String> getBillTypeList() {
		return billTypeList;
	}

	public void setBillTypeList(List<String> billTypeList) {
		this.billTypeList = billTypeList;
	}

	@Override
	public String toString() {
		return "BillMediumInfo [billTypeList=" + billTypeList + ", addedBillTypeList=" + addedBillTypeList + ", removedBillTypeList=" + removedBillTypeList + "]";
	}

}
