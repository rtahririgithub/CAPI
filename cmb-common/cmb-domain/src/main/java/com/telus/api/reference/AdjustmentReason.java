/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.reference;

/**
  * A credit (adjustment) that can be applied to an account
  *
  * Note:  The term adjustment reason is for legacy concerns, the text for the credit was
  * retrieved from the activity reason text (activity_rsn_text) table.
  */
public interface AdjustmentReason extends BillAdjustment {

   public static final char ADJUSTMENT_LEVEL_CODE_GENERAL = 'A';
   public static final char ADJUSTMENT_LEVEL_CODE_CHARGE = 'C';
   public static final char ADJUSTMENT_LEVEL_CODE_INVOICE = 'I';
   public static final char ADJUSTMENT_LEVEL_CODE_CALL = 'M';

   // Recurring Credits
   int FREQUENCY_NONE = 0;
   int FREQUENCY_MONTHLY = 1;
   int FREQUENCY_BI_MONTHLY = 2;
   int FREQUENCY_QUARTERLY = 3;
   int FREQUENCY_HALF_A_YEAR = 6;
   int FREQUENCY_YEARLY = 12;
   String ACTIVITY_CODE_RECURRING = "RCC";

   public String getAdjustmentLevelCode();
   public String getAdjustmentActivityCode();
   public String getAdjustmentTaxIndicator();
   public String getAdjustmentCategory();

   // Recurring Credits
   int getFrequency();
   int getMaxNumberOfRecurringCredits();
   java.util.Date getExpiryDate();
   //boolean isPromotional();
   boolean isRecurring();
   //boolean isAssociated(ServiceSummary service, int term, boolean current);
   //boolean isAssociated(String province, boolean current);
}
