package com.telus.cmb.account.lifecyclemanager.dao;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;
import com.telus.eas.framework.info.ChargeAdjustmentWithTaxInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;

public interface AdjustmentDao {
	
	/**
	 * @param Integer banId
	 * @return Array of Fee WaiverInfo
	 * @throws ApplicationException
	 */
	List<FeeWaiverInfo> retrieveFeeWaivers(int banId,String sessionID) throws ApplicationException;
	
	/**
     * Applies FeeWaiver.
     *
     * @param feeWaiver FeeWaiverInfo
     * @throws ApplicationException
     * 
     */
    void applyFeeWaiver(FeeWaiverInfo feeWaiver,String sessionId) throws ApplicationException;
    

    /**
     * Reverse an existing credit (Subscriber-Level)
     *
     * @param   CreditInfo    all attributes necessary for a credit
     * @param   String        reason code for reversal
     * @param   String        memo text
     * @param   boolean       Override users threshold
     *
     * @exception TelusException
     *
     * @see CreditInfo
     */
	void reverseCreditForSubscriber(CreditInfo creditInfo, String reversalReasonCode, String memoText, boolean overrideThreshold, String sessionId)  throws ApplicationException;

	/**
	   * Reverse an existing credit (BAN-level)
	   *
	   * @param   CreditInfo    all attributes necessary for a credit
	   * @param   String        reason code for reversal
	   * @param   String        memo text
	   * @param   boolean       Override users threshold
	   *
	   * @exception TelusException
	   *
	   * @see CreditInfo
	   */
	void reverseCreditForBan(CreditInfo creditInfo, String reversalReasonCode, String memoText, boolean overrideThreshold, String sessionId) throws ApplicationException;

	/**
	   * Delete an existing charge (Subscriber-Level)
	   *
	   * @param   ChargeInfo    all attributes necessary for a charge
	   * @param   String        reason code for deletion
	   * @param   String        memo text
	   * @param   boolean       Override users threshold
	   *
	   * @exception TelusException
	   *
	   * @see ChargeInfo
	   */
	void deleteChargeForSubscriber(ChargeInfo chargeInfo, String deletionReasonCode, String memoText, boolean overrideThreshold, String sessionId) throws ApplicationException;

	/**
	   * Delete an existing charge (BAN-level)
	   *
	   * @param   ChargeInfo    all attributes necessary for a charge
	   * @param   String        reason code for deletion
	   * @param   String        memo text
	   * @param   boolean       Override users threshold
	   *
	   * @exception TelusException
	   *
	   * @see ChargeInfo
	   */
	void deleteChargeForBan(ChargeInfo chargeInfo, String deletionReasonCode, String memoText, boolean overrideThreshold, String sessionId) throws ApplicationException;

	/**
	   * Apply a new charge to a billing account (Subscriber-Level)
	   *
	   * @param   ChargeInfo    all attributes necessary for a charge
	   * @param   boolean       Override users threshold
	   *
	   * @exception TelusException
	   *
	   * @see ChargeInfo
	   */
	double applyChargeToAccountForSubscriber(ChargeInfo chargeInfo, boolean overrideThreshold, String sessionId) throws ApplicationException;
	
	/**
	   * Apply a new charge to a billing account (BAN-level)
	   *
	   * @param   ChargeInfo    all attributes necessary for a charge
	   * @param   boolean       Override users threshold
	   *
	   * @exception TelusException
	   *
	   * @see ChargeInfo
	   */
	double applyChargeToAccountForBan(ChargeInfo chargeInfo, boolean overrideThreshold, String sessionId) throws ApplicationException;
    /**
     * Adjust the Charges based on Subscriber
     * 
     * @param ChargeInfo
     * @param double
     * @param String
     * @param String
     * @param boolean
     * @param String 
     * @return
     * @throws ApplicationException
     */
    double adjustChargeForSubscriber(ChargeInfo pChargeInfo, double pAdjustmentAmount, String pAdjustmentReasonCode, String pMemoText, boolean pOverrideThreshold, String sessionId) 
    		throws ApplicationException;
    /**
     * Adjust the Charges based on BAN
     * 
     * @param ChargeInfo
     * @param double
     * @param String 
     * @param String
     * @param boolean
     * @param String
     * @return
     * @throws ApplicationException
     */
    double adjustChargeForBan(ChargeInfo pChargeInfo, double pAdjustmentAmount, String pAdjustmentReasonCode, String pMemoText, boolean pOverrideThreshold, String sessionId) 
    		throws ApplicationException;
    
    /**
     * Applies Credit based on Subscriber
     * 
     * @param CreditInfo 	creditInfo
     * @param boolean		pOverrideThreshold
     * @param String		sessionId
     * @return true  to indicate the adjustment(s) is created in KB
     * @throws ApplicationException
     */
    boolean applyCreditToAccountForSubscriber(CreditInfo creditInfo, boolean overrideThreshold, String sessionId) throws ApplicationException;
    
    /**
     * Applies Credit based on BAN
     * 
     * @param CreditInfo	creditInfo
     * @param boolean		overrideThreshold
     * @param String		sessionId
     * @return true  to indicate the adjustment(s) is created in KB
     * @throws ApplicationException
     */
    boolean applyCreditToAccountForBan(CreditInfo creditInfo, boolean overrideThreshold, String sessionId) throws ApplicationException;

	/**
	 * Applies discount to account based on Subscriber
	 * 
	 * @param DiscountInfo discountInfo
	 * @throws ApplicationException
	 */
	void applyDiscountToAccountForSubscriber(DiscountInfo discountInfo, String sessionId) throws ApplicationException;
	
	/**
	 * Applies discount to account based on BAN
	 * 
	 * @param DiscountInfo discountInfo
	 * @throws ApplicationException
	 */
	void applyDiscountToAccountForBan(DiscountInfo discountInfo, String sessionId) throws ApplicationException;
	
	/**
	 * Applies charges and Adjustment to account based on BAN
	 * 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccount(List<ChargeAdjustmentInfo> chargeInfoList, String sessionId) throws ApplicationException;

	/**
	 * Applies charges and Adjustment to account for the  Subscriber
	 * 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccountForSubscriber(List<ChargeAdjustmentInfo> chargeInfoList, String sessionId) throws ApplicationException;
	
	/**
	 * Applies charges and Adjustment to account based on BAN
	 * 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccountWithTax(List<ChargeAdjustmentWithTaxInfo> chargeInfoList, String sessionId) throws ApplicationException;

	/**
	 * Applies charges and Adjustment to account for the  Subscriber
	 * 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccountForSubscriberWithTax(List<ChargeAdjustmentWithTaxInfo> chargeInfoList, String sessionId) throws ApplicationException;
	
}
