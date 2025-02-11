
package com.telus.api.reference;

public interface NetworkType extends Reference {

	public static final String NETWORK_TYPE_IDEN = "I";
	public static final String NETWORK_TYPE_CDMA = "C";
	public static final String NETWORK_TYPE_HSPA = "H";
	public static final String NETWORK_TYPE_ALL = "9"; /** This means CDMA+HSPA. It does NOT mean CDMA+HSPA+IDEN.	 */
	public static final String[] NETWORK_TYPE_ALL_LIST = {NETWORK_TYPE_CDMA, NETWORK_TYPE_HSPA};

	public String getCode();
	public String getDescription();
	public String getDescriptionFrench();
	
}