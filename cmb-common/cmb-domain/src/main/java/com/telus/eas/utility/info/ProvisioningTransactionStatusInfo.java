/*
 * Created on 24-Jun-04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.ProvisioningTransactionStatus;

/**
 * @author zhangji
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProvisioningTransactionStatusInfo
		implements ProvisioningTransactionStatus {
    static final long serialVersionUID = 1L;

	private String code;
	private String description;
	private String description_fr;

	public String getCode() {
		return code;	
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return description_fr;
	}

	public void setCode( String code ) {
		this.code = code;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public void setDescriptionFrench( String description_fr ) {
		this.description_fr = description_fr;
	}
}
