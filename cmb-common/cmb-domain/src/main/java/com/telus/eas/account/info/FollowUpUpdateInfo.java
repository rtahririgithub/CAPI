package com.telus.eas.account.info;

import com.telus.eas.framework.info.Info;

import java.util.ArrayList;

public class FollowUpUpdateInfo extends Info {

	static final long serialVersionUID = 1L;
	
	public static final String FOLLOWUP_TYPE_ADJUSTMENT = "ADJT";
		// charge adjustment
	public static final String FOLLOW_REASON_ADJUST_CHARGE = "TYPE=ADC";
		// stand alone manual credit
	public static final String FOLLOW_REASON_ADJUST_BAN = "TYPE=ADB";
		
    private int ban;
    private int followUpId;
    private String assignedToWorkPositionId;
    private String assignedToDepartmentCode;
    private String assignedToFunctionCode;
    private String closeReasonCode;
    private ArrayList additionalTextList;
    private boolean isApproved;
    private boolean isDenied;
	
    
    //the following fields are created to capture the original follow up's properties - for back-end
    //to determine what type of the follow up is for: adjustment, charge, or something else.
    //they are not used when updating the follow up in KB
    private String followUpType;
    private String followUpText; //the original follow text;
    private String productType;
    private String subscriberId;

    public int getBan() {
        return ban;
    }

    public void setBan(int ban) {
        this.ban = ban;
    }

    public FollowUpUpdateInfo() {
    }

    public FollowUpUpdateInfo(int ban, int followUpId) {
        this.ban = ban;
        this.followUpId = followUpId;
    }

    public int getFollowUpId() {
        return followUpId;
    }

    public void setFollowUpId(int followUpId) {
        this.followUpId = followUpId;
    }

    public String getAssignedToWorkPositionId() {
        return assignedToWorkPositionId;
    }

    public void setAssignedToWorkPositionId(String assignedToWorkPositionId) {
        this.assignedToWorkPositionId = assignedToWorkPositionId;
    }

    public String getAssignedToDepartmentCode() {
        return assignedToDepartmentCode;
    }

    public void setAssignedToDepartmentCode(String assignedToDepartmentCode) {
        this.assignedToDepartmentCode = assignedToDepartmentCode;
    }

    public String getAssignedToFunctionCode() {
        return assignedToFunctionCode;
    }

    public void setAssignedToFunctionCode(String assignedToFunctionCode) {
        this.assignedToFunctionCode = assignedToFunctionCode;
    }

    public String getCloseReasonCode() {
        return closeReasonCode;
    }

    public void setCloseReasonCode(String closeReasonCode) {
        this.closeReasonCode = closeReasonCode;
    }

    public void addText(String text) {
        if (additionalTextList == null)
            additionalTextList = new ArrayList();

        additionalTextList.add(text);
    }

    public String[] getAdditionalText() {
        String[] additionalText = null;

        int additionalTextSize = additionalTextList != null ? additionalTextList.size() : 0;

        if (additionalTextSize > 0) {
            additionalText = new String[additionalTextSize];

            for (int i = 0; i < additionalTextSize; i++) {
                additionalText[i] = (String) additionalTextList.get(i);
            }
        }
        return additionalText;
    }
    
	public boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(boolean isApproved) {
		this.isApproved = isApproved;
		if (isApproved)
			this.isDenied = false;
	}

	public boolean getIsDenied() {
		return isDenied;
	}

	public void setIsDenied(boolean isDenied) {
		this.isDenied = isDenied;
		if (isDenied)
			this.isApproved = false;
	}

	public String getFollowUpType() {
		return followUpType;
	}

	public void setFollowUpType(String followUpType) {
		this.followUpType = followUpType;
	}

	public String getFollowUpText() {
		return followUpText;
	}

	public void setFollowUpText(String followUpText) {
		this.followUpText = followUpText;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public boolean isFollowUpApprovalForChargeAdj() {
		return getFollowUpText()!=null && getFollowUpText().indexOf(FollowUpUpdateInfo.FOLLOW_REASON_ADJUST_CHARGE) != -1;
	}
	
	public boolean isFollowUpApprovalForManualCredit() {
		return getFollowUpText()!=null && getFollowUpText().indexOf(FollowUpUpdateInfo.FOLLOW_REASON_ADJUST_BAN) != -1;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("FollowUpUpdateInfo [ban=");
		buffer.append(ban);
		buffer.append(", followUpId=");
		buffer.append(followUpId);
		buffer.append(", assignedToWorkPositionId=");
		buffer.append(assignedToWorkPositionId);
		buffer.append(", assignedToDepartmentCode=");
		buffer.append(assignedToDepartmentCode);
		buffer.append(", assignedToFunctionCode=");
		buffer.append(assignedToFunctionCode);
		buffer.append(", closeReasonCode=");
		buffer.append(closeReasonCode);
		buffer.append(", additionalTextList=");
		buffer.append(additionalTextList);
		buffer.append(", isApproved=");
		buffer.append(isApproved);
		buffer.append(", isDenied=");
		buffer.append(isDenied);
		buffer.append(", followUpType=");
		buffer.append(followUpType);
		buffer.append(", followUpText=");
		buffer.append(followUpText);
		buffer.append(", productType=");
		buffer.append(productType);
		buffer.append(", subscriberId=");
		buffer.append(subscriberId);
		buffer.append("]");
		return buffer.toString();
	}
	
	
}
