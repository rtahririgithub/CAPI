/**
 * Title:        AccountTypeInfo<p>
 * Description:  The AccountTypeInfo contains the account type information.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.Brand;
import com.telus.eas.framework.info.Info;

public class BrandInfo extends Info implements Brand{

	static final long serialVersionUID = 1L;
	
	
	protected String description ;
	protected String descriptionFrench ;
	protected String shortDescription ;
	protected String shortDescriptionFrench ;
	private int brandId;
	

	public void setDescription(String newDescription) {
		description = newDescription;
	}	
	
	public void setDescriptionFrench(String newDescriptionFrench) {
		descriptionFrench = newDescriptionFrench;
	}
	
	public void setShortDescription(String newShortDescription) {
		shortDescription = newShortDescription;
	}	
	
	public void setShortDescriptionFrench(String newShortDescriptionFrench) {
		shortDescriptionFrench = newShortDescriptionFrench;
	}
	
	public void setBrandId (int brandId) {
		this.brandId = brandId;
	}

	public String getCode() {
		return String.valueOf(brandId);
	}
	
	public String getDescription() {
		return description;
	}
		
	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public String getShortDescription() {
		return shortDescription;
	}
		
	public String getShortDescriptionFrench() {
		return shortDescriptionFrench;
	}
	
	public int getBrandId() {
		return brandId;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		s.append("Brand:{\n");
		s.append("    brandID=[").append(brandId).append("]\n");
		s.append("    code=[").append(getCode()).append("]\n");
		s.append("    description=[").append(description).append("]\n");
		s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
		s.append("    shortDescription=[").append(shortDescription).append("]\n");
		s.append("    shortDescriptionFrench=[").append(shortDescriptionFrench).append("]\n");
		s.append("}");
		
		return s.toString();
	}

}
