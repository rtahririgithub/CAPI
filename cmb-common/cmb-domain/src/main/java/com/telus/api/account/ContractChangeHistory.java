package com.telus.api.account;

import java.util.Date;

public interface ContractChangeHistory {

    Date getDate();

    Date getNewCommitmentStartDate();

    Date getNewCommitmentEndDate();

    String getReasonCode();
    
    /**
     * Returns the Knowbility Dealer Code.
     *
     */

    String getDealerCode();
    
    /**
     * Returns the Knowbility Sales Rep Id.
     *
     */
    
    String getSalesRepId();
    
    int getNewCommitmentMonths();
    
    /**
     * Returns the Knowbility Operator ID.
     *
     */
    String getKnowbilityOperatorID();
    
    /**
     * Returns the Application ID.
     *
     */
    String getApplicationID();

}
