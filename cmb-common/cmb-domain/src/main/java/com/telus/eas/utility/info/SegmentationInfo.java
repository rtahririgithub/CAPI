/**
 * Title:        SegmentationInfo<p>
 * Description:  The SegmentationInfo contains the segmentation information.<p>
 * Copyright:    Copyright (c) Tsz Chung Tong<p>
 * Company:      Telus Mobility Inc<p>
 * @author Tsz Chung Tong
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.Segmentation;
import com.telus.eas.framework.info.Info;

public class SegmentationInfo extends Info implements Segmentation{

	static final long serialVersionUID = 1L;
	
	
	protected String description ;
	protected String descriptionFrench ;
	protected String shortDescription ;
	protected String shortDescriptionFrench ;
	private int brandId;
	private String accType;
	private String province;
	private String segment;
	private String subSegment;
	

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
		return String.valueOf(brandId)+accType+province;
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
	
	public void setSegment (String seg) {
		segment = seg;
	}
	
	public String getSegment () {
		return segment;
	}
	
	public void setSubSegment (String ss) {
		subSegment = ss;
	}
	
	public String getSubSegment () {
		return subSegment;
	}
	
	public void setAccType(String account_type) {
		accType = account_type;
	}
	
	public String getAccType() {
		return accType;
	}
	
	public void setProvince (String prov) {
		province = prov;
	}
	
	public String getProvince() {
		return province;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		s.append("Segmentation:{\n");
		s.append("    brandId=[").append(brandId).append("]\n");
		s.append("    accType=[").append(accType).append("]\n");
		s.append("    province=[").append(province).append("]\n");
		s.append("    segment=[").append(segment).append("]\n");
		s.append("    subSegment=[").append(subSegment).append("]\n");
		s.append("    code=[").append(getCode()).append("]\n");
		s.append("    description=[").append(description).append("]\n");
		s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
		s.append("    shortDescription=[").append(shortDescription).append("]\n");
		s.append("    shortDescriptionFrench=[").append(shortDescriptionFrench).append("]\n");
		s.append("}");
		
		return s.toString();
	}

}
