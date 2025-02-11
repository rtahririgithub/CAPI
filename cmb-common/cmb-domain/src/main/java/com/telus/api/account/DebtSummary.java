
package com.telus.api.account;

import java.util.Date;


/**
 * <CODE>DebtSummary</CODE>
 *
 */
public interface DebtSummary {
  double getPastDue1to30Days();

  double getPastDue31to60Days();

  double getPastDue61to90Days();

  double getPastDueOver90Days();

  double getPastDue();

  double getCurrentDue();

  /**
 * Returns  Due Date of the Last Bill
 * Returns null, if there are no Bills issued for the Account
 *
 */
  Date getBillDueDate();
  
  double getAccountRealTimeBalance() ;
		
	

}



