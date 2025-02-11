package com.telus.eas.utility.info;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.telus.eas.framework.info.Info;

public class ProvisioningRequestInfo extends Info {
	
	private static final long serialVersionUID = 1L;
	
	// Request headers
	private String ban;
	private String billingType;
	private String brand;
	private String requestActionType;
	private String sourceSystemCode;
	
	// Request parameters
	private Map requestParams = new HashMap();

	public String getBan() {
		return ban;
	}

	public void setBan(String ban) {
		this.ban = ban;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getRequestActionType() {
		return requestActionType;
	}

	public void setRequestActionType(String requestActionType) {
		this.requestActionType = requestActionType;
	}

	public String getSourceSystemCode() {
		return sourceSystemCode;
	}

	public void setSourceSystemCode(String sourceSystemCode) {
		this.sourceSystemCode = sourceSystemCode;
	}

	public Map getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map requestParams) {
		this.requestParams = requestParams;
	}
	
	public String toString() {
		
		StringBuffer sb = new StringBuffer();

		sb.append("ProvisioningRequestInfo: {\n");
		sb.append("    ban=["+ ban + "]\n");
		sb.append("    brandId=["+ brand + "]\n");
		sb.append("    requestActionType=["+ requestActionType + "]\n");
		sb.append("    billingType=[" + billingType + "]\n");
		sb.append("    sourceSystemCode=[" + sourceSystemCode + "]\n");
		sb.append("    request parameters: {\n");
		if (requestParams == null || requestParams.size() == 0) {
			sb.append("        null\n");
		} else {
			Iterator iterator = requestParams.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				sb.append("        " + entry.getKey() + "=[" + entry.getValue() + "]\n");
			}
		}
		sb.append("    }");
		sb.append("}");

		return sb.toString();
	}
	
}