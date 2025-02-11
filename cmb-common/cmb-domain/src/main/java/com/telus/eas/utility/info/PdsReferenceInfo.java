package com.telus.eas.utility.info;

public class PdsReferenceInfo extends ReferenceInfo {
	
	private static final long serialVersionUID = -4846400096513594665L;
	private String pdsCode;

	public String getPdsCode() {
		return pdsCode;
	}

	public void setPdsCode(String pdsCode) {
		this.pdsCode = pdsCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "PdsReferenceInfo [pdsCode=" + pdsCode + ", toString()=" + super.toString() + "]";
	}
	
	
}
