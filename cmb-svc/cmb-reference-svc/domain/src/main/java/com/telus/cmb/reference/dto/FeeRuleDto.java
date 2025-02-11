package com.telus.cmb.reference.dto;



public class FeeRuleDto {
	
	private String code = "";
	private String description = "";
	private String descriptionFrench = "";

	private boolean manualCharge = false;
	private double amount = 0.0;
	private boolean amountOverrideable = true;
	
	private String productType = "";
	private char level = '\0';
	private char balanceImpact = '\0';
	
	private int brandId=0;
	private String provinceCode="";
	private char accountType='\0';
	private char accountSubType='\0';
	private String segment="";
	

	public FeeRuleDto(
			String code,
		    String desc,
		    String descFr,
		    String productType,
		    boolean isManual,
		    double amount,
		    boolean isAmountOverrideable,
		    char level,
		    char balanceImpact,
		    int brandId, String provinceCode, char accountType, char accountSubType, String segment
	) {
		this.code = code;
	    this.description = desc;
	    this.descriptionFrench = descFr;
	    this.manualCharge = isManual;
	    this.amount = amount;
	    this.amountOverrideable = isAmountOverrideable;
	    this.productType = productType;
	    this.level = level;
	    this.balanceImpact = balanceImpact;
		this.brandId=brandId;
		this.provinceCode=provinceCode;
		this.accountType= accountType;
		this.accountSubType=accountSubType;
		this.segment=segment;
	}

	
	public String toString()
	  {
	      StringBuffer s = new StringBuffer(128);

	      s.append("FeeRuleDto:[\n");
	      s.append("    code=[").append(code).append("]\n");
	      s.append("    description=[").append(description.trim()).append("]\n");
	      s.append("    descriptionFrench=[").append(descriptionFrench.trim()).append("]\n");
	      s.append("    manualCharge=[").append(manualCharge).append("]\n");
	      s.append("    amount=[").append(amount).append("]\n");
	      s.append("    amountOverrideable=[").append(amountOverrideable).append("]\n");
	      s.append("    productType=[").append(productType).append("]\n");
	      s.append("    level=[").append(level).append("]\n");
	      s.append("    balanceImpact=[").append(balanceImpact).append("]\n");
	      s.append("    brandId=[").append(brandId).append("]\n");
	      s.append("    provinceCode=[").append(provinceCode).append("]\n");
	      s.append("    accountType=[").append(accountType).append("]\n");
	      s.append("    accountSubType=[").append(accountSubType).append("]\n");
	      s.append("    segment=[").append(segment).append("]\n");
	      s.append("]");

	      return s.toString();
	  }


	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}


	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the descriptionFrench
	 */
	public String getDescriptionFrench() {
		return descriptionFrench;
	}


	/**
	 * @param descriptionFrench the descriptionFrench to set
	 */
	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}


	/**
	 * @return the manualCharge
	 */
	public boolean isManualCharge() {
		return manualCharge;
	}


	/**
	 * @param manualCharge the manualCharge to set
	 */
	public void setManualCharge(boolean manualCharge) {
		this.manualCharge = manualCharge;
	}


	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}


	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}


	/**
	 * @return the amountOverrideable
	 */
	public boolean isAmountOverrideable() {
		return amountOverrideable;
	}


	/**
	 * @param amountOverrideable the amountOverrideable to set
	 */
	public void setAmountOverrideable(boolean amountOverrideable) {
		this.amountOverrideable = amountOverrideable;
	}


	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}


	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}


	/**
	 * @return the level
	 */
	public char getLevel() {
		return level;
	}


	/**
	 * @param level the level to set
	 */
	public void setLevel(char level) {
		this.level = level;
	}


	/**
	 * @return the balanceImpact
	 */
	public char getBalanceImpact() {
		return balanceImpact;
	}


	/**
	 * @param balanceImpact the balanceImpact to set
	 */
	public void setBalanceImpact(char balanceImpact) {
		this.balanceImpact = balanceImpact;
	}


	/**
	 * @return the brandId
	 */
	public int getBrandId() {
		return brandId;
	}


	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}


	/**
	 * @return the provinceCode
	 */
	public String getProvinceCode() {
		return provinceCode;
	}


	/**
	 * @param provinceCode the provinceCode to set
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}


	/**
	 * @return the accountType
	 */
	public char getAccountType() {
		return accountType;
	}


	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(char accountType) {
		this.accountType = accountType;
	}


	/**
	 * @return the accountSubType
	 */
	public char getAccountSubType() {
		return accountSubType;
	}


	/**
	 * @param accountSubType the accountSubType to set
	 */
	public void setAccountSubType(char accountSubType) {
		this.accountSubType = accountSubType;
	}


	/**
	 * @return the segment
	 */
	public String getSegment() {
		return segment;
	}


	/**
	 * @param segment the segment to set
	 */
	public void setSegment(String segment) {
		this.segment = segment;
	}
	
	
	
	
}
