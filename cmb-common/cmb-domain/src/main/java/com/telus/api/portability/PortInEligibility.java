package com.telus.api.portability;

public interface PortInEligibility {
	
	/**
	 * @deprecated
	 * @see #PORT_VISIBILITY_TYPE_EXTERNAL_2C
	 */
	public static final String PORT_VISIBILITY_EXTERNAL_2P = "EXT_2P";
	
	/**
	 * @deprecated
	 * @see #PORT_VISIBILITY_TYPE_EXTERNAL_2I
	 */
	public static final String PORT_VISIBILITY_EXTERNAL_2M = "EXT_2M";
	
	/**
	 * @deprecated use <tt>PORT_VISIBILITY_EXTERNAL_2P</tt> instead
	 */
	public static final String PORT_VISIBILITY_EXTERNAL = PORT_VISIBILITY_EXTERNAL_2P;
	
	/**
	 * @deprecated
	 */
	public static final String PORT_VISIBILITY_INTERNAL_M2P = "INT_M2P";
	
	/**
	 * @deprecated
	 */	
	public static final String PORT_VISIBILITY_INTERNAL_P2M = "INT_P2M";
	
	/**
	 * @deprecated use <tt>PORT_VISIBILITY_INTERNAL_M2P</tt>
	 */
	public static final String PORT_VISIBILITY_INTERNAL = PORT_VISIBILITY_INTERNAL_M2P;
	
	public static final String PORT_PROCESS_INTER_BRAND_PORT = "INTER_BRAND";
	public static final String PORT_PROCESS_INTER_CARRIER_PORT = "INTER_CARRIER";
	public static final String PORT_PROCESS_MIGRATION = "MIGRATION";
	public static final String PORT_PROCESS_ROLLBACK = "ROLLBACK";

	public static final String PORT_DIRECTION_INDICATOR_WIRELESS_WIRELESS ="A";
	public static final String PORT_DIRECTION_INDICATOR_WIRELINE_WIRELESS ="C";
	
	public static final String PORT_VISIBILITY_TYPE_M2P_I2C = "M2P_I2C";
	public static final String PORT_VISIBILITY_TYPE_M2P_I2H = "M2P_I2H";
	
	public static final String PORT_VISIBILITY_TYPE_P2M_C2I = "P2M_C2I";
	public static final String PORT_VISIBILITY_TYPE_P2M_H2I = "P2M_H2I";

	public static final String PORT_VISIBILITY_TYPE_P2P_2C = "P2P_2C";
	public static final String PORT_VISIBILITY_TYPE_P2P_2H = "P2P_2H";

	public static final String PORT_VISIBILITY_TYPE_INTERNAL_2C = "INT_2C";
	public static final String PORT_VISIBILITY_TYPE_INTERNAL_2H = "INT_2H";
	public static final String PORT_VISIBILITY_TYPE_INTERNAL_2I = "INT_2I";
	
	public static final String PORT_VISIBILITY_TYPE_EXTERNAL_2C = "EXT_2C";
	public static final String PORT_VISIBILITY_TYPE_EXTERNAL_2I = "EXT_2I";
	public static final String PORT_VISIBILITY_TYPE_EXTERNAL_2H = "EXT_2H";

	public static final String PORT_VISIBILITY_TYPE_SWAP_2C = "SWAP_2C";
	public static final String PORT_VISIBILITY_TYPE_SWAP_2H = "SWAP_2H";
	
	/**
	 * This constant represents the TELUS platform when returned from the WLNP services. This is
	 * used to determine if the port-in to the TELUS managed brands is coming from the MVNE system.
	 */
	public static final int PORT_PLATFORM_TELUS = 1;
	
	/**
	 * This constant is used internally to drive flows that are specific to an MVNE port.
	 */
	public static final String PORT_PROCESS_INTER_MVNE_PORT = "INTER_MVNE";

	String getPortVisibility();
	String  getCurrentServiceProvider();
	/**
	 * @deprecated replaced by either {@link #isCDMACoverage()} or {@link #isHSPACoverage()}
	 * @see #isCDMACoverage()
	 * @see #isHSPACoverage()
	 */
	boolean isPCSCoverage();  
	boolean isIDENCoverage();  
	/**
	 * @deprecated
	 * @see #isCDMAPrepaidCoverage()
	 * @see #isHSPAPrepaidCoverage()
	 */
	boolean isPrepaidCoverage();
	/**
	 * @deprecated
	 * @see #isCDMAPostpaidCoverage()
	 * @see #isHSPAPostpaidCoverage()
	 */
	boolean isPostPaidCoverage();
	String getPortDirectionIndicator();
	int getIncomingBrandId();
	int getOutgoingBrandId();
	String getPhoneNumber();
	boolean isCDMACoverage();
	boolean isHSPACoverage();
	boolean isCDMAPrepaidCoverage();
	boolean isHSPAPrepaidCoverage();
	boolean isCDMAPostpaidCoverage();
	boolean isHSPAPostpaidCoverage();
	
	/**
	 * Sets the platform identifier to indicate the system responsible for handling the
	 * port.  Current values are 1 for TELUS managed brands and 2 for the RedKnee
	 * Mobile Virtual Network Enabler (MVNE)
	 */
	public void setPlatformId(int platformId);

	/**
	 * Gets the platform identifier to indicate the system responsible for handling the
	 * port.  Current values are 1 for TELUS managed brands and 2 for the RedKnee
	 * Mobile Virtual Network Enabler (MVNE)
	 */
	public int getPlatformId();
	
	/**
	 * This method is a convenience method that returns true if the platformId is not TELUS
	 * (platformId != 1) otherwise it returns false
	 */
	public boolean isPortInFromMVNE();
}
