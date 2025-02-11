package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class AdditionalMsiSdnFtrInfo extends Info{
 
	private int 		ban;
	private String 		subscriberNumber;
	private String 		soc;
	private String 		feature;
	private int 		socSeqNo;
	private int 		ftrSocVerNo;
	private double 		servFtrSeqNo;
	private Date 		ftrEffDate;
	private Date 		ftrExpDate;
	private int 		ftrTrxId;

	public int getBan() {
		return ban;
	}
	
	public void setBan(int ban) {
		this.ban = ban;
	}
	
	public String getFeature() {
		return feature;
	}
	
	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	public Date getFtrEffDate() {
		return ftrEffDate;
	}
	
	public void setFtrEffDate(Date ftrEffDate) {
		this.ftrEffDate = ftrEffDate;
	}
	
	public Date getFtrExpDate() {
		return ftrExpDate;
	}
	
	public void setFtrExpDate(Date ftrExpDate) {
		this.ftrExpDate = ftrExpDate;
	}
	
	public int getFtrSocVerNo() {
		return ftrSocVerNo;
	}
	
	public void setFtrSocVerNo(int ftrSocVerNo) {
		this.ftrSocVerNo = ftrSocVerNo;
	}
	
	public int getFtrTrxId() {
		return ftrTrxId;
	}
	
	public void setFtrTrxId(int ftrTrxId) {
		this.ftrTrxId = ftrTrxId;
	}
	
	public double getServFtrSeqNo() {
		return servFtrSeqNo;
	}
	
	public void setServFtrSeqNo(double servFtrSeqNo) {
		this.servFtrSeqNo = servFtrSeqNo;
	}
	
	public String getSoc() {
		return soc;
	}
	
	public void setSoc(String soc) {
		this.soc = soc;
	}
	
	public int getSocSeqNo() {
		return socSeqNo;
	}
	
	public void setSocSeqNo(int socSeqNo) {
		this.socSeqNo = socSeqNo;
	}
	
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	
	public String toString() {
	    StringBuffer s = new StringBuffer(128);

	    s.append("AdditionalMsiSdnFtrInfo:[\n");
	    s.append("ban=[").append(ban).append("]\n");
	    s.append("subscriberNumber=[").append(subscriberNumber).append("]\n");
	    s.append("soc=[").append(soc).append("]\n");
	    s.append("feature=[").append(feature).append("]\n");
	    s.append("socSeqNo=[").append(socSeqNo).append("]\n");
	    s.append("ftrSocVerNo=[").append(ftrSocVerNo).append("]\n");
	    s.append("servFtrSeqNo=[").append(servFtrSeqNo).append("]\n");
	    s.append("ftrEffDate=[").append(ftrEffDate).append("]\n");
	    s.append("ftrExpDate=[").append(ftrExpDate).append("]\n");
	    s.append("ftrTrxId=[").append(ftrTrxId).append("]\n");
	    s.append("]");

	    return s.toString();
	}

}
