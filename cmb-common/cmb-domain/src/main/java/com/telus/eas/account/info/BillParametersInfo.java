package com.telus.eas.account.info;

import com.telus.eas.framework.info.Info;

public class BillParametersInfo extends Info {

	static final long serialVersionUID = 1L;
	public static final String DEFAULT_BILL_FORMAT = "M1"; //refer to BILL_PARAM_FORMAT table for detail
	public static final String DEFAULT_BILL_MEDIA = "PA"; //refer to BILL_PARAM_FORMAT table for detail

	private short noOfInvoice = 0;

	private String billFormat;

	private String mediaCategory;

	public short getNoOfInvoice() {
		return noOfInvoice;
	}
	
	public void setNoOfInvoice (short noOfInvoice) {
		this.noOfInvoice = noOfInvoice;
	}
	
	public String getBillFormat() {
		return billFormat;
	}

	public void setBillFormat(String billFormat) {
		this.billFormat = billFormat;
	}

	public String getMediaCategory() {
		return mediaCategory;
	}

	public void setMediaCategory(String mediaCategory) {
		this.mediaCategory = mediaCategory;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("BillParametersInfo:{\n");
		s.append("    noOfInvoice=[").append(noOfInvoice).append("]\n");
		s.append("    billFormat=[").append(billFormat).append("]\n");
		s.append("    mediaCategory=[").append(mediaCategory).append("]\n");
		s.append("}");

		return s.toString();
	}

}
