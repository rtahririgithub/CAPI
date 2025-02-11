/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class CreditClassInfo extends Info implements CreditClass{

  static final long serialVersionUID = 1L;

	protected String code;
	protected String description;
	protected String descriptionFrench;
	protected String depositReqInd;
	protected String onlineSelectInd;
	protected String accountEligibilityForLatePaymentInd;
	protected String shortDescription;
	protected String shortDescriptionFrench;

    public String toString() {
        StringBuffer s = new StringBuffer(128);

        s.append("CreditClassInfo:[\n");
        s.append("     code="+getCode()+"\n");
        s.append("     description="+getDescription()+"\n");
        s.append("     descriptionFrench="+getDescriptionFrench()+"\n");
        s.append("     depositReqInd="+getDepositReqInd()+"\n");
        s.append("     onlineSelectInd="+getOnlineSelectInd()+"\n");
        s.append("     accountEligibilityForLatePaymentInd="+getAccountEligibilityForLatePaymentInd()+"\n");
        s.append("     shortDescription="+getShortDescription()+"\n");
        s.append("     shortDescriptionFrench="+getDescriptionFrench()+"\n");
        s.append("]");

        return s.toString();
    }	
	/**
	 * @param accountEligibilityForLatePaymentInd The accountEligibilityForLatePaymentInd to set.
	 */
	public void setAccountEligibilityForLatePaymentInd(
			String accountEligibilityForLatePaymentInd) {
		this.accountEligibilityForLatePaymentInd = accountEligibilityForLatePaymentInd;
	}
	/**
	 * @param depositReqInd The depositReqInd to set.
	 */
	public void setDepositReqInd(String depositReqInd) {
		this.depositReqInd = depositReqInd;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param descriptionFrench The descriptionFrench to set.
	 */
	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}
	/**
	 * @param onlineSelectInd The onlineSelectInd to set.
	 */
	public void setOnlineSelectInd(String onlineSelectInd) {
		this.onlineSelectInd = onlineSelectInd;
	}
	/**
	 * @param shortDescription The shortDescription to set.
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	/**
	 * @param shortDescriptionFrench The shortDescriptionFrench to set.
	 */
	public void setShortDescriptionFrench(String shortDescriptionFrench) {
		this.shortDescriptionFrench = shortDescriptionFrench;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @return Returns the accountEligibilityForLatePaymentInd.
	 */
	public String getAccountEligibilityForLatePaymentInd() {
		return accountEligibilityForLatePaymentInd;
	}
	/**
	 * @return Returns the depositReqInd.
	 */
	public String getDepositReqInd() {
		return depositReqInd;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return Returns the descriptionFrench.
	 */
	public String getDescriptionFrench() {
		return descriptionFrench;
	}
	/**
	 * @return Returns the onlineSelectInd.
	 */
	public String getOnlineSelectInd() {
		return onlineSelectInd;
	}
	/**
	 * @return Returns the shortDescription.
	 */
	public String getShortDescription() {
		return shortDescription;
	}
	/**
	 * @return Returns the shortDescriptionFrench.
	 */
	public String getShortDescriptionFrench() {
		return shortDescriptionFrench;
	}
}