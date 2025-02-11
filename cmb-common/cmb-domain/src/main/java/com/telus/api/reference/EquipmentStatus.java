/**
 * Title:        AccountTypeInfo<p>
 * Description:  The AccountTypeInfo contains the account type information.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Marina Kuper
 * @version 1.0
 */
package com.telus.api.reference;

public interface EquipmentStatus extends Reference{

	
	/**
     * Returns Status ID for the equipment 
     *
     */
	public long getEquipmentStatusID();
	
	/**
     * Returns Status Type ID  for the  equipment 
     *
     */
	
	public long getEquipmentStatusTypeID();


}
