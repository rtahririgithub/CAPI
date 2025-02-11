/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class SeatTypeInfo extends Info implements SeatType {

	static final long serialVersionUID = 1L;

	protected String code;
	protected String description;
	protected String descriptionFrench;

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public void setCode(String newCode) {
		code = newCode;
	}

	public void setDescription(String newDescription) {
		description = newDescription;
	}

	public void setDescriptionFrench(String newDescriptionFrench) {
		descriptionFrench = newDescriptionFrench;
	}

}