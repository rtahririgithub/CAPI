package com.telus.provider.util;

import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.DeprecatedEquipmentInfo;

public class InfoObjectFactory {

	/**
	 * This factory is used to return the instance of info class based on
	 * project rollback flags .
	 */

	//NOTE:  Not removing the below code as part of covent rollback clean-up 
	//because developer can use below code snippet as example if there is any need to apply rollback flag in Info classes ,use below approach suggested by Canh/Chung.
	
	
	/**public static EquipmentInfo getEquipmentInfo() {
		if (AppConfiguration.isCoventRollback()) {
			return new DeprecatedEquipmentInfo();
		} else {
			return new EquipmentInfo();
		}
	}
	 **/
	
}
