package com.telus.api.account;

import com.telus.api.*;
import com.telus.api.reference.AdjustmentReason;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import java.util.*;

public interface Credit {
    
    public static final char TAX_OPTION_NO_TAX		= 'N';
    public static final char TAX_OPTION_GST_ONLY	= 'G';
    public static final char TAX_OPTION_ALL_TAXES	= 'A';
    
    /**
     * Returns the tax option used to create this credit.  Not applicable to credits already applied.
     * 
     * @return char	- one of the TAX_OPTION_XXXX constants.
     */
    public char getTaxOption();
    
    String getSubscriberId();
    
    String getText();
    
    void setText(String newText);
    
    void setReasonCode(String reasonCode);
    
    String getReasonCode();
    
    void setEffectiveDate(Date effectiveDate);
    
    Date getEffectiveDate();
    
    void setBalanceImpactFlag(boolean balanceImpactFlag);
    
    boolean isBalanceImpactFlag();
    
    public int getId();
    
    public Date getCreationDate();
    
    public String getActivityCode();
    
    public char getApprovalStatus();
    
    public boolean isBalanceIgnoreFlag();
    
    public int getOperatorId();
    
    public String getProductType();
    
    public String getSOC();
    
    public String getFeatureCode();
    
    public boolean isBilled();
    
    public double getRelatedChargeId();
    
    /**
     * Returns the base amount without taxes.
     *
     */
    double getAmount();
    
    /**
     * Sets the base amount without taxes.  If this credit is taxable, this method
     * triggers an update of the tax and total fields.
     *
     * @see #isTaxable()
     * @see #getPSTAmount()
     * @see #getGSTAmount()
     * @see #getHSTAmount()
     * @see #getTotalAmount()
     *
     */
    void setAmount(double newAmount) throws TelusAPIException;
    
    /**
     * Deprecated in favour of getTaxOption().
     * 
     * Returns <CODE>true</CODE> if all taxes will be applied.  
     * 
     * @see #getTaxOption()
     */
    boolean isTaxable();
    
    double getPSTAmount();
    
    double getGSTAmount();
    
    double getHSTAmount();
    
    /**
     * Returns the base amount plus any applicable taxes.
     *
     */
    double getTotalAmount();
    
    /**
     * Applies a credit.
     *
     * @param overrideUserLimit <CODE>true</CODE> if the user's per-transaction limit should be ignored.
     *
     * <P>This method may involve a remote method call.
     *
     * @throws LimitExceededException if the user's per-transaction limit is exceeded.
     *
     */
    public void apply(boolean overrideUserLimit) throws LimitExceededException,
    TelusAPIException;
    
    /**
     * Applies a credit, ignoring the user's per-transaction limit.
     *
     * <P>This method may involve a remote method call.
     *
     */
    public void apply() throws TelusAPIException;
    
    /**
     * Reverses a credit
     *
     * @param reversalReasonCode reason code for reversal
     * @param memoText text to be stored in memo
     * @param overrideUserLimit overrideUserLimit - true, if the user's per-transaction limit should be ignored.
     *
     * <P>This method may involve a remote method call.
     *
     */
    public void reverse(String reversalReasonCode, String memoText,
            boolean overrideUserLimit) throws LimitExceededException,
            TelusAPIException;
    
    /**
     * Reverses a credit, ignoring the user's per-transaction limit.
     *
     * @param reversalReasonCode reason code for reversal
     * @param memoText text to be stored in memo
     *
     * <P>This method may involve a remote method call.
     *
     */
    public void reverse(String reversalReasonCode, String memoText) throws
    TelusAPIException;
    
    /**
     * Return AdjustmentReason, a reference object, associated with the Credit.
     *
     * @return AdjustmentReason
     */
    AdjustmentReason getAdjustmentReason();
    
    /**
     * For an open recurring credit with predefined frequency, the
     * calling program defines the number of recurring credits to be applied.
     *
     * @param numberOfRecurring int
     */
    void setNumberOfRecurring(int numberOfRecurring);
    
    /**
     * Set to <code>true</code> if reverse for all recurring credits for the same reason code on the contract desired.
     * @param reverseAllRecurring boolean
     */
    void setReverseAllRecurring(boolean reverseAllRecurring);
    
    double getRoamingTaxAmount();
    
    Charge[] getRelatedCharges() throws TelusAPIException;
}
