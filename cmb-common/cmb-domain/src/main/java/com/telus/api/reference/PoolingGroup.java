package com.telus.api.reference;

public interface PoolingGroup extends Reference {  
	
	// pooling group ID constants
	static final int AIRTIME = 1;
	static final int DIRECT_CONNECT = 2;
	static final int AIRTIME_AND_DIRECT_CONNECT = 3;
	static final int LONG_DISTANCE = 4;
	static final int DIRECT_CONNECT_LD = 5;
	static final int LONG_DISTANCE_AND_DIRECT_CONNECT_LD = 6;
	static final int ROAMING_AIRTIME = 7;
	static final int ROAMING_LONG_DISTANCE = 8;
	
	// coverage type constant arrays
	static final String[] airtimeCoverageTypes = {"H"};
	static final String[] longDistanceCoverageTypes = {"G", "O"};
	
	int getPoolingGroupId();
	String getCoverageType();

}
