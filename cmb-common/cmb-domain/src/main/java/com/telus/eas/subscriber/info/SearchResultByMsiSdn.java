package com.telus.eas.subscriber.info;

import com.telus.eas.framework.info.Info;

public class SearchResultByMsiSdn extends Info{
	private AdditionalMsiSdnFtrInfo effectiveFtr;
	private AdditionalMsiSdnFtrInfo[] allFtrArray;

	public AdditionalMsiSdnFtrInfo getEffectiveFtr() {
		return effectiveFtr;
	}
	
	public void setEffectiveFtr(AdditionalMsiSdnFtrInfo effectiveFtr) {
		this.effectiveFtr = effectiveFtr;
	}
	
	public AdditionalMsiSdnFtrInfo[] getAllFtrArray() {
		return allFtrArray;
	}
	
	public void setAllFtrArray(AdditionalMsiSdnFtrInfo[] allFtrArray) {
		this.allFtrArray = allFtrArray;
	}
	public String toString() {
	    StringBuffer s = new StringBuffer(128);

	    s.append("SearchResultByMsiSdn:[\n");
	    s.append("effectiveFtr=[").append(effectiveFtr).append("]\n");
	    if (allFtrArray != null){
	    	for (int i = 0; i < allFtrArray.length ; i++){
	    		s.append("allFtrArray" + "[" + i + "]" + "=[").append(allFtrArray[i]).append("]\n");
	    	}
	    }
	    
	    s.append("]");

	    return s.toString();
	}	
	
}
