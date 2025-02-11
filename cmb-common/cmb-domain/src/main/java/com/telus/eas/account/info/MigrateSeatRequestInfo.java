package com.telus.eas.account.info;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.BCMobileSeatToPCSPostPaidMigrateSeatRequest;
import com.telus.api.account.Contract;
import com.telus.api.account.MigrateSeatRequest;
import com.telus.api.account.PCSPostPaidToBCMobileSeatMigrateSeatRequest;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.MigrationType;
import com.telus.eas.framework.info.Info;

public  class MigrateSeatRequestInfo extends Info implements MigrateSeatRequest, PCSPostPaidToBCMobileSeatMigrateSeatRequest, BCMobileSeatToPCSPostPaidMigrateSeatRequest {

    static final long serialVersionUID = 1L;

    private final MigrationType type;
    private int targetBan;
    private String targetPricePlanCode;
    private String migrationReasonCode;
    private String dealerCode;
    private String salesRepCode;
    private String userMemoText;
    private String targetSeatTypeCode;
    private String targetSeatGroupId;
    
    private String requestorId;
    private ActivationOptionInfo activationOption;

    public MigrateSeatRequestInfo(MigrationType type) {
    	this.type = type;
    }

    public Account getNewAccount() {
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}
	
	public Contract getNewContract() throws TelusAPIException{
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}
    
	public Equipment getNewEquipment() {
        throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}
	
	public int getTargetBan() {
		return targetBan;
	}

    public void setTargetBan(int targetBan) {
		this.targetBan = targetBan;
	}
    
	public String getTargetPricePlanCode() {
		return targetPricePlanCode;
	}

    public void setTargetPricePlanCode(String targetPricePlanCode) {
		this.targetPricePlanCode = targetPricePlanCode;
	}
    
	public String getMigrationReasonCode() {
		return migrationReasonCode;
	}

    public void setMigrationReasonCode(String migrationReasonCode) {
		this.migrationReasonCode = migrationReasonCode;
	}
    
	public MigrationType getMigrationType() {
		return type;
	}
	
	public String getDealerCode() {
		return dealerCode;
	}
	
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	
	public String getSalesRepCode() {
		return salesRepCode;
	}
	
	public void setSalesRepCode(String salesRepCode) {
		this.salesRepCode = salesRepCode;
	}

    public String getUserMemoText() { 
    	return userMemoText;	
    }
    
    public void setUserMemoText(String userMemoText) {
    	this.userMemoText = userMemoText;
    }

	public String getTargetSeatTypeCode() {
		return targetSeatTypeCode;
	}

	public void setTargetSeatTypeCode(String targetSeatTypeCode) {
		this.targetSeatTypeCode = targetSeatTypeCode;
	}
	
	public String getTargetSeatGroupId() {
		return targetSeatGroupId;
	}

	public void setTargetSeatGroupId(String targetSeatGroupId) {
		this.targetSeatGroupId = targetSeatGroupId;
	}
	
	public String getRequestorId() {
		return requestorId;
	}
	
	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}

    public void setActivationOption(ActivationOption option) {
        activationOption = (ActivationOptionInfo)option;       
    }

    public ActivationOption getActivationOption() {
        return activationOption;
    }
	
	public boolean isBCMobileToPCSPostPaid() {
		return type.getCode().equals(MigrationType.BC_MOBILE_TO_PCS_POST);
	}
	
	public boolean isPCSPostPaidToBCMobile() {
		return type.getCode().equals(MigrationType.PCS_POST_TO_BCMOBILE);
	}

}