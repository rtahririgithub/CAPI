package com.telus.eas.account.info;


import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.ActivationTopUp;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.Contract;
import com.telus.api.account.CreditCard;
import com.telus.api.account.MigrationRequest;
import com.telus.api.account.MikeToPCSPostMigrationRequest;
import com.telus.api.account.MikeToPCSPreMigrationRequest;
import com.telus.api.account.PCSPostPaidToPrePaidMigrationRequest;
import com.telus.api.account.PCSPrePaidToPCSPostPaidMigrationRequest;
import com.telus.api.account.PCSToMikeMigrationRequest;
import com.telus.api.equipment.Equipment;
import com.telus.api.fleet.MemberIdentity;
import com.telus.api.reference.MigrationType;
import com.telus.eas.framework.info.Info;


public  class MigrationRequestInfo extends Info implements
	MigrationRequest
	,PCSPrePaidToPCSPostPaidMigrationRequest
	,PCSPostPaidToPrePaidMigrationRequest
	,PCSToMikeMigrationRequest
	,MikeToPCSPostMigrationRequest
	,MikeToPCSPreMigrationRequest
{

    static final long serialVersionUID = 1L;

    private final MigrationType type;
    private String migrationReasonCode;
    private CreditCard activationCreditCard;
    private String activationAirtimeCardNumber;
    private ActivationTopUp activationTopUp;
    private boolean dealerAccepteddeposit;
    private  String dealerCode;
    private  String salesRepCode;
    private  String requestorId;
    private  String userMemoText;

    private boolean depositTransferInd=false;
    private boolean phoneOnly=false;
    private boolean PTNBasedFleet=true;
    private MemberIdentity memberIdentity;
    private ActivationOptionInfo activationOption;
    
    private double activationCreditAmount;
    private int activationType = -1;

    public MigrationRequestInfo(MigrationType type){    	this.type = type;    }

    public Account getNewAccount() {
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}

    public String getActivationAirtimeCardNumber() {
		return activationAirtimeCardNumber;
	}

    public void setActivationAirtimeCardNumber(String activationAirtimeCardNumber) {
		this.activationAirtimeCardNumber = activationAirtimeCardNumber;
	}

    public CreditCard getActivationCreditCard() {
		return activationCreditCard;
	}

    public void setActivationCreditCard(CreditCard activationCreditCard) {
		this.activationCreditCard = activationCreditCard;
	}

    public ActivationTopUp getActivationTopUp() {
		return activationTopUp;
	}

    public void setActivationTopUp(ActivationTopUp activationTopUp) {
		this.activationTopUp = activationTopUp;
	}

    public boolean isDealerAccepteddeposit() {
		return dealerAccepteddeposit;
	}

    public void setDealerAccepteddeposit(boolean dealerAccepteddeposit) {
		this.dealerAccepteddeposit = dealerAccepteddeposit;
	}
	public Equipment getNewEquipment() {

        throw new java.lang.UnsupportedOperationException("Method not implemented here");
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
	public Contract getNewContract() throws TelusAPIException{
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getRequestorId() {
		return requestorId;
	}
	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}
	public String getSalesRepCode() {
		return salesRepCode;
	}
	public void setSalesRepCode(String salesRepCode) {
		this.salesRepCode = salesRepCode;
	}

    public String getUserMemoText() {        return userMemoText;    }
    public void setUserMemoText(String userMemoText) {        this.userMemoText = userMemoText;    }


    //--- PCSToMikeMigrationRequest specific interfaces - begin
	public MemberIdentity getMemeberIdentity() {
		return memberIdentity;
	}
	public void setMemberIdentity(MemberIdentity memberIdentity) {
		this.memberIdentity = memberIdentity;
	}
	public boolean isDepositTransferInd() {
		return depositTransferInd;
	}
	public void setDepositTransferInd(boolean depositTransferInd) {
		this.depositTransferInd = depositTransferInd;
	}
	public boolean isPTNBasedFleet() {
		return PTNBasedFleet;
	}
	public void setPTNBasedFleet(boolean PTNBasedFleet) {
		this.PTNBasedFleet = PTNBasedFleet;
	}
	public boolean isPhoneOnly() {
		return phoneOnly;
	}
	public void setPhoneOnly(boolean phoneOnly) {
		this.phoneOnly = phoneOnly;
	}
	public boolean isP2M() {
		return type.getCode().equals(MigrationType.PCS_POST_TO_IDEN)
        || type.getCode().equals(MigrationType.PCS_PRE_TO_IDEN);
	}
	public boolean isM2P() {
		return type.getCode().equals(MigrationType.IDEN_TO_PCSPOST)
    	|| type.getCode().equals(MigrationType.IDEN_TO_PCSPRE);
	}
	public boolean isP2P() {
		return type.getCode().equals(MigrationType.PCS_POST_TO_PCSPRE)
    	|| type.getCode().equals(MigrationType.PCS_PRE_TO_PCSPOST);
	}
	public boolean isToMike()
	{
		return isP2M();
	}
	public boolean isToPCS()
	{
		return isM2P() || isP2P();
	}
    //--- PCSToMikeMigrationRequest specific interfaces - end

    public void setActivationOption(ActivationOption option) {
        activationOption = (ActivationOptionInfo)option;       
    }

    public ActivationOption getActivationOption() {
        return activationOption;
    }
    
	public Contract getNewPrepaidContract() throws TelusAPIException{
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}
	
	public double getActivationCreditAmount() {
		return activationCreditAmount;
	}
	
	public void setActivationCreditAmount(double activationCreditAmount) {
		this.activationCreditAmount = activationCreditAmount;
	}
	
	public int getActivationType() {
		return activationType;
	}
	
	public void setActivationType(int activationType) {
		this.activationType = activationType;
	}
	public void setActivationCreditCard (CreditCard creditCard, AuditHeader auditHeader){
		setActivationCreditCard(creditCard);
	}
	
}