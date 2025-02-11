package com.telus.eas.utility.info;

import com.telus.api.reference.SpecialNumberRange;
import com.telus.eas.framework.info.Info;

public class SpecialNumberRangeInfo extends Info implements SpecialNumberRange {
	private static final long serialVersionUID = 1L;
	private String firstNumberInRange;
	private String lastNumberInRange;
	private String description;
	private String descriptionFrench;
	private long firstNumber;
	private long lastNumber;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescriptionFrench() {
		return descriptionFrench;
	}
	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}
	public String getFirstNumberInRange() {
		return firstNumberInRange;
	}
	public void setFirstNumberInRange(String firstNumberInRange) {
		this.firstNumberInRange = firstNumberInRange;
		this.firstNumber = Long.parseLong(firstNumberInRange);
	}
	public String getLastNumberInRange() {
		return lastNumberInRange;
	}
	public void setLastNumberInRange(String lastNumberInRange) {
		this.lastNumberInRange = lastNumberInRange;
		this.lastNumber = Long.parseLong(lastNumberInRange );
	}
	public String getCode() {
		return firstNumberInRange+"-"+lastNumberInRange;
	}
	public boolean isNumberInRange(String longNumber) {
		boolean result = false;
		try {
			long number = Long.parseLong(longNumber );
			result = number<=lastNumber && number>=firstNumber;
		} catch(NumberFormatException e ) {

		}
		return result;
	}

}
