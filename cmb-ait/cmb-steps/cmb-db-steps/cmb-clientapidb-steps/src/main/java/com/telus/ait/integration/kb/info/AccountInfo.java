package com.telus.ait.integration.kb.info;

public class AccountInfo {
	
	private String accountType;
    private String accountSubType;
    private String banStatus;

	public final static String BAN_STATUS_OPEN = "O";
	public final static String BAN_STATUS_NEW = "N";
	public final static String BAN_STATUS_TENTATIVE = "T";

    public AccountInfo(String accountType, String accountSubType, String banStatus) {
        this.accountType = accountType;
        this.accountSubType = accountSubType;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountSubType() {
        return accountSubType;
    }
    
    public String getBanStatus() {
    	return banStatus;
    }
    
    public boolean isOpen() {
    	return BAN_STATUS_OPEN.equals(banStatus);
    }
}
