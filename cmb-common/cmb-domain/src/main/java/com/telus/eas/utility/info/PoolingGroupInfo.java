package com.telus.eas.utility.info;

import com.telus.api.reference.PoolingGroup;

public class PoolingGroupInfo  extends ReferenceInfo implements PoolingGroup {
    
	static final long serialVersionUID = 1L;
    
    private int poolingGroupId;
    private String coverageType;
        
	public String getCode() {
		return String.valueOf(poolingGroupId);
	}
	
	public int getPoolingGroupId() {
		return poolingGroupId;
	}
	
	public void setPoolingGroupId(int poolingGroupId) {
		this.poolingGroupId = poolingGroupId;
	}
    
    public String getCoverageType() {
       return coverageType;
    }

    public void setCoverageType(String coverageType) {
        this.coverageType = coverageType;
    }

}
