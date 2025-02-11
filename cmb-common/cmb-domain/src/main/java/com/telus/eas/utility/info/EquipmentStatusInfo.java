/**
 * Title:        AccountTypeInfo<p>
 * Description:  The AccountTypeInfo contains the account type information.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class EquipmentStatusInfo extends Info implements EquipmentStatus{

	static final long serialVersionUID = 1L;

	private long equipmentStatusID;
	private long equipmentStatusTypeID;
	private String description ;
	private String descriptionFrench;
	
  public String getCode(){
    return String.valueOf(equipmentStatusTypeID) + "^" + String.valueOf(equipmentStatusID);
  }
  
    
	/**
	 * @return Returns the equipmentStatusID.
	 */
	public long getEquipmentStatusID() {
		return equipmentStatusID;
	}
	/**
	 * @param equipmentStatusID The equipmentStatusID to set.
	 */
	public void setEquipmentStatusID(long equipmentStatusID) {
		this.equipmentStatusID = equipmentStatusID;
	}
	/**
	 * @return Returns the equipmentStatusType.
	 */
	public long getEquipmentStatusTypeID() {
		return equipmentStatusTypeID;
	}
	/**
	 * @param equipmentStatusTypeID The equipmentStatusType to set.
	 */
	public void setEquipmentStatusTypeID(long equipmentStatusTypeID) {
		this.equipmentStatusTypeID = equipmentStatusTypeID;
	}
	/**
	 * @return Returns the descriptionFrench.
	 */
	public String getDescriptionFrench() {
		return descriptionFrench;
	}
	/**
	 * @param descriptionFrench The descriptionFrench to set.
	 */
	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	 public String toString() {
	    StringBuffer s = new StringBuffer();

	    s.append("EquipmentStatus:{\n");
	    s.append("    equipmentStatusID=[").append(equipmentStatusID).append("]\n");
	    s.append("    equipmentStatusTypeID=[").append(equipmentStatusTypeID).append("]\n");
	    s.append("    description=[").append(description).append("]\n");
	    s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
	    s.append("}");

	    return s.toString();
	  }
	  
}
