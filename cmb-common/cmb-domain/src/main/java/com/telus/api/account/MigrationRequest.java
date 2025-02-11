package com.telus.api.account;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.*;
public interface MigrationRequest {	
    MigrationType getMigrationType();	
    Account getNewAccount();	
    String getMigrationReasonCode();	
    void setMigrationReasonCode(String code);	
    Contract getNewContract()throws TelusAPIException;	
    Equipment getNewEquipment();    
    String getUserMemoText();    
    void setUserMemoText(String userMemoText);
    void setActivationOption(ActivationOption option);
    
}

