package com.telus.eas.utility.info;

import java.util.ArrayList;
import java.util.List;

import com.telus.eas.framework.info.Info;

public class OfferPricePlanSetInfo extends Info {

	static final long serialVersionUID = 1L;

	private int enforcementCode;
	private List<String> offerPricePlanCodeList;
	private List<String> offerPricePlanGroupCodeList;
	protected List<String> offerIncompatiblePricePlanCodeList;
	protected boolean fetchInMarketPricePlansInd = true;
	protected boolean inMarketPricePlansOfferInd;

	public int getEnforcementCode() {
		return enforcementCode;
	}

	public void setEnforcementCode(int enforcementCode) {
		this.enforcementCode = enforcementCode;
	}

	public List<String> getOfferPricePlanCodeList() {
		if (offerPricePlanCodeList == null) {
			offerPricePlanCodeList = new ArrayList<String>();
		}
		return offerPricePlanCodeList;
	}

	public void setOfferPricePlanCodeList(List<String> offerPricePlanCodeList) {
		this.offerPricePlanCodeList = offerPricePlanCodeList;
	}

	public List<String> getOfferPricePlanGroupCodeList() {
		if (offerPricePlanGroupCodeList == null) {
			offerPricePlanGroupCodeList = new ArrayList<String>();
		}
		return offerPricePlanGroupCodeList;
	}

	public void setOfferPricePlanGroupCodeList(List<String> offerPricePlanGroupCodeList) {
		this.offerPricePlanGroupCodeList = offerPricePlanGroupCodeList;
	}

	public List<String> getOfferIncompatiblePricePlanCodeList() {
		if (offerIncompatiblePricePlanCodeList == null) {
			offerIncompatiblePricePlanCodeList = new ArrayList<String>();
		}
		return offerIncompatiblePricePlanCodeList;
	}

	public void setOfferIncompatiblePricePlanCodeList(List<String> offerIncompatiblePricePlanCodeList) {
		this.offerIncompatiblePricePlanCodeList = offerIncompatiblePricePlanCodeList;
	}

	public boolean isFetchInMarketPricePlansInd() {
		return fetchInMarketPricePlansInd;
	}

	public void setFetchInMarketPricePlansInd(boolean fetchInMarketPricePlansInd) {
		this.fetchInMarketPricePlansInd = fetchInMarketPricePlansInd;
	}

	public boolean isInMarketPricePlansOfferInd() {
		return inMarketPricePlansOfferInd;
	}

	public void setInMarketPricePlansOfferInd(boolean inMarketPricePlansOfferInd) {
		this.inMarketPricePlansOfferInd = inMarketPricePlansOfferInd;
	}
	

	public boolean isCompatiblePricePlan(String kbPricePlanCode) {
		return offerPricePlanCodeList != null && offerPricePlanCodeList.contains(kbPricePlanCode);
	}
	
	public boolean isIncompatiblePricePlan(String kbPricePlanCode) {
		return offerIncompatiblePricePlanCodeList != null && offerIncompatiblePricePlanCodeList.contains(kbPricePlanCode);
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("[OfferPricePlanSetInfo");
		buf.append(" | enforcementCode: " + enforcementCode);
		buf.append(" | offerPricePlanCodeList: " + offerPricePlanCodeList);
	    buf.append(" | offerPricePlanGroupCodeList: " + offerPricePlanGroupCodeList);
	    buf.append(" | offerIncompatiblePricePlanCodeList: " + offerIncompatiblePricePlanCodeList);
		buf.append(" | fetchInMarketPricePlansInd: " + fetchInMarketPricePlansInd);
		buf.append(" | inMarketPricePlansOfferInd: " + inMarketPricePlansOfferInd);
		buf.append("]");
		return buf.toString();
	}

}
