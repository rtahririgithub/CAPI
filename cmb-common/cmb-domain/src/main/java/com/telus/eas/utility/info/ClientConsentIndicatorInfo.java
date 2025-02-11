

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.ClientConsentIndicator;
import com.telus.api.reference.Reference;

public class ClientConsentIndicatorInfo extends ReferenceInfo implements ClientConsentIndicator {
	static final long serialVersionUID = 1L;

	public boolean equals(Object obj) {
		// Defect PROD00170695 fix, compare the two ClientConsentIndicator object by its code
		if ( obj != null
			&& obj instanceof ClientConsentIndicator 
			&& this.code != null) {
			return this.code.equals(((Reference) obj).getCode());
		} else {
			return super.equals(obj);
		}
	}

}

